package com.edropple.leftin.netty.internal

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.{ChannelInitializer, ChannelOption}
import io.netty.channel.nio.NioEventLoopGroup
import com.typesafe.scalalogging.slf4j.Logging
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.http.{HttpRequestDecoder, HttpObjectAggregator, HttpResponseEncoder}
import com.google.inject.Injector
import io.netty.handler.stream.ChunkedWriteHandler
import com.typesafe.config.Config
import javax.inject.{Inject, Singleton}
import com.edropple.leftin.config.ConfigKeys
import com.edropple.leftin.netty.NettyHttpConfiguration

@Singleton
class NettyHttpServer @Inject() (private val injector: Injector, private val httpConfig: NettyHttpConfiguration)
        extends Logging {

    def start() {
        logger.info("Starting Netty server...");

        val bossGroup = new NioEventLoopGroup();
        val workerGroup = new NioEventLoopGroup();

        try {
            val bootstrap = new ServerBootstrap();

            bootstrap.option[Integer](ChannelOption.SO_BACKLOG, 1024);
            bootstrap.group(bossGroup, workerGroup);
            bootstrap.channel(classOf[NioServerSocketChannel]);

            bootstrap.childHandler(new ChannelInitializer[SocketChannel] {
                def initChannel(channel: SocketChannel) {
                    val pipeline = channel.pipeline();

                    pipeline.addLast("decoder", new HttpRequestDecoder());
                    pipeline.addLast("aggregator", new HttpObjectAggregator(64 * 1024 * 1024));
                    pipeline.addLast("encoder", new HttpResponseEncoder());
                    pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

                    pipeline.addLast("handler", injector.getInstance(classOf[NettyHttpHandler]));
                }
            });

            logger.info("Binding server to port {}.", Integer.valueOf(httpConfig.port));
            bootstrap.bind(httpConfig.port).sync().channel().closeFuture().sync();

        } catch {
            case ex: Exception => {
                logger.error("Unhandled exception in Netty server.", ex);
                throw ex;
            }
        } finally {
            logger.info("finally reached, shutting down.");

            bossGroup.shutdownGracefully().get();
            workerGroup.shutdownGracefully().get();
        }
    }
}
