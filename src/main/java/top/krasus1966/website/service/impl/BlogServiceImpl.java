package top.krasus1966.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.krasus1966.website.dao.BlogRepository;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.exception.NotFoundException;
import top.krasus1966.website.mapper.*;
import top.krasus1966.website.pojo.Blog;
import top.krasus1966.website.pojo.Comment;
import top.krasus1966.website.pojo.Type;
import top.krasus1966.website.pojo.vo.BlogCommentReply;
import top.krasus1966.website.pojo.vo.BlogQuery;
import top.krasus1966.website.pojo.vo.BlogTags;
import top.krasus1966.website.service.BlogService;
import top.krasus1966.website.utils.MarkdownUtils;
import top.krasus1966.website.utils.MyBeanUtils;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * @author Krasus1966
 * @date 2020/3/5 23:45
 **/
@Service
public class BlogServiceImpl  extends ServiceImpl<BlogMapper,Blog> implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BlogCommentReplyMapper blogCommentReplyMapper;

    @Autowired
    private BlogTagsMapper blogTagsMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private TypeMapper typeMapper;

    @Autowired
    private BlogRepository blogRepository;


    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                if(!"".equals(blogQuery.getTitle())&& blogQuery.getTitle() !=null){
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));
                }
                if(blogQuery.getTypeId() != null){
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type"),blogQuery.getTypeId()));
                }
                if(blogQuery.isRecommend()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));

                return null;
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join = root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        }, pageable);
    }

    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery(query, pageable);
    }

    @Override
    public Map<String, List<Blog>> archiveBlog(Long id) {
        List<String> years = blogRepository.findGroupYear(id);
        Map<String ,List<Blog>> map = new HashMap<>();
        for(String year :years){
            map.put(year,blogRepository.findByYear(year,id));
        }
        return map;
    }

    @Override
    public Integer countBlog(Long id) {
        return blogMapper.selectCount(new QueryWrapper<Blog>().eq("user_id",id));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog getAndConvert(Long id) {
        Blog blog = blogRepository.findById(id).orElse(null);
        if(blog == null){
            throw new CustomizeException(CustomizeErrorCode.BLOG_NOT_FOUND);
        }
        Blog blog1 = new Blog();
        BeanUtils.copyProperties(blog,blog1);
        String content = blog1.getContent();
        blog1.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);
        return blog1;
    }

    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
//        Sort sort= Sort.by(Sort.Direction.DESC,"updateTime");
        Pageable pageable = PageRequest.of(0,size,Sort.by(Sort.Direction.DESC,"updateTime"));
        return blogRepository.findTop(pageable);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog saveBlog(Blog blog) {
        if (blog.getId() == null){
            blog.setCreateTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
            blog.setLikeCount(0);

        }else {
            blog.setUpdateTime(new Date());
        }
        return blogRepository.save(blog);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog blog1 = blogRepository.getOne(id);
        if (blog1 == null){
            throw  new NotFoundException();
        }
        BeanUtils.copyProperties(blog,blog1, MyBeanUtils.getNullPropertyNames(blog));
        blog1.setUpdateTime(new Date());
        return blogRepository.save(blog1);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteBlog(Long id) {
        blogTagsMapper.delete(new QueryWrapper<BlogTags>().eq("blogs_id",id));
        List<Comment> commentList = commentMapper.selectList(new QueryWrapper<Comment>().eq("blog_id",id));
        for (Comment comment : commentList){
            blogCommentReplyMapper.delete(new QueryWrapper<BlogCommentReply>().eq("k_blog_comment_id",comment.getParentCommentId()));
            comment.setParentCommentId(null);
            commentMapper.update(comment,new UpdateWrapper<Comment>().eq("id",comment.getId()).set("parent_comment_id",null));
        }
        commentMapper.delete(new QueryWrapper<Comment>().eq("blog_id",id));
        blogMapper.deleteById(id);
    }

    @Override
    public IPage<Blog> iPageBlog(Long userId,Integer current,BlogQuery blogQuery) {
        IPage<Blog> iPage = blogMapper.selectPage(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Blog>(current,15),new QueryWrapper<Blog>().eq("user_id",userId));
        return iPage;
    }

    @Override
    public IPage<Blog> searchByTitleOrTypeId(Integer current, String title, Integer typeId, Long id) {
        IPage<Blog> iPage = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<Blog>(current,15);
        QueryWrapper<Blog> queryWrapper = new QueryWrapper<>();
        if (typeId == null){
            queryWrapper.eq("user_id",id).and(i->{
                i.like("title",title);
            });
        }else{
            queryWrapper.eq("user_id",id).and(i->{
                i.like("title",title).eq("type_id",typeId);
            });
        }
        IPage<Blog> result = blogMapper.selectPage(iPage,queryWrapper);
        return result;
    }
}
