package com.cqk.frame.net.netty.server;

import com.cqk.frame.net.netty.server.handler.WebSocketHandler;
import com.cqk.frame.net.netty.server.handler.WebSocketHandler2;
import com.sun.net.httpserver.HttpServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * 1、HttpServerCodec，用于编码解码
 * 3、aggregator，消息聚合器（重要）。为什么能有FullHttpRequest这个东西，就是因为有他，HttpObjectAggregator，如果没有他，就不会有那个消息是FullHttpRequest的那段Channel，同样也不会有FullHttpResponse。
 * 如果我们将z'h
 * HttpObjectAggregator(512 * 1024)的参数含义是消息合并的数据大小，如此代表聚合的消息内容长度不超过512kb。
 * 4、添加我们自己的处理接口
 *
 * 在使用中，如果是客户端的并发连接数channel多，且每个客户端channel的业务请求阻塞不多，那么使用EventExecutorGroup；
 * 如果客户端并发连接数channel不多，但是客户端channle的业务请求阻塞较多（复杂业务处理和数据库处理），那么使用ExecutorService
 *
 * @author cqk
 */
public class NettySocketServer {
    private final int port;

    public NettySocketServer(int port) {
        this.port = port;
    }


    public static void main(String[] args) throws Exception {
        new NettySocketServer(7000).start();
    }

    public void start() throws Exception {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //因为基于http协议，使用http编码和解码器
                            pipeline.addLast(new HttpServerCodec());
                            // 以块的方式写，添加chunkedWriteHandler
                            pipeline.addLast(new ChunkedWriteHandler());
                            /**
                             * http数据在传输过程中是分段的，httpObjectAggregator就是将多个段聚合
                             * 这就是为什么，当浏览器发送大量请求时，会发出多次http请求
                             */
                            pipeline.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 对于websocket他的数据是以帧frame的形式传递的
                             * 可以看到websocketFrame下面有六个子类
                             * 浏览器请求时，ws://localhost:7000/hello表示请求的uri
                             * webSocketServerProtocolHandler核心功能是将http协议升级未ws，保持长连接
                             * 是通过一个状态码101
                             */
                            pipeline.addLast(new WebSocketServerProtocolHandler("/web","v1,v2"));
                            pipeline.addLast(new IdleStateHandler(0,0,6));
                            pipeline.addLast(new WebSocketHandler());


                            ChannelPipeline pipeline2 = ch.pipeline();
                            //因为基于http协议，使用http编码和解码器
                            pipeline2.addLast(new HttpServerCodec());
                            // 以块的方式写，添加chunkedWriteHandler
                            pipeline2.addLast(new ChunkedWriteHandler());
                            /**
                             * http数据在传输过程中是分段的，httpObjectAggregator就是将多个段聚合
                             * 这就是为什么，当浏览器发送大量请求时，会发出多次http请求
                             */
                            pipeline2.addLast(new HttpObjectAggregator(8192));
                            /**
                             * 对于websocket他的数据是以帧frame的形式传递的
                             * 可以看到websocketFrame下面有六个子类
                             * 浏览器请求时，ws://localhost:7000/hello表示请求的uri
                             * webSocketServerProtocolHandler核心功能是将http协议升级未ws，保持长连接
                             * 是通过一个状态码101
                             */
                            pipeline2.addLast(new WebSocketServerProtocolHandler("/web2","v1,v2"));
                            pipeline2.addLast(new IdleStateHandler(0,0,6));
                            pipeline2.addLast(new WebSocketHandler2());
                        }
                    });
            ChannelFuture future = bootstrap.bind(7000).sync();
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
