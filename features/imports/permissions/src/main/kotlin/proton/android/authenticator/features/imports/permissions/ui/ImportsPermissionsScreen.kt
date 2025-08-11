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

package proton.android.authenticator.features.imports.permissions.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import proton.android.authenticator.features.imports.permissions.presentation.ImportsPermissionsEvent
import proton.android.authenticator.features.imports.permissions.presentation.ImportsPermissionsViewModel
import proton.android.authenticator.shared.common.domain.models.MimeType
import proton.android.authenticator.shared.ui.R
import proton.android.authenticator.shared.ui.domain.components.bars.SmallTopBar
import proton.android.authenticator.shared.ui.domain.models.UiIcon
import proton.android.authenticator.shared.ui.domain.modifiers.backgroundScreenGradient
import proton.android.authenticator.shared.ui.domain.screens.ScaffoldScreen

@Composable
fun ImportsPermissionsScreen(
    onNavigationClick: () -> Unit,
    onCompleted: (Int) -> Unit,
    onFailed: (Int) -> Unit,
    onOpenAppSettingsClick: () -> Unit,
    onPermissionGranted: (Int) -> Unit
) = with(hiltViewModel<ImportsPermissionsViewModel>()) {
    val state by stateFlow.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current

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

    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    (
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                        )
                        .takeIf { isGranted -> isGranted }
                        ?.also { onPermissionGranted(state.importType.ordinal) }
                }

                else -> Unit
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(key1 = state.event) {
        when (val event = state.event) {
            ImportsPermissionsEvent.Idle -> Unit

            is ImportsPermissionsEvent.OnImportFailed -> {
                onFailed(event.reason)
            }

            is ImportsPermissionsEvent.OnImportSucceeded -> {
                onCompleted(event.importedEntriesCount)
            }
        }

        onConsumeEvent(event = state.event)
    }

    ScaffoldScreen(
        modifier = Modifier
            .fillMaxSize()
            .backgroundScreenGradient(),
        topBar = {
            SmallTopBar(
                navigationIcon = UiIcon.Resource(id = R.drawable.ic_arrow_left),
                onNavigationClick = onNavigationClick
            )
        }
    ) { innerPaddingValues ->
        ImportsPermissionsContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPaddingValues),
            onOpenAppSettingsClick = onOpenAppSettingsClick,
            onImportFromGalleryClick = {
                Intent(Intent.ACTION_GET_CONTENT)
                    .apply {
                        type = MimeType.All.value
                        addCategory(Intent.CATEGORY_OPENABLE)
                        putExtra(Intent.EXTRA_ALLOW_MULTIPLE, state.isMultiSelectionAllowed)
                        putExtra(Intent.EXTRA_MIME_TYPES, state.mimeTypes.toTypedArray())
                    }
                    .also(launcher::launch)
            }
        )
    }
}
