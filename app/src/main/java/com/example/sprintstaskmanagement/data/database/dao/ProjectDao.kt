package com.example.sprintstaskmanagement.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.relations.ProjectWithTasks
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Transaction
    @Query("SELECT * FROM projects WHERE owner_id = :ownerId")
    fun getProjectsForUserFlow(ownerId: Int): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE owner_id = :ownerId")
    suspend fun getProjectsForUserSuspend(ownerId: Int): List<Project>

    @Transaction
    @Query("SELECT * FROM projects WHERE project_id = :projectId")
    fun getProjectWithTasks(projectId: Int): Flow<ProjectWithTasks>
}