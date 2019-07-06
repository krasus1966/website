package top.krasus1966.website.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.krasus1966.website.mapper.BlogLikeListMapper;
import top.krasus1966.website.mapper.BlogMapper;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.BlogLikeList;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.service.TagsService;
import top.krasus1966.website.service.TypeService;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Krasus1966
 * @date 2020/3/6 19:53
 **/
@Controller
@RequestMapping("/blog")
public class IndexBlogController {


    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private BlogLikeListMapper blogLikeListMapper;

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;


    @GetMapping("/")
    public String indexBlog(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable, Model model){
        model.addAttribute("page",blogService.listBlog(pageable));
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagsService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "blog/index";
    }

    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC)Pageable pageable,
                         @RequestParam("query") String query,
                         Model model){
        model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
        model.addAttribute("query",query);
        return "blog/search";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable("id") Long id,Model model,HttpSession httpSession){
        Integer likeStatus = 0;
        if(httpSession.getAttribute("user")!=null){
            User user = (User) httpSession.getAttribute("user");
            BlogLikeList blogLikeList = new BlogLikeList();
            blogLikeList = blogLikeListMapper.selectOne(new QueryWrapper<BlogLikeList>().eq("blog_id",id).eq("user_id",user.getId()));
            if(blogLikeList!=null){
                likeStatus = 1;
            }
        }
        model.addAttribute("blog",blogService.getAndConvert(id));
        model.addAttribute("like",likeStatus);
        return "blog/blog";
    }

    @GetMapping("/footer/newblog")
    public String newBlogs(Model model){
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "fragments/fragments :: newblogList";
    }


    @GetMapping("/{id}/like")
    @ResponseBody
    @Transactional
    public Map likeBlogById(@PathVariable("id") Long blogId, HttpSession httpSession) {
        Blog blog = blogService.getBlog(blogId);
        User user = (User) httpSession.getAttribute("user");
        Map<String,Integer> map = new HashMap<>();
        if (user == null){
            map.put("userLikeStatus",0);
            return map;
        }
        BlogLikeList blogLikeList = blogLikeListMapper.selectOne(new QueryWrapper<BlogLikeList>().eq("blog_id",blogId).eq("user_id",user.getId()));
        if(user.getId().equals(blog.getUser().getId())){
            map.put("userLikeStatus",2);
            return map;
        }else if(blogLikeList!=null){
            blogLikeListMapper.delete(new QueryWrapper<>(blogLikeList));
            blog.setLikeCount(1);
            blogMapper.delLikeCount(blog);
            map.put("userLikeStatus",0);
            return map;
        } else{
            BlogLikeList blogLikeList1 = new BlogLikeList();
            blogLikeList1.setBlogId(blogId);
            blogLikeList1.setUserId(user.getId());
            blogLikeListMapper.insert(blogLikeList1);
            blog.setLikeCount(1);
            blogMapper.incLikeCount(blog);
            map.put("userLikeStatus",1);
            return map;
        }
    }
}
