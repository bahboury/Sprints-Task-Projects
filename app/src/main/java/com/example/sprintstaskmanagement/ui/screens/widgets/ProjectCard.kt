package com.example.sprintstaskmanagement.ui.screens.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sprintstaskmanagement.data.database.model.Project

@Composable
fun ProjectCard(project: Project) {
    val horizontalPadding = 16.dp
    val verticalPadding = 8.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = horizontalPadding,
                vertical = verticalPadding
            )
    ) {
        Column(modifier = Modifier.padding(horizontalPadding)) {
            Text(
                text = project.title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Project ID: ${project.id}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}