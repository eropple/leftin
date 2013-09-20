package com.edropple.leftin

import com.google.inject.{Guice, Injector, AbstractModule}
import com.typesafe.scalalogging.slf4j.Logging
import com.typesafe.config.Config

/**
 * The core application class for a Leftin-consuming application.
 *
 * @author  eropple
 * @since   17 Sep 2013
 */
abstract class LeftinApplication(injectionModule: AbstractModule) extends Logging {

    val injector: Injector = Guice.createInjector(injectionModule);

    def start(): Unit = {
        runServer();
    }

    def runServer();
}
