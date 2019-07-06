package top.krasus1966.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.vo.BlogQuery;

import java.util.List;
import java.util.Map;


/**
 * @author Krasus1966
 * @date 2020/3/5 23:44
 **/
public interface BlogService extends IService<Blog> {

    Blog getBlog(Long id);

    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);

    Page<Blog> listBlog(Pageable pageable);

    Page<Blog> listBlog(Long tagId,Pageable pageable);

    Page<Blog> listBlog(String query,Pageable pageable);

    Map<String,List<Blog>> archiveBlog(Long id);

    Integer countBlog(Long id);

    Blog getAndConvert(Long id);

    List<Blog> listRecommendBlogTop(Integer size);

    Blog saveBlog(Blog blog);

    Blog updateBlog(Long id,Blog blog);

    void deleteBlog(Long id);

    IPage<Blog> iPageBlog(Long userId,Integer current,BlogQuery blogQuery);

    IPage<Blog> searchByTitleOrTypeId(Integer current, String title, Integer typeId, Long id);
}
