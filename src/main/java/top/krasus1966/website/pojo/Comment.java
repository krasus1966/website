package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 13:38
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "k_blog_comment")
@TableName("k_blog_comment")
public class Comment {

    @Id
    @GeneratedValue
    private Long id;
    private String content;
    private Long userId;
    private String avatar;
    private String nickname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    private boolean adminComments;

    @ManyToOne
    @TableField(exist = false)
    private Blog blog;

    @Transient
    @TableField(value = "blog_id")
    private Long commentBlogId;

    @OneToMany
    @TableField(exist = false)
    private List<Comment> replyComments = new ArrayList<>();

    @ManyToOne
    @TableField(exist = false)
    private Comment parentComment;

    @Transient
    @TableField(value = "parent_comment_id",updateStrategy = FieldStrategy.IGNORED)
    private Long parentCommentId;
}
