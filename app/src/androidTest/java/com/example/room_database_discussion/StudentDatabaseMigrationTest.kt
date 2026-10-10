
package com.example.room_database_discussion

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.room_database_discussion.data.StudentDatabase
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import android.content.Context

@RunWith(AndroidJUnit4::class)
class StudentDatabaseMigrationTest {

    @Test
    fun migrate1To2PreservesStudentsAndAddsAge() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val databaseName = "migration_test_database"

        context.deleteDatabase(databaseName)

        val oldDatabase = context.openOrCreateDatabase(databaseName, 0, null)

        oldDatabase.execSQL(
            """
            CREATE TABLE students (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                email TEXT NOT NULL,
                course TEXT NOT NULL
            )
            """.trimIndent()
        )

        val values = ContentValues().apply {
            put("name", "Nikko")
            put("email", "nikko@example.com")
            put("course", "Computer Engineering")
        }

        oldDatabase.insertOrThrow("students", null, values)
        oldDatabase.version = 1
        oldDatabase.close()

        val database = Room.databaseBuilder(
            context,
            StudentDatabase::class.java,
            databaseName
        )
            .addMigrations(StudentDatabase.MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()

        try {
            val result = database.openHelper.readableDatabase.query(
                "SELECT id, name, email, course, age FROM students"
            )

            result.use {
                assertEquals(1, it.count)
                assertEquals(true, it.moveToFirst())
                assertEquals("Nikko", it.getString(it.getColumnIndexOrThrow("name")))
                assertEquals(
                    "nikko@example.com",
                    it.getString(it.getColumnIndexOrThrow("email"))
                )
                assertEquals(
                    "Computer Engineering",
                    it.getString(it.getColumnIndexOrThrow("course"))
                )
                assertEquals(0, it.getInt(it.getColumnIndexOrThrow("age")))
                assertNotNull(it.getString(it.getColumnIndexOrThrow("name")))
            }
        } finally {
            database.close()
            context.deleteDatabase(databaseName)
        }
    }
}
