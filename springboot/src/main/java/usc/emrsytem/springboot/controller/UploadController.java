package usc.emrsytem.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import usc.emrsytem.springboot.common.Result;
import usc.emrsytem.springboot.utils.UploadUtil;

import java.util.Map;

@RestController
public class UploadController {

    @Autowired
    private UploadUtil uploadUtil;

    @PostMapping("/upload")
    public Result uploadFile(@RequestBody MultipartFile file){
        String filePath = uploadUtil.upload(file);

        Map<String, String> map = Map.of("filePath", filePath);
        return Result.success(map);
    }

}

