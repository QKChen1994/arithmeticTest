package com.cqk.frame.net.netty.server.handler;

import com.sun.corba.se.spi.activation.ServerManager;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AsciiString;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cqk
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 客户端超时次数
    private Map<String,Integer> clientOvertimeMap = new ConcurrentHashMap<>();
    // 超时次数超过该值则注销连接
    private final int  MAX_OVERTIME  = 2;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        System.out.println("服务器收到消息："+msg.text());
        TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame("服务器时间："+ LocalDateTime.now()+" "+msg.text());
        ctx.channel().writeAndFlush(textWebSocketFrame);
    }

    // 当web客户端连接后，触发方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示唯一的值，longtext是唯一的，shortText不是唯一的
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用"+ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用"+ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.out.println("异常发生"+cause.getMessage());
        ctx.close();
    }



    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        //心跳包检测超时
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.ALL_IDLE) {
                System.err.println("客户端心跳包检测超时");
                String channelId = ctx.channel().id().toString();
                int overtimeTimes = clientOvertimeMap.getOrDefault(channelId, 0);
                if(overtimeTimes < MAX_OVERTIME){
                    addUserOvertime(channelId);
                }else{
                    System.out.println("关闭不活动的链接");
                    ctx.channel().close();
                }
            }
        }
    }

    private void addUserOvertime(String channelId){
        int oldTimes = 0;
        if(clientOvertimeMap.containsKey(channelId)){
            oldTimes = clientOvertimeMap.get(channelId);
        }
        oldTimes++;
        clientOvertimeMap.put(channelId, oldTimes);
    }
}
