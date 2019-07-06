package top.krasus1966.website.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.enums.UserTypeEnum;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.QuestionDTO;
import top.krasus1966.website.service.*;
import top.krasus1966.website.utils.UploadAvatarImg;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/4/8 14:29
 **/
@Controller
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;

    @GetMapping("/admin")
    public String adminPage(@RequestParam(value = "current", defaultValue = "1") Integer current, Model model, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录");
            return "redirect:/user/login";
        } else if (!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())) {
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message", "您无此权限");
            return "redirect:/user/profile";
        }
        IPage<User> iPage = new Page<User>(current, 15);
        IPage<User> userIPage = userService.page(iPage, new QueryWrapper<User>().eq("type", UserTypeEnum.NORMAL_USER.getType()));
        model.addAttribute("page", userIPage);
        return "user/admin/userlist";
    }

    @GetMapping("/admin/user/{id}/input")
    public String adminUpdateProfilePage(@PathVariable("id") Long id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        model.addAttribute("tags", tagsService.list());
        return "user/admin/updateuser";
    }

    @PostMapping("/admin/user/profile/toUpdateProfile")
    public String adminToUpdateProfile(
            User user,
            @RequestParam(value = "avatarFile") MultipartFile file,
            HttpSession httpSession,
            RedirectAttributes redirectAttributes) {
//        User user = userService.getById(id);
        if (!file.isEmpty()) {
            String avatar = UploadAvatarImg.uploadAvatar(user, file);
            user.setAvatar(avatar);
        }
//        user.setNickname(nickname);
//        user.setEmail(email);
//        user.setSex(sex);
//        user.setDescription(description);
//        if (tags.length() != 0) {
//            user.setTags(tags);
//        }

        userService.updateById(user);
        redirectAttributes.addFlashAttribute("status", 1);
        redirectAttributes.addFlashAttribute("message", "修改成功");
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/blogs")
    public String adminBlogsPage(@PathVariable("id") Long userId, Model model, @RequestParam(value = "current", defaultValue = "1") Integer current) {
        model.addAttribute("blogs", blogService.page(new Page<>(current, 15), new QueryWrapper<Blog>().eq("user_id", userId)));
        model.addAttribute("types", typeService.list());
        model.addAttribute("userId", userId);
        return "user/admin/blogs";
    }

    /**
     * 管理员 修改 用户 博客 页面
     *
     * @param id
     * @param userId
     * @param model
     * @param httpSession
     * @return
     */
    @GetMapping("/admin/{userId}/blogs/{id}/input")
    public String editInput(@PathVariable("id") Long id, @PathVariable("userId") Long userId, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagsService.list());

        return "user/admin/blogs-input";
    }

    /**
     * 管理员 修改 用户 博客 请求
     *
     * @param blog
     * @param redirectAttributes
     * @param httpSession
     * @return
     */
    @PostMapping("/admin/blogs")
    public String adminUpdateBlog(Blog blog, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagsService.listTag(blog.getTagIds()));
        Blog b;
        b = blogService.updateBlog(blog.getId(), blog);
        Long userId = b.getUser().getId();
        redirectAttributes.addFlashAttribute("message", "操作成功");
        return "redirect:/admin/" + userId + "/blogs";
    }

    @GetMapping("/admin/{userId}/blogs/{id}/delete")
    public String adminDeleteBlog(RedirectAttributes redirectAttributes, @PathVariable("id") Long id, @PathVariable("userId") Long userId) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin/" + userId + "/blogs";
    }

    @GetMapping("/admin/{id}/questions")
    public String adminQuestionsPage(@PathVariable("id") Long userId, Model model, @RequestParam(value = "current", defaultValue = "1") Integer current) {
        model.addAttribute("questions", questionService.page(new Page<>(current, 15), new QueryWrapper<Question>().eq("creator", userId)));
        model.addAttribute("userId", userId);
        return "user/admin/questions";
    }

    @GetMapping("/admin/{userId}/question/{id}/input")
    public String adminUpdateQuestion(@PathVariable("id") Long id, @PathVariable("userId") Long userId, Model model) {
        QuestionDTO questionDTO = questionService.getQuestion(id, 1);
        model.addAttribute("question", questionDTO);
        model.addAttribute("tags", tagsService.list());
        return "user/admin/question-input";
    }

    @GetMapping("/admin/{userId}/question/{id}/delete")
    public String adminDeleteQuestion(@PathVariable("userId") Long userId, @PathVariable("id") Long id) {
        questionService.removeById(id);
        return "redirect:/admin/" + userId + "/questions";
    }

    @PostMapping("/admin/bbs/publish")
    public String doPublish(
            Question question,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println(question);
        String description = "";
        Question question1 = new Question();
        if (question.getId() != null) {
            question1 = questionService.getById(question.getId());
            question1.setDescription(question.getDescription());
            description = question1.getDescription().replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
            if (description.length() >= 50) {
                question1.setDescriptionView(description.substring(0, 49));
            }else{
                question1.setDescriptionView(description);
            }
            question1.setUpdateTime(new Date());
        }
        question1.setTags(question.getTags());
        if (questionService.updateById(question1)) {
            redirectAttributes.addFlashAttribute("message", "发表成功");
            return "redirect:/admin/" + question1.getCreator() + "/questions";
        } else {
            redirectAttributes.addFlashAttribute("message", "发表失败");
            return "redirect:/admin/" + question1.getCreator() + "/question/" + question1.getId() + "/input";
        }
    }

    @GetMapping("/admin/user/{id}/delete")
    public String adminDeleteUser(@PathVariable("id") Long id,
                                  RedirectAttributes redirectAttributes,
                                  HttpSession httpSession
    ) {
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("message","您的权限不足");
            return "redirect:/user/profile";
        }
        userService.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/admin";
    }
}
