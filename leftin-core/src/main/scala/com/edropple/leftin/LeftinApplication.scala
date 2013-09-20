package com.edropple.leftin

import com.google.inject.{Guice, Injector, AbstractModule}
import com.typesafe.scalalogging.slf4j.Logging
import com.typesafe.config.Config
import com.google.common.eventbus.EventBus
import com.edropple.leftin.events.{PostStartupEvent, PreStartupEvent}

/**
 * The core application class for a Leftin-consuming application.
 *
 * @author  eropple
 * @since   17 Sep 2013
 */
abstract class LeftinApplication(injectionModule: AbstractModule) extends Logging {

    val injector: Injector = Guice.createInjector(injectionModule);
    val eventBus: EventBus = new EventBus();

    def start(): Unit = {
        eventBus.post(PreStartupEvent());

        // do any startup stuff

        eventBus.post(PostStartupEvent());
        runServer();
    }

    def postStartup() = {}

    def runServer();
}
