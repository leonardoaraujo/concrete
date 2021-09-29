/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class WebConfig.
 */
@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {
	
	
	/**
	 * Adds the cors mappings.
	 *
	 * @param registry the registry
	 */
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", 
					"OPTIONS", "HEAD", "TRACE", "CONNECT");
	}
	
	/**
	 * Configure content negotiation.
	 *
	 * @param config the config
	 */
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer config) {
		config.favorPathExtension(false)
			.favorParameter(false)
			.ignoreAcceptHeader(false)
			.useRegisteredExtensionsOnly(false)
			.defaultContentType(MediaType.APPLICATION_JSON)
			.mediaType("json", MediaType.APPLICATION_JSON);
	}

}
