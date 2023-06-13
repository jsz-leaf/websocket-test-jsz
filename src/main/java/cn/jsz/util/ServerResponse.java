package cn.jsz.util;
 
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
 
import java.io.Serializable;
 
//用于封装数据的类，加泛型实现序列化接口
//通过注解约束其序列化方式，即非空的类不允许序列化
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;
    //编写不同的构造方法，更改为private，禁止外部创建该类的对象
    private ServerResponse(int status) {
        this.status = status;
    }
 
    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }
 
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }
 
    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    //提供get方法，后续要返回json数据，没有get方法无法返回数据
    public int getStatus() {
        return status;
    }
 
    public String getMsg() {
        return msg;
    }
 
    public T getData() {
        return data;
    }
    //在内部提供静态方法供外部访问
    public static <T> ServerResponse<T> createSuccess() {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse<T> createSuccessMsg(String msg) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg);
    }
    public static <T> ServerResponse<T> createSuccessData(T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse<T> createSuccessMsgData(String msg,T data) {
        return new ServerResponse<>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    public static <T> ServerResponse<T> createErrorMsg(String errorMsg) {
        return new ServerResponse<>(ResponseCode.ERROR.getCode(),errorMsg);
    }
 
}