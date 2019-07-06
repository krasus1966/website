package top.krasus1966.website.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import top.krasus1966.website.pojo.User;

/**
 * @author Krasus1966
 * @date 2020/3/5 14:49
 **/
@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsernameAndPassword(String username,String password);
}
