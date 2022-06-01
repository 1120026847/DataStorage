package com.example.datastorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class SharedPreferences : AppCompatActivity() {
    private lateinit var inputText: EditText
    private lateinit var save_Button: Button
    private lateinit var load_Button: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preferences)
        inputText = findViewById(R.id.editText);
        save_Button=findViewById(R.id.button_save)
        load_Button=findViewById(R.id.button_load)
        save_Button.setOnClickListener {
//调用SharedPreferences对象的edit()方法获取一个SharedPreferences.Editor对象,名称是data
            val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
            //向SharedPreferences.Editor对象中添加数据
            editor.putString("name", inputText.text.toString())
            //调用apply()方法将添加的数据提交，从而完成数据存储操作
            editor.apply()
        }
        load_Button.setOnClickListener {

            val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
            val name = prefs.getString("name", "")
            inputText.setText(name)
        }

    }
}