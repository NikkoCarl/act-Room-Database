
package com.example.room_database_discussion.ui

import android.util.Patterns
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.room_database_discussion.data.Student
import com.example.room_database_discussion.viewmodel.StudentViewModel

private val AppBackground = Color(0xFF0D0B12)
private val AppCard = Color(0xFF191520)
private val AppCardLight = Color(0xFF221B2D)
private val Purple = Color(0xFFBB86FC)
private val PurpleDark = Color(0xFF8E5AD7)
private val MainText = Color(0xFFF7F3FC)
private val MutedText = Color(0xFFB8ADCA)
private val BorderColor = Color(0xFF40334F)
private val ErrorColor = Color(0xFFFF7D91)

private val courseOptions = listOf(
    "BSCpE",
    "BSCS",
    "BSEMC"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentScreen(viewModel: StudentViewModel) {
    val students by viewModel.students.collectAsState()
    val totalStudents by viewModel.totalStudents.collectAsState()
    val search by viewModel.search.collectAsState()
    val selectedCourse by viewModel.course.collectAsState()

    var showForm by remember { mutableStateOf(false) }
    var editingStudent by remember { mutableStateOf<Student?>(null) }
    var studentToDelete by remember { mutableStateOf<Student?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = AppBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "STUDENT MANAGER",
                        color = Purple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Students",
                        color = MainText,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Manage your student records",
                        color = MutedText,
                        fontSize = 12.sp
                    )
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = AppCardLight
                    ),
                    border = BorderStroke(1.dp, BorderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 17.dp,
                            vertical = 10.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = totalStudents.toString(),
                            color = Purple,
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "TOTAL",
                            color = MutedText,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = search,
                onValueChange = viewModel::searchStudents,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(14.dp),
                placeholder = {
                    Text("Search by student name", color = MutedText)
                },
                leadingIcon = {
                    Text("⌕", color = Purple, fontSize = 24.sp)
                },
                colors = darkFieldColors()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Course filters
            Text(
                text = "FILTER BY COURSE",
                color = MutedText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                val filters = listOf("All Courses") + courseOptions

                filters.forEach { courseName ->
                    FilterChip(
                        selected = selectedCourse == courseName,
                        onClick = {
                            viewModel.filterStudentsByCourse(courseName)
                        },
                        label = {
                            Text(
                                text = courseName,
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = androidx.compose.material3.FilterChipDefaults
                            .filterChipColors(
                                selectedContainerColor = PurpleDark,
                                selectedLabelColor = Color.White,
                                containerColor = AppCard,
                                labelColor = MutedText
                            ),
                        border = androidx.compose.material3.FilterChipDefaults
                            .filterChipBorder(
                                enabled = true,
                                selected = selectedCourse == courseName,
                                borderColor = BorderColor,
                                selectedBorderColor = Purple
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Student Records",
                    color = MainText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Button(
                    onClick = {
                        editingStudent = null
                        showForm = true
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleDark,
                        contentColor = Color.White
                    ),
                    contentPadding = androidx.compose.foundation.layout
                        .PaddingValues(horizontal = 13.dp, vertical = 9.dp)
                ) {
                    Text("+ Add Student", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            if (students.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "No students found",
                            color = MainText,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            text = "Add a student or change your search.",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    items(
                        items = students,
                        key = { it.id }
                    ) { student ->
                        StudentCard(
                            student = student,
                            onEdit = {
                                editingStudent = student
                                showForm = true
                            },
                            onDelete = {
                                studentToDelete = student
                            }
                        )
                    }
                }
            }
        }
    }


    if (showForm) {
        StudentFormDialog(
            student = editingStudent,
            onDismiss = { showForm = false },
            onSave = { name, email, course, age ->
                val current = editingStudent

                if (current == null) {
                    viewModel.addStudent(
                        name.trim(),
                        email.trim(),
                        course,
                        age
                    )
                } else {
                    viewModel.updateStudent(
                        current.copy(
                            name = name.trim(),
                            email = email.trim(),
                            course = course,
                            age = age
                        )
                    )
                }

                showForm = false
            }
        )
    }


    studentToDelete?.let { student ->
        AlertDialog(
            onDismissRequest = { studentToDelete = null },
            containerColor = AppCard,
            titleContentColor = MainText,
            textContentColor = MutedText,
            title = {
                Text("Delete student?")
            },
            text = {
                Text("Remove ${student.name} from the student records?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteStudent(student)
                        studentToDelete = null
                    }
                ) {
                    Text("Delete", color = ErrorColor)
                }
            },
            dismissButton = {
                TextButton(onClick = { studentToDelete = null }) {
                    Text("Cancel", color = Purple)
                }
            }
        )
    }
}

@Composable
private fun StudentCard(
    student: Student,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = AppCard),
        border = BorderStroke(1.dp, BorderColor)
    ) {
        Column(modifier = Modifier.padding(13.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color = AppCardLight,
                            shape = RoundedCornerShape(13.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = student.name.trim()
                            .firstOrNull()
                            ?.uppercase() ?: "?",
                        color = Purple,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 11.dp)
                ) {
                    Text(
                        text = student.name,
                        color = MainText,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = student.email,
                        color = MutedText,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = student.course,
                    color = Purple,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(9.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Age: ${student.age}",
                    color = MutedText,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.weight(1f))

                TextButton(
                    onClick = onEdit,
                    contentPadding = androidx.compose.foundation.layout
                        .PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("Edit", color = Purple, fontSize = 12.sp)
                }

                TextButton(
                    onClick = onDelete,
                    contentPadding = androidx.compose.foundation.layout
                        .PaddingValues(horizontal = 8.dp, vertical = 0.dp)
                ) {
                    Text("Delete", color = ErrorColor, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun StudentFormDialog(
    student: Student?,
    onDismiss: () -> Unit,
    onSave: (name: String, email: String, course: String, age: Int) -> Unit
) {
    var name by remember(student) {
        mutableStateOf(student?.name.orEmpty())
    }
    var email by remember(student) {
        mutableStateOf(student?.email.orEmpty())
    }
    var course by remember(student) {
        mutableStateOf(
            student?.course?.takeIf { it in courseOptions }
                ?: courseOptions.first()
        )
    }
    var age by remember(student) {
        mutableStateOf(student?.age?.toString().orEmpty())
    }
    var courseMenuExpanded by remember { mutableStateOf(false) }
    var errorMessage by remember(student) {
        mutableStateOf<String?>(null)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = AppCard,
        titleContentColor = MainText,
        textContentColor = MutedText,
        shape = RoundedCornerShape(22.dp),
        title = {
            Text(
                text = if (student == null) "Add Student" else "Edit Student",
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Enter the student details below.",
                    color = MutedText,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Full name") },
                    shape = RoundedCornerShape(12.dp),
                    colors = darkFieldColors()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMessage = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Email address") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = darkFieldColors()
                )


                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { courseMenuExpanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, BorderColor)
                    ) {
                        Text(
                            text = "Course: $course",
                            color = MainText
                        )
                    }

                    DropdownMenu(
                        expanded = courseMenuExpanded,
                        onDismissRequest = {
                            courseMenuExpanded = false
                        },
                        modifier = Modifier.background(AppCardLight)
                    ) {
                        courseOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(option, color = MainText)
                                },
                                onClick = {
                                    course = option
                                    courseMenuExpanded = false
                                    errorMessage = null
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = age,
                    onValueChange = {
                        if (it.length <= 3 && it.all(Char::isDigit)) {
                            age = it
                            errorMessage = null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Age (1–120)") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = darkFieldColors()
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage!!,
                        color = ErrorColor,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val cleanName = name.trim()
                    val cleanEmail = email.trim()
                    val parsedAge = age.toIntOrNull()

                    errorMessage = when {
                        cleanName.isBlank() ->
                            "Please enter the student's name."

                        cleanEmail.isBlank() ->
                            "Please enter an email address."

                        !Patterns.EMAIL_ADDRESS.matcher(cleanEmail).matches() ->
                            "Please enter a valid email address."

                        course !in courseOptions ->
                            "Please select a valid course."

                        parsedAge == null || parsedAge !in 1..120 ->
                            "Age must be between 1 and 120."

                        else -> null
                    }

                    if (errorMessage == null && parsedAge != null) {
                        onSave(cleanName, cleanEmail, course, parsedAge)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PurpleDark,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (student == null) "Add Student" else "Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = Purple)
            }
        }
    )
}

@Composable
private fun darkFieldColors() =
    androidx.compose.material3.OutlinedTextFieldDefaults.colors(
        focusedTextColor = MainText,
        unfocusedTextColor = MainText,
        focusedContainerColor = AppCard,
        unfocusedContainerColor = AppCard,
        focusedBorderColor = Purple,
        unfocusedBorderColor = BorderColor,
        focusedLabelColor = Purple,
        unfocusedLabelColor = MutedText,
        cursorColor = Purple,
        focusedPlaceholderColor = MutedText,
        unfocusedPlaceholderColor = MutedText
    )
