package top.krasus1966.website.exception;

/**
 * @author Krasus1966
 * @date 2020/4/6 14:22
 **/
public class CustomizeException extends RuntimeException {
    private Integer code;
    private String message;


    public CustomizeException(ICustomizeErrorCode errorCode){
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
    @Override
    public String getMessage(){
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
