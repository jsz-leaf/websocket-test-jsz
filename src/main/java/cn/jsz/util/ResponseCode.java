package cn.jsz.util;
 
/**
 * 编写枚举类的步骤
 * (1)编写所需的变量
 * (2)编写枚举类构造方法
 * (3)编写枚举的值，调用构造方法，使用逗号隔开
 * (4)编写方法获取枚举类中对应的值
 */
public enum ResponseCode {
    //(3)编写枚举的值，调用构造方法，使用逗号隔开
    SUCCESS(0,"SUCCESS"),
    ERROR(1,"ERROR");
    //(1)编写所需的变量
    private final int code;
    private final String desc;
    //(2)编写枚举类构造方法
    ResponseCode(int code,String desc) {
        this.code=code;
        this.desc=desc;
    }
    //(4)编写方法获取枚举类中对应的值
    public int getCode() {
        return code;
    }
    public String getDesc() {
        return desc;
    }
}