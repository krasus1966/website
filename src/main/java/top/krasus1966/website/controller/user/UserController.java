package top.krasus1966.website.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.enums.NotificationStatusEnum;
import top.krasus1966.website.enums.NotificationTypeEnum;
import top.krasus1966.website.pojo.*;
import top.krasus1966.website.pojo.dto.NotificationDTO;
import top.krasus1966.website.service.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/16 18:12
 **/
@Controller
public class UserController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private QuestionCommentService questionCommentService;

    @Autowired
    private TagsService tagsService;

    String[] areas = {"myQuestion", "myArticle"};

    @GetMapping("/user/profile")
    public String profilePage(@RequestParam(value = "area", defaultValue = "myQuestion") String area, HttpSession httpSession, Model model) {
        int select = 0;
        for (int i = 0; i < areas.length; i++) {
            if (area.equals(areas[i])) {
                area = areas[i];
                select = i + 1;
            }
        }
        model.addAttribute("area", area);
        model.addAttribute("select", select);
        return "user/profile";
    }

    @GetMapping("/user/profile/myQuestion")
    public String myQuestion(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", user.getId()).orderByDesc("update_time");
        List<Question> questionList = questionService.list(queryWrapper);
        model.addAttribute("questionList", questionList);
        System.out.println(questionList);
        return "fragments/user_fragments :: myQuestion";
    }

    @GetMapping("/user/profile/myArticle")
    public String myArticle(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", user.getId()).orderByDesc("update_time");
        List<Blog> blogList = blogService.list(queryWrapper);
        model.addAttribute("blogList", blogList);
        return "fragments/user_fragments :: myArticle";
    }

    @GetMapping("/user/{id}/profile")
    public String profilePage2(@RequestParam(value = "area", defaultValue = "myQuestion") String area, @PathVariable("id") Long id, Model model) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", id);
        User user = userService.getById(id);
        user.setPassword("");
        List<Question> questionList = questionService.list(queryWrapper);
        int select = 0;
        for (int i = 0; i < areas.length; i++) {
            if (area.equals(areas[i])) {
                area = areas[i];
                select = i + 1;
            }
        }
        model.addAttribute("user", user);
        model.addAttribute("userId", id);
        model.addAttribute("area", area);
        model.addAttribute("select", select);
        model.addAttribute("questionList", questionList);
        return "user/otherprofile";
    }

    @GetMapping("/user/{id}/profile/myQuestion")
    public String userQuestion(@PathVariable("id") Long id, Model model) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("creator", id).orderByDesc("update_time");
        List<Question> questionList = questionService.list(queryWrapper);
        model.addAttribute("questionList", questionList);

        return "fragments/user_fragments :: myQuestion";
    }

    @GetMapping("/user/{id}/profile/myArticle")
    public String userArticle(@PathVariable("id") Long id, Model model) {
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", id).orderByDesc("update_time");
        List<Blog> blogList = blogService.list(queryWrapper);
        model.addAttribute("blogList", blogList);
        return "fragments/user_fragments :: myArticle";
    }

    @GetMapping("/user/replies")
    public String replyPage(@RequestParam(value = "current", defaultValue = "1") Integer current, @RequestParam(value = "status", required = false) Integer status, HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            return "redirect:/user/login";
        }
        IPage<NotificationDTO> iPage = notificationService.pageNotificationDTO(user, current, status);
        model.addAttribute("NotificationPage", iPage);
        return "user/reply";
    }


    @GetMapping("/user/getUnReadbbsReply")
    public String getUnReadReply(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            model.addAttribute("unReadbbs", 0);
        } else {
            Integer unReadReply = notificationService.count(new QueryWrapper<Notification>().eq("status", NotificationStatusEnum.UNREAD.getStatus()).eq("receiver", user.getId()));
            model.addAttribute("unReadbbs", unReadReply);
        }
        return "fragments/fragments :: unReadbbs";
    }

    @GetMapping("/user/readReply")
    public String getReadReply(@RequestParam("id") Long id, HttpSession httpSession, Model model, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        Notification notification = notificationService.getById(id);
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录");
            return "redirect:/user/login";
        } else if (!notification.getReceiver().equals(user.getId())) {
            redirectAttributes.addFlashAttribute("message", "请先登录");
            return "redirect:/user/login";
        } else {
            notification.setStatus(NotificationStatusEnum.READ.getStatus());
            notificationService.updateById(notification);
            if(notification.getType()== NotificationTypeEnum.REPLY_QUESTION.getType()){
                return "redirect:/bbs/question/"+notification.getOuterId();
            }else if(notification.getType()==NotificationTypeEnum.REPLY_COMMENT.getType()){
                QuestionComment questionComment = questionCommentService.getById(notification.getOuterId());
                return "redirect:/bbs/question/"+questionComment.getQuestionId();
            }else if (notification.getType() == NotificationTypeEnum.REPLY_BLOG.getType()){
                return "redirect:/blog/"+notification.getOuterId();
            }else {
                return "redirect:/blog/"+notification.getOuterId();
            }
        }
    }

    @GetMapping("/user/reply/markAllRead")
    public String markAllRead(HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录");
            return "redirect:/user/login";
        } else {
            notificationService.update(new UpdateWrapper<Notification>().eq("receiver", user.getId()).set("status", NotificationStatusEnum.READ.getStatus()));
            return "redirect:/user/replies";
        }
    }

    @GetMapping("/user/reply/deleteReply")
    public String deleteAllReply(@RequestParam(value = "id", required = false) Long id, HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "请先登录");
            return "redirect:/user/login";
        } else {
            if (id != null) {
                notificationService.remove(new QueryWrapper<Notification>().eq("id", id).eq("receiver", user.getId()));
            } else {
                notificationService.remove(new QueryWrapper<Notification>().eq("receiver", user.getId()));
            }
            return "redirect:/user/replies";
        }
    }

    @GetMapping("/user/profile/input")
    public String updateProfilePage(HttpSession httpSession, Model model) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        model.addAttribute("tags",tagsService.list());
        return "user/updateProfile";
    }

    @GetMapping("/user/profile/updatePwd")
    public String updatePwdPage(HttpSession httpSession, RedirectAttributes redirectAttributes) {
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            redirectAttributes.addFlashAttribute("message","请先登录");
            return "redirect:/user/login";
        }else{
            return "user/updatePwd";
        }
    }
}
