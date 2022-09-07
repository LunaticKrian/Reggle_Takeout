package com.krian.reggle.controller;

import com.krian.reggle.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggle.save-upload-file-path}")
    private String basePath;

    /**
     * @param file
     * @return
     * @Func 文件上传
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {  // MultipartFile是一个临时文件，需要转存到指定位置，否则本次请求完成之后临时文件将会被删除
        log.info(file.toString());

        // 获取原始文件名：
        String originalFilename = file.getOriginalFilename();

        // 获取文件后缀名；
        assert originalFilename != null;
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 使用UUID重新生成文件名，防止文件名重复造成文件覆盖：
        String fileName = UUID.randomUUID().toString() + suffix;

        // 创建文件目录：
        File dir = new File(basePath);
        if (!dir.exists()){  // basePath不存在，需要创建
            dir.mkdir();
        }

        // 转存临时文件到指定位置：
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {
            // 输入流，通过输入流读取文件内容：
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            // 输出流，通过输出流将文件写回浏览器，在浏览器展示图片：
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            // 循环写出数据到前端：
            while ((len = fileInputStream.read(bytes)) != -1){
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }

            // 关闭传输流：
            outputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
