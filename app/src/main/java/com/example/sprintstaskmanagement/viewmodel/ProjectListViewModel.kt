package com.example.sprintstaskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.repo.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProjectListViewModel(
    private val repository: TaskRepository
) : ViewModel() {
    // Using a hardcoded ID for demonstration purposes
    private val ownerId = 1

    val projects: StateFlow<List<Project>> = repository.getProjectsForUser(ownerId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addProject(title: String) {
        viewModelScope.launch {
            repository.insertProject(Project(title = title, ownerId = ownerId))
        }
    }
}