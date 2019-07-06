package top.krasus1966.website.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.krasus1966.website.pojo.User;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 图片上传接口
 * @author Krasus1966
 * @date 2019/11/13 11:06
 **/

@RestController
@RequestMapping("/img")
public class UploadImg {

    @PostMapping("/cross_upload")
    public Map<String, Object> upLoad(@RequestParam(value = "editormd-image-file") MultipartFile file,
                                      HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String resource = System.getProperty("user.dir");
            File targetFile = new File(resource+"/target/classes/static/"+user.getUsername() + "/img/", Objects.requireNonNull(file.getOriginalFilename()));
            if (!targetFile.exists()) {
                targetFile.getParentFile().mkdirs();
            }
            file.transferTo(targetFile);
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功");
            resultMap.put("url", "http://localhost:8081/" + user.getUsername() + "/img/" + file.getOriginalFilename());
        } catch (IOException e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败");
            e.printStackTrace();
        }
        return resultMap;
    }
}
