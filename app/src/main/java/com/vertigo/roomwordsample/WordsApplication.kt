package com.vertigo.roomwordsample

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Создал экземпляр базы данных.
 * Создал экземпляр репозитория на основе базы данных DAO.
 * Поскольку эти объекты должны создаваться только тогда,
когда они впервые нужны, а не при запуске приложения, вы используете делегирование свойств Kotlin: by lazy.
 */
class WordsApplication: Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { WordRepository(database.wordDao()) }
}