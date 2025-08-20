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

package proton.android.authenticator.business.entries.domain

import proton.android.authenticator.shared.common.domain.models.MimeType

enum class EntryImportType(
    val hasOptions: Boolean,
    val isSupported: Boolean,
    val mimeTypes: List<MimeType>
) {
    Aegis(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Json)
    ),
    Authy(
        hasOptions = false,
        isSupported = false,
        mimeTypes = emptyList()
    ),
    Bitwarden(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.CommaSeparatedValues, MimeType.Csv, MimeType.Json)
    ),
    Ente(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Text)
    ),
    Google(
        hasOptions = true,
        isSupported = true,
        mimeTypes = listOf(MimeType.Image)
    ),
    LastPass(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Json)
    ),
    Microsoft(
        hasOptions = false,
        isSupported = false,
        mimeTypes = emptyList()
    ),
    ProtonAuthenticator(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Json)
    ),
    ProtonPass(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Zip)
    ),
    TwoFas(
        hasOptions = false,
        isSupported = true,
        mimeTypes = listOf(MimeType.Binary)
    )
}
