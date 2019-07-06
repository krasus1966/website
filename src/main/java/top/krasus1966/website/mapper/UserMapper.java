package top.krasus1966.website.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.krasus1966.website.pojo.User;

/**
 * @author Krasus1966
 * @date 2020/3/5 14:32
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
