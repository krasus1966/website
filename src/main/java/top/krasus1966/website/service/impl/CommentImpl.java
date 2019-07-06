package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.dao.CommentRepository;
import top.krasus1966.website.enums.NotificationTypeEnum;
import top.krasus1966.website.mapper.CommentMapper;
import top.krasus1966.website.mapper.UserMapper;
import top.krasus1966.website.pojo.Comment;
import top.krasus1966.website.pojo.Notification;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.CommentService;
import top.krasus1966.website.service.NotificationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/7 22:10
 **/
@Service
public class CommentImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private NotificationService notificationSerivce;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = Sort.by("createTime");
        List<Comment> comments = commentRepository.findByBlogIdAndParentCommentNull(blogId, sort);
        return eachCommtent(comments);
    }

    /**
     * 循环每个顶级评论节点
     *
     * @param comments
     * @return
     */
    private List<Comment> eachCommtent(List<Comment> comments) {
        List<Comment> commentsView = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment();
            BeanUtils.copyProperties(comment, c);
            commentsView.add(c);
        }
        combineChildren(commentsView);
        return commentsView;
    }

    /**
     * 存放迭代找出的所有子代的集合
     */
    private List<Comment> tempReplys = new ArrayList<>();
    /**
     * 修改顶级节点的reply集合为迭代处理后的集合
     *
     * @param comments
     */
    private void combineChildren(List<Comment> comments) {
        for (Comment comment : comments) {
            List<Comment> replys1 = comment.getReplyComments();
            for (Comment reply1 : replys1) {
                tempReplys.add(reply1);
                recursively(reply1);
            }
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }



    /**
     * 递归迭代
     *
     * @param comment
     */
    private void recursively(Comment comment) {
        if (comment.getReplyComments().size() > 0) {
            List<Comment> replys = comment.getReplyComments();
            for (Comment reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplyComments().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Comment saveComment(Comment comment,Long blogId) {
        List<Comment> replyComments = new ArrayList<>();
        if (comment.getParentComment().getId() != -1) {
            Comment parentComment = commentRepository.getOne(comment.getParentComment().getId());
            replyComments = parentComment.getReplyComments();
            replyComments.add(comment);
            parentComment.setReplyComments(replyComments);
            comment.setParentComment(parentComment);
        } else {
            comment.setParentComment(null);
        }
        comment.setCreateTime(new Date());
        User user = userMapper.selectById(comment.getUserId());
        comment.setAvatar(user.getAvatar());
        comment.setNickname(user.getNickname());
        comment = commentRepository.save(comment);
        Notification notification = new Notification();
        notification.setOuterId(blogId);
        String outerTitle = "";
        if (comment.getParentComment() == null) {
            notification.setReceiver(comment.getBlog().getUser().getId());
            notification.setOuterTitle(comment.getBlog().getTitle());
            notification.setType(NotificationTypeEnum.REPLY_BLOG.getType());
        } else {
            notification.setReceiver(comment.getParentComment().getUserId());
            if (comment.getParentComment().getContent().length() >= 20) {
                outerTitle = comment.getParentComment().getContent().substring(0, 19);
                notification.setOuterTitle(outerTitle);
                notification.setType(NotificationTypeEnum.REPLY_BLOG_COMMENT.getType());
            } else {
                outerTitle = comment.getParentComment().getContent();
                notification.setOuterTitle(outerTitle);
                notification.setType(NotificationTypeEnum.REPLY_BLOG_COMMENT.getType());
            }
        }
        notification.setNotifier(comment.getUserId());
        notification.setCreateTime(new Date());
        notificationSerivce.save(notification);
        return comment;
    }
}
