package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.AiChatMessageEntity
import com.example.data.local.entity.CourseEntity
import com.example.data.local.entity.EnrollmentEntity
import com.example.data.local.entity.ExamEntity
import com.example.data.local.entity.LessonEntity
import com.example.data.local.entity.ProjectEntity
import com.example.data.local.entity.SkillEntity
import com.example.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserFlow(userId: String = "default_user_1"): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUserOnce(userId: String = "default_user_1"): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("UPDATE users SET roleName = :roleName WHERE id = :userId")
    suspend fun updateUserRole(roleName: String, userId: String = "default_user_1")

    @Query("UPDATE users SET selectedWorld = :worldName WHERE id = :userId")
    suspend fun updateSelectedWorld(worldName: String, userId: String = "default_user_1")

    @Query("UPDATE users SET xpPoints = xpPoints + :addXp WHERE id = :userId")
    suspend fun addXpPoints(addXp: Int, userId: String = "default_user_1")
}

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE academyWorld = :world")
    fun getCoursesByWorld(world: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId LIMIT 1")
    fun getCourseById(courseId: String): Flow<CourseEntity?>

    @Query("SELECT * FROM courses WHERE isEnrolled = 1")
    fun getEnrolledCourses(): Flow<List<CourseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourses(courses: List<CourseEntity>)

    @Query("UPDATE courses SET isEnrolled = :isEnrolled WHERE id = :courseId")
    suspend fun updateEnrollmentStatus(courseId: String, isEnrolled: Boolean)
}

@Dao
interface LessonDao {
    @Query("SELECT * FROM lessons WHERE courseId = :courseId")
    fun getLessonsForCourse(courseId: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLessons(lessons: List<LessonEntity>)

    @Query("UPDATE lessons SET isCompleted = :isCompleted WHERE id = :lessonId")
    suspend fun updateLessonCompletion(lessonId: String, isCompleted: Boolean)
}

@Dao
interface EnrollmentDao {
    @Query("SELECT * FROM enrollments WHERE userId = :userId")
    fun getUserEnrollments(userId: String = "default_user_1"): Flow<List<EnrollmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: EnrollmentEntity)
}

@Dao
interface SkillDao {
    @Query("SELECT * FROM skills WHERE userId = :userId")
    fun getUserSkills(userId: String = "default_user_1"): Flow<List<SkillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSkill(skill: SkillEntity)
}

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects WHERE userId = :userId")
    fun getUserProjects(userId: String = "default_user_1"): Flow<List<ProjectEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)
}

@Dao
interface ExamDao {
    @Query("SELECT * FROM exams")
    fun getAllExams(): Flow<List<ExamEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExams(exams: List<ExamEntity>)

    @Query("UPDATE exams SET isAttempted = 1, lastScorePercent = :score WHERE id = :examId")
    suspend fun recordExamScore(examId: String, score: Int)
}

@Dao
interface AiChatDao {
    @Query("SELECT * FROM ai_chats ORDER BY timestamp ASC")
    fun getAllChatMessages(): Flow<List<AiChatMessageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: AiChatMessageEntity)

    @Query("DELETE FROM ai_chats")
    suspend fun clearHistory()
}
