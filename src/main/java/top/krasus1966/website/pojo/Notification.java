package top.krasus1966.website.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/4/4 16:03
 **/
@Data
public class Notification {

    private Long id;
    private Long notifier;
    private Long receiver;
    private Long outerId;
    private String outerTitle;
    private int type;
    private Date createTime;
    private Integer status;
}
