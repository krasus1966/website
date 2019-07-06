package top.krasus1966.website.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 13:36
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "k_type")
@TableName("k_type")
public class Type {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "分类名不能为空")
    private String name;

    @OneToMany(mappedBy = "type")
    @TableField(exist = false)
    private List<Blog> blogs = new ArrayList<>();

    @Override
    public String toString() {
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
