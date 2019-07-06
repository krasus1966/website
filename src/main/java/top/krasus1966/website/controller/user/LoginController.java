package top.krasus1966.website.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.UserService;

import javax.servlet.http.HttpSession;

/**
 * @author Krasus1966
 * @date 2020/3/5 15:01
 **/
@Controller
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "forwardURL",required = false)String forwardURL, Model model) {
        model.addAttribute("forwardURL",forwardURL);
        return "user/login";
    }

    @PostMapping("/toLogin")
    public String login(HttpSession httpSession,
                        @RequestParam("username") String username,
                        @RequestParam("password") String password,
                        @RequestParam(value = "forwardURL",required = false)String forwardURL,
                        RedirectAttributes redirectAttributes
    ) {
        User user = userService.checkUser(username, password);
        System.out.println(user);
        if (user != null) {
            user.setPassword(null);
            httpSession.setAttribute("user", user);
            if("".equals(forwardURL)){
                return "redirect:/blog/";
            }
            return "redirect:"+forwardURL;
        } else {
            redirectAttributes.addFlashAttribute("message", "用户名或密码错误！");
            return "redirect:/user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession httpSession,@RequestParam("forwardURL")String forwardURL) {
        httpSession.removeAttribute("user");
        return "redirect:"+forwardURL;
    }

}
