# 📌 Sprints Task Management App

This project is a Task Management app data layer built with Room Database. It demonstrates handling Users, Projects, Tasks, and Attachments, with features such as `TypeConverters`, `Relations`, `Suspend` vs `Flow` queries, and performance testing.

-----

## 📂 Project Structure

```
data/database/
  ├── dao/ (UserDao, ProjectDao, TaskDao, AttachmentDao)
  ├── model/ (User, Project, Task, Attachment)
  ├── relations/ (ProjectWithTasks)
  ├── repo/ (TaskRepository)
  ├── AppDatabase.kt
  └── Converters.kt
```

-----

## 🛠️ Tech Stack

  * **Kotlin**
  * **Room Database**
  * **Coroutines / Flow**
  * **Jetpack Compose** for UI

-----

## ✅ 2.2 Room Schema UML

Below is the UML diagram of the Room schema.

```mermaid
erDiagram
    User {
        INT user_id PK
        STRING user_name
        STRING user_email UNIQUE
    }
    Project {
        INT project_id PK
        STRING project_title
        INT owner_id FK
    }
    Task {
        INT task_id PK
        STRING task_description
        INT project_id FK
    }
    Attachment {
        INT attachment_id PK
        STRING file_path
        INT task_id FK
    }

    User ||--o{ Project : "owns"
    Project ||--o{ Task : "contains"
    Task ||--o{ Attachment : "has"
```

-----

## ✅ 2.3 TypeConverters & Relations

We created two `TypeConverters`:

  * **`Date`** ↔ **`Long`** (timestamps)
  * **`List<String>`** ↔ **`String`** (comma-separated)

We also implemented a relation using `@Embedded` and `@Relation` to model a one-to-many relationship.

```kotlin
data class ProjectWithTasks(
    @Embedded val project: Project,
    @Relation(
        parentColumn = "project_id",
        entityColumn = "project_id"
    )
    val tasks: List<Task>
)
```

**Example log:**

```
Log.d("DB_TEST", "Project with Tasks: $result")
```

-----

## ✅ 2.4 Suspend DAO vs Flow DAO

In simple terms:

  * **`suspend` fun queries** return a one-time snapshot. They run once and do not update automatically.
  * **`Flow` queries** return a continuous stream of updates. Whenever the database changes, a new list is emitted automatically.

**Example logs:**

```
Suspend Result: 1 project found.
Flow Updated Result: 2 projects found.
```

-----

## ✅ 2.5 LiveData & Flow Integration

  * **`LiveData`**: Observed in the UI using `.observe()` (or `observeAsState()` in Compose).
  * **`Flow`**: Collected inside `lifecycleScope.launch { collectLatest { … } }`.

**Example usage in a ViewModel:**

```kotlin
val projects: StateFlow<List<Project>> = repository.getProjectsForUser(ownerId)
    .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
```

**Example log:**

```
Observed Projects via LiveData: [...]
Collected Tasks via Flow: [...]
```

-----

## ✅ 2.6 Raw SQL & Performance Comparison

We tested fetching all projects with more than three tasks using both `@Query` and `@RawQuery`.

| Query Type | Total Time (100 runs) | Average per Run |
| :--- | :--- | :--- |
| `@Query` (Room) | 120 ms | 1.20 ms |
| `@RawQuery` (SQL) | 105 ms | 1.05 ms |

**Observation:**

  * `@RawQuery` is slightly faster since it bypasses Room’s parsing.
  * `@Query` is safer with compile-time validation.
  * For most use cases, **`@Query` is preferred**.
