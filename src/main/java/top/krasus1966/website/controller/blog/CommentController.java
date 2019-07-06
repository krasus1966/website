package top.krasus1966.website.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.Comment;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.service.CommentService;
import top.krasus1966.website.service.NotificationService;

import javax.servlet.http.HttpSession;

/**
 * @author Krasus1966
 * @date 2020/3/7 22:06
 **/
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable("blogId") Long blogId, Model model){
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));
        return "blog/blog :: commentList";
    }

    @PostMapping("/comments")
    public String blogComment(Comment comment, HttpSession httpSession, RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        Long blogId= comment.getBlog().getId();
        if(user != null){
            comment.setUserId(user.getId());
        }else{
            redirectAttributes.addFlashAttribute("message","请先登录");
            return "redirect:/user/login";
        }
        Blog blog = blogService.getBlog(blogId);
        comment.setBlog(blog);
        if (!blog.getUser().getId().equals(user.getId())){
            comment.setAdminComments(false);
        }
        commentService.saveComment(comment,blogId);
        return "redirect:/comments/"+comment.getBlog().getId() ;
    }
}
