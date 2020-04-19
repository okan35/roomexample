package com.example.roomexample.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [User::class],version = 2)
abstract class AppDatabase  : RoomDatabase() {
    abstract fun userDao(): UserDao



   /* val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Book ADD COLUMN pub_year INTEGER")
        }
    }*/


    private class UserDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    var wordDao = database.userDao()

                    // Delete all content here.
                    wordDao.deleteAll()

                    // Add sample words.
                    var word =
                        User("okan", "kaya")
                    wordDao.insert(word)
                    word = User("ozan", "kaya")
                    wordDao.insert(word)

                    // TODO: Add your own words!
                    word = User("cdc", "kaya")
                    wordDao.insert(word)
                }
            }
        }
    }

    companion object {
        //Singleton prevents multiple instances of db opening at the same time
        @Volatile
        private var INSTANCE: AppDatabase? = null
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                //database.execSQL("ALTER TABLE user DROP PRIMARY KEY, ADD uid INT NOT NULL AUTO_INCREMENT PRIMARY KEY")
                database.execSQL("DROP TABLE IF EXISTS user")
                database.execSQL("CREATE TABLE user (" +
                        "uid INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                        "first_name TEXT," +
                        "last_name TEXT)"
                )
            }
        }

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "word_database"
                ).addCallback(UserDatabaseCallback(scope))
                    .addMigrations(MIGRATION_1_2)
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}