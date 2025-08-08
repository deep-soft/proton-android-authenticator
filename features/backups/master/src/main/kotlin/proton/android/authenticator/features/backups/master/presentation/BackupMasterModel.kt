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

package proton.android.authenticator.features.backups.master.presentation

import android.net.Uri
import me.proton.core.crypto.common.keystore.EncryptedString
import proton.android.authenticator.business.backups.domain.Backup
import proton.android.authenticator.business.backups.domain.BackupFrequencyType
import proton.android.authenticator.shared.ui.domain.models.UiDate

internal data class BackupMasterModel(
    internal val isEnabled: Boolean,
    internal val frequencyType: BackupFrequencyType,
    internal val maxBackupCount: Int,
    internal val directoryUri: Uri,
    internal val encryptedPassword: EncryptedString?,
    private val count: Int,
    private val lastBackupMillis: Long?
) {

    internal val lastBackupDate: UiDate? = lastBackupMillis?.let(UiDate::Backup)

    internal val frequencyOptions: List<BackupsMasterFrequencyOption> = buildList {
        add(BackupsMasterFrequencyOption.Daily(selectedType = frequencyType))
        add(BackupsMasterFrequencyOption.Weekly(selectedType = frequencyType))
        add(BackupsMasterFrequencyOption.Monthly(selectedType = frequencyType))

        if (frequencyType == BackupFrequencyType.QA) {
            add(BackupsMasterFrequencyOption.QA(selectedType = frequencyType))
        }
    }

    internal fun asBackup(): Backup = Backup(
        isEnabled = isEnabled,
        frequencyType = frequencyType,
        lastBackupMillis = lastBackupMillis,
        count = count,
        directoryUri = directoryUri,
        encryptedPassword = encryptedPassword
    )

}
