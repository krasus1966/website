package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/4/7 16:43
 **/
@Data
@TableName("k_blog_like")
public class BlogLikeList {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long blogId;
    private Long userId;

}
