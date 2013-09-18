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

@Singleton
@Sharable
class NettyHttpHandler @Inject() (val injector: Injector)
        extends SimpleChannelInboundHandler[FullHttpRequest]
        with Logging {

    val routing = injector.getInstance(classOf[RoutingTable]);

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

