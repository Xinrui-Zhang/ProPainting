package seu.se.practice2.propainting.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import seu.se.practice2.propainting.config.interceptor.TokenInterceptorAdapter;

@Configuration
@EnableWebMvc
public class WebConfigurer implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptorAdapter tokenInterceptorAdapter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptorAdapter).addPathPatterns("/**");
    }
}
