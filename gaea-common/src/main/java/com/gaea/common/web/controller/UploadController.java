package com.gaea.common.web.controller;

import javax.servlet.ServletResponse;

import com.gaea.common.result.AjaxResultStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.gaea.common.aliyun.oss.AliyunOssFileService;
import com.gaea.common.aliyun.oss.FileType;
import com.gaea.common.aliyun.oss.OSSUploadResult;
import com.gaea.common.result.AjaxResult;

/**
 * Created by chengpanwang on 7/7/15.
 */
@Controller
@RequestMapping("/common")
public class UploadController extends BaseController {

    /**
     *
     * @param file
     * @param type
     * @param bucketType
     * yzk-institution, yzk-teacher
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/upload.json", method = RequestMethod.POST)
    public AjaxResult upload(@RequestParam MultipartFile file, String type, @RequestParam("bucketType") String bucketType) {
        AjaxResult ajaxResult = newAjaxResult();
        if (file == null || file.isEmpty()) {
            logger.warn("upload file error, file is empty");
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
            return ajaxResult;
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String suffix = StringUtils.substringAfterLast(fileName, ".");

        FileType fileType = FileType.getFileType(contentType);

        logger.info("upload file, user:" + getXUser().getUserName() + "fileName:" + fileName + ", contentType:" + contentType + ", size:"
                    + file.getSize());

        try {
            OSSUploadResult uploadResult = AliyunOssFileService.uploadFile(file.getInputStream(), suffix, bucketType, fileName, fileType);
            logger.info("upload success:" + JSON.toJSONString(uploadResult));
            String url = AliyunOssFileService.getOSSFileUrl(uploadResult);
            logger.info("file url :" + url);
            ajaxResult.setData(uploadResult);
        } catch (Exception e) {
            logger.error("upload file to ali yun error", e);
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
        }

        return ajaxResult;
    }

    @ResponseBody
    @RequestMapping(value = "/ckeditorUpload.htm")
    public void ckeditorUpload(@RequestParam MultipartFile upload, String type, @RequestParam("bucketType") String bucketType,
                               @RequestParam("CKEditorFuncNum") String CKEditorFuncNum, ServletResponse response) {
        if (upload == null || upload.isEmpty()) {
            logger.warn("upload file error, file is empty");
            return;
        }
        String fileName = upload.getOriginalFilename();
        String contentType = upload.getContentType();
        String suffix = StringUtils.substringAfterLast(fileName, ".");

        FileType fileType = FileType.getFileType(contentType);

        logger.info("upload file, user:" + getXUser().getUserName() + "fileName:" + fileName + ", contentType:" + contentType + ", size:"
                    + upload.getSize());

        try {
            OSSUploadResult uploadResult = AliyunOssFileService.uploadFile(upload.getInputStream(), suffix, bucketType, fileName, fileType);
            logger.info("upload success:" + JSON.toJSONString(uploadResult));
            String url = AliyunOssFileService.getOSSFileUrl(uploadResult);
            logger.info("file url :" + url);
            response.getWriter().print("<script type=\"text/javascript\">" + "window.parent.CKEDITOR.tools.callFunction('" + CKEditorFuncNum + "', '"
                                               + url.replace("/", "\\/") + "', '');" + "</script>");
        } catch (Exception e) {
            logger.error("upload file to ali yun error", e);
        }

    }

    @ResponseBody
    @RequestMapping(value = "/uploadResizeImage.json", method = RequestMethod.POST)
    public AjaxResult resizeImage(@RequestParam MultipartFile file, @RequestParam("bucketType") String bucketType, @RequestParam String x,
                                  @RequestParam String y, @RequestParam String w, @RequestParam String h) {
        AjaxResult ajaxResult = newAjaxResult();
        if (file == null || file.isEmpty()) {
            logger.warn("upload file error, file is empty");
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
            return ajaxResult;
        }
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        String suffix = StringUtils.substringAfterLast(fileName, ".");

        FileType fileType = FileType.getFileType(contentType);

        logger.info("upload file, user:" + getXUser().getUserName() + "fileName:" + fileName + ", contentType:" + contentType + ", size:"
                    + file.getSize());

        try {
            OSSUploadResult uploadResult = AliyunOssFileService.uploadFile(file.getInputStream(), suffix, bucketType, fileName, fileType);
            logger.info("upload success:" + JSON.toJSONString(uploadResult));
            String url = AliyunOssFileService.getOSSFileUrl(uploadResult);
            logger.info("file url :" + url);
            ajaxResult.setData(uploadResult);
        } catch (Exception e) {
            logger.error("upload file to ali yun error", e);
            ajaxResult.setStatus(AjaxResultStatusEnum.ERROR);
        }

        return ajaxResult;
    }
}
