package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.dao.TypeRepository;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.exception.NotFoundException;
import top.krasus1966.website.mapper.BlogMapper;
import top.krasus1966.website.mapper.TypeMapper;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.Type;
import top.krasus1966.website.service.TypeService;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:25
 **/
@Service
public class TypeSerivceImpl extends ServiceImpl<TypeMapper, Type> implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private TypeRepository typeRepository;

    /**
     * 新增分类 JPA
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type saveType(Type type) {
        return typeRepository.save(type);
    }

    /**
     * 根据ID查询分类 Mybatis-plus
     * @param id
     * @return
     */
    @Override
    public Type getType(Long id) {
        return typeMapper.selectById(id);
    }

    /**
     * 通过标签名查询标签 JPA
     * @param name
     * @return
     */
    @Override
    public Type getTypeByName(String name) {

        return typeRepository.findByName(name);
    }

    /**
     * 分页查询 JPA
     * @param pageable
     * @return
     */
    @Override
    public Page<Type> listType(Long userId,Pageable pageable) {

        return typeRepository.findByUserId(pageable);
    }

    /**
     * 通过ID更新分类 JPA
     * @param id
     * @param type
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Type updateType(Long id, Type type) {
        Type t = typeRepository.getOne(id);
        if(t == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);
        return typeRepository.save(t);
    }

    /**
     * 删除分类 Mybatis-plus
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteType(Long id) {
        if (blogMapper.selectList(new QueryWrapper<Blog>().eq("type_id",id)).size()!=0){
            throw new CustomizeException(CustomizeErrorCode.TYPE_NOT_NULL);
        }
        typeMapper.deleteById(id);
    }

    @Override
    public List<Type> listType() {
        return typeRepository.findAll();
    }

    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return typeRepository.findTop(pageable);
    }
}
