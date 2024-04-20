package com.kape.vpnmanager.testutils

import com.kape.vpnmanager.data.externals.ICache
import com.kape.vpnmanager.domain.datasources.CacheDatasource
import com.kape.vpnmanager.domain.datasources.ICacheDatasource

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

internal object GivenGateway {

    fun cacheDatasource(
        cache: ICache = GivenExternal.cache(),
    ): ICacheDatasource =
        CacheDatasource(cache = cache)
}
