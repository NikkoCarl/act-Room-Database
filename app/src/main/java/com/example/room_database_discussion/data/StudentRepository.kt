
package com.example.room_database_discussion.data

import kotlinx.coroutines.flow.Flow

class StudentRepository(private val studentDao: StudentDao) {

    val students: Flow<List<Student>> = studentDao.getAllStudents()

    val totalStudents: Flow<Int> = studentDao.getStudentCount()

    fun searchStudents(search: String): Flow<List<Student>> {
        return studentDao.searchStudents(search)
    }

    fun filterStudentsByCourse(course: String): Flow<List<Student>> {
        return studentDao.filterStudentsByCourse(course)
    }

    fun getFilteredStudents(
        search: String,
        course: String
    ): Flow<List<Student>> {
        return studentDao.getFilteredStudents(search, course)
    }

    suspend fun insertStudent(student: Student) {
        studentDao.insertStudent(student)
    }

    suspend fun updateStudent(student: Student) {
        studentDao.updateStudent(student)
    }

    suspend fun deleteStudent(student: Student) {
        studentDao.deleteStudent(student)
    }
}
