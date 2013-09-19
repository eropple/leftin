package com.edropple.leftin.routing

import com.typesafe.scalalogging.slf4j.Logging
import scala.collection.JavaConverters._
import javax.inject.Inject

/**
 * The core routing device for an application. Used to define URL
 * patterns in an application. Immutable once created.
 *
 * @author  eropple
 * @since   17 Sep 2013
 */
class RoutingTable @Inject() (module: RoutingModule) extends Logging {
    val entries: Seq[RoutingEntry] = module.getRoutingEntries.asScala.toSeq

    val methodEntries: Map[HttpMethod, Seq[RoutingEntry]] = Map(
        (HttpMethod.GET,        entries.filter(p => p.method == HttpMethod.GET)),
        (HttpMethod.POST,       entries.filter(p => p.method == HttpMethod.POST)),
        (HttpMethod.PUT,        entries.filter(p => p.method == HttpMethod.PUT)),
        (HttpMethod.DELETE,     entries.filter(p => p.method == HttpMethod.DELETE)),
        (HttpMethod.PATCH,      entries.filter(p => p.method == HttpMethod.PATCH))
    );
}
