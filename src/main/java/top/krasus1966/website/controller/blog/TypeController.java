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
import top.krasus1966.website.pojo.Type;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.service.TypeService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:36
 **/
@Controller
@RequestMapping("/user")
public class TypeController {

    @Autowired
    private TypeService typeService;


    /**
     * 分类列表
     * @param model
     * @param pageable
     * @return
     */
    @GetMapping("/types")
    public String list(
            HttpSession httpSession,
            Model model,
            @PageableDefault(size = 15,sort = {"id"},direction = Sort.Direction.DESC) Pageable pageable){
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("page",typeService.listType(user.getId(),pageable));
        return "user/types/types";
    }

    /**
     * 新增分类
     * @param model
     * @return
     */
    @GetMapping("/types/input")
    public String input(Model model){
        model.addAttribute("type",new Type());
        return "user/types/type-input";
    }

    /**
     * 修改分类 页面
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return "user/types/type-input";
    }

    /**
     * 删除分类
     * @param id
     * @param redirectAttributes
     * @return
     */
    @GetMapping("/types/{id}/delete")
    public String deleteType(@PathVariable Long id,RedirectAttributes redirectAttributes){
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/user/types";
    }

    /**
     * 新增分类请求
     * @param type
     * @param bindingResult
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/types")
    public String saveTypes(@Valid Type type,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,HttpSession httpSession){
//        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name",type.getName());
        User user = (User) httpSession.getAttribute("user");

        Type t = typeService.getOne(new QueryWrapper<Type>().eq("name",type.getName()));
        if(t!=null){
            bindingResult.rejectValue("name","nameError","该分类已存在");
        }
        if(bindingResult.hasErrors()){
            return "user/types/type-input";
        }
        Type t1 = typeService.saveType(type);
        if(t1==null){
            redirectAttributes.addFlashAttribute("message","添加失败");
        }else{
            redirectAttributes.addFlashAttribute("message","添加成功");
        }
        return "redirect:/user/types";
    }

    /**
     * 修改分类请求
     * @param type
     * @param bindingResult
     * @param id
     * @param redirectAttributes
     * @return
     */
    @PostMapping("/types/{id}")
    public String editTypes(@Valid Type type,
                            BindingResult bindingResult,
                            @PathVariable Long id,
                            RedirectAttributes redirectAttributes,HttpSession httpSession){
//        QueryWrapper<Type> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("name",type.getName());
        User user = (User) httpSession.getAttribute("user");

        Type t = typeService.getOne(new QueryWrapper<Type>().eq("name",type.getName()));
        if(t!=null){
            bindingResult.rejectValue("name","nameError","该分类已存在");
        }
        if(bindingResult.hasErrors()){
            return "user/types/type-input";
        }
        Type t1 = typeService.updateType(id,type);
        if(t1==null){
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else{
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/user/types";
    }


}
