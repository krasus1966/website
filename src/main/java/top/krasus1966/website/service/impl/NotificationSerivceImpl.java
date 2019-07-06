package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.krasus1966.website.mapper.NotificationMapper;
import top.krasus1966.website.mapper.QuestionCommentMapper;
import top.krasus1966.website.mapper.QuestionMapper;
import top.krasus1966.website.mapper.UserMapper;
import top.krasus1966.website.pojo.Notification;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.NotificationDTO;
import top.krasus1966.website.service.NotificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/4/4 16:44
 **/
@Service
public class NotificationSerivceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionCommentMapper questionCommentMapper;

    @Override
    public IPage<NotificationDTO> pageNotificationDTO(User user,Integer current,Integer status) {
        QueryWrapper<Notification> queryWrapper = new QueryWrapper<Notification>().eq("receiver",user.getId()).orderByDesc("create_time");
        if (status!=null){
            queryWrapper.eq("status",status);
        }
        IPage<Notification> notificationIPage = notificationMapper.selectPage(new Page<Notification>(current,15),queryWrapper);
        List<NotificationDTO> dtoList = new ArrayList<>();
        IPage<NotificationDTO> notificationDTOIPage = new Page<>();
        for (Notification notification:notificationIPage.getRecords()){
            User notifier = new User();
            NotificationDTO notificationDTO = new NotificationDTO();
            notifier = userMapper.selectById(notification.getNotifier());
            BeanUtils.copyProperties(notification,notificationDTO);
            notificationDTO.setNotifier(notifier);
            dtoList.add(notificationDTO);
        }
        BeanUtils.copyProperties(notificationIPage,notificationDTOIPage);
        notificationDTOIPage.setRecords(dtoList);
        return notificationDTOIPage;
    }
}
