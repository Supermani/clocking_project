package com.primeton.manage.utils;

public enum DateType {

    HOLIDAY(2),
    WORKDAY(1);

    private Integer type;

    private DateType(Integer type) {
        this.type = type;
    }

    public  Integer getType(){
        return type;
    }
}

