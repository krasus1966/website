package top.krasus1966.website.enums;

/**
 * @author Krasus1966
 * @date 2020/4/4 16:54
 **/
public enum NotificationStatusEnum {
    UNREAD(0),READ(1);

    private int status;


    public int getStatus() {
        return status;
    }

    NotificationStatusEnum(int status) {
        this.status = status;
    }
}
