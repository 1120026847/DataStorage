package com.example.datastorage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


/*
Room 负责您平常使用 SQLiteOpenHelper 所处理的单调乏味的任务。
Room 使用 DAO 向其数据库发出查询请求。
为避免界面性能不佳，默认情况下，Room 不允许在主线程上发出查询请求。当 Room 查询返回 Flow 时，这些查询会在后台线程上自动异步运行。

 Room 数据库类必须是抽象且必须扩展 RoomDatabase。整个应用通常只需要一个 Room 数据库实例

 通过 @Database 将该类注解为 Room 数据库，并使用注解参数声明数据库中的实体以及设置版本号。每个实体都对应一个将在数据库中创建的表
 您定义了一个单例 WordRoomDatabase,，以防出现同时打开数据库的多个实例的情况。
getDatabase 会返回该单例。首次使用时，它会创建数据库，具体方法是：使用 Room 的数据库构建器在 WordRoomDatabase 类的应用上下文中创建 RoomDatabase 对象，并将其命名为 "word_database"

 在 WordRoomDatabase 中，您将创建 RoomDatabase.Callback() 的自定义实现，该实现也会获取 CoroutineScope 作为构造函数参数。然后，您将替换 onOpen 方法以填充数据库。

 若要在每次创建应用时删除所有内容并重新填充数据库，您将创建一个 RoomDatabase.Callback 并替换 onCreate()。由于您无法在界面线程上执行 Room 数据库操作，因此 onCreate() 会在 IO 调度程序上启动协程。
 */
// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.wordDao()

                    // Delete all content here.
                    wordDao.deleteAll()

                    // Add sample words.
                    var word = Word("Hello")
                    wordDao.insert(word)
                    word = Word("World!")
                    wordDao.insert(word)

                    // TODO: Add your own words!
                    word = Word("TODO!")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                        //将回调添加到数据库构建序列，然后在 Room.databaseBuilder() 上调用 .build()：
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}