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

package proton.android.authenticator.features.settings.master.usecases

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import proton.android.authenticator.business.anonymous.data.application.find.FindAnonymousDataQuery
import proton.android.authenticator.business.anonymous.data.domain.AnonymousData
import proton.android.authenticator.features.shared.users.usecases.ObserveUserUseCase
import proton.android.authenticator.shared.common.domain.infrastructure.queries.QueryBus
import javax.inject.Inject

internal class ObserveAnonymousDataUseCase @Inject constructor(
    private val observeUserUseCase: ObserveUserUseCase,
    private val queryBus: QueryBus
) {

    @OptIn(ExperimentalCoroutinesApi::class)
    internal operator fun invoke(): Flow<AnonymousData?> = observeUserUseCase()
        .flatMapLatest { user ->
            if (user == null) {
                flowOf(null)
            } else {
                FindAnonymousDataQuery(userId = user.id)
                    .let { query -> queryBus.ask<AnonymousData>(query = query) }
            }
        }

}
