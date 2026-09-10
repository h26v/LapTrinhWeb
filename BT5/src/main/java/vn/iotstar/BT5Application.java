package vn.iotstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Diem khoi dong cua BT5.
 *
 * <p>Ke thua {@link SpringBootServletInitializer} de war co the deploy len Tomcat 11 ben ngoai,
 * dong thoi van chay duoc truc tiep bang {@code mvn spring-boot:run}.
 */
@SpringBootApplication
public class BT5Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BT5Application.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BT5Application.class);
    }
}
