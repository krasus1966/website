package top.krasus1966.website.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.BlogService;

import javax.servlet.http.HttpSession;

/**
 * @author Krasus1966
 * @date 2020/3/7 23:38
 **/
@Controller
public class ArchiveShowController {

    @Autowired
    private BlogService blogService;

    @GetMapping("/blog/archives")
    public String archives(Model model, HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            return "redirect:/user/login";
        }
        model.addAttribute("archiveMap",blogService.archiveBlog(user.getId()));
        model.addAttribute("blogCount",blogService.countBlog(user.getId()));
        return "blog/archives";
    }
}
