package com.jwof.basebusinessOSS;

import com.jwolf.common.constant.ErrorEnum;
import com.jwolf.common.exception.CommonException;
import io.minio.*;
import io.minio.http.Method;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class MinioService {


    @Autowired
    public MinioClient client;

    /**
     * 应用启动时，枚举类对应的bucket不存在则创建
     */
    @PostConstruct
    public void init() {
        Arrays.stream(ObjectBucketEnum.values()).forEach(ObjectBucketEnum -> {
            String bucketEnum = ObjectBucketEnum.getBucketName();
            BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                    .bucket(bucketEnum)
                    .build();
            try {
                if (!client.bucketExists(bucketExistsArgs)) {
                    MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder().bucket(bucketEnum).build();
                    client.makeBucket(makeBucketArgs);
                }

            } catch (Exception e) {
                log.warn("init minio bucket:{} err", bucketEnum, e);
            }


        });
    }

    /**
     * 流上传
     * @param bucketEnum
     * @param objectName
     * @param inputStream
     * @return
     */
    public ObjectWriteResponse putObject(ObjectBucketEnum bucketEnum, String objectName, InputStream inputStream) {
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketEnum.getBucketName())
                    .object(objectName)
                    .contentType(MediaType.ALL_VALUE)
                    .stream(inputStream, inputStream.available(), -1)
                    .build();
            return client.putObject(putObjectArgs);
        } catch (Exception e) {
            throw new CommonException(ErrorEnum.UPLOAD_ERROR, e);
        }

    }

    /**
     * file上传 
     * @param bucketEnum
     * @param file
     * @return
     */
    public ObjectWriteResponse putObject(ObjectBucketEnum bucketEnum, File file) {
        try {
            return client.uploadObject(
                    UploadObjectArgs.builder()
                            .bucket(bucketEnum.getBucketName())
                            .filename(file.getCanonicalPath())
                            .object(file.getName())
                            .build());
        } catch (Exception e) {
            throw new CommonException(ErrorEnum.UPLOAD_ERROR, e);
        }
    }


    /**
     * 删除
     * @param bucketEnum
     * @param objectName
     */
    public void removeObject(ObjectBucketEnum bucketEnum, String objectName) {
        RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                .bucket(bucketEnum.getBucketName())
                .object(objectName)
                .build();
        try {
            client.removeObject(removeObjectArgs);
        } catch (Exception e) {
            throw new CommonException(ErrorEnum.DELETE_ERROR, e);
        }
    }


    /**
     * 获取url
     * @param bucketEnum
     * @param objectName
     * @param expireSecond
     * @return
     */
    public String getObjectUrl(ObjectBucketEnum bucketEnum, String objectName, int expireSecond) {
        GetPresignedObjectUrlArgs urlArgs = GetPresignedObjectUrlArgs.builder()
                .bucket(bucketEnum.getBucketName())
                .expiry(expireSecond, TimeUnit.SECONDS)
                .method(Method.GET)
                .object(objectName).build();
        try {
            return client.getPresignedObjectUrl(urlArgs);
        } catch (Exception e) {
            throw new CommonException(ErrorEnum.REMOTE_CALL_ERROR, e);
        }

    }

    /**
     * 浏览器下载
     * @param bucketEnum
     * @param objectName
     * @param response
     */
    public void downloadObject(ObjectBucketEnum bucketEnum, String objectName, HttpServletResponse response) {
        GetObjectArgs objectArgs = GetObjectArgs.builder()
                .bucket(bucketEnum.getBucketName())
                .object(objectName)
                .build();
        try {
            GetObjectResponse getObjectResponse = client.getObject(objectArgs);
            response.setHeader("Content-Disposition", "attachment;filename=" + "mydownload.jpg");
            response.setContentType("application/force-download");
            response.setCharacterEncoding("UTF-8");
            IOUtils.copy(getObjectResponse, response.getOutputStream());
        } catch (Exception e) {
            throw new CommonException(ErrorEnum.DOWNLOAD_ERROR, e);
        }


    }


}
