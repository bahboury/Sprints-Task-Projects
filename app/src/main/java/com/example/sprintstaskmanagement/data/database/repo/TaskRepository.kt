package com.example.sprintstaskmanagement.data.database.repo

import com.example.sprintstaskmanagement.data.database.dao.AttachmentDao
import com.example.sprintstaskmanagement.data.database.dao.ProjectDao
import com.example.sprintstaskmanagement.data.database.dao.TaskDao
import com.example.sprintstaskmanagement.data.database.dao.UserDao
import com.example.sprintstaskmanagement.data.database.model.Attachment
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.model.Task
import com.example.sprintstaskmanagement.data.database.model.User
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val userDao: UserDao,
    private val projectDao: ProjectDao,
    private val taskDao: TaskDao,
    private val attachmentDao: AttachmentDao
) {
    // --- User operations ---
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    fun getAllUsers(): Flow<List<User>> = userDao.getAllUsers()

    // --- Project operations ---
    suspend fun insertProject(project: Project) = projectDao.insertProject(project)
    fun getProjectsForUser(ownerId: Int) = projectDao.getProjectsForUserFlow(ownerId)

    // --- Task operations ---
    suspend fun insertTask(task: Task) = taskDao.insertTask(task)
    fun getTasksForProjects(projectId: Int) = taskDao.getTasksForProject(projectId)

    // --- Attachment operations ---
    suspend fun insertAttachment(attachment: Attachment) = attachmentDao.insertAttachment(attachment)
    fun getAttachmentsForTask(taskId: Int): Flow<List<Attachment>> = attachmentDao.getAttachmentsForTask(taskId)
}