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

package com.kape.vpnprotocol.domain.controllers.wireguard

import com.kape.vpnmanager.api.DisconnectReason
import com.kape.vpnmanager.api.VPNManagerConnectionStatus
import com.kape.vpnprotocol.domain.usecases.common.IClearCache
import com.kape.vpnprotocol.domain.usecases.common.IReportConnectivityStatus
import com.kape.vpnprotocol.domain.usecases.wireguard.IDestroyWireguardTunnel
import com.kape.vpnprotocol.domain.usecases.wireguard.IGetWireguardTunnelHandle
import com.kape.vpnprotocol.domain.usecases.wireguard.IStopWireguardByteCountJob

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

internal class StopWireguardConnectionController(
    private val reportConnectivityStatus: IReportConnectivityStatus,
    private val getWireguardTunnelHandle: IGetWireguardTunnelHandle,
    private val stopWireguardByteCountJob: IStopWireguardByteCountJob,
    private val destroyWireguardTunnel: IDestroyWireguardTunnel,
    private val clearCache: IClearCache,
) : IStopWireguardConnectionController {

    // region IStopWireguardConnectionController
    override suspend fun invoke(disconnectReason: DisconnectReason): Result<Unit> {
        val result = reportConnectivityStatus(connectivityStatus = VPNManagerConnectionStatus.Disconnecting)
            .mapCatching {
                stopWireguardByteCountJob().getOrThrow()
            }
            .mapCatching {
                getWireguardTunnelHandle().getOrThrow()
            }
            .mapCatching {
                destroyWireguardTunnel(tunnelHandle = it).getOrThrow()
            }
            .mapCatching {
                reportConnectivityStatus(
                    connectivityStatus = VPNManagerConnectionStatus.Disconnected(disconnectReason)
                ).getOrThrow()
            }
            .mapCatching {
                clearCache().getOrThrow()
            }

        return result.fold(
            onSuccess = {
                Result.success(it)
            },
            onFailure = {
                clearCache()
                Result.failure(it)
            }
        )
    }
    // endregion
}
