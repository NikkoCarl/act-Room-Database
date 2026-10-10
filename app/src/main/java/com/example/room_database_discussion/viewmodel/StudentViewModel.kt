
package com.example.room_database_discussion.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.room_database_discussion.data.Student
import com.example.room_database_discussion.data.StudentDatabase
import com.example.room_database_discussion.data.StudentRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class StudentViewModel(application: Application) :
    AndroidViewModel(application) {

    private val database = StudentDatabase.getData(application)
    private val repository = StudentRepository(database.studentDao())

    private val _search = MutableStateFlow("")
    val search: StateFlow<String> = _search.asStateFlow()

    private val _course = MutableStateFlow("All Courses")
    val course: StateFlow<String> = _course.asStateFlow()

    val students: StateFlow<List<Student>> =
        combine(_search, _course) { searchText, selectedCourse ->
            searchText to selectedCourse
        }.flatMapLatest { (searchText, selectedCourse) ->
            repository.getFilteredStudents(searchText, selectedCourse)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    val totalStudents: StateFlow<Int> =
        repository.totalStudents.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0
        )

    fun searchStudents(value: String) {
        _search.value = value
    }

    fun filterStudentsByCourse(value: String) {
        _course.value = value
    }

    fun addStudent(name: String, email: String, course: String, age: Int) {
        viewModelScope.launch {
            repository.insertStudent(
                Student(
                    name = name.trim(),
                    email = email.trim(),
                    course = course,
                    age = age
                )
            )
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch {
            repository.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch {
            repository.deleteStudent(student)
        }
    }
}
