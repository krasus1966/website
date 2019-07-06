package top.krasus1966.website.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/4/9 14:57
 **/
@Data
@TableName("k_blog_tags")
public class BlogTags {
    private Long blogsId;
    private Long tagsId;
}
