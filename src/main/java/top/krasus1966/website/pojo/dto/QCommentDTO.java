package top.krasus1966.website.pojo.dto;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/3/21 14:53
 **/
@Data
public class QCommentDTO {

    private Long parentId;
    private String comment;
    private Integer type;
    private Long questionId;
}
