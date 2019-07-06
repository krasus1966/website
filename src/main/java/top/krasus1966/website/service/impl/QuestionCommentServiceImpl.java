package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.enums.CommentTypeEnum;
import top.krasus1966.website.enums.NotificationStatusEnum;
import top.krasus1966.website.enums.NotificationTypeEnum;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.mapper.*;
import top.krasus1966.website.pojo.*;
import top.krasus1966.website.pojo.dto.QuestionCommentDTO;
import top.krasus1966.website.service.QuestionCommentService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/20 17:14
 **/
@Service
@Transactional
public class QuestionCommentServiceImpl extends ServiceImpl<QuestionCommentMapper, QuestionComment> implements QuestionCommentService {

    @Autowired
    private QuestionCommentMapper questionCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionReplyMapper questionReplyMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private NotificationMapper notificationMapper;


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveQuestionComment(QuestionComment questionComment) {
//        QuestionComment parentComment = questionCommentMapper.selectOne(new QueryWrapper<QuestionComment>().eq("parent_id",questionComment.getParentId()));
        QuestionReply questionReply = new QuestionReply();
        questionComment.setCreateTime(new Date());
        questionComment.setUpdateTime(new Date());
        if (questionComment.getParentId() != -1) {
            questionCommentMapper.insert(questionComment);
            questionReply.setCommentId(questionComment.getParentId());
            questionReply.setReplyCommentId(questionComment.getId());
            questionReplyMapper.insert(questionReply);
        } else {
            questionComment.setParentId(null);
            questionCommentMapper.insert(questionComment);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createQuestionComment(QuestionComment questionComment) {
        Question question = new Question();
        if (questionComment.getParentId() == null || questionComment.getParentId() == -1 || questionComment.getQuestionId() == null) {
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        } else if (questionComment.getType() == null || !CommentTypeEnum.isExist(questionComment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_WRONG);
        } else if (questionComment.getType().equals(CommentTypeEnum.COMMENT.getType())) {
            QuestionComment questionComment1 = questionCommentMapper.selectById(questionComment.getParentId());
            if (questionComment1 == null) {
                throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
            }
            question = questionMapper.selectById(questionComment.getQuestionId());
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
            questionCommentMapper.insert(questionComment);
            questionComment1.setReplyCount(1);
            questionCommentMapper.incReplyCommentCount(questionComment1);
            //通知功能
            createNotify(questionComment, questionComment1.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);
        } else {
            question = questionMapper.selectById(questionComment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            questionCommentMapper.insert(questionComment);
            question.setUpdateTime(new Date());
            questionMapper.updateById(question);
            question.setCommentCount(1);
            questionMapper.incCommentCount(question);
            //通知功能
            createNotify(questionComment, question.getCreator(), NotificationTypeEnum.REPLY_QUESTION);
        }
    }

    @Override
    public List<QuestionCommentDTO> secondComment(Long id, CommentTypeEnum type) {
        QueryWrapper<QuestionComment> questionCommentQueryWrapper = new QueryWrapper<>();
        questionCommentQueryWrapper.eq("parent_id", id);
        questionCommentQueryWrapper.eq("type", type.getType());
        List<QuestionComment> questionCommentList = new ArrayList<>();
        List<QuestionCommentDTO> questionCommentDTOList = new ArrayList<>();
        questionCommentList = questionCommentMapper.selectList(questionCommentQueryWrapper);
        for (QuestionComment questionComment : questionCommentList) {
            QuestionCommentDTO questionCommentDTO = new QuestionCommentDTO();
            BeanUtils.copyProperties(questionComment, questionCommentDTO);
            questionCommentDTO.setUser(userMapper.selectById(questionComment.getCommentator()));
            questionCommentDTO.setParentName(userMapper.selectById(questionCommentMapper.selectById(questionComment.getParentId()).getCommentator()).getNickname());
            questionCommentDTOList.add(questionCommentDTO);
        }
        return questionCommentDTOList;
    }

    /**
     * 通知功能
     *
     * @param questionComment
     * @param receiver
     */
    @Transactional(rollbackFor = Exception.class)
    private void createNotify(QuestionComment questionComment, Long receiver, NotificationTypeEnum notificationType) {
        Notification notification = new Notification();
        notification.setCreateTime(new Date());
        notification.setType(notificationType.getType());
        notification.setOuterId(questionComment.getParentId());
        String outerTitle = questionComment.getComment().replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
        if(outerTitle.length()>=20){
            notification.setOuterTitle(outerTitle.substring(0,19));
        }else{
            notification.setOuterTitle(outerTitle);
        }
        notification.setNotifier(questionComment.getCommentator());
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notification.setReceiver(receiver);
        notificationMapper.insert(notification);
    }

    @Override
    public List<QuestionCommentDTO> listComment(Long questionId) {
        QueryWrapper<QuestionComment> questionCommentQueryWrapper = new QueryWrapper<>();
        questionCommentQueryWrapper.eq("question_id", questionId);
        questionCommentQueryWrapper.eq("parent_id", -1);
        List<QuestionComment> questionCommentList = questionCommentMapper.selectList(questionCommentQueryWrapper);
        return eachQuestionComment(questionCommentList);
    }

    private List<QuestionCommentDTO> eachQuestionComment(List<QuestionComment> questionCommentList) {
        List<QuestionCommentDTO> questionCommentDTOList = new ArrayList<>();
        for (QuestionComment questionComment : questionCommentList) {
            QuestionCommentDTO questionCommentDTO = new QuestionCommentDTO();
            BeanUtils.copyProperties(questionComment, questionCommentDTO);
            questionCommentDTO.setUser(userMapper.selectById(questionComment.getCommentator()));
            questionCommentDTOList.add(questionCommentDTO);
        }
        combineChildren(questionCommentDTOList);
        return questionCommentDTOList;
    }

    private void combineChildren(List<QuestionCommentDTO> questionCommentDTOList) {
        for (QuestionCommentDTO questionCommentDTO : questionCommentDTOList) {
            List<QuestionReply> questionReplyList = questionReplyMapper.selectList(new QueryWrapper<QuestionReply>().eq("comment_id", questionCommentDTO.getId()));
            List<QuestionCommentDTO> replys = new ArrayList<>();
            for (QuestionReply questionReply : questionReplyList) {
                QuestionComment questionComment = questionCommentMapper.selectOne(new QueryWrapper<QuestionComment>().eq("id", questionReply.getReplyCommentId()));
                QuestionCommentDTO questionCommentDTO1 = new QuestionCommentDTO();
                BeanUtils.copyProperties(questionComment, questionCommentDTO1);
                questionCommentDTO1.setUser(userMapper.selectOne(new QueryWrapper<User>().eq("id", questionComment.getCommentator())));
                System.out.println(userMapper.selectOne(new QueryWrapper<User>().eq("id", questionComment.getCommentator())));
                replys.add(questionCommentDTO1);
            }
            for (QuestionCommentDTO reply : replys) {
                recursively(reply);
            }
            questionCommentDTO.setReplys(tempReplys);
            tempReplys = new ArrayList<>();
        }
    }

    private List<QuestionCommentDTO> tempReplys = new ArrayList<>();

    private void recursively(QuestionCommentDTO questionCommentDTO) {
        tempReplys.add(questionCommentDTO);
        if (questionCommentDTO.getReplys().size() > 0) {
            List<QuestionCommentDTO> replys = questionCommentDTO.getReplys();
            for (QuestionCommentDTO reply : replys) {
                tempReplys.add(reply);
                if (reply.getReplys().size() > 0) {
                    recursively(reply);
                }
            }
        }
    }
}
