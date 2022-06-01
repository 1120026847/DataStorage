package com.example.datastorage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
/*
@Entity(tableName = "word_table") 每个 @Entity 类代表一个 SQLite 表。为您的类声明添加注解，以表明它是实体。
如果您希望表的名称与类的名称不同，可以指定表的名称，此处的表名为“word_table”。

@PrimaryKey 每个实体都需要主键。为简便起见，每个字词都可充当自己的主键。
@ColumnInfo(name = "word") 如果您希望该表中列的名称与成员变量的名称不同，可以指定表中列的名称，此处的列名为“word”。
 */
@Entity(tableName = "word_table")
class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)