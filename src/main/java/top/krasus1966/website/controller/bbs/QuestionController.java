package top.krasus1966.website.controller.bbs;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.Tag;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.QuestionDTO;
import top.krasus1966.website.service.QuestionService;
import top.krasus1966.website.service.TagsService;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/18 14:59
 **/
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private TagsService tagsService;

    @GetMapping("/bbs/question/{id}")
    public String question(@PathVariable("id")Long id, Model model){
        questionService.updateView(id);
        QuestionDTO questionDTO = questionService.getQuestion(id,1);
        model.addAttribute("question",questionDTO);
        return "bbs/question";
    }

    @GetMapping("/bbs/question/{id}/input")
    public String updateQuestion(@PathVariable("id")Long id,Model model){
        QuestionDTO questionDTO = questionService.getQuestion(id,1);
        List<Tag> tags = tagsService.list();
        model.addAttribute("tags",tags);
        model.addAttribute("question",questionDTO);
        return "user/bbs/question-input";
    }

    @GetMapping("/bbs/otherQuestion/{id}/{tags}")
    public String otherQuestion(@PathVariable("id")Long id,@PathVariable("tags")String tags,Model model){
        String[] tagList = tags.split(",");
        List<Question> questionList = questionService.findOtherQuestion(id,tagList);
        model.addAttribute("otherQuestions",questionList);
        return "bbs/question :: otherQuestion";
    }

    @GetMapping("/bbs/tag/{tagId}")
    public String tagsQuestion(@PathVariable("tagId") Long tagId, @RequestParam(value = "current",defaultValue = "1")int current, Model model){
        IPage<QuestionDTO> questionList = questionService.findByTagId(tagId,current);
        List<Tag> tags = tagsService.list();
        model.addAttribute("questionList",questionList);
        model.addAttribute("tags",tags);
        model.addAttribute("activeTagsId",tagId);
        return "bbs/tags";
    }

    @GetMapping("/bbs/question/{id}/delete")
    public String deleteQuestion(@PathVariable("id")Long id){
        questionService.removeById(id);
        return "redirect:/user/questions";
    }

    @GetMapping("/user/questions")
    public String questionList(@RequestParam(value = "current",defaultValue = "1")Integer current, HttpSession httpSession,Model model){
        User user = (User) httpSession.getAttribute("user");
        List<Tag> tags = tagsService.list();
        model.addAttribute("tags",tags);
        model.addAttribute("userId",user.getId());
        model.addAttribute("questions", questionService.page(new Page<>(current, 15), new QueryWrapper<Question>().eq("creator", user.getId())));
        return "user/bbs/questions";
    }
}
