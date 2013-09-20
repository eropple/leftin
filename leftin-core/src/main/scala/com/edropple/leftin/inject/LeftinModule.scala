package com.edropple.leftin.inject

import com.google.inject.AbstractModule
import com.typesafe.config.{ConfigFactory, Config}
import com.edropple.leftin.routing.{RoutingModule}
import javax.inject.Singleton
import com.edropple.leftin.db.Database
import com.typesafe.scalalogging.slf4j.Logging

import scala.collection.JavaConverters._
import com.google.inject.name.Names
import com.google.common.eventbus.EventBus

abstract class LeftinModule extends AbstractModule with Logging {
    protected def defaultConfiguration() {
        val cfg = config();
        bind(classOf[Config]).toInstance(cfg);
        bind(classOf[RoutingModule]).toInstance(routing());
        bind(classOf[EventBus]).toInstance(new EventBus());


        if (cfg.hasPath("db")) {
            logger.info("Parsing databases to create pools...");

            val set = cfg.getConfig("db").root().entrySet().asScala;

            for (pair <- set) {
                val name: String = pair.getKey;

                if (pair.getValue != null) {
                    val db = Database.create(name, cfg);

                    if (name == "default") {
                        bind(classOf[Database]).toInstance(db);
                    } else {
                        bind(classOf[Database]).annotatedWith(Names.named(name)).toInstance(db);
                    }
                }
            }

        } else {
            logger.info("No database configurations found; database access will not work.");
        }
    }

    protected def config(): Config = ConfigFactory.load();


    protected def routing(): RoutingModule;
}
