package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/4 16:51
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "k_blog")
@TableName("k_blog")
public class Blog {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String content;

    private String firstPicture;

    private String flag;

    private Integer views;

    private Integer likeCount;

    private Boolean published;


    @Basic(fetch = FetchType.LAZY)
    @Lob
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;


    @ManyToOne
    @TableField(exist = false)
    @NotFound(action=NotFoundAction.IGNORE)
    private Type type;

    @Transient
    private Long typeId;

    @ManyToMany(cascade = {CascadeType.PERSIST})
    @TableField(exist = false)
    @NotFound(action= NotFoundAction.IGNORE)
    private List<Tag> tags = new ArrayList<>();

    @Transient
    @TableField(exist = false)
    private String tagIds;

    public void init(){
        this.tagIds = tagsToIds(this.getTags());
    }
    private String tagsToIds(List<Tag> tags){
        if(!tags.isEmpty()){
            StringBuffer ids = new StringBuffer();
            boolean flag =false;
            for(Tag tag : tags){
                if(flag){
                    ids.append(",");
                }else{
                    flag=true;
                }
                ids.append(tag.getId());
            }
            return ids.toString();
        }else{
            return tagIds;
        }
    }
    @ManyToOne
    @TableField(exist = false)
    private User user;

    @OneToMany(mappedBy = "blog")
    @TableField(exist = false)
    private List<Comment> comments = new ArrayList<>();

}
