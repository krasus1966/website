package top.krasus1966.website.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.TagsService;
import top.krasus1966.website.service.UserService;
import top.krasus1966.website.utils.MD5Utils;
import top.krasus1966.website.utils.UploadAvatarImg;

import javax.servlet.http.HttpSession;

/**
 * @author Krasus1966
 * @date 2020/4/6 18:15
 **/
@Controller
public class RegisterController {
    @Autowired
    private TagsService tagsService;

    @Autowired
    private UserService userService;

    @GetMapping("/user/register")
    public String registerPage(Model model) {
        model.addAttribute("tags", tagsService.listTag());
        return "user/register";
    }

    /**
     * 注册请求
     * @param user 用户信息
     * @param file 用户头像
     * @return 重定向至登录页面
     */
    @PostMapping("/user/toRegister")
    public String toRegister(User user, @RequestParam(value = "avatarFile") MultipartFile file,RedirectAttributes redirectAttributes) {
        User testUser = userService.getOne(new QueryWrapper<User>().eq("username",user.getUsername()));
        if(testUser != null){
            redirectAttributes.addFlashAttribute("message","此用户名已被注册");
            return "redirect:/user/register";
        }
        if (file.isEmpty()) {
            user.setAvatar("/img/defaultAvatar.jpg");
        } else {
            String avatar = UploadAvatarImg.uploadAvatar(user, file);
            user.setAvatar(avatar);
        }
        userService.saveUser(user);
        return "redirect:/user/login";
    }

    @PostMapping("/user/profile/toUpdateProfile")
    public String toUpdateProfile(@RequestParam("nickname") String nickname,
                                  @RequestParam("email") String email,
                                  @RequestParam("sex") String sex,
                                  @RequestParam("description") String description,
                                  @RequestParam("tags") String tags,
                                  @RequestParam(value = "avatarFile") MultipartFile file,
                                  HttpSession httpSession,
                                  RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if(!file.isEmpty()){
            String avatar = UploadAvatarImg.uploadAvatar(user, file);
            user.setAvatar(avatar);
        }
        user.setNickname(nickname);
        user.setEmail(email);
        user.setSex(sex);
        user.setDescription(description);
        if (tags.length() != 0) {
            user.setTags(tags);
        }
        userService.updateById(user);
        redirectAttributes.addFlashAttribute("status", 1);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/user/profile";
    }

    @PostMapping("/user/profile/toUpdatePwd")
    public String toUpdatePwd(
            @RequestParam("oldPwd") String oldPassword,
            @RequestParam("newPwd") String newPassword,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) httpSession.getAttribute("user");
        Integer result = userService.checkPwd(user, oldPassword, newPassword);
        if (result == 0) {
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message", "原密码输入错误");
            return "redirect:/user/profile/updatePwd";
        } else if (result == 1) {
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message", "原密码不能与旧密码相同");
            return "redirect:/user/profile/updatePwd";
        } else {
            user.setPassword(MD5Utils.code(newPassword));
            userService.updateById(user);
            redirectAttributes.addFlashAttribute("status", 1);
            redirectAttributes.addFlashAttribute("message", "密码修改成功");
            return "redirect:/user/profile";
        }
    }
}
