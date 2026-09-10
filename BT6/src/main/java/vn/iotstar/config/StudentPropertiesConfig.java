package vn.iotstar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Nap student.properties bang UTF-8.
 *
 * <p>application.properties duoc Spring Boot doc theo ISO-8859-1 nen tieng Viet co dau
 * se bi loi font; tach rieng file va chi dinh encoding de sinh vien sua ten truc tiep.
 */
@Configuration
@PropertySource(value = "classpath:student.properties", encoding = "UTF-8")
public class StudentPropertiesConfig {
}
