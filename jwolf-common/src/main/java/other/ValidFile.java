/*
package other;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;

*/
/**
 * 默认:<br/>
 * 上传文件不能为空<br/>
 * 允许上传所有后缀格式的文件<br/>
 * 文件后缀名不区分大小写<br/>
 * 文件最大不超过springmvc配置的文件大小<br/>
 * 文件最小不小于0 kb <br/>
 * Created by bruce on 2018/8/30 16:34
 *
 * @author jwolf
 * <p>
 * AliasFor("endsWith")
 * <p>
 * 支持的文件后缀类型,默认全部,AliasFor("value")
 * <p>
 * 文件后缀是否区分大小写
 * <p>
 * 上传的文件是否允许为空
 * <p>
 * Max file size. Values can use the suffixes "MB" or "KB" to indicate megabytes or
 * kilobytes respectively.<br/>
 * 默认不限制但必须小于等于SpringMVC中文件上传配置
 * <p>
 * Min file size. Values can use the suffixes "MB" or "KB" to indicate megabytes or
 * kilobytes respectively. default byte
 *//*

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidFile.Validator.class)
public @interface ValidFile {

    String DEFAULT_MAXSIZE = "-1";

    */
/**
 * AliasFor("endsWith")
 *//*

    String[] value() default {};

    */
/**
 * 支持的文件后缀类型,默认全部,AliasFor("value")
 *//*

    String[] endsWith() default {};

    */
/**
 * 文件后缀是否区分大小写
 *//*

    boolean ignoreCase() default true;

    */
/**
 * 上传的文件是否允许为空
 *//*

    boolean allowEmpty() default false;

    */
/**
 * Max file size. Values can use the suffixes "MB" or "KB" to indicate megabytes or
 * kilobytes respectively.<br/>
 * 默认不限制但必须小于等于SpringMVC中文件上传配置
 *//*

    String maxSize() default DEFAULT_MAXSIZE;

    */
/**
 * Min file size. Values can use the suffixes "MB" or "KB" to indicate megabytes or
 * kilobytes respectively. default byte
 *//*

    String minSize() default "0";

    String message() default "The uploaded file is not verified.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class Validator implements ConstraintValidator<ValidFile, MultipartFile> {

        @Autowired
        private MultipartProperties multipartProperties;

        private String maxSize = "10MB";
        private String minSize = "0MB";

        private ValidFile fileValid;
        private ArrayList<String> extensions = new ArrayList<>();

        @Override
        public void initialize(ValidFile constraintAnnotation) {
            this.fileValid = constraintAnnotation;
            //支持的文件扩展名集合
            Collections.addAll(extensions, fileValid.value());
            Collections.addAll(extensions, fileValid.endsWith());
            //文件上传的最大值
            if (constraintAnnotation.maxSize().equals(ValidFile.DEFAULT_MAXSIZE)) {
                //默认最大值采用Spring中配置的单文件大小
                this.maxSize =String.valueOf(multipartProperties.getMaxFileSize());
            } else {
                this.maxSize = constraintAnnotation.maxSize();
            }
            //文件上传的最小值
            this.minSize =constraintAnnotation.minSize();
        }

        private void validMessage(String message, ConstraintValidatorContext cvc) {
            cvc.disableDefaultConstraintViolation();
            cvc.buildConstraintViolationWithTemplate(message)
                    .addConstraintViolation();
        }

        private long parseSize(String size) {
            Assert.hasLength(size, "Size must not be empty");
            size = size.toUpperCase();
            if (size.endsWith("KB")) {
                return Long.valueOf(size.substring(0, size.length() - 2)) * 1024;
            }
            if (size.endsWith("MB")) {
                return Long.valueOf(size.substring(0, size.length() - 2)) * 1024 * 1024;
            }
             if (size.endsWith("B")) {
                return Long.valueOf(size.substring(0, size.length() - 1));
            }
            return Long.valueOf(size);
        }

        @Override
        public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext cvc) {
            //上传的文件是空的情况
            if (multipartFile == null || multipartFile.isEmpty()) {
                if (fileValid.allowEmpty()) {
                    return true;
                }
                validMessage("上传文件不能为空", cvc);
                return false;
            }


            //上传的文件不是空的情况,验证其他条件是否成立
            //获取文件名,如果上传文件后缀名不区分大小写则统一转成小写
            String originalFilename = multipartFile.getOriginalFilename();
            if (fileValid.ignoreCase()) {
                originalFilename = originalFilename.toLowerCase();
            }
            //TODO 可以添加读取文件头信息
            if (extensions.size() > 0 && extensions.stream().noneMatch(originalFilename::endsWith)) {
                validMessage("只支持文件后缀:"+ StringUtil.toPrettyString(extensions), cvc);
                return false;
            }
            //上传文件字节数
            long size = multipartFile.getSize();
            if (size <  parseSize(this.minSize)) {
                validMessage("文件大小不能小于"+this.minSize, cvc);
                return false;
            }
            if (size > parseSize(this.maxSize)) {
                validMessage("文件大小不能大于"+this.maxSize, cvc);
                return false;
            }
            return true;
        }
    }

}*/
