package com.example.sprintstaskmanagement.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sprintstaskmanagement.data.database.model.Task
import com.example.sprintstaskmanagement.data.database.repo.TaskRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskDetailViewModel(
    private val taskRepository: TaskRepository,
    private val projectId: Int
) : ViewModel() {
    val tasks = taskRepository.getTasksForProjects(projectId)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTask(description: String) {
        viewModelScope.launch {
            taskRepository.insertTask(
                Task(description = description, projectId = projectId)
            )
        }
    }
}