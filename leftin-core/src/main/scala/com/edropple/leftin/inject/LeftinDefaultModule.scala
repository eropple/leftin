package com.edropple.leftin.inject

import com.google.inject.AbstractModule
import com.typesafe.config.{ConfigFactory, Config}
import com.edropple.leftin.routing.{RoutingModule, RoutingTable}

abstract class LeftinDefaultModule extends AbstractModule {
    def configure() {
        bind(classOf[Config]).toInstance(getConfig());
        bind(classOf[RoutingModule]).toInstance(getRouting());
    }

    protected def getConfig(): Config = ConfigFactory.load();

    protected def getRouting(): RoutingModule;
}
