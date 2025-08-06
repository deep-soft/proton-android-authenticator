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

package proton.android.authenticator.features.backups.passwords.presentation

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import proton.android.authenticator.features.shared.usecases.backups.ObserveBackupUseCase
import proton.android.authenticator.features.shared.usecases.backups.UpdateBackupUseCase
import proton.android.authenticator.shared.crypto.domain.contexts.EncryptionContextProvider
import javax.inject.Inject

@HiltViewModel
internal class BackupsPasswordViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val encryptionContextProvider: EncryptionContextProvider,
    private val observeBackupUseCase: ObserveBackupUseCase,
    private val updateBackupUseCase: UpdateBackupUseCase
) : ViewModel() {

    private val backupUri = requireNotNull<String>(savedStateHandle[ARGS_URI])
        .let(Uri::parse)

    private val passwordState = mutableStateOf<String?>(value = null)

    private val isPasswordVisibleFlow = MutableStateFlow(value = false)

    private val eventFlow = MutableStateFlow<BackupsPasswordEvent>(
        value = BackupsPasswordEvent.Idle
    )

    internal val stateFlow: StateFlow<BackupsPasswordState> = combine(
        snapshotFlow { passwordState.value.orEmpty() },
        isPasswordVisibleFlow,
        eventFlow,
        ::BackupsPasswordState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5_000),
        initialValue = BackupsPasswordState.Initial
    )

    internal fun onConsumeEvent(event: BackupsPasswordEvent) {
        eventFlow.compareAndSet(expect = event, update = BackupsPasswordEvent.Idle)
    }

    internal fun onPasswordChange(newPassword: String) {
        passwordState.value = newPassword
    }

    internal fun onEnableBackupWithPassword() {
        passwordState.value?.let { password ->
            viewModelScope.launch {
                encryptionContextProvider.withEncryptionContext {
                    encrypt(password)
                }.also(::enableBackup)
            }
        }
    }

    internal fun onEnableBackupWithoutPassword() {
        enableBackup(password = null)
    }

    private fun enableBackup(password: String?) {
        viewModelScope.launch {
            observeBackupUseCase().first()
                .copy(
                    isEnabled = true,
                    directoryUri = backupUri
                )
                .also { backup -> updateBackupUseCase(newBackup = backup) }
        }
    }

    private companion object {

        private const val ARGS_URI = "uri"

    }

}
