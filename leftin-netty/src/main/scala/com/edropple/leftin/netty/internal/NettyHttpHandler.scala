package com.edropple.leftin.netty.internal

import javax.inject.{Inject, Singleton}
import io.netty.channel.{ChannelFutureListener, SimpleChannelInboundHandler, ChannelHandlerContext, ChannelInboundHandlerAdapter}
import com.typesafe.scalalogging.slf4j.Logging
import io.netty.handler.codec.http._
import com.google.inject.Injector
import com.edropple.leftin.routing.RoutingTable
import io.netty.channel.ChannelHandler.Sharable
import io.netty.buffer.Unpooled
import com.google.common.base.Charsets
import com.twitter.util.LruMap
import java.lang.reflect.Method
import com.typesafe.config.Config
import com.edropple.leftin.config.ConfigKeys
import com.edropple.leftin.netty.NettyHttpConfiguration

@Singleton
@Sharable
class NettyHttpHandler @Inject() (private val httpConfig: NettyHttpConfiguration, private val routing: RoutingTable)
        extends SimpleChannelInboundHandler[FullHttpRequest]
        with Logging {

    private val pathCache = {
        val size = httpConfig.pathCacheSize;
        logger.info("Creating path cache with a size of {}.", Integer.valueOf(size));
        LruMap.makeUnderlying[String, (Class[_], Method)](size);
    }

    def channelRead0(ctx: ChannelHandlerContext, req: FullHttpRequest) {
        throw new Exception();
    }

    override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        super.exceptionCaught(ctx, cause);

        logger.error("Unhandled exception in NettyHttpHandler.", cause);
        // TODO: should do more than this here

        val response = new DefaultFullHttpResponse(
                            HttpVersion.HTTP_1_1,
                            HttpResponseStatus.INTERNAL_SERVER_ERROR,
                            Unpooled.copiedBuffer("<html><body>Error!</body></html>", Charsets.UTF_8));

        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html");

        ctx.channel().writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }
}

