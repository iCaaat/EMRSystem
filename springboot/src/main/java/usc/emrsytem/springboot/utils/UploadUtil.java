package usc.emrsytem.springboot.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.util.Objects;
import java.util.UUID;

@Component
public class UploadUtil {

    @Value("${upload.path}")
    public String basePath;

    public String upload(MultipartFile file){
        try {
            // 校验文件大小
            if (file.getSize() > 8 * 1024 * 1024) {
                return "error.file.size";
            }

            // 将文件保存为字节数组
            byte[] fileBytes = file.getBytes();

            String filePath = basePath + UUID.randomUUID() + Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));


            FileOutputStream outputStream = new FileOutputStream( filePath);
            outputStream.write(fileBytes);
            outputStream.close();
            return filePath;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
