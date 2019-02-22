package ro.msg.learning.shop.odata.core;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfiguration {
    @Bean
    public ServletRegistrationBean oDataServlet(ShopODataServlet servlet) {
        return new ServletRegistrationBean(servlet, "/odata/*");
    }
}
