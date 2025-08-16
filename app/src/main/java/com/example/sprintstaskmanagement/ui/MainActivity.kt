package com.example.sprintstaskmanagement.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sprintstaskmanagement.data.database.AppDatabase
import com.example.sprintstaskmanagement.data.database.model.Attachment
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.model.Task
import com.example.sprintstaskmanagement.data.database.model.User
import com.example.sprintstaskmanagement.data.database.repo.TaskRepository
import com.example.sprintstaskmanagement.ui.screens.ProjectListScreen
import com.example.sprintstaskmanagement.ui.theme.SprintsTaskManagementTheme
import com.example.sprintstaskmanagement.viewmodel.AppViewModelFactory
import com.example.sprintstaskmanagement.viewmodel.ProjectListViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(this)

        val repository = TaskRepository(
            userDao = database.userDao(),
            projectDao = database.projectDao(),
            taskDao = database.taskDao(),
            attachmentDao = database.attachmentDao()
        )

        // 3. Create the ViewModel Factory
        val factory = AppViewModelFactory(repository)

        runDatabaseTest(this)

        setContent {
            SprintsTaskManagementTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val projectListViewModel =
                        ViewModelProvider(this, factory)[ProjectListViewModel::class.java]

                    // 1. Wrap your content with a Scaffold
                    Scaffold(
                        topBar = {
                            // You can place your top bar here, e.g., a TopAppBar
                        }
                    ) { innerPadding ->
                        // 2. Pass the innerPadding to your screen composable
                        ProjectListScreen(
                            modifier = Modifier.padding(innerPadding),
                            viewModel = projectListViewModel
                        )
                    }
                }
            }
        }
    }
    // This function can be called from onCreate() in MainActivity
    fun runDatabaseTest(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val userDao = db.userDao()
        val projectDao = db.projectDao()
        val taskDao = db.taskDao()
        val attachmentDao = db.attachmentDao()
        val ownerId = 1

        lifecycleScope.launch {
            // --- 1. Insert and Log User ---
            Log.d("DB_TEST", "--- User Insert Demonstration ---")
            val user = User(name = "John Doe", email = "john.doe@example.com")
            val userId = userDao.insertUser(user)
            Log.d("DB_TEST", "Inserted User: $user")

            // --- 2. Insert and Log Project ---
            Log.d("DB_TEST", "--- Project Insert Demonstration ---")
            val project = Project(title = "First Project", ownerId = ownerId)
            projectDao.insertProject(project)
            Log.d("DB_TEST", "Inserted Project: $project")

            // --- 3. Insert and Log Task ---
            Log.d("DB_TEST", "--- Task Insert Demonstration ---")
            val task = Task(description = "Complete UI design", projectId = 1)
            taskDao.insertTask(task)
            Log.d("DB_TEST", "Inserted Task: $task")

            // --- 4. Insert and Log Attachment ---
            Log.d("DB_TEST", "--- Attachment Insert Demonstration ---")
            val attachment = Attachment(filePath = "\"F:\\schema.png\"", taskId = 1)
            attachmentDao.insertAttachment(attachment)
            Log.d("DB_TEST", "Inserted Attachment: $attachment")

            // --- 5. Demonstrate Suspend Query ---
            Log.d("DB_TEST", "--- Suspend Query Demonstration ---")
            val projectsOneShot = projectDao.getProjectsForUserSuspend(ownerId)
            Log.d("DB_TEST", "Suspend Result: ${projectsOneShot.size} projects found.")

            // Insert another project
            projectDao.insertProject(Project(title = "Second Project", ownerId = ownerId))
            Log.d("DB_TEST", "Inserted Project: Second Project")

            // Re-run the suspend query
            val projectsOneShotAgain = projectDao.getProjectsForUserSuspend(ownerId)
            Log.d(
                "DB_TEST",
                "Suspend Result (After new insert): ${projectsOneShotAgain.size} projects found."
            )

            // --- 6. Demonstrate Flow Query ---
            Log.d("DB_TEST", "--- Flow Query Demonstration ---")
            val projectsFlow = projectDao.getProjectsForUserFlow(ownerId)
            val initialProjects = projectsFlow.first()
            Log.d("DB_TEST", "Flow Initial Result: ${initialProjects.size} projects found.")

            // Insert another project
            projectDao.insertProject(Project(title = "Third Project", ownerId = ownerId))
            Log.d("DB_TEST", "Inserted Project: Third Project")

            // Collect updated Flow
            val updatedProjects = projectsFlow.first()
            Log.d(
                "DB_TEST",
                "Flow Updated Result (Automatic): ${updatedProjects.size} projects found."
            )

            Log.d("DB_TEST", "--- Project With Tasks Demonstration ---")
            val projectWithTasksFlow = projectDao.getProjectWithTasks(1)
            val projectWithTasks = projectWithTasksFlow.first()
            Log.d("DB_TEST", "Project with Tasks: $projectWithTasks")
        }
    }
}
