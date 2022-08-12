import firebase_admin
from firebase_admin import credentials
from firebase_admin import firestore
import pandas as pd
from socket import socket, AF_INET, SOCK_STREAM

# 파이어베이스 연결
cred = credentials.Certificate('serviceAccountkeyPython.json')
firebase_admin.initialize_app(cred)

db = firestore.client()

# 파이어베이스 데이터를 데이터프레임으로
users_ref = db.collection(u'plantData')
docs = users_ref.stream()

items = list(map(lambda x: {**x.to_dict(), 'id': x.id}, docs))

inPlant = pd.DataFrame(items)
inPlant.set_index('id', inplace=True)

# 소켓 통신
ip = "내 ip주소"
port = "포트 넘버"
server_socket = socket(AF_INET, SOCK_STREAM)

server_socket.bind((ip, port))
server_socket.listen(100)
print("대기중입니다.")

client_socket, addr = server_socket.accept()
print(str(addr), "에서 접속되었습니다.")
data = client_socket.recv(1024)
print("Received from", addr, data.decode())
decodeData = data.decode()
list(decodeData)

# 추천 시스템(환경) 필터링
plantFilter = pd.DataFrame()
# 햇빛의 양
if decodeData[1] == "1":
    temp = inPlant[inPlant['lighttdemanddoCodeNm'].isin(['높은 광도(1,500~10,000 Lux)'])]
elif decodeData[1] == "2":
    temp = inPlant[inPlant['lighttdemanddoCodeNm'].isin(['중간 광도(800~1,500 Lux)'])]
else:
    temp = inPlant[inPlant['lighttdemanddoCodeNm'].isin(['낮은 광도(300~800 Lux)'])]

plantFilter = pd.concat([plantFilter, temp])

# 물의양
if decodeData[2] == "1":
    plantFilter[plantFilter['watercycleAutumnCodeNm'].isin(['053001', '053002'])]
elif decodeData[2] == "2":
    plantFilter[plantFilter['watercycleAutumnCodeNm'].isin(['053003'])]
else:
    plantFilter[plantFilter['watercycleAutumnCodeNm'].isin(['053004'])]

# 겨울철 집 온도
if decodeData[3] == "1":
    plantFilter[plantFilter['winterLwetTpCode'].isin(['057001', '057002'])]
elif decodeData[3] == "2":
    plantFilter[plantFilter['winterLwetTpCode'].isin(['057002', '057003'])]
elif decodeData[3] == "3":
    plantFilter[plantFilter['winterLwetTpCode'].isin(['057003', '057004'])]
else:
    plantFilter[plantFilter['winterLwetTpCode'].isin(['057004', '057005'])]

# 사랑 줄 수 있는지
if decodeData[4] == "1":
    plantFilter[plantFilter['managelevelCode'].isin(['089003', '089002'])]
elif decodeData[4] == "2":
    plantFilter[plantFilter['managelevelCode'].isin(['089002', '089001'])]

# 반려동물
if decodeData[5] == 1:
    plantFilter[~plantFilter['toxctyInfo'].isin(['있음', '수액이 인체의 피부나 점막을 자극할 수 있음', '복용시 치명적 독성',
                                                 '수액이 인체의 피부나 점막을 자극할 수 있다.', '애완동물에게 해로울 수 있음',
                                                 '애완동물에게 해로울 수 있음.', '애완동물에 독성', '강함 (수액이 피부 자극을 일으킬 수 있음)',
                                                 '있음 (수액이 피부 자극을 일으킬 수 있음)', '강함 (식용하면 안됨)', '열매식용하면 안됨',
                                                 '식물체 독성임. (어린이주변에 두지말 것)', '(섭취하면 안됨)',
                                                 'Parts of plant are poisonous if ingestedHandling',
                                                 'Unknown', '줄기에 검은색의 열매가 맺히지만 독성이 있으므로 먹지 않는 것이 좋다.',
                                                 '약간 있음', 'All parts of plant are poisonous if ingested', '강함'])]

# 가중치 합산할 열 추가
plantFilter['score'] = 0

# 성향에 따른 추천
def perfect(arr):
    if arr == '058002':
        return 0.33
    elif arr == '058003':
        return 0.66
    elif arr == '058004':
        return 1
    else:
        return 0


def lazy(arr):
    if arr == '058003':
        return 0.33
    elif arr == '058002':
        return 0.66
    elif arr == '058001':
        return 1
    else:
        return 0


def busy(arr):
    if arr == '090001':
        return 1
    elif arr == '090002':
        return 0.5
    else:
        return 0


def nobusy(arr):
    if arr == '090003':
        return 1
    elif arr == '090002':
        return 0.5
    else:
        return 0


def sense(arr):
    if arr == '079002':
        return 0.33
    elif arr == '079003':
        return 0.66
    elif arr == '079004':
        return 1
    else:
        return 0


def nosense(arr):
    if arr == '079003':
        return 0.33
    elif arr == '079002':
        return 0.66
    elif arr == '079001':
        return 1
    else:
        return 0


def cold(arr):
    if arr == '082003':
        return 0.33
    elif arr == '082002':
        return 0.66
    elif arr == '082001':
        return 1
    else:
        return 0


def warm(arr):
    if arr == '082002':
        return 0.33
    elif arr == '082003':
        return 0.66
    elif arr == '082004':
        return 1
    else:
        return 0


if decodeData[6] == "1":
    plantFilter['score'] += plantFilter.apply(lambda row: perfect(row['managedemanddoCode']), axis=1)
elif decodeData[6] == "2":
    plantFilter['score'] += plantFilter.apply(lambda r: lazy(r['managedemanddoCode']), axis=1)

if decodeData[7] == '1':
    plantFilter['score'] += plantFilter.apply(lambda row: busy(row['grwtveCode']), axis=1)
elif decodeData[7] == '2':
    plantFilter['score'] += plantFilter.apply(lambda r: nobusy(r['grwtveCode']), axis=1)

if decodeData[8] == '1':
    plantFilter['score'] += plantFilter.apply(lambda row: sense(row['smellCode']), axis=1)
elif decodeData[8] == '2':
    plantFilter['score'] += plantFilter.apply(lambda r: nosense(r['smellCode']), axis=1)

if decodeData[9] == '1':
    plantFilter['score'] += plantFilter.apply(lambda row: cold(row['grwhTpCode']), axis=1)
elif decodeData[9] == '2':
    plantFilter['score'] += plantFilter.apply(lambda r: warm(r['grwhTpCode']), axis=1)

#점수가 가장 높은 순대로 상위 4개 출력
plantFilter.sort_values(by=['score'], axis=0, ascending=False, inplace=True)

dic = plantFilter['cntntsNo'].head(4).to_dict()
recomList = []
for k in dic.values():
    recomList.append(k)

result = ' '.join(str(s) for s in recomList)
client_socket.send(result.encode("UTF-8"))

client_socket.close()
server_socket.close()
