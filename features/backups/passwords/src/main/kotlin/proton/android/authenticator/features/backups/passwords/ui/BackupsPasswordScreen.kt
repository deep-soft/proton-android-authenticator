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

package proton.android.authenticator.features.backups.passwords.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import proton.android.authenticator.features.backups.passwords.R
import proton.android.authenticator.features.backups.passwords.presentation.BackupsPasswordEvent
import proton.android.authenticator.features.backups.passwords.presentation.BackupsPasswordViewModel
import proton.android.authenticator.shared.ui.domain.models.UiText
import proton.android.authenticator.shared.ui.domain.screens.CustomDialogScreen

@Composable
fun BackupsPasswordScreen(onDismissed: () -> Unit) = with(hiltViewModel<BackupsPasswordViewModel>()) {
    val state by stateFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = state.event) {
        when (val event = state.event) {
            BackupsPasswordEvent.Idle -> Unit
        }

        onConsumeEvent(event = state.event)
    }

    CustomDialogScreen(
        title = UiText.Resource(id = R.string.backups_password_dialog_title),
        message = UiText.Resource(id = R.string.backups_password_dialog_message),
        confirmText = UiText.Resource(id = R.string.backups_password_dialog_action_confirm),
        isConfirmEnabled = state.isConfirmEnabled,
        onConfirmClick = ::onEnableBackupWithPassword,
        cancelText = UiText.Resource(id = R.string.backups_password_dialog_action_cancel),
        onCancelClick = ::onEnableBackupWithoutPassword,
        onDismissed = onDismissed
    ) {

    }
}
