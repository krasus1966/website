package top.krasus1966.website.controller.bbs;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import top.krasus1966.website.enums.CommentTypeEnum;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.pojo.QuestionComment;
import top.krasus1966.website.pojo.User;
import top.krasus1966.website.pojo.dto.QCommentDTO;
import top.krasus1966.website.pojo.dto.QuestionCommentDTO;
import top.krasus1966.website.pojo.dto.ResultDTO;
import top.krasus1966.website.service.QuestionCommentService;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/20 16:44
 **/
@Controller
public class QuestionCommentController {

    @Autowired
    private QuestionCommentService questionCommentService;


    @GetMapping("/bbs/comments/{questionId}")
    public String questionComment(@PathVariable("questionId") Long questionId, HttpSession httpSession, Model model){
        List<QuestionCommentDTO> questionCommentDTOS = questionCommentService.listComment(questionId);
        model.addAttribute("questionComments",questionCommentDTOS);
        return "bbs/question :: commentList";
    }
//
//    @PostMapping("/bbs/comments")
//    public String saveComment(@RequestParam("question.id")Long questionId, @RequestParam("parentComment.id")Long parentId, @RequestParam("comment")String comment, HttpSession httpSession){
//        User user = (User) httpSession.getAttribute("user");
//        QuestionComment questionComment = new QuestionComment();
//        if(user != null){
//            questionComment.setCommentator(user.getId());
//            questionComment.setQuestionId(questionId);
//            questionComment.setParentId(parentId);
//            questionComment.setComment(comment);
//            questionCommentService.saveQuestionComment(questionComment);
//            return "redirect:/bbs/comments/"+questionId;
//        }else{
//
//            return "redirect:/bbs/comments/"+questionId;
//        }
//    }

    @ResponseBody
    @PostMapping("/bbs/questioncomment")
    public Object postComment(@RequestBody QCommentDTO qCommentDTO, HttpSession httpSession, RedirectAttributes redirectAttributes){
        User user = (User) httpSession.getAttribute("user");
        if(user == null){
            redirectAttributes.addFlashAttribute("message","请先登录");
            return ResultDTO.errorOf(CustomizeErrorCode.NOT_LOGIN);
        }
        if (qCommentDTO == null || StringUtils.isBlank(qCommentDTO.getComment())){
            return "空";
        }
        QuestionComment questionComment = new QuestionComment();
        BeanUtils.copyProperties(qCommentDTO,questionComment);
        questionComment.setCreateTime(new Date());
        questionComment.setUpdateTime(new Date());
        questionComment.setCommentator(user.getId());
        questionCommentService.createQuestionComment(questionComment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @GetMapping("/bbs/secondcomment/{id}")
    public ResultDTO<List<QuestionCommentDTO>> secondComments(@PathVariable("id") Long id){
        List<QuestionCommentDTO> questionCommentDTOList = questionCommentService.secondComment(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(questionCommentDTOList);
    }

    @GetMapping("/bbs/secondcomments/{id}")
    public String secondComment(@PathVariable("id") Long id,Model model){
        List<QuestionCommentDTO> questionCommentDTOList = questionCommentService.secondComment(id, CommentTypeEnum.COMMENT);
        model.addAttribute("SecondComment",questionCommentDTOList);
        return "bbs/question :: commentArea";
    }
}
