package com.emphealth.business.customer.enums;

import com.yoga.core.base.BaseEnum;

public enum GenderType implements BaseEnum<String> {

    male("男"),
    female("女");
    private final String desc;
    GenderType(String desc) {
        this.desc = desc;
    }

    @Override
    public String getCode() {
        return toString();
    }
    @Override
    public String getName() {
        return desc;
    }
}
