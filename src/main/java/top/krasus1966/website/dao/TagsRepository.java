package top.krasus1966.website.dao;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.krasus1966.website.pojo.Tag;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:28
 **/
public interface TagsRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);

    @Query(value = "select t from top.krasus1966.website.pojo.Tag as t")
    List<Tag> findTop(Pageable pageable);
}
