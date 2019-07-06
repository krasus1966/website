package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/3/20 16:37
 **/
@Data
@TableName("k_question_comment")
public class QuestionComment {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Date createTime;
    private Date updateTime;
    private Long likeCount;
    private Long commentator;
    private String comment;
    private Long parentId;
    private Long questionId;
    private Integer type;
    private Integer replyCount=0;

}
