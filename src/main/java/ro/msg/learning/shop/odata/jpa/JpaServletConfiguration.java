package ro.msg.learning.shop.odata.jpa;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JpaServletConfiguration {
    @Bean
    public ServletRegistrationBean jpaServlet(JpaODataServlet servlet) {
        return new ServletRegistrationBean(servlet, "/jpa/odata/*");
    }
}
