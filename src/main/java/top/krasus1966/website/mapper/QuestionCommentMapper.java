package top.krasus1966.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.krasus1966.website.pojo.QuestionComment;

/**
 * @author Krasus1966
 * @date 2020/3/20 17:13
 **/
@Mapper
public interface QuestionCommentMapper extends BaseMapper<QuestionComment> {

    @Update("update k_question_comment set reply_count = reply_count + #{record.replyCount,jdbcType=BIGINT} where id = #{record.id}")
    void incReplyCommentCount(@Param("record") QuestionComment questionComment);
}
