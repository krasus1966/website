package top.krasus1966.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.krasus1966.website.pojo.Comment;

import java.util.List;

/**
 * @author Krasus1966
 * @date 2020/3/7 22:10
 **/
public interface CommentService extends IService<Comment> {

    List<Comment> listCommentByBlogId(Long blogId);

    Comment saveComment(Comment comment,Long blogId);

}
