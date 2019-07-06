package top.krasus1966.website.enums;

/**
 * @author Krasus1966
 * @date 2020/4/6 18:11
 **/
public enum UserStatusEnum {

    NORMAL(0,"正常"),
    UNREPLY(1,"禁止评论"),
    LOCK(2,"锁定");

    private Integer status;
    private String message;

    UserStatusEnum(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
