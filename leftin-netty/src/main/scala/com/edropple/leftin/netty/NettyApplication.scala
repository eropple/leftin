package com.edropple.leftin.netty

import com.edropple.leftin.LeftinApplication
import com.edropple.leftin.netty.internal.NettyHttpServer
import com.typesafe.scalalogging.slf4j.Logging
import com.google.inject.AbstractModule

class NettyApplication(injectionModule: AbstractModule) extends
        LeftinApplication(injectionModule) with Logging {

    val server = injector.getInstance(classOf[NettyHttpServer]);

    override def runServer() {
        logger.info("Starting Netty server.");

        server.start();

        logger.info("Netty server terminated.");
    }
}
