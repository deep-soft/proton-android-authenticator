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

package proton.android.authenticator.features.imports.scan.presentation

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
import kotlinx.coroutines.launch
import proton.android.authenticator.business.entries.application.importall.ImportEntriesReason
import proton.android.authenticator.business.entries.domain.EntryImportType
import proton.android.authenticator.features.imports.shared.usecases.ImportEntriesUseCase
import proton.android.authenticator.shared.common.logs.AuthenticatorLogger
import javax.inject.Inject

@HiltViewModel
class ImportsScanViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val importEntriesUseCase: ImportEntriesUseCase
) : ViewModel() {

    private val importType = requireNotNull<Int>(savedStateHandle[ARGS_IMPORT_TYPE])
        .let(enumValues<EntryImportType>()::get)

    private val eventFlow = MutableStateFlow<ImportsScanEvent>(value = ImportsScanEvent.Idle)

    @OptIn(ExperimentalCoroutinesApi::class)
    internal val stateFlow: StateFlow<ImportsScanState> = eventFlow
        .mapLatest { event ->
            ImportsScanState(
                importType = importType,
                event = event
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
            initialValue = ImportsScanState(
                event = ImportsScanEvent.Idle,
                importType = importType
            )
        )

    internal fun onConsumeEvent(event: ImportsScanEvent) {
        eventFlow.compareAndSet(expect = event, update = ImportsScanEvent.Idle)
    }

    internal fun onFilesPicked(uris: List<Uri>) {
        if (uris.isEmpty()) {
            AuthenticatorLogger.i(TAG, "Import canceled: no files selected")

            return
        }

        viewModelScope.launch {
            importEntriesUseCase(uris, importType).fold(
                onFailure = { reason ->
                    AuthenticatorLogger.w(TAG, "Failed to import entries for $importType: $reason")

                    when (reason) {
                        ImportEntriesReason.BadContent,
                        ImportEntriesReason.BadPassword,
                        ImportEntriesReason.DecryptionFailed,
                        ImportEntriesReason.FileTooLarge -> {
                            ImportsScanEvent.OnImportFailed(reason = reason.ordinal)
                        }

                        ImportEntriesReason.MissingPassword -> {
                            ImportsScanEvent.OnImportPasswordRequired(
                                uri = uris.first().toString(),
                                importType = importType.ordinal
                            )
                        }
                    }
                },
                onSuccess = { importedEntriesCount ->
                    AuthenticatorLogger.i(TAG, "Entries import succeeded for $importType")

                    ImportsScanEvent.OnImportSucceeded(importedEntriesCount = importedEntriesCount)
                }
            ).also { event -> eventFlow.update { event } }
        }
    }

    internal fun onQrCodeScanned(qrCodeBytes: ByteArray) {
        viewModelScope.launch {
            importEntriesUseCase(bytes = qrCodeBytes, importType = importType).fold(
                onFailure = { reason ->
                    AuthenticatorLogger.w(TAG, "Failed to scan entries for $importType: $reason")

                    ImportsScanEvent.OnImportFailed(reason = reason.ordinal)
                },
                onSuccess = { importedEntriesCount ->
                    AuthenticatorLogger.i(TAG, "Entries scan succeeded for $importType")

                    ImportsScanEvent.OnImportSucceeded(importedEntriesCount = importedEntriesCount)
                }
            ).also { event -> eventFlow.update { event } }
        }
    }

    private companion object {

        private const val TAG = "ImportsScanViewModel"

        private const val ARGS_IMPORT_TYPE = "importType"

    }

}
