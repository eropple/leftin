package com.edropple.leftin.routing

import scala.collection.JavaConverters._

class ScalaRoutingModule(val entries: Seq[RoutingEntry]) extends RoutingModule {
    def getRoutingEntries: java.lang.Iterable[RoutingEntry] = entries.asJava;
}
