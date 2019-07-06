package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.mapper.QuestionCommentMapper;
import top.krasus1966.website.mapper.QuestionMapper;
import top.krasus1966.website.mapper.TagsMapper;
import top.krasus1966.website.mapper.UserMapper;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.QuestionComment;
import top.krasus1966.website.pojo.Tag;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.QuestionCommentDTO;
import top.krasus1966.website.pojo.dto.QuestionDTO;
import top.krasus1966.website.service.QuestionService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/14 18:24
 **/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private QuestionCommentMapper questionCommentMapper;

    @Autowired
    private TagsMapper tagsMapper;

    @Override
    public IPage<QuestionDTO> listQuestionDTO(int current) {
        IPage<Question> questionIPage = questionMapper.selectPage(new Page<>(current, 15), new QueryWrapper<Question>().orderByDesc("update_time"));
//        List<Question> questionList = questionMapper.selectList(new QueryWrapper<>());
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        IPage<QuestionDTO> questionDTOIPage = new Page();
        for (Question question : questionIPage.getRecords()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
            queryWrapper.eq("id", question.getCreator());
            User user = userMapper.selectOne(queryWrapper);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        BeanUtils.copyProperties(questionIPage, questionDTOIPage);
        questionDTOIPage.setRecords(questionDTOList);
        return questionDTOIPage;
    }

    @Override
    public QuestionDTO getQuestion(Long id, Integer type) {
        QuestionDTO questionDTO = new QuestionDTO();
        Question question = questionMapper.selectById(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
        queryWrapper.eq("id", question.getCreator());
        User user = userMapper.selectOne(queryWrapper);
        BeanUtils.copyProperties(question, questionDTO);
        List<QuestionComment> questionCommentList = new ArrayList<>();
        List<QuestionCommentDTO> questionCommentDTOList = new ArrayList<>();
        questionCommentList = questionCommentMapper.selectList(new QueryWrapper<QuestionComment>().eq("parent_id", question.getId()).eq("type", type));
        for (QuestionComment questionComment : questionCommentList) {
            QuestionCommentDTO questionCommentDTO = new QuestionCommentDTO();
            BeanUtils.copyProperties(questionComment, questionCommentDTO);
            questionCommentDTO.setUser(userMapper.selectById(questionComment.getCommentator()));
            questionCommentDTOList.add(questionCommentDTO);
        }
        questionDTO.setQuestionCommentDTOList(questionCommentDTOList);
        questionDTO.setUser(user);
        return questionDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionMapper.updateView(question);
    }

    @Override
    public List<Question> findOtherQuestion(Long id, String[] tags) {
        QueryWrapper<Question> questionQueryWrapper = new QueryWrapper<>();
        questionQueryWrapper.ne("id", id).and(queryWrapper -> {
            for (int i = 0; i < tags.length; i++) {
                if (i + 1 == tags.length) {
                    queryWrapper.like("tags",tags[i]);
                } else {
                    queryWrapper.like("tags",tags[i]).or();
                }
            }
        });
        List<Question> questionList = questionMapper.selectList(questionQueryWrapper);
        return questionList;
    }

    @Override
    public IPage<QuestionDTO> findByTagId(Long tagId,Integer current) {
        Tag tag = tagsMapper.selectById(tagId);
        IPage<Question> questionIPage = questionMapper.selectPage(new Page<Question>(current,15),new QueryWrapper<Question>().like("tags",tag.getName()));
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        IPage<QuestionDTO> questionDTOIPage = new Page();
        for(Question question:questionIPage.getRecords()){
            QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
            queryWrapper.eq("id", question.getCreator());
            User user = userMapper.selectOne(queryWrapper);
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        BeanUtils.copyProperties(questionIPage, questionDTOIPage);
        questionDTOIPage.setRecords(questionDTOList);
        return questionDTOIPage;
    }
}
