package top.krasus1966.website.pojo.dto;

import lombok.Data;

/**
 * @author Krasus1966
 * @date 2020/3/10 17:26
 **/
@Data
public class AccessTokenDTO {

    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
