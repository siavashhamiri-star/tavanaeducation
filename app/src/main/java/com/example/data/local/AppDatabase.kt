package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.AiChatDao
import com.example.data.local.dao.CourseDao
import com.example.data.local.dao.EnrollmentDao
import com.example.data.local.dao.ExamDao
import com.example.data.local.dao.LessonDao
import com.example.data.local.dao.ProjectDao
import com.example.data.local.dao.SkillDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.AiChatMessageEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.EnrollmentEntity
import com.example.data.local.entity.ExamEntity
import com.example.data.local.entity.LessonEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.SkillEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        CourseEntity::class,
        LessonEntity::class,
        EnrollmentEntity::class,
        SkillEntity::class,
        ProjectEntity::class,
        ExamEntity::class,
        AiChatMessageEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun courseDao(): CourseDao
    abstract fun lessonDao(): LessonDao
    abstract fun enrollmentDao(): EnrollmentDao
    abstract fun skillDao(): SkillDao
    abstract fun projectDao(): ProjectDao
    abstract fun examDao(): ExamDao
    abstract fun aiChatDao(): AiChatDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tavana_education_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
