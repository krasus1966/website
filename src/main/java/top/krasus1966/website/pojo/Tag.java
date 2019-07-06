package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 13:38
 **/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "k_tag")
@TableName("k_tag")
public class Tag {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "标签名不能为空")
    private String name;

    @ManyToMany(mappedBy = "tags")
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();
}
