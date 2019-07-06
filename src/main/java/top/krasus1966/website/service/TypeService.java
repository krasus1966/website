package top.krasus1966.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.krasus1966.website.pojo.Type;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:24
 **/
public interface TypeService extends IService<Type> {
    Type saveType(Type type);

    Type getType(Long id);

    Type getTypeByName(String name);

    Page<Type> listType(Long userId,Pageable pageable);

    Type updateType(Long id,Type type);

    void deleteType(Long id);

    List<Type> listType();

    List<Type> listTypeTop(Integer size);
}
