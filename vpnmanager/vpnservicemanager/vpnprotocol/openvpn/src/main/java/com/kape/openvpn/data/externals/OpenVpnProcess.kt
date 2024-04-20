/*
 *
 *  *  Copyright (c) "2023" Private Internet Access, Inc.
 *  *
 *  *  This file is part of the Private Internet Access Android Client.
 *  *
 *  *  The Private Internet Access Android Client is free software: you can redistribute it and/or
 *  *  modify it under the terms of the GNU General Public License as published by the Free
 *  *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *  *
 *  *  The Private Internet Access Android Client is distributed in the hope that it will be useful,
 *  *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  *  details.
 *  *
 *  *  You should have received a copy of the GNU General Public License along with the Private
 *  *  Internet Access Android Client.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.kape.openvpn.data.externals

import com.kape.openvpn.presenters.OpenVpnError
import com.kape.openvpn.presenters.OpenVpnErrorCode
import java.lang.Error

/*
 *  Copyright (c) 2022 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Android Client.
 *
 *  The Private Internet Access Android Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Android Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Android Client.  If not, see <https://www.gnu.org/licenses/>.
 */

internal class OpenVpnProcess(
    private val filePath: IFilePath,
    private val openVpnProcessBuilder: IOpenVpnProcessBuilder,
) : IOpenVpnProcess {

    // region IOpenVpnProcess
    override fun start(commandLineParams: List<String>): Result<Process> {
        val executablePath = filePath.getExecutablePath().getOrElse {
            return Result.failure(
                OpenVpnError(
                    code = OpenVpnErrorCode.PROCESS_COULD_NOT_START,
                    error = Error("Executable path missing.")
                )
            )
        }
        val librariesPath = filePath.getLibrariesPath().getOrElse {
            return Result.failure(
                OpenVpnError(
                    code = OpenVpnErrorCode.PROCESS_COULD_NOT_START,
                    error = Error("Libraries path missing.")
                )
            )
        }

        val commands = commandLineParams.toMutableList()
        commands.add(0, executablePath)

        return openVpnProcessBuilder.start(
            commands = commands,
            ldLibrariesPath = librariesPath
        )
    }

    override fun stop(pid: Int): Result<Unit> {
        try {
            android.os.Process.killProcess(pid)
            return Result.success(Unit)
        } catch (throwable: Throwable) {
            return Result.failure(
                OpenVpnError(
                    code = OpenVpnErrorCode.PROCESS_COULD_NOT_STOP,
                    error = Error(throwable.message)
                )
            )
        }
    }
    // endregion
}
