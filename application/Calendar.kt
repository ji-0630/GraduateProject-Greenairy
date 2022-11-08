package com.example.plantdiary

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class Calendar : AppCompatActivity() {
    var userID: String = "userID"
    lateinit var documentId: String
    lateinit var diary:String
    lateinit var str: String
    lateinit var calendarView: CalendarView
    lateinit var updateBtn: Button
    lateinit var deleteBtn:Button
    lateinit var saveBtn:Button
    lateinit var diaryTextView: TextView
    lateinit var diaryContent:TextView
    lateinit var title:TextView
    lateinit var contextEditText: EditText
    private val db: FirebaseFirestore by lazy {
        Firebase.firestore
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // 유저 아이디 불러오기
        getUserId()

        val diary = "diary"
        // UI값 생성
        calendarView=findViewById(R.id.calendarView)
        diaryTextView=findViewById(R.id.diaryTextView)
        saveBtn=findViewById(R.id.saveBtn)
        deleteBtn=findViewById(R.id.deleteBtn)
        updateBtn=findViewById(R.id.updateBtn)
        diaryContent=findViewById(R.id.diaryContent)
        title=findViewById(R.id.title)
        contextEditText=findViewById(R.id.contextEditText)

        title.text = "달력 일기장"

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            documentId = year.toString() + "-" + (month + 1).toString() + "-" + dayOfMonth.toString()
            readDiaryFromFireStore(diary) {
                it?.let { content ->
                    diaryTextView.visibility = View.VISIBLE
                    saveBtn.visibility = View.INVISIBLE
                    diaryContent.visibility = View.VISIBLE
                    diaryContent.text = content
                    updateBtn.visibility = View.VISIBLE
                    deleteBtn.visibility = View.VISIBLE
                    diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
                    contextEditText.visibility = View.INVISIBLE
                } ?: run {
                    diaryTextView.visibility = View.VISIBLE
                    saveBtn.visibility = View.VISIBLE
                    diaryContent.visibility = View.INVISIBLE
                    updateBtn.visibility = View.INVISIBLE
                    deleteBtn.visibility = View.INVISIBLE
                    diaryTextView.text = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
                    contextEditText.setText("")
                    contextEditText.visibility = View.VISIBLE
                }
            }
        }

        saveBtn.setOnClickListener {
            createDiaryFromFireStore(diary, documentId, contextEditText.text.toString())

            contextEditText.visibility = View.INVISIBLE
            saveBtn.visibility = View.INVISIBLE
            updateBtn.visibility = View.VISIBLE
            deleteBtn.visibility = View.VISIBLE
            str = contextEditText.text.toString()
            diaryContent.text = str
            diaryContent.visibility = View.VISIBLE
        }

        updateBtn.setOnClickListener {
            contextEditText.visibility = View.VISIBLE
            diaryContent.visibility = View.INVISIBLE
            contextEditText.setText(diaryContent.text)
            saveBtn.visibility = View.VISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            diaryContent.text = contextEditText.text
        }

        deleteBtn.setOnClickListener {
            deleteDiaryFromFireStore(diary, documentId)

            diaryContent.visibility = View.INVISIBLE
            updateBtn.visibility = View.INVISIBLE
            deleteBtn.visibility = View.INVISIBLE
            contextEditText.setText("")
            contextEditText.visibility = View.VISIBLE
            saveBtn.visibility = View.VISIBLE
        }
    }

    private fun getUserId() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            userID = user.uid
        }
    }

    private fun createDiaryFromFireStore(diary: String, day: String, content: String) {

        db.collection(diary).document(userID).collection(day).document(day)
            .set(DiaryModel(content))
            .addOnCompleteListener {

            }.addOnFailureListener {

            }
    }

    private fun readDiaryFromFireStore(diary: String, completion: (str: String?) -> Unit) {
        db.collection(diary).document(userID).collection(documentId).document(documentId)
            .get()
            .addOnCompleteListener {
                completion(it.result.get("content") as? String)
            }.addOnFailureListener {

            }
    }

    private fun deleteDiaryFromFireStore(diary: String, day: String) {
        db.collection(diary).document(userID).collection(day).document(day)
            .delete()
            .addOnCompleteListener {

            }.addOnFailureListener {

            }
    }
}
