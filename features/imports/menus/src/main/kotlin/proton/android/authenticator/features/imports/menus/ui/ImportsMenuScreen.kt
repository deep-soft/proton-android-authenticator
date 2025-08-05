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

package proton.android.authenticator.features.imports.menus.ui

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import proton.android.authenticator.features.imports.menus.presentation.ImportsMenuEvent
import proton.android.authenticator.features.imports.menus.presentation.ImportsMenuViewModel
import proton.android.authenticator.shared.common.domain.models.MimeType
import proton.android.authenticator.shared.ui.domain.screens.BottomSheetScreen

@Composable
fun ImportsMenuScreen(onDismissed: () -> Unit) = with(hiltViewModel<ImportsMenuViewModel>()) {
    val state by stateFlow.collectAsStateWithLifecycle()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
        onResult = { result ->
            buildList<Uri> {
                result.data?.data?.also(::add)

                result.data?.clipData?.let { clipData ->
                    for (index in 0 until clipData.itemCount) {
                        clipData.getItemAt(index).uri.also(::add)
                    }
                }
            }.also(::onFilesPicked)
        }
    )

    LaunchedEffect(key1 = state.event) {
        when (state.event) {
            ImportsMenuEvent.Idle -> Unit

            ImportsMenuEvent.OnScanQrCode -> {
                println("JIBIRI: Scan QR Code")
            }

            ImportsMenuEvent.OnSelectFromGallery -> {
                Intent(Intent.ACTION_GET_CONTENT)
                    .apply {
                        type = MimeType.All.value
                        addCategory(Intent.CATEGORY_OPENABLE)
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, state.isMultiSelectionAllowed)
                        putExtra(Intent.EXTRA_MIME_TYPES, state.mimeTypes.toTypedArray())
                    }
                    .also(launcher::launch)
            }
        }

        onConsumeEvent(event = state.event)
    }

    BottomSheetScreen(onDismissed = onDismissed) {
        ImportsMenuContent(
            state = state,
            onOptionSelected = ::onOptionSelected
        )
    }
}
