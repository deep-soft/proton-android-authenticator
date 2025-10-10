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

package proton.android.authenticator.business.anonymous.data.infrastructure

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.proton.core.domain.arch.DataResult
import me.proton.core.domain.entity.UserId
import me.proton.core.usersettings.domain.repository.UserSettingsRepository
import proton.android.authenticator.business.anonymous.data.domain.AnonymousData
import proton.android.authenticator.business.anonymous.data.domain.AnonymousDataRepository
import javax.inject.Inject

internal class CoreAnonymousDataRepository @Inject constructor(
    private val userSettingsRepository: UserSettingsRepository
) : AnonymousDataRepository {

    override fun observeAnonymousData(userId: String): Flow<AnonymousData> = UserId(id = userId)
        .let { userId -> userSettingsRepository.getUserSettingsFlow(sessionUserId = userId) }
        .map { userSettingsResult ->
            when (userSettingsResult) {
                is DataResult.Error,
                is DataResult.Processing -> AnonymousData(
                    userId = userId,
                    isCrashReportEnabled = null,
                    isTelemetryEnabled = null
                )

                is DataResult.Success -> AnonymousData(
                    userId = userId,
                    isCrashReportEnabled = userSettingsResult.value.crashReports,
                    isTelemetryEnabled = userSettingsResult.value.telemetry
                )
            }
        }

    override suspend fun updateAnonymousData(anonymousData: AnonymousData) {
        UserId(id = anonymousData.userId)
            .let { userId -> userSettingsRepository.getUserSettings(sessionUserId = userId) }
            .copy(
                crashReports = anonymousData.isCrashReportEnabled,
                telemetry = anonymousData.isTelemetryEnabled
            )
            .also { updatedUserSettings ->
                userSettingsRepository.updateUserSettings(userSettings = updatedUserSettings)
            }
    }

}
