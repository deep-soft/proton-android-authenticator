/*
 * Copyright (c) 2025 Proton AG
 * This file is part of Proton AG and Proton Authenticator.
 *
 * Proton Authenticator is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Proton Authenticator is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Proton Authenticator.  If not, see <https://www.gnu.org/licenses/>.
 */

package proton.android.authenticator.business.shared.infrastructure.persistence.room

import androidx.room.migration.AutoMigrationSpec
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val autoMigrations: List<AutoMigrationSpec> = emptyList()

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AuthenticatorDatabase::class.java,
        autoMigrations,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrateAll() {
        // Create earliest version of the database.
        helper.createDatabase(AuthenticatorDatabase.NAME, 1).apply { close() }

        val builder = androidx.room.Room.databaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            AuthenticatorDatabase::class.java,
            AuthenticatorDatabase.NAME
        ).addMigrations(*AuthenticatorDatabase.ManualMigrations)

        autoMigrations.forEach { builder.addAutoMigrationSpec(it) }

        val database = builder.build()

        // Ensure we are on the latest version
        helper.runMigrationsAndValidate(
            AuthenticatorDatabase.NAME,
            AuthenticatorDatabase.VERSION,
            VALIDATE_DROPPED_TABLES,
            *AuthenticatorDatabase.ManualMigrations
        )

        // Open latest version of the database. Room will validate the schema
        // once all migrations execute.
        database.openHelper.writableDatabase.close()
    }


    companion object {
        private const val VALIDATE_DROPPED_TABLES = true
    }
}
