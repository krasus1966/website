package top.krasus1966.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import top.krasus1966.website.pojo.Blog;

/**
 * @author Krasus1966
 * @date 2020/3/5 23:43
 **/
@Mapper
public interface BlogMapper extends BaseMapper<Blog> {

    @Update("update k_blog set like_count = like_count + #{record.likeCount,jdbcType=BIGINT} where id = #{record.id}")
    void incLikeCount(@Param("record") Blog blog);

    @Update("update k_blog set like_count = like_count - #{record.likeCount,jdbcType=BIGINT} where id = #{record.id}")
    void delLikeCount(@Param("record")Blog blog);
}
