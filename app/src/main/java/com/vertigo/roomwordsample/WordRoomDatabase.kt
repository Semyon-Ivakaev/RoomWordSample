package com.vertigo.roomwordsample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Класс базы данных для Room должен быть abstract и расширятьRoomDatabase.
 * Вы аннотируете класс как базу данных Room @Database и используете параметры аннотации для объявления сущностей, которые принадлежат базе данных,
 и установки номера версии. Каждой сущности соответствует таблица, которая будет создана в базе данных.
 Миграции базы данных выходят за рамки этой кодовой таблицы, поэтому exportSchema здесь установлено значение false,
 чтобы избежать предупреждения сборки. В реальном приложении рассмотрите возможность установки каталога для Room,
 который будет использоваться для экспорта схемы, чтобы вы могли проверить текущую схему в своей системе контроля версий.
 * База данных предоставляет DAO с помощью абстрактного метода получения для каждого @Dao.
 * Вы определили синглтон , WordRoomDatabase,чтобы предотвратить одновременное открытие нескольких экземпляров базы данных.
 * getDatabase возвращает синглтон. Он создаст базу данных при первом доступе, используя построитель базы данных Room,
 чтобы создать Room Database объект в контексте приложения из WordRoomDatabase класса и присвоить ему имя "word_database".
 */

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
public abstract class WordRoomDatabase: RoomDatabase() {
    abstract fun wordDao(): WordDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }
}