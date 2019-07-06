package top.krasus1966.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.krasus1966.website.pojo.User;

/**
 * @author Krasus1966
 * @date 2020/3/5 14:33
 **/

public interface UserService extends IService<User> {

    User checkUser(String username,String password);

    void saveUser(User user);

    Integer checkPwd(User user, String oldPassword, String newPassword);

    void deleteById(Long id);
}
