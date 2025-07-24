package gift.common.config;

import gift.auth.JwtAuthenticationFilter;
import gift.auth.JwtProvider;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilter(JwtProvider jwtProvider) {
        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new JwtAuthenticationFilter(jwtProvider));
        registrationBean.addUrlPatterns("/api/*"); // 원하는 패턴에 적용
        return registrationBean;
    }
}