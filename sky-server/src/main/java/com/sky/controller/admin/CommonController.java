package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * @Author：tantantan
 * @Package：com.sky.controller.admin
 * @Project：sky-take-out
 * @name：CommonController
 * @Date：2024/5/24 19:36
 * @Filename：CommonController
 */
@RestController
@Slf4j
@RequestMapping("/admin/common")
@Api(tags = "通用接口")
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * 文件上传控制
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ApiOperation("文件上传")
    public Result<String> uploadFile(MultipartFile file) throws IOException {
        log.info("文件上传操作");
        //1、获取原始文件名
        String originalFilename = file.getOriginalFilename();
            //获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        //2、通过UUID生成随机文件名
        String fileName = UUID.randomUUID().toString().replace("-", "") + fileType;
        //3、通过AliOss工具将文件上传到阿里云服务器
        String upload = aliOssUtil.upload(file.getBytes(), fileName);
        return Result.success(upload);
    }
}
