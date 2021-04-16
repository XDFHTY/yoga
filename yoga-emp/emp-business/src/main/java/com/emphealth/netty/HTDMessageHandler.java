package com.emphealth.netty;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@ChannelHandler.Sharable
public class HTDMessageHandler extends SimpleChannelInboundHandler<HTDMessage> {

    @Autowired
    private HTDMessageListener messageListener;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HTDMessage message) throws Exception {
        messageListener.onMessage(message);
    }
}
