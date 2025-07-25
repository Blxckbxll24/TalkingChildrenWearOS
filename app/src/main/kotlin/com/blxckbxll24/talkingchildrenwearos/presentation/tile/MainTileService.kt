package com.blxckbxll24.talkingchildrenwearos.presentation.tile

import androidx.wear.tiles.TileService
import androidx.wear.tiles.TileBuilders.Tile
import androidx.wear.tiles.RequestBuilders.TileRequest
import androidx.wear.tiles.ResourceBuilders.Resources
import androidx.wear.tiles.RequestBuilders.ResourcesRequest
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.Futures

class MainTileService : TileService() {

    override fun onTileRequest(requestParams: TileRequest): ListenableFuture<Tile> {
        return Futures.immediateFuture(
            Tile.Builder()
                .setResourcesVersion("1")
                .build()
        )
    }

    override fun onResourcesRequest(requestParams: ResourcesRequest): ListenableFuture<Resources> {
        return Futures.immediateFuture(
            Resources.Builder()
                .setVersion("1")
                .build()
        )
    }
}