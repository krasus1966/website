package top.krasus1966.website.pojo.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Krasus1966
 * @date 2020/3/6 13:43
 **/
@Data
@NoArgsConstructor
public class BlogQuery {

    private String title;
    private Long typeId;
    private boolean recommend;
}
