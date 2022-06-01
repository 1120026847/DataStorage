package com.example.datastorage

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/*
将存储库和数据库实例化
您希望应用中的数据库和存储库只有一个实例。实现该目的的一种简单的方法是，将它们作为 Application 类的成员进行创建

 创建了一个数据库实例。
创建了一个基于数据库 DAO 的存储库实例。
由于这些对象只在首次需要时才应该创建，而非在应用启动时创建，因此您将使用 Kotlin 的属性委托：by lazy

包含 applicationScope，然后将其传递给 WordRoomDatabase.getDatabase
 */
class WordsApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}