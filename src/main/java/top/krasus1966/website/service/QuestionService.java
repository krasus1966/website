package top.krasus1966.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.dto.QuestionDTO;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/14 18:24
 **/
public interface QuestionService extends IService<Question> {
    IPage<QuestionDTO> listQuestionDTO(int current);

    QuestionDTO getQuestion(Long id,Integer type);

    void updateView(Long id);

    List<Question> findOtherQuestion(Long id,String[] tags);

    IPage<QuestionDTO> findByTagId(Long tagId,Integer current);
}
