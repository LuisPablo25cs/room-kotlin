package cardenas.pablo.tareasapp.ui.theme

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import cardenas.pablo.tareasapp.AppDatabase
import cardenas.pablo.tareasapp.TaskDao
import cardenas.pablo.tareasapp.TaskEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class TaskViewModel (private val dao: TaskDao) : ViewModel() {
    private val _searchInput = MutableStateFlow("")
    val searchInput: StateFlow<String> = _searchInput.asStateFlow()
    private val _activeQuery = MutableStateFlow("")
    @OptIn(ExperimentalCoroutinesApi::class)
    val tasks: StateFlow<List<TaskEntity>> = _activeQuery
        .flatMapLatest { query ->
            dao.searchTasks(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addTask(title: String){
        if (title.isBlank()) return
        viewModelScope.launch {
            dao.insert(TaskEntity(titulo = title.trim()))
        }
    }
    fun toggleCompleted(task: TaskEntity) {
        viewModelScope.launch {
            dao.update(task.copy(completado = !task.completado))
        }
    }
    fun deleteTask(task: TaskEntity) {
        viewModelScope.launch {
            dao.delete(task)
        }
    }
    fun onSearchInputChanged(text: String) {
        _searchInput.value = text
    }
    fun executeSearch(){
        _activeQuery.value = _searchInput.value.trim()
    }
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                val dao = AppDatabase
                    .getInstance(application)
                    .taskDao()
                TaskViewModel(dao)
            }
        }
    }

}