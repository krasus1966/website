package top.krasus1966.website.controller.blog;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.enums.UserTypeEnum;
import top.krasus1966.website.pojo.Tag;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.TagsService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:36
 **/
@Controller
@RequestMapping("/user")
public class TagsController {

    @Autowired
    private TagsService tagsService;

    /**
     * 分类列表
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/tags")
    public String list(
            HttpSession httpSession,
            Model model,
            RedirectAttributes redirectAttributes,
            @PageableDefault(size = 15,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable){
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("message","您的权限不足");
            return "redirect:/user/profile";
        }
        model.addAttribute("page", tagsService.listTags(pageable));
        return "user/tags/tags";
    }

    /**
     * 新增分类
     * @param model
     * @return
     */
    @GetMapping("/tags/input")
    public String input(Model model,HttpSession httpSession ,RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message","您无此权限");
            return "redirect:/user/profile";
        }
        model.addAttribute("tag",new Tag());
        return "user/tags/tags-input";
    }

    /**
     * 修改分类 页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model,HttpSession httpSession,RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message","您无此权限");
            return "redirect:/user/profile";
        }
        model.addAttribute("tag", tagsService.getTags(id));
        return "user/tags/tags-input";
    }

    /**
     * 删除分类
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/tags/{id}/delete")
    public String deleteType(@PathVariable Long id,RedirectAttributes redirectAttributes,HttpSession httpSession){
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message","您无此权限");
            return "redirect:/user/profile";
        }
        tagsService.deleteTags(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/user/tags";
    }

    /**
     * 新增分类请求
     * @param tag
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/tags")
    public String saveTypes(@Valid Tag tag,
                            BindingResult bindingResult,
                            HttpSession httpSession,
                            RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        if(!user.getType().equals(UserTypeEnum.ADMIN_USER.getType())){
            redirectAttributes.addFlashAttribute("status", 0);
            redirectAttributes.addFlashAttribute("message","您无此权限");
            return "redirect:/user/profile";
        }
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",tag.getName());
        Tag t = tagsService.getOne(queryWrapper);
        if(t!=null){
            bindingResult.rejectValue("name","nameError","该分类已存在");
        }
        if(bindingResult.hasErrors()){
            return "user/tags/tags-input";
        }
        Tag t1 = tagsService.saveTags(tag);
        if(t1==null){
            redirectAttributes.addFlashAttribute("message","添加失败");
        }else{
            redirectAttributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/user/tags";
    }

    /**
     * 修改分类请求
     * @param tag
     * @param bindingResult
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/tags/{id}")
    public String editTypes(@Valid Tag tag,
                            BindingResult bindingResult,
                            @PathVariable Long id,
                            RedirectAttributes redirectAttributes){
        QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",tag.getName());
        Tag t = tagsService.getOne(queryWrapper);
        if(t!=null){
            bindingResult.rejectValue("name","nameError","该标签已存在");
        }
        if(bindingResult.hasErrors()){
            return "user/tags/tags-input";
        }
        Tag t1 = tagsService.updateTags(id,tag);
        if(t1==null){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else{
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/user/tags";
    }
}
