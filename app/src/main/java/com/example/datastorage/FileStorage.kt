package com.example.datastorage

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.io.*

class FileStorage : AppCompatActivity() {
    //https://blog.csdn.net/xingyu19911016/article/details/121053021
    private lateinit var inputText: EditText
    private lateinit var save_Button:Button
    private lateinit var load_Button:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_storage)
        inputText = findViewById(R.id.editText);
        save_Button=findViewById(R.id.button_save)
        load_Button=findViewById(R.id.button_load)
        save_Button.setOnClickListener {
            val inputText = inputText.text.toString()
            save(inputText)
        }
        load_Button.setOnClickListener {
            val text = load()
            if (text.isNotEmpty()) {
                inputText.setText(text)
                inputText.setSelection(text.length)
                Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show()
            }
        }

    }
    fun save(inputText: String) {
        try {
            val output = openFileOutput("data", Context.MODE_PRIVATE)
            val writer = BufferedWriter(OutputStreamWriter(output))
            writer.use {
                it.write(inputText)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    fun load(): String {
        val content = StringBuilder()
        try {
            val input = openFileInput("data")
            val reader = BufferedReader(InputStreamReader(input))
            reader.use {
                reader.forEachLine {
                    content.append(it)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return content.toString()
    }

}