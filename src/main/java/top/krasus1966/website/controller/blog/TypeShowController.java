package top.krasus1966.website.controller.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import top.krasus1966.website.pojo.Type;
import top.krasus1966.website.pojo.vo.BlogQuery;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.service.TypeService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/7 23:00
 **/
@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{id}")
    public String types(@PathVariable("id")Long id,
                        @PageableDefault(size = 15,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        HttpSession httpSession,
                        Model model){

        List<Type> types = typeService.listTypeTop(10000);
        if(id==-1){
            if(types.size()!=0){
                id = types.get(0).getId();
            }
        }
        BlogQuery blogQuery = new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        model.addAttribute("activeTypeId",id);
        return "blog/types";
    }
}
