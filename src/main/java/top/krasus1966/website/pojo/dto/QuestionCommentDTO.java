package top.krasus1966.website.pojo.dto;

import lombok.Data;
import top.krasus1966.website.pojo.QuestionComment;
import top.krasus1966.website.pojo.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/20 16:37
 **/
@Data
public class QuestionCommentDTO {

    private Long id;
    private Date createTime;
    private Date updateTime;
    private Long likeCount;
    private Long commentator;
    private String comment;
    private Long parentId;
    private Long questionId;
    private Integer replyCount;

    private User user;

    private String parentName;

    private QuestionComment parentComment;

    private List<QuestionCommentDTO> replys=new ArrayList<>();


}
