package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.dao.UserRepository;
import top.krasus1966.website.enums.UserStatusEnum;
import top.krasus1966.website.enums.UserTypeEnum;
import top.krasus1966.website.mapper.*;
import top.krasus1966.website.pojo.*;
import top.krasus1966.website.pojo.vo.BlogCommentReply;
import top.krasus1966.website.pojo.vo.BlogTags;
import top.krasus1966.website.service.UserService;
import top.krasus1966.website.utils.MD5Utils;

import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 14:34
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionCommentMapper questionCommentMapper;

    @Autowired
    private BlogCommentReplyMapper blogCommentReplyMapper;

    @Autowired
    private BlogTagsMapper blogTagsMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;



    @Autowired
    private UserRepository userRepository;

    @Override
    public User checkUser(String username, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("username",username).eq("password", MD5Utils.code(password));
        User user =  userMapper.selectOne(queryWrapper);
        return user;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUser(User user) {
        user.setPassword(MD5Utils.code(user.getPassword()));
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus(UserStatusEnum.NORMAL.getStatus());
        user.setType(UserTypeEnum.NORMAL_USER.getType());
        userMapper.insert(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer checkPwd(User user, String oldPassword, String newPassword) {
        User oldUser =  userMapper.selectById(user.getId());
        if(!oldUser.getPassword().equals(MD5Utils.code(oldPassword))){
            return 0;
        }else if (oldUser.getPassword().equals(MD5Utils.code(newPassword))){
            return 1;
        }else{
            return 2;
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteById(Long id) {
        List<Blog> blogList = blogMapper.selectList(new QueryWrapper<Blog>().eq("user_id",id));
        for (Blog blog :blogList){
            blogTagsMapper.delete(new QueryWrapper<BlogTags>().eq("blogs_id",blog.getId()));
            List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("blog_id",blog.getId()));
            for (Comment comment : commentList){
                blogCommentReplyMapper.delete(new QueryWrapper<BlogCommentReply>().eq("k_blog_comment_id",comment.getParentCommentId()));
                comment.setParentCommentId(null);
                commentMapper.update(comment,new UpdateWrapper<Comment>().eq("id",comment.getId()).set("parent_comment_id",null));
            }
            commentMapper.delete(new QueryWrapper<Comment>().eq("blog_id",blog.getId()));
            blogMapper.deleteById(blog.getId());
        }
        List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("creator",id));
        for(Question question :questionList){
            questionCommentMapper.delete(new QueryWrapper<QuestionComment>().eq("commentator",id));
            questionCommentMapper.delete(new QueryWrapper<QuestionComment>().eq("question_id",question.getId()));
            questionMapper.deleteById(question.getId());
        }
        userMapper.deleteById(id);
    }
}
