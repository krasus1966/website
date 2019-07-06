package top.krasus1966.website.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.pojo.dto.ResultDTO;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Krasus1966
 * @date 2020/3/3 15:53
 **/
@ControllerAdvice
public class ControllerExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object exceptionHandler(Model model, HttpServletRequest httpServletRequest, Exception e) throws Exception {
        logger.error("Request URL : {},Exception : {}", httpServletRequest.getRequestURL(), e);
        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
            throw e;
        }
        String contentType = httpServletRequest.getContentType();
        if ("application/json;charset=UTF-8".equals(contentType)) {
            //返回JSON格式的错误信息
            if (e instanceof CustomizeException) {
                return ResultDTO.errorOf((CustomizeException) e);
            } else {
                return ResultDTO.errorOf(CustomizeErrorCode.SYSTEM_ERROR);
            }
        } else {
            //跳转到错误页面
            ModelAndView mv = new ModelAndView();
            if (e instanceof CustomizeException) {
                model.addAttribute("code", ((CustomizeException) e).getCode());
                model.addAttribute("message", e.getMessage());
            } else {
                model.addAttribute("code", CustomizeErrorCode.SYSTEM_ERROR.getCode());
                model.addAttribute("message", CustomizeErrorCode.SYSTEM_ERROR.getMessage());
            }
            mv.addObject("url", httpServletRequest.getRequestURL());
            mv.addObject("exception", e);
            mv.setViewName("error/error");
            return mv;
        }
    }
}
