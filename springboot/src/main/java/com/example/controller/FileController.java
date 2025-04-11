package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import com.example.exception.CustomException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*文件相关*/


@RestController
@RequestMapping("/files")
public class FileController {
    //getProperty获取到当前项目根路径
    //文件上传的目录的路径
    private static final String filePath = System.getProperty("user.dir") + "/files/";
    @PostMapping("/upload")
    public Result upload(MultipartFile file){//文件流的形式接收前端发过来的文件
        String originalFilename = file.getOriginalFilename();
        if(!FileUtil.isDirectory(filePath)){
            FileUtil.mkdir(filePath);//创建目录
        }
        //提供文件存储的完整路径
        //给文件名加一个唯一的标识
        String fileName = System.currentTimeMillis() + "_" + originalFilename;
        String realPath = filePath + fileName;//完整的文件路径
        try {
            FileUtil.writeBytes(file.getBytes(),realPath);//将文件流写入到指定路径
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件上传失败");
        }
        String url = "http://localhost:9090/files/download/" + fileName;
        //返回一个网络连接
        return Result.success(url);
    }

    /*文件下载*/
    @GetMapping("/download/{fileName}")
    public void download(@PathVariable String fileName, HttpServletResponse response){
        try {
            response.addHeader("Content-Disposition","attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.setContentType("application/octet-stream");
            OutputStream os =  response.getOutputStream();
            String realPath = filePath + fileName;//完整的文件路径
            //获取到文件的字节数组
            byte[] bytes =  FileUtil.readBytes(realPath);
            os.write(bytes);
            os.flush();//清空缓存
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new CustomException("500","文件下载失败");
        }

    }
}
