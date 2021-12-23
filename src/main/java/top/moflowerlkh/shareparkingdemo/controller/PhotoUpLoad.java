package top.moflowerlkh.shareparkingdemo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "照片上传模块")
@RestController
public class PhotoUpLoad {
    @ApiOperation("停车位上传捕获的照片信息")
    @PostMapping("/photo-upload")
    public String photoUpload(@RequestPart MultipartFile multipartFile) {
        return "";
    }
}
