package com.example.sprintstaskmanagement.data.database.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.example.sprintstaskmanagement.data.database.model.Project
import com.example.sprintstaskmanagement.data.database.model.Task

data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "project_id",
        entityColumn = "project_id"
    )
    val tasks: List<Task>
)
