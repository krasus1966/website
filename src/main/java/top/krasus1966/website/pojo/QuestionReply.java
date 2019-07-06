package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/3/20 19:21
 **/
@Data
@TableName("k_question_comment_reply_comment")
public class QuestionReply {
    private Long commentId;
    private Long replyCommentId;
}
