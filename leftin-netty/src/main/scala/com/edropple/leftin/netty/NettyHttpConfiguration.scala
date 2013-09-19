package com.edropple.leftin.netty

import javax.inject.{Inject, Singleton}
import com.typesafe.config.Config
import com.edropple.leftin.config.ConfigKeys

@Singleton
class NettyHttpConfiguration @Inject() (config: Config) {
    val port = config.getInt(ConfigKeys.HTTP_PORT);
    val pathCacheSize = config.getInt(ConfigKeys.HTTP_PATH_CACHE_SIZE);
}
