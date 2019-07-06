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
import top.krasus1966.website.dao.TagsRepository;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.exception.NotFoundException;
import top.krasus1966.website.mapper.BlogTagsMapper;
import top.krasus1966.website.mapper.QuestionMapper;
import top.krasus1966.website.mapper.TagsMapper;
import top.krasus1966.website.pojo.Question;
import top.krasus1966.website.pojo.Tag;
import top.krasus1966.website.pojo.vo.BlogTags;
import top.krasus1966.website.service.TagsService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:25
 **/
@Service
public class TagsSerivceImpl extends ServiceImpl<TagsMapper, Tag> implements TagsService {

    @Autowired
    private TagsMapper tagsMapper;

    @Autowired
    private BlogTagsMapper blogTagsMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private TagsRepository tagsRepository;

    /**
     * 新增标签 JPA
     * @param tag
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag saveTags(Tag tag) {
        return tagsRepository.save(tag);
    }

    /**
     * 根据ID查询标签 Mybatis-plus
     * @param id
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag getTags(Long id) {
        return tagsMapper.selectById(id);
    }

    /**
     * 通过标签名查询标签 JPA
     * @param name
     * @return
     */
    @Override
    public Tag getTagsByName(String name) {

        return tagsRepository.findByName(name);
    }

    /**
     * 分页查询 JPA
     * @param pageable
     * @return
     */
    @Override
    public Page<Tag> listTags(Pageable pageable) {
        return tagsRepository.findAll(pageable);
    }

    /**
     * 通过ID更新标签 JPA
     * @param id
     * @param tag
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Tag updateTags(Long id, Tag tag) {
        Tag t = tagsRepository.getOne(id);
        if(t == null){
            throw new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(tag,t);
        return tagsRepository.save(t);
    }

    @Override
    public List<Tag> listTag() {
        return tagsRepository.findAll();
    }

    @Override
    public List<Tag> listTag(String ids) {

        return tagsRepository.findAllById(convertToList(ids));
    }

    @Override
    public List<Tag> listTagTop(Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC,"blogs.size");
        Pageable pageable = PageRequest.of(0,size,sort);
        return tagsRepository.findTop(pageable);
    }

    private List<Long> convertToList(String ids){
        List<Long> list = new ArrayList<>();
        if(!"".equals(ids) && ids !=null){
            String[] idarray = ids.split(",");
            for (int i=0;i<idarray.length;i++){
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }

    /**
     * 删除标签 Mybatis-plus
     * @param id
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteTags(Long id) {
        Tag tag = tagsMapper.selectById(id);
        List<Question> questionList = questionMapper.selectList(new QueryWrapper<Question>().eq("tags",tag.getName()));
        if (blogTagsMapper.selectList(new QueryWrapper<BlogTags>().eq("tags_id",id)).size()!=0){
            throw new CustomizeException(CustomizeErrorCode.TAGS_NOT_NULL);
        }else if(questionMapper.selectList(new QueryWrapper<Question>().eq("tags",tag.getName())).size()!=0){
            throw new CustomizeException(CustomizeErrorCode.TAGS_NOT_NULL);
        }else{
            tagsRepository.deleteById(id);
        }
    }



}
