package top.krasus1966.website.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import top.krasus1966.website.pojo.Comment;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/7 22:13
 **/
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogIdAndParentCommentNull(Long blogId, Sort sort);

}
