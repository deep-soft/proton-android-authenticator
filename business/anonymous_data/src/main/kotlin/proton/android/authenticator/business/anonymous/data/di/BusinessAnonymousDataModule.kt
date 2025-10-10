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

package proton.android.authenticator.business.anonymous.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import proton.android.authenticator.business.anonymous.data.application.find.FindAnonymousDataQuery
import proton.android.authenticator.business.anonymous.data.application.find.FindAnonymousDataQueryHandler
import proton.android.authenticator.business.anonymous.data.application.update.UpdateAnonymousDataCommand
import proton.android.authenticator.business.anonymous.data.application.update.UpdateAnonymousDataCommandHandler
import proton.android.authenticator.business.anonymous.data.domain.AnonymousDataRepository
import proton.android.authenticator.business.anonymous.data.infrastructure.CoreAnonymousDataRepository
import proton.android.authenticator.shared.common.di.CommandHandlerKey
import proton.android.authenticator.shared.common.di.QueryHandlerKey
import proton.android.authenticator.shared.common.domain.infrastructure.commands.CommandHandler
import proton.android.authenticator.shared.common.domain.infrastructure.queries.QueryHandler
import javax.inject.Singleton

@[Module InstallIn(SingletonComponent::class)]
internal abstract class BusinessAnonymousDataModule {

    @[Binds Singleton IntoMap QueryHandlerKey(FindAnonymousDataQuery::class)]
    internal abstract fun bindFindAnonymousDataQueryHandler(impl: FindAnonymousDataQueryHandler): QueryHandler<*, *>

    @[Binds Singleton IntoMap CommandHandlerKey(UpdateAnonymousDataCommand::class)]
    internal abstract fun bindUpdateAnonymousDataCommandHandler(
        impl: UpdateAnonymousDataCommandHandler
    ): CommandHandler<*, *, *>

    @[Binds Singleton]
    internal abstract fun bindAnonymousDataRepository(impl: CoreAnonymousDataRepository): AnonymousDataRepository

}
