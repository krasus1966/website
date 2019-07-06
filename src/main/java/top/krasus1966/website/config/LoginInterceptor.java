package top.krasus1966.website.config;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:08
 **/
public class LoginInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String lastRequestURL = request.getRequestURI();
        System.out.println(lastRequestURL);
        if(request.getSession().getAttribute("user")==null){
            response.sendRedirect("/user/login?forwardURL="+lastRequestURL);
            return false;
        }
        return true;
    }
}
