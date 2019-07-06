package top.krasus1966.website.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import top.krasus1966.website.pojo.Type;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/5 16:28
 **/
public interface TypeRepository extends JpaRepository<Type,Long> {

    Type findByName(String name);

    @Query(value = "select t from top.krasus1966.website.pojo.Type t")
    Page<Type> findByUserId(Pageable pageable);

    @Query(value = "select t from top.krasus1966.website.pojo.Type t")
    List<Type> findTop(Pageable pageable);
}
