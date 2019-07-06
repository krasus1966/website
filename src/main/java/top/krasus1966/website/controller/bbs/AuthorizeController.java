package top.krasus1966.website.controller.bbs;//package top.krasus1966.website.community.controller.bbs;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import top.krasus1966.website.community.mapper.UserMapper;
//import top.krasus1966.website.community.pojo.User;
//import top.krasus1966.website.community.pojo.dto.AccessTokenDTO;
//import top.krasus1966.website.community.pojo.dto.GithubUser;
//import top.krasus1966.website.community.provider.GithubProvider;
//
//import javax.servlet.http.Cookie;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.UUID;
//
///**
// * @author Krasus1966
// * @date 2020/3/10 14:50
// **/
//@Controller
//@RequestMapping("/user")
//public class AuthorizeController {
//    @Autowired
//    private GithubProvider githubProvider;
//
//    @Value("${github.client.id}")
//    private String clientId;
//
//    @Value("${github.client.secret}")
//    private String clientSecret;
//
//    @Value("${github.client.uri}")
//    private String redirectUri;
//
//    @Autowired
//    private UserMapper userMapper;
//
//    @GetMapping("/callback")
//    public String callback(@RequestParam("code")String code,
//                           @RequestParam("state") String state,
//                           HttpServletRequest request,
//                           HttpServletResponse response
//                           ){
//        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
//        accessTokenDTO.setClient_id(clientId);
//        accessTokenDTO.setClient_secret(clientSecret);
//        accessTokenDTO.setCode(code);
//        accessTokenDTO.setRedirect_uri(redirectUri);
//        accessTokenDTO.setState(state);
//        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
//        GithubUser githubUser = githubProvider.getUser(accessToken);
//        if(githubUser != null){
//            User user = new User();
//            String token =UUID.randomUUID().toString();
//            user.setToken(token);
//            user.setName(githubUser.getName());
//            user.setAccountId(String.valueOf(githubUser.getId()));
//            user.setGmtCreate(System.currentTimeMillis());
//            user.setGmtModified(user.getGmtCreate());
//            user.setAvatar(githubUser.getAvatarUrl());
//            userMapper.insert(user);
//
//            response.addCookie(new Cookie("token",token));
//
//            return "redirect:/bbs/";
//        }else{
//            return "redirect:/bbs/";
//        }
//    }
//}
