package vn.iotstar.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Thong tin sinh vien thuc hien, nap tu student.properties (tien to {@code student.}).
 * Header dung {@code avatar}, footer dung cac truong con lai.
 */
@ConfigurationProperties(prefix = "student")
public record StudentInfo(
        String fullName,
        String studentId,
        String className,
        String email,
        String avatar,
        String course,
        String instructor) {
}
