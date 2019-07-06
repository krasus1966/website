package top.krasus1966.website.pojo.dto;

import lombok.Data;
import top.krasus1966.website.pojo.User;

import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/14 18:22
 **/
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String descriptionView;
    private Date createTime;
    private Date updateTime;
    private Long creator;
    private Integer viewCount;
    private Integer likeCount;
    private String tags;
    private User user;
    private List<QuestionCommentDTO> questionCommentDTOList;
}
