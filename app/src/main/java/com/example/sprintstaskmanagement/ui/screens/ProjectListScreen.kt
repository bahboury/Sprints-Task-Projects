package com.example.sprintstaskmanagement.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sprintstaskmanagement.ui.screens.widgets.ProjectCard
import com.example.sprintstaskmanagement.viewmodel.ProjectListViewModel

@Composable
fun ProjectListScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectListViewModel = viewModel()
) {
    val padding = 16.dp
    val projects by viewModel.projects.collectAsState()

    Column(modifier = modifier){
        if (projects.isEmpty()) {
            Text(
                text = "No projects found",
                modifier = Modifier.padding(padding)
            )
        } else {
            LazyColumn {
                items(projects) { project ->
                    ProjectCard(project = project)
                }
            }
        }
    }
}