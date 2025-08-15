package com.example.sprintstaskmanagement.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.sprintstaskmanagement.data.database.AppDatabase
import com.example.sprintstaskmanagement.data.database.model.Project
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
                    // 4. Get the ViewModel using the factory
                    val projectListViewModel =
                        ViewModelProvider(this, factory)[ProjectListViewModel::class.java]

                    // 5. Pass the ViewModel to your UI
                    ProjectListScreen(projectListViewModel)
                }
            }
        }
    }

    // This function can be called from onCreate() in MainActivity
    fun runDatabaseTest(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val projectDao = db.projectDao()
        val ownerId = 1

        lifecycleScope.launch {
            // --- 1. Demonstrate Suspend Query ---
            Log.d("DB_TEST", "--- Suspend Query Demonstration ---")

            // Insert a project
            projectDao.insertProject(Project(title = "First Project", ownerId = ownerId))

            // Perform a one-time fetch
            val projectsOneShot = projectDao.getProjectsForUserSuspend(ownerId)
            Log.d("DB_TEST", "Suspend Result: ${projectsOneShot.size} projects found.")

            // Insert another project. The suspend query will not be notified.
            projectDao.insertProject(Project(title = "Second Project", ownerId = ownerId))

            // Re-run the suspend query to get the updated list
            val projectsOneShotAgain = projectDao.getProjectsForUserSuspend(ownerId)
            Log.d("DB_TEST", "Suspend Result (After new insert): ${projectsOneShotAgain.size} projects found.")

            // --- 2. Demonstrate Flow Query ---
            Log.d("DB_TEST", "--- Flow Query Demonstration ---")

            val projectsFlow = projectDao.getProjectsForUserFlow(ownerId)

            // Collect the initial value from the Flow
            val initialProjects = projectsFlow.first()
            Log.d("DB_TEST", "Flow Initial Result: ${initialProjects.size} projects found.")

            // Insert another project. The Flow will automatically emit a new value.
            projectDao.insertProject(Project(title = "Third Project", ownerId = ownerId))

            // Now, collect again. The Flow will automatically emit the updated list.
            val updatedProjects = projectsFlow.first()
            Log.d("DB_TEST", "Flow Updated Result (Automatic): ${updatedProjects.size} projects found.")
        }
    }
}

