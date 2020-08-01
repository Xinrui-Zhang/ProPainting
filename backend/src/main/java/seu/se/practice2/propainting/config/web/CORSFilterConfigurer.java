package seu.se.practice2.propainting.config.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * author: leimei date: 2020/7/22 1:01
 */
//@Configuration
public class CORSFilterConfigurer {

	private CorsConfiguration corsConfiguration() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("content-type,x-requested-with,Authorization,x-ui-request,lang,x-api-token");
		corsConfiguration.addAllowedMethod("GET, HEAD, PUT, POST, DELETE, PATCH");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(3600L);
		return corsConfiguration;
	}

//	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration());
		return new CorsFilter(source);
	}
}
