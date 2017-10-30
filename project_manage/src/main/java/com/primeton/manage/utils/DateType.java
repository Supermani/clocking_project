package com.primeton.manage.utils;

public enum DateType {

    //2节假日,1休息日,0工作日

    HOLIDAY(2),
    RESTDAY(1),
    WORKDAY(0);

    private Integer type;

    private DateType(Integer type) {
        this.type = type;
    }

    public  Integer getType(){
        return type;
    }
}

