package com.emphealth.netty;

import com.yoga.core.utils.NumberUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class HTDMessage {
    private String magic;
    private String command;
    private int length;
    private String imei;
    private String pid;
    private LocalDateTime time;
    private String param;

    HTDMessage(String[] parts) {
        this.magic = parts[0];
        this.command = parts[1];
        this.length = NumberUtil.intValue(parts[2]);
    }
}
