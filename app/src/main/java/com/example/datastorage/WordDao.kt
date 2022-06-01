package com.example.datastorage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/*
在 DAO（数据访问对象）中，您可以指定 SQL 查询并将其与方法调用相关联。编译器会检查 SQL 并根据常见查询的方便的注解（如 @Insert）生成查询。Room 会使用 DAO 为代码创建整洁的 API。

 WordDao 是一个接口，DAO 必须是接口或抽象类。
@Dao 注解将其标识为 Room 的 DAO 类。
suspend fun insert(word: Word)：声明挂起函数以插入一个字词。
@Insert 注解是一种特殊的 DAO 方法注解，使用 DAO 方法注解时，您无需提供任何 SQL！（还有用于删除和更新行的 @Delete 和 @Update 注解，不过在此应用中未用到这些注解。）
onConflict = OnConflictStrategy.IGNORE：所选 onConflict 策略将忽略与列表中的现有字词完全相同的新字词。如需详细了解可用的冲突策略，请参阅相关文档。
suspend fun deleteAll()：声明挂起函数以删除所有字词。

 当数据发生变化时，您通常需要执行某些操作，例如在界面中显示更新后的数据。这意味着您必须观察数据，以便在数据发生变化后作出回应。

为了观察数据变化情况，您将使用 kotlinx-coroutines 中的 Flow。在方法说明中使用 Flow 类型的返回值；当数据库更新时，Room 会生成更新 Flow 所需的所有代码。
 */
@Dao
interface WordDao {
//获取所有字词（按字母顺序排序）
@Query("SELECT * FROM word_table ORDER BY word ASC")
fun getAlphabetizedWords(): Flow<List<Word>>
//获取所有字词（按字母顺序排序）
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: Word)
//删除所有字词
    @Query("DELETE FROM word_table")
    suspend fun deleteAll()
}