package com.vertigo.roomwordsample

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Entity(tableName = "word_table") - Каждый @Entity класс представляет собой таблицу SQLite.
Аннотируйте свое объявление класса, чтобы указать, что это сущность. Вы можете указать имя таблицы, если хотите, чтобы оно отличалось от имени класса.
Это называет таблицу "word_table".
 * @PrimaryKey Каждой сущности нужен первичный ключ. Для простоты каждое слово действует как собственный первичный ключ.
 * @ColumnInfo(name = "word")Задает имя столбца в таблице, если вы хотите, чтобы оно отличалось от имени переменной-члена. Это имя столбца «слово».
 * Каждое свойство, хранящееся в базе данных, должно быть общедоступным, что является значением по умолчанию в Kotlin.
 * @PrimaryKey(autoGenerate = true) val id: Int - можно автоматически генерировать
 */
@Entity(tableName = "word_table")
data class Word(@PrimaryKey @ColumnInfo(name = "word") val word: String)
