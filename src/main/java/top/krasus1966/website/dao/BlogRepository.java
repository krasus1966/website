package top.krasus1966.website.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.pojo.Blog;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 23:44
 **/
public interface BlogRepository extends JpaRepository<Blog,Long>, JpaSpecificationExecutor<Blog> {

    @Query(value = "select b from top.krasus1966.website.pojo.Blog as b ")
    List<Blog> findTop(Pageable pageable);

    @Query(value = "select b from top.krasus1966.website.pojo.Blog b where b.title like ?1 or b.content like ?1")
    Page<Blog> findByQuery(String query, Pageable pageable);

    @Query(value = "select function('date_format',b.createTime,'%Y') as year from top.krasus1966.website.pojo.Blog b where b.user.id = ?1 group by function('date_format',b.createTime,'%Y') order by year")
    List<String> findGroupYear(Long id);

    @Query(value = "select b from top.krasus1966.website.pojo.Blog b where function('date_format',b.updateTime,'%Y') =?1 and b.user.id = ?2")
    List<Blog> findByYear(String year,Long id);

    @Transactional
    @Modifying
    @Query(value = "update top.krasus1966.website.pojo.Blog b set b.views = b.views+1 where b.id =?1")
    int updateViews(Long id);


}
