package com.kape.vpnservicemanager.testutils.mocks

import com.kape.vpnservicemanager.data.models.VPNServiceServerPeerInformation
import com.kape.vpnservicemanager.domain.usecases.IStartConnection
import com.kape.vpnservicemanager.presenters.VPNServiceManagerError
import com.kape.vpnservicemanager.presenters.VPNServiceManagerErrorCode
import com.kape.vpnservicemanager.testutils.GivenModel

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

internal class StartConnectionMock(
    private val shouldSucceed: Boolean,
    private val vpnServiceServerPeerInformation: VPNServiceServerPeerInformation = GivenModel.vpnServiceServerPeerInformation(),
) : IStartConnection {

    // region IStartConnection
    override suspend fun invoke(): Result<VPNServiceServerPeerInformation> {
        return if (shouldSucceed) {
            Result.success(vpnServiceServerPeerInformation)
        } else {
            Result.failure(VPNServiceManagerError(VPNServiceManagerErrorCode.SERVICE_CONFIGURATION_ERROR))
        }
    }
    // endregion
}
