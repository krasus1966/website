package top.krasus1966.website.utils;

import org.springframework.web.multipart.MultipartFile;
import top.krasus1966.website.exception.CustomizeErrorCode;
import top.krasus1966.website.exception.CustomizeException;
import top.krasus1966.website.pojo.User;

import java.io.File;
import java.io.IOException;

/**
 * @author Krasus1966
 * @date 2020/4/6 19:51
 **/
public class UploadAvatarImg {

    public static String uploadAvatar(User user, MultipartFile file){
        if (file.isEmpty()) {
            throw new CustomizeException(CustomizeErrorCode.UPLOAD_IMG_WRONG);
        }
        String resource = System.getProperty("user.dir");
        // 文件名
        String fileName = file.getOriginalFilename();
        // 后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        // 上传后的路径
        String filePath = resource+"/target/classes/static/"+user.getUsername()+"/img/avatar/";
        // 新文件名
        fileName =user.getUsername()+"-"+ System.currentTimeMillis() +suffixName;
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String avatar = "/"+user.getUsername()+"/img/avatar/"+fileName;
        return avatar;
    }
}
