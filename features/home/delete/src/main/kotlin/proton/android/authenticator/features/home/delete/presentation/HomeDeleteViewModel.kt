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

package proton.android.authenticator.features.home.delete.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import proton.android.authenticator.features.home.delete.usecases.DeleteEntryUseCase
import proton.android.authenticator.shared.common.logs.AuthenticatorLogger
import javax.inject.Inject

@[HiltViewModel OptIn(ExperimentalCoroutinesApi::class)]
internal class HomeDeleteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deleteEntryUseCase: DeleteEntryUseCase
) : ViewModel() {

    private val entryId = requireNotNull<String>(savedStateHandle[ARGS_ENTRY_ID])

    private val eventFlow = MutableStateFlow<HomeDeleteEvent>(value = HomeDeleteEvent.Idle)

    internal val stateFlow: StateFlow<HomeDeleteState> = eventFlow
        .mapLatest(::HomeDeleteState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = HomeDeleteState.Initial
        )

    internal fun onConsumeEvent(event: HomeDeleteEvent) {
        eventFlow.compareAndSet(expect = event, update = HomeDeleteEvent.Idle)
    }

    internal fun onDeleteEntry() {
        viewModelScope.launch {
            deleteEntryUseCase(id = entryId)
                .fold(
                    onFailure = { reason ->
                        AuthenticatorLogger.w(TAG, "Failed to delete entry: $reason")

                        HomeDeleteEvent.OnDeleteEntryError(errorReason = reason.ordinal)
                    },
                    onSuccess = {
                        AuthenticatorLogger.i(TAG, "Entry successfully deleted")

                        HomeDeleteEvent.OnDeleteEntrySuccess
                    }
                )
                .also { event -> eventFlow.update { event } }
        }
    }

    private companion object {

        private const val TAG = "HomeDeleteViewModel"

        private const val ARGS_ENTRY_ID = "entryId"

    }

}
