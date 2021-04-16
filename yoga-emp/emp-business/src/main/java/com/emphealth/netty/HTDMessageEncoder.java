package com.emphealth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class HTDMessageEncoder extends MessageToByteEncoder<HTDMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HTDMessage msg, ByteBuf out) throws Exception {

    }
}
