package top.krasus1966.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.krasus1966.website.pojo.Notification;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.NotificationDTO;

/**
 * @author Krasus1966
 * @date 2020/4/4 16:44
 **/
public interface NotificationService extends IService<Notification> {

    IPage<NotificationDTO> pageNotificationDTO(User user,Integer current,Integer status);
}
