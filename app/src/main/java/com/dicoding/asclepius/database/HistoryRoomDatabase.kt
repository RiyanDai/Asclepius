package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [History::class], version = 2, exportSchema = false)
abstract class HistoryRoomDatabase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): HistoryRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HistoryRoomDatabase::class.java,
                    "history_database"
                )
                    // Callback for creating and opening the database
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // This is where initial setup can be done after database creation
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            // This is where you can handle actions on database open if necessary
                        }
                    })
                    // Handling migration strategy from version 1 to version 2
                    .addMigrations(MIGRATION_1_2)
                    .fallbackToDestructiveMigration()  // Use destructive migration as fallback
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Migration from version 1 to version 2
        private val MIGRATION_1_2 = object : androidx.room.migration.Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Example migration script to add a new column
                // Uncomment and modify the following line according to your schema changes
                // database.execSQL("ALTER TABLE history ADD COLUMN new_column_name TEXT DEFAULT ''")
            }
        }
    }
}
