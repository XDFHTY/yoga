package com.emphealth.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.time.DateUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class HTDMessageDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        byte[] data = new byte[in.readableBytes()];
        in.readBytes(data);
        String line = new String(data, StandardCharsets.UTF_8).trim();
        String[] parts = line.split(":");
        if (parts.length > 3 && "HTD01".equals(parts[0])) {
            HTDMessage message = parse(parts);
            if (message != null) out.add(message);
        }
    }

    private HTDMessage parse(String[] parts) {
        String cmdId = parts[1];
        switch (cmdId) {
            case "CHKIN": return checkIn(parts);
            case "SPO2":
            case "NIBP":
            case "TEMP":
            case "GLU":
            case "UA": return result(parts);
            default: return null;
        }
    }

    private HTDMessage checkIn(String[] parts) {
        if (parts.length < 4) return null;
        HTDMessage message = new HTDMessage(parts);
        message.setImei(parts[3]);
        return message;
    }
    private HTDMessage result(String[] parts) {
        if (parts.length < 7) return null;
        HTDMessage message = new HTDMessage(parts);
        message.setPid(parts[3]);
        message.setImei(parts[4]);
        message.setTime(parseDate(parts[5]));
        message.setParam(parts[6]);
        return message;
    }

    private LocalDateTime parseDate(String text) {
        if (text == null || text.length() != 15) return null;
        try {
            Date date = DateUtils.parseDate(text, "yyyyMMdd_HHmmss");
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        } catch (Throwable ex) {
            return null;
        }
    }
}
