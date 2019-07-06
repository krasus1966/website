package top.krasus1966.website.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.krasus1966.website.pojo.Tag;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.service.TagsService;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/7 23:04
 **/
@Controller
public class TagsShowController {

    @Autowired
    private TagsService tagsService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable("id")Long id,
                        @PageableDefault(size = 15,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        Model model){
        List<Tag> tags = tagsService.listTagTop(10000);
        if(id==-1){
            if(tags.size()!=0){
                id = tags.get(0).getId();
            }
        }
        model.addAttribute("tags",tags);
        model.addAttribute("page",blogService.listBlog(id,pageable));
        model.addAttribute("activeTagsId",id);
        return "blog/tags";
    }
}
