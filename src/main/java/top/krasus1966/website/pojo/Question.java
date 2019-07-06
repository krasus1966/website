package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/3/13 14:25
 **/
@Data
@TableName("k_question")
@Entity(name = "k_question")
public class Question {

    @Id
    @GeneratedValue
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String description;
    private String descriptionView;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
    private Long creator;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private String tags;
}
