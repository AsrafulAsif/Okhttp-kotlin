package AIA.missyou

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class log_in : AppCompatActivity() {
    lateinit var btn: Button
    lateinit var textView: TextView
    lateinit var editText: EditText
    var url = "http://192.168.0.104:5000/testing"
    private val client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//            // Get new FCM registration token
//            val msg = task.result
//            println(msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//        })
        editText = findViewById(R.id.et_text)
        textView = findViewById(R.id.text)
        btn = findViewById(R.id.btn)
        btn.setOnClickListener {
            post()
        }
    }

    fun post() {
        val requestBody = FormBody.Builder().add("text", editText.text.toString()).build()
        val request = Request.Builder().url(url).post(requestBody).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")
                    val responseData = response.body?.string()
                    runOnUiThread {
                        try {
                            var json = JSONObject(responseData)
                            println("Request Successful!!")
                            println(json)
                            textView.text = json["status"].toString()
//                            val responseObject = json.getJSONObject("response")
//                            val docs = json.getJSONArray("docs")
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                }
            }
        })
    }
}