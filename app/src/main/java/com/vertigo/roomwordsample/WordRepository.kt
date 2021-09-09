package com.vertigo.roomwordsample

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

/**
 * DAO передается конструктору репозитория, а не всей базе данных. Это связано с тем, что ему нужен только доступ к DAO,
 поскольку DAO содержит все методы чтения / записи для базы данных. Нет необходимости предоставлять всю базу данных в репозиторий.
 * Список слов является публичным достоянием. Он инициализируется получением Flow списка слов из Room;
 это можно сделать благодаря тому, как вы определили getAlphabetizedWords метод возврата Flow на шаге «Наблюдение за изменениями базы данных».
 Room выполняет все запросы в отдельном потоке.
 * suspend Модификатор указывает компилятору , что это должен быть вызван из сопрограммы или другой функции подвешенной.
 * Room выполняет запросы на приостановку вне основного потока.
 */

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
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