package com.taotao.service.impl;

import com.taotao.common.utils.FtpUtil;
import com.taotao.common.utils.IDUtils;
import com.taotao.service.PictureService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
@Service
public class PictureServiceImpl implements PictureService {

    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    @Value("${FTP_PORT}")
    private Integer FTP_PORT;
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    @Value("${FTP_PASSWARD}")
    private String FTP_PASSWARD;
    @Value("${FTP_BASE_PATH}")
    private String FTP_BASE_PATH;
    @Value("${IMAGES_BASE_URL}")
    private String IMAGES_BASE_URL;

    @Override
    public Map uploadPicture(MultipartFile uploadFile) {
        Map resultMap = new HashMap<>();
        try {
            String oldName = uploadFile.getOriginalFilename();
            String newName = IDUtils.genImageName();
            newName = newName + oldName.substring(oldName.lastIndexOf("."));
            String imagePath = new DateTime().toString("/yyyy/MM/dd");
            boolean result = FtpUtil.uploadFile(FTP_ADDRESS, FTP_PORT, FTP_USERNAME, FTP_PASSWARD, FTP_BASE_PATH, imagePath , newName, uploadFile.getInputStream());
            if(!result){
                resultMap.put("error",1);
                resultMap.put("message","文件上传失败");
                return resultMap;
            }
            resultMap.put("error",0);
            resultMap.put("url",IMAGES_BASE_URL + imagePath + "/" + newName);
            return resultMap;
        } catch (IOException e) {
            resultMap.put("error",1);
            resultMap.put("message","文件上传发生异常");
            return resultMap;
        }
    }
}
