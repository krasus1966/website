package top.krasus1966.website.controller.bbs;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.QuestionDTO;
import top.krasus1966.website.service.QuestionService;
import top.krasus1966.website.service.TagsService;
import top.krasus1966.website.service.TypeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author Krasus1966
 * @date 2020/3/10 14:40
 **/
@Controller
public class CommunityController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagsService tagsService;


    @GetMapping("/user/bbs/input")
    public String inputQuestion(Model model) {
        QuestionDTO questionDTO = new QuestionDTO();
        model.addAttribute("question", questionDTO);
        model.addAttribute("tags", tagsService.list());
        return "user/bbs/question-input";
    }

    @PostMapping("/bbs/publish")
    public String doPublish(
            Question question,
            HttpServletRequest request,
            HttpServletResponse response,
            RedirectAttributes redirectAttributes,
            HttpSession httpSession,
            Model model
    ) {
        User user = (User) httpSession.getAttribute("user");
        System.out.println(user);
        System.out.println(question);
        // 屏蔽关键词
//        Body body = TextFilter.textFilter(question.getDescription());
//        if (Objects.requireNonNull(body).getRes()==0 || body.getStatus()==500){
//            redirectAttributes.addFlashAttribute("message",body.getMsg());
//            return "redirect:/user/bbs/input";
//        }
        String description = "";
        Question question1 = new Question();
        if (user == null) {
            redirectAttributes.addFlashAttribute("message", "用户未登录");
            return "redirect:/user/login";
        } else {
            if (question.getId() != null) {
                question1 = questionService.getById(question.getId());
                question1.setDescription(question.getDescription());
                description = question1.getDescription().replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
                if (description.length() >= 50) {
                    question1.setDescriptionView(description.substring(0, 49));
                } else {
                    question1.setDescriptionView(description);
                }
                question1.setUpdateTime(new Date());
            } else {
                BeanUtils.copyProperties(question, question1);
                description = question1.getDescription().replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("[(/>)<]", "");
                if (description.length() >= 50) {
                    question1.setDescriptionView(description.substring(0, 49));
                } else {
                    question1.setDescriptionView(description);
                }
                question1.setCreator(user.getId());
                question1.setCreateTime(new Date());
                question1.setUpdateTime(new Date());
            }
            question1.setTags(question.getTags());
            if (questionService.saveOrUpdate(question1)) {
                redirectAttributes.addFlashAttribute("message", "发表成功");
                return "redirect:/bbs/";
            } else {
                redirectAttributes.addFlashAttribute("message", "发表失败");
                return "redirect:/user/bbs/input";
            }
        }
    }

}
