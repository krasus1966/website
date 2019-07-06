package top.krasus1966.website.pojo.dto;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/3/10 17:48
 **/
@Data
public class GithubUser {

    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
