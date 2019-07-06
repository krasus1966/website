package top.krasus1966.website.pojo.dto;

import lombok.Data;
import top.krasus1966.website.pojo.User;

import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/4/4 18:06
 **/
@Data
public class NotificationDTO {

    private Long id;
    private User notifier;
    private String outerTitle;
    private Long outerId;
    private String notifile;
    private int type;
    private Date createTime;
    private Integer status;
}
