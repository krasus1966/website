package top.krasus1966.website.pojo.vo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/4/9 15:17
 **/
@Data
@TableName("k_blog_comment_reply_comments")
public class BlogCommentReply {

    private Long kBlogCommentId;
    private Long replyCommentsId;
}
