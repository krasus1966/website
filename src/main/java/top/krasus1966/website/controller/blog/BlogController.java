package top.krasus1966.website.controller.blog;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.vo.BlogQuery;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.service.TagsService;
import top.krasus1966.website.service.TypeService;

import javax.servlet.http.HttpSession;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:01
 **/
@Controller
@RequestMapping("/user")
public class BlogController {


    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;

    @GetMapping("/blogs")
    public String blogs(
            @PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            HttpSession httpSession,
            BlogQuery blogQuery,
            Model model) {
        User user = (User) httpSession.getAttribute("user");
        IPage<Blog> iPage = blogService.iPageBlog(user.getId(), current, blogQuery);
        model.addAttribute("page", iPage);
        //model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("types", typeService.listType());
        return "user/blogs/blogs";
    }

    @PostMapping("/blogs/search")
    public String search(
            @PageableDefault(size = 2, sort = {"updateTime"}, direction = Sort.Direction.DESC) Pageable pageable,
            BlogQuery blogQuery, Model model,
            @RequestParam("title") String title,
            @RequestParam("typeId") Integer typeId,
            @RequestParam("page") Integer current,
            HttpSession httpSession
    ) {
        User user = (User) httpSession.getAttribute("user");
        IPage<Blog> iPage = blogService.searchByTitleOrTypeId(current, title, typeId, user.getId());
        //model.addAttribute("page", blogService.listBlog(pageable, blogQuery));
        model.addAttribute("page", iPage);
        model.addAttribute("types", typeService.listType());
        return "user/blogs/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        model.addAttribute("blog", new Blog());
        model.addAttribute("types", typeService.list());
        model.addAttribute("tags", tagsService.list());
        return "user/blogs/blogs-input";
    }

    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");

        Blog blog = blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog", blog);
        model.addAttribute("types", typeService.list());
        model.addAttribute("tags", tagsService.list());

        return "user/blogs/blogs-input";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes, HttpSession httpSession) {
        blog.setUser((User) httpSession.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagsService.listTag(blog.getTagIds()));
        Blog b;
        // 屏蔽关键词
//        Body body = TextFilter.textFilter(blog.getContent());
//        if (Objects.requireNonNull(body).getRes()==0 || body.getStatus()==500){
//            redirectAttributes.addFlashAttribute("message",body.getMsg());
//            return "redirect:/user/blogs";
//        }
        if (blog.getId() == null) {
            b = blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }
        if (b == null) {
            redirectAttributes.addFlashAttribute("message", "操作失败");
        } else {
            redirectAttributes.addFlashAttribute("message", "操作成功");
        }
        return "redirect:/user/blogs";
    }

    @GetMapping("/blogs/{id}/delete")
    public String delete(RedirectAttributes redirectAttributes, @PathVariable Long id) {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message", "删除成功");
        return "redirect:/user/blogs";
    }
}
