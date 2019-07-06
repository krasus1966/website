package top.krasus1966.website.controller.bbs;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.krasus1966.website.pojo.dto.QuestionDTO;
import top.krasus1966.website.service.QuestionService;
import top.krasus1966.website.service.TagsService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/14 16:47
 **/
@Controller
public class IndexBBSController {


    @Autowired
    private QuestionService questionService;

    @Autowired
    private TagsService tagsService;


    @GetMapping("/bbs/")
    public String bbsIndex(Model model, @RequestParam(value = "current", defaultValue = "1") int current) {
        IPage<QuestionDTO> questionList = questionService.listQuestionDTO(current);
        model.addAttribute("page", questionList);
        Long pages = questionList.getPages();
        List<Integer> index = new ArrayList<>();
        if(current>4){
            for(int start=current-4;start<current;start++){
                index.add(start);
            }
        }else{
            for(int start=1;start<current;start++){
                index.add(start);
            }
        }
        if(current+5<=pages){
            for(int last=current;last<=current+5;last++){
                index.add(last);
            }
        }else {
            for(int last=current;last<=pages;last++){
                index.add(last);
            }
        }
        model.addAttribute("index",index);
        model.addAttribute("tags",tagsService.list());
        System.out.println(questionList);
        return "bbs/index";
//        Cookie[] cookies = request.getCookies();
//        if (cookies != null) {
//            for (Cookie cookie : cookies) {
//                if ("token".equals(cookie.getName())) {
//                    String token = cookie.getValue();
//                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
//                    queryWrapper.eq("token", token);
//                    User user = userMapper.selectOne(queryWrapper);
//                    if (user != null) {
//                        request.getSession().setAttribute("user", user);
//                    }
//                    break;
//                }
//            }
//            return "bbs/index";
//        } else {
//            List<QuestionDTO> questionList = questionService.listQuestionDTO();
//            model.addAttribute("questions",questionList);
//            System.out.println(questionList);
//            return "bbs/index";
//        }
    }
}
