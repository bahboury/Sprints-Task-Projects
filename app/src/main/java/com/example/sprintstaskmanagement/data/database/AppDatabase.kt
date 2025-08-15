package com.example.sprintstaskmanagement.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.sprintstaskmanagement.data.database.dao.AttachmentDao
import com.example.sprintstaskmanagement.data.database.dao.ProjectDao
import com.example.sprintstaskmanagement.data.database.dao.TaskDao
import com.example.sprintstaskmanagement.data.database.dao.UserDao
import com.example.sprintstaskmanagement.data.database.model.Attachment
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.model.Task
import com.example.sprintstaskmanagement.data.database.model.User

@Database(
    entities = [
        User::class,
        Project::class,
        Task::class,
        Attachment::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun projectDao(): ProjectDao
    abstract fun taskDao(): TaskDao
    abstract fun attachmentDao(): AttachmentDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}