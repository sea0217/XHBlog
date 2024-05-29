package com.xiaohai.controller;

import com.xiaohai.domain.ResponseResult;
import com.xiaohai.service.UploadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Api(tags = "文件上传相关接口")
public class UploadController {
    @Autowired
    private UploadService uploadService;
    @PostMapping("/upload")
    @ApiOperation(value = "文件上传", notes = "上传图片类型的文件")
    public ResponseResult uploadImg(@RequestParam("img") MultipartFile img){
        return uploadService.uploadImg(img);
    }
}
