package com.example.datastorage

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
/*
存储库类会将多个数据源的访问权限抽象化
存储库类会提供一个整洁的 API，用于获取对应用其余部分的数据访问权限。

存储库可管理查询，且允许您使用多个后端。在最常见的示例中，存储库可实现对以下任务做出决定时所需的逻辑：是否从网络中提取数据；是否使用缓存在本地数据库中的结果。


 */
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}