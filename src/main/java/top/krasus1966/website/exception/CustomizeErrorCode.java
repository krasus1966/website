package top.krasus1966.website.exception;

/**
 * @author Krasus1966
 * @date 2020/4/6 14:19
 **/
public enum  CustomizeErrorCode implements ICustomizeErrorCode{

    QUESTION_NOT_FOUND(2001,"您要找的问题不见了，换一个看看？"),
    TARGET_NOT_FOUND(2002,"回复的评论或问题不存在或回复了错误的评论"),
    NOT_LOGIN(1001,"未登录，请先登录"),
    SYSTEM_ERROR(5000,"服务器出错啦，请稍后再试"),
    TYPE_WRONG(2003,"评论类型错误"),
    UPLOAD_IMG_WRONG(3001,"图片上传错误"),
    BLOG_NOT_FOUND(2004,"您要找的博客消失啦，换一个看看？"),
    TYPE_NOT_NULL(3001,"您要删除的分类中还存在文章，请先处理文章再试"),
    TAGS_NOT_NULL(3002,"有内容还在使用此标签，请处理后再试")
    ;

    private Integer code;
    private String message;


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    CustomizeErrorCode(Integer code,String message){
        this.code = code;
        this.message = message;
    }
}
