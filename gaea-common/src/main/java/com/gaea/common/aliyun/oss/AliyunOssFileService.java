package com.gaea.common.aliyun.oss;

import java.io.InputStream;
import java.util.*;

import com.gaea.common.exception.ParamException;
import com.gaea.common.exception.SystemException;
import com.gaea.common.util.FastJson;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;

/**
 * Created by Fe on 15/7/3.
 */
public class AliyunOssFileService {

    public static final Logger logger                       = LoggerFactory.getLogger(AliyunOssFileService.class);

    public static final String ALIYUN_OSS_FILE_HOST         = "aliyuncs.com";

    //TODO 不同的fileType存储的oss Bucket不一样
    public static final String FILE_TYPE_INSTITUTION        = "yzk-institution";

    public static final String FILE_TYPE_TEACHER            = "yzk-teacher";

    public static final String FILE_TYPE_COURSE             = "yzk-course";

    public static final String FILE_TYPE_ARTICLE            = "yzk-article";

    public static final String FILE_TYPE_INTEGRAL_COMMODITY = "yzk-integral-commodity";

    public static final String FILE_TYPE_COURSE_COMMENT     = "yzk-course-comment";

    public static String       uploadAliyunHost;

    public static Set<String>  fileTypeSet                  = new HashSet<String>();

    static {
        fileTypeSet.add(FILE_TYPE_INSTITUTION);
        fileTypeSet.add(FILE_TYPE_TEACHER);
        fileTypeSet.add(FILE_TYPE_COURSE);
        fileTypeSet.add(FILE_TYPE_ARTICLE);
        fileTypeSet.add(FILE_TYPE_INTEGRAL_COMMODITY);
        fileTypeSet.add(FILE_TYPE_COURSE_COMMENT);

    }

    private static String      accessKeyId;
    private static String      accessKeySecret;

    /**
     *
     * @param inputStream 文件字节流
     * @param suffix 文件后缀
     * @param bucketName oss桶名称
     * @param fileName 文件名
     * @param fileType 文件类型
     * @return
     */
    public static OSSUploadResult uploadFile(InputStream inputStream, String suffix, String bucketName, String fileName, FileType fileType) {
        logger.info("createImageFile param , suffix : {}, bucketName : {}", suffix, bucketName);
        if (inputStream == null || StringUtils.isEmpty(suffix) || StringUtils.isEmpty(bucketName)) {
            throw new ParamException("param error !");
        }

        if (!fileTypeSet.contains(bucketName)) {
            throw new ParamException("type not exists!");
        }
        try {
            String host = "http://" + uploadAliyunHost;
            OSSClient ossClient = new OSSClient(host, accessKeyId, accessKeySecret);
            ObjectMetadata objectMeta = new ObjectMetadata();
            objectMeta.setContentLength(Long.parseLong(inputStream.available() + ""));

            if (fileType.equals(FileType.ATTACHMENT)) {
                objectMeta.setContentType("application/octet-stream");
            }

            if (fileType.equals(FileType.IMAGE)) {
                objectMeta.setContentType("image/" + suffix);
            }

            String ossFileName = UUID.randomUUID().toString() + "." + suffix;
            String ossLocation = ossClient.getBucketLocation(bucketName);

            logger.info("ossClient.putObject param , bucketName: {},ossFileName:{}", bucketName, ossFileName);
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, ossFileName, inputStream, objectMeta);
            logger.info("ossClient.putObject result : {}", FastJson.toJson(putObjectResult));

            OSSUploadResult ossUploadResult = new OSSUploadResult();
            ossUploadResult.setOssfileName(ossFileName);
            ossUploadResult.setFileName(fileName);
            ossUploadResult.setOssLocation(ossLocation);
            ossUploadResult.setOssBucKet(bucketName);

            ossUploadResult.setUrl(getOSSFileUrl(ossUploadResult));

            return ossUploadResult;
        } catch (Exception e) {
            logger.error("createImageFile error  e : {}", e);
            throw new SystemException("createImageFile error");
        }
    }

    public static String getOSSFileUrl(OSSUploadResult ossUploadResult) {
        if (ossUploadResult == null
            || StringUtils.isAnyBlank(ossUploadResult.getOssBucKet(), ossUploadResult.getFileName(), ossUploadResult.getOssLocation())) {
            return StringUtils.EMPTY;
        }
        return "http://" + ossUploadResult.getOssBucKet() + "." + ossUploadResult.getOssLocation() + "." + ALIYUN_OSS_FILE_HOST + "/"
               + ossUploadResult.getOssfileName();

    }

    public static String getOSSFileUrl(String ossUploadResult) {
        try {
            if (StringUtils.isBlank(ossUploadResult)) {
                return StringUtils.EMPTY;
            }

            OSSUploadResult result = JSON.parseObject(ossUploadResult, OSSUploadResult.class);
            return getOSSFileUrl(result);
        } catch (Exception e) {
            logger.error("", e);
        }

        return StringUtils.EMPTY;

    }

    public static List<String> getBatchOSSFileUrl(List<OSSUploadResult> ossUploadResults) {
        List<String> urlList = null;
        if (ossUploadResults != null && ossUploadResults.size() > 0) {
            urlList = new ArrayList<String>();
            for (OSSUploadResult ossUploadResult : ossUploadResults) {
                String url = getOSSFileUrl(ossUploadResult);
                urlList.add(url);
            }
        }
        return urlList;
    }

    public static InputStream downLoadOssFile(OSSUploadResult ossUploadResult) {
        OSSClient ossClient = new OSSClient(accessKeyId, accessKeySecret);
        OSSObject ossObject = ossClient.getObject(ossUploadResult.getOssBucKet(), ossUploadResult.getOssfileName());

        if (ossObject != null) return ossObject.getObjectContent();
        else return null;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public void setUploadAliyunHost(String uploadAliyunHost) {
        this.uploadAliyunHost = uploadAliyunHost;
    }
}
