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

package proton.android.authenticator.features.imports.menus.presentation

import android.net.Uri
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
import proton.android.authenticator.business.entries.domain.EntryImportType
import javax.inject.Inject

@[HiltViewModel OptIn(ExperimentalCoroutinesApi::class)]
internal class ImportsMenuViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val importType = requireNotNull<Int>(savedStateHandle[ARGS_IMPORT_TYPE])
        .let(enumValues<EntryImportType>()::get)

    private val eventFlow = MutableStateFlow<ImportsMenuEvent>(value = ImportsMenuEvent.Idle)

    internal val stateFlow: StateFlow<ImportsMenuState> = eventFlow
        .mapLatest { event ->
            ImportsMenuState(
                importType = importType,
                event = event
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ImportsMenuState(
                importType = importType,
                event = ImportsMenuEvent.Idle
            )
        )

    internal fun onConsumeEvent(event: ImportsMenuEvent) {
        eventFlow.compareAndSet(expect = event, update = ImportsMenuEvent.Idle)
    }

    internal fun onFilesPicked(uris: List<Uri>) {
        if (uris.isEmpty()) return
    }

    internal fun onOptionSelected(option: ImportsMenuOption) {
        when (option.optionType) {
            ImportsMenuOptionType.ScanQrCode -> ImportsMenuEvent.OnScanQrCode
            ImportsMenuOptionType.SelectFromGallery -> ImportsMenuEvent.OnSelectFromGallery
        }.also { event -> eventFlow.update { event } }
    }

    private companion object {

        private const val ARGS_IMPORT_TYPE = "importType"

    }

}
