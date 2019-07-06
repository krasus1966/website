package top.krasus1966.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.krasus1966.website.enums.CommentTypeEnum;
import top.krasus1966.website.pojo.QuestionComment;
import top.krasus1966.website.pojo.dto.QuestionCommentDTO;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/20 17:13
 **/
public interface QuestionCommentService extends IService<QuestionComment> {
    List<QuestionCommentDTO> listComment(Long questionId);

    void saveQuestionComment(QuestionComment questionComment);

    void createQuestionComment(QuestionComment questionComment);

    List<QuestionCommentDTO> secondComment(Long id, CommentTypeEnum comment);
}
