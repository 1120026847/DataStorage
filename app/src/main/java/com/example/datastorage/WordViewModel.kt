package com.example.datastorage

import androidx.lifecycle.*
import kotlinx.coroutines.launch
/*
ViewModel 的作用是向界面提供数据，不受配置变化的影响。ViewModel 充当存储库和界面之间的通信中心

ViewModel 以一种可以感知生命周期的方式保存应用的界面数据

activity 和 fragment 负责将数据绘制到屏幕上，ViewModel 则负责保存并处理界面所需的所有数据。

LiveData 是一种可观察的数据存储器，每当数据发生变化时，您都会收到通知,ViewModel 会将存储库中的数据从 Flow 转换为 LiveData，并将字词列表作为 LiveData 传递给界面。这样可以确保每次数据库中的数据发生变化时，界面都会自动更新。

 创建了一个名为 WordViewModel 的类，该类可获取 WordRepository 作为参数并扩展 ViewModel。存储库是 ViewModel 需要的唯一依赖项。如果需要其他类，系统也会在构造函数中传递相应的类。
添加了一个公开的 LiveData 成员变量以缓存字词列表。
使用存储库中的 allWords Flow 初始化了 LiveData。然后，您通过调用 asLiveData(). 将该 Flow 转换成了 LiveData。
创建了一个可调用存储库的 insert() 方法的封装容器 insert() 方法。这样一来，便可从界面封装 insert() 的实现。我们将启动新协程并调用存储库的挂起函数 insert。如上所述，ViewModel 的协程作用域基于它的名为 viewModelScope 的生命周期（您将在这里使用）。
创建了 ViewModel，并实现了 ViewModelProvider.Factory，后者可获取创建 WordViewModel 所需的依赖项作为参数：WordRepository。


 */
class WordViewModel(private val repository: WordRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allWords: LiveData<List<Word>> = repository.allWords.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }
}

class WordViewModelFactory(private val repository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
