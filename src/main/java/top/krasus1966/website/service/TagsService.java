package top.krasus1966.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.krasus1966.website.pojo.Tag;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:24
 **/
public interface TagsService extends IService<Tag> {
    Tag saveTags(Tag tag);

    Tag getTags(Long id);

    Tag getTagsByName(String name);

    Page<Tag> listTags(Pageable pageable);

    Tag updateTags(Long id,Tag tag);

    List<Tag> listTag();

    List<Tag> listTag(String ids);

    List<Tag> listTagTop(Integer size);

    void deleteTags(Long id);
}
