package top.krasus1966.website.enums;

/**
 * @author Krasus1966
 * @date 2020/4/4 16:46
 **/
public enum  NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论"),
    REPLY_BLOG(3,"回复了博客"),
    REPLY_BLOG_COMMENT(4,"回复了博客的评论")
    ;

    private int type;
    private String name;

    NotificationTypeEnum(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
