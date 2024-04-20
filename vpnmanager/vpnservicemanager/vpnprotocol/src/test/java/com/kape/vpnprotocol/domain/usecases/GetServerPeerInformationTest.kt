package com.kape.vpnprotocol.domain.usecases

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.kape.vpnprotocol.data.externals.common.ICacheProtocol
import com.kape.vpnprotocol.domain.usecases.common.GetServerPeerInformation
import com.kape.vpnprotocol.domain.usecases.common.IGetServerPeerInformation
import com.kape.vpnprotocol.domain.usecases.common.ISetServerPeerInformation
import com.kape.vpnprotocol.domain.usecases.common.SetServerPeerInformation
import com.kape.vpnprotocol.testutils.GivenExternal
import com.kape.vpnprotocol.testutils.GivenModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

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

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
internal class GetServerPeerInformationTest {

    @Test
    fun `should succeed retrieving from cache the set server peer information`() = runTest {
        // given
        val context: Context = ApplicationProvider.getApplicationContext()
        val serverPeerInformationMock = GivenModel.vpnProtocolServerPeerInformation()
        val cacheProtocol: ICacheProtocol = GivenExternal.cache(context = context)
        val setServerPeerInformation: ISetServerPeerInformation =
            SetServerPeerInformation(cacheProtocol = cacheProtocol)
        val getServerPeerInformation: IGetServerPeerInformation =
            GetServerPeerInformation(cacheProtocol = cacheProtocol)

        // when
        val setResult = setServerPeerInformation(serverPeerInformation = serverPeerInformationMock)
        val getResult = getServerPeerInformation()

        // then
        assert(setResult.isSuccess)
        assert(getResult.isSuccess)
        assert(getResult.getOrThrow() == serverPeerInformationMock)
    }

    @Test
    fun `should fail retrieving from cache when there is no server peer information`() = runTest {
        // given
        val context: Context = ApplicationProvider.getApplicationContext()
        val cacheProtocol: ICacheProtocol = GivenExternal.cache(context = context)
        val getServerPeerInformation: IGetServerPeerInformation =
            GetServerPeerInformation(cacheProtocol = cacheProtocol)

        // when
        val result = getServerPeerInformation()

        // then
        assert(result.isFailure)
    }
}
