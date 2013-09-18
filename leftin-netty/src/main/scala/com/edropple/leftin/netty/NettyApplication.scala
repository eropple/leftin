package com.edropple.leftin.netty

import com.edropple.leftin.LeftinApplication
import com.edropple.leftin.inject.LeftinDefaultModule
import com.edropple.leftin.netty.internal.NettyHttpServer
import com.typesafe.scalalogging.slf4j.Logging

class NettyApplication(injectionModule: LeftinDefaultModule) extends
        LeftinApplication(injectionModule) with Logging {

    val server = injector.getInstance(classOf[NettyHttpServer]);

    def run() {
        logger.info("Starting Netty server.");

        server.start();

        logger.info("Netty server terminated.");
    }
}
