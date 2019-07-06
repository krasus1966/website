package top.krasus1966.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.krasus1966.website.pojo.Question;

/**
 * @author Krasus1966
 * @date 2020/3/13 14:24
 **/
public interface QuestionMapper extends BaseMapper<Question> {

    @Update("update k_question set view_count = view_count + #{record.viewCount,jdbcType=BIGINT} where id = #{record.id}")
    void updateView(@Param("record") Question record);

    @Update("update k_question set comment_count = comment_count + #{record.commentCount,jdbcType=BIGINT} where id = #{record.id}")
    void incCommentCount(@Param("record") Question question);
}
