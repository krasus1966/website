package top.krasus1966.website.enums;

/**
 * @author Krasus1966
 * @date 2020/4/6 17:22
 **/
public enum UserTypeEnum {
    NORMAL_USER(0),
    ADMIN_USER(1),
    CONTROL_USER(2);

    private Integer type;

    public Integer getType() {
        return type;
    }

    UserTypeEnum(Integer type) {
        this.type = type;
    }
}
