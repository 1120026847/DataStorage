package com.example.datastorage

import android.annotation.SuppressLint
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.facebook.stetho.Stetho
/*
edge://inspect/#devices
 */
class SQLite : AppCompatActivity() {
    private lateinit var createDatabase: Button
    private lateinit var addData:Button
    private lateinit var updateData:Button
    private lateinit var deleteData:Button
    private lateinit var queryData:Button
    private lateinit var replaceData:Button
    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)
        //初始化stetho
        Stetho.initializeWithDefaults(this);
        createDatabase = findViewById(R.id.createDatabase)
        addData=findViewById(R.id.addData)
        updateData=findViewById(R.id.updateData)
        deleteData=findViewById(R.id.deleteData)
        queryData=findViewById(R.id.queryData)
        replaceData=findViewById(R.id.replaceData)

        val dbHelper = MyDatabaseHelper(this, "BookStore.db", 2)

        createDatabase.setOnClickListener {
            dbHelper.writableDatabase
        }
        addData.setOnClickListener {
            /*
            调用SQLiteOpenHelper的getReadableDatabase()或getWritableDatabase()方法会返回一个SQLiteDatabase对象，借助这个对象就可以对数据进行CRUD操作了
             */
            val db = dbHelper.writableDatabase
            val value1 = ContentValues().apply {
                // 开始组装第一条数据
                put("name", "The Da Vinci Code")
                put("author", "Dan Brown")
                put("pages", 454)
                put("price", 16.96)
            }
            /*
            SQLiteDatabase中提供了一个insert()方法，专门用于添加数据。 它接收3个参数：

第一个参数是表名，我们希望向哪张表里添加数据，这里就传入该表的名字；
第二个参数用于在未指定添加数据的情况下给某些可为空的列自动赋值NULL，一般我们用不到这个功能，直接传入null即可；
第三个参数是一个ContentValues对象，它提供了一系列的put()方法重载，用于向ContentValues中添加数据，只需要将表中的每个列名以及相应的待添加数据传入即可。
             */
            db.insert("Book", null, value1) // 插入第一条数据
            val value2 = ContentValues().apply {
                // 开始组装第二条数据
                put("name", "The Lost Symbol")
                put("author", "Dan Brown")
                put("pages", 510)
                put("price", 19.95)
            }
            db.insert("Book", null, value2) // 插入第二条数据
        }
        /*
        SQLiteDatabase中提供了一个update()方法，用于对数据进行更新。 这个方法 接收4个参数：

第一个参数和insert()方法一样，也是表名，指定更新哪张表里的数据；
第二 个参数是ContentValues对象，要把更新数据在这里组装进去；
第三、第四个参数用于约束更新某一行或某几行中的数据，不指定的话默认会更新所有行；

这里在更新数据按钮的点击事件里面构建了一个ContentValues对象，并且只给它指定了一组数据，说明我们只是想把价格这一列的数据更新成10.99。然后调用了SQLiteDatabase的update()方法执行具体的更新操作，可以看到，这里使用了第三、第四个参数来指定具体更新 哪几行。第三个参数对应的是SQL语句的where部分，表示更新所有name等于?的行，而?是一 个占位符，可以通过第四个参数提供的一个字符串数组为第三个参数中的每个占位符指定相应 的内容，arrayOf()方法是Kotlin提供的一种用于便捷创建数组的内置方法。因此上述代码想表达的意图就是将The Da Vinci Code这本书的价格改成10.99。

         */
        updateData.setOnClickListener {
            val db = dbHelper.writableDatabase
            val values = ContentValues()
            values.put("price", 10.99)
            db.update("Book", values, "name = ?", arrayOf("The Da Vinci Code"))
        }
        /*
        SQLiteDatabase中提供了一个delete()方法，专门用于删除数据。这个方法接收3个参数：
第一个参数仍然是表名；
第二、第三个参数用于约束删除某一行或某几行的数据，不指定的话默认会删除所有行；
通过第二、第三个参 数来指定仅删除那些页数超过500页的书。
         */
        deleteData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.delete("Book", "pages > ?", arrayOf("500"))
        }
        /*
        在SQLiteDatabase中还提供了一个query()方法用于对数据进行查询。**这个方法的参数非常复杂，最短的一个方法重载也需要传入7个参数：

第一个参数是表名；
第二个参数用于指定去查询哪几列，如果不指定则默认查询所有列；
第三、第四个参数用于约束查询某一行或某几行的数据，不指定则默认查询所有行的数据；
第五个参数用于指定需要去group by的列，不指定则表示不对查询结果进行group by操作；
第六个参数用于对group by之后的数据进行进一步的过滤，不指定则表示不进行过滤；
第七个参数用于指定查询结果的排序方式，不指定则表示使用默认的排序方式；

在查询按钮的点击事件里面调用了SQLiteDatabase的query()方法查询 数据。这里的query()方法非常简单，只使用了第一个参数指明查询Book表，后面的参数全部为null。这就表示希望查询这张表中的所有数据，虽然这张表中目前只剩下一条数据了。查询 完之后就得到了一个Cursor对象，接着我们调用它的moveToFirst()方法，将数据的指针移动到第一行的位置，然后进入一个循环当中，去遍历查询到的每一行数据。在这个循环中可以通过Cursor的getColumnIndex()方法获取某一列在表中对应的位置索引，然后将这个索引 传入相应的取值方法中，就可以得到从数据库中读取到的数据了。最后别忘了调用close()方法来关闭Cursor。

         */
        queryData.setOnClickListener {
            queryData.setOnClickListener {
                val db = dbHelper.writableDatabase
                val cursor = db.query("Book", null, null, null, null, null, null)
                if (cursor.moveToFirst()) {
                    do {
                        val name = cursor.getString(cursor.getColumnIndex("name"))
                        val author = cursor.getString(cursor.getColumnIndex("author"))
                        val pages = cursor.getString(cursor.getColumnIndex("pages"))
                        val price = cursor.getString(cursor.getColumnIndex("price"))
                        Log.e("db", "Book name is $name")
                        Log.e("db", "Book author is $author")
                        Log.e("db", "Book pages is $pages")
                        Log.e("db", "Book price is $price")
                    } while (cursor.moveToNext())
                }
                cursor.close()
            }

        }
        /*
        SQLite数据库是支持事务的，事务的特性可以保证让一系列的操作要么全部完成，要么一个都不会完成

        首先调用SQLiteDatabase的beginTransaction()方法开启一个事务，然后在一个异常捕获的代码块中执行具体的数据库操作，当所有的操作都完成之后，调用setTransactionSuccessful()表示事务已经执行成功了，最后在finally代码块中调用endTransaction()结束事务。注意观察，我们在删除旧数据的操作完成后手动抛出了一个NullPointerException，这样添加新数据的代码就执行不到了。不过由于事务的存在，中途出现异常会导致事务的失败，此时旧数据应该是删除不掉的。

         */
        replaceData.setOnClickListener {
            val db = dbHelper.writableDatabase
            db.beginTransaction() // 开启事务
            try {
                db.delete("Book", null, null)
                if (true) {
                    throw NullPointerException() // 手动抛出一个异常，让事务失败
                }
                val values = ContentValues().apply {
                    put("name", "Game of Thrones")
                    put("author", "George Martin")
                    put("pages", 720)
                    put("price", 20.85)
                }
                db.insert("Book", null, values)
                db.setTransactionSuccessful() // 事务已经执行成功
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                db.endTransaction() // 结束事务
            }
        }


    }

}