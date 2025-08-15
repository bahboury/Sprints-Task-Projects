package com.example.sprintstaskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sprintstaskmanagement.data.database.repo.TaskRepository

class AppViewModelFactory(private val repository: TaskRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectListViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(TaskDetailViewModel::class.java)) {
            // Note: TaskDetailViewModel needs a projectId, so you'd need to pass that
            // to the factory or handle it in a different way.
            // This is a simplified example.
            throw IllegalArgumentException("TaskDetailViewModel requires a projectId to be passed to the factory.")
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}