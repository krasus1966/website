package top.krasus1966.website.pojo;

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
 * @date 2020/3/5 13:41
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "k_user")
@TableName("k_user")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    private String avatar;
    private String description;
    private Integer status;
    private Integer type;
    private String sex;
    private String tags;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @OneToMany(mappedBy = "user")
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();
}
