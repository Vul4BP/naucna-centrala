package root.demo;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import root.demo.Dto.CasopisDto;
import root.demo.model.Casopis;
import root.demo.repository.CasopisRepository;
import root.demo.services.others.CasopisService;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Objects;

@Configuration
@EnableWebMvc
@SpringBootApplication
public class DemoApplication {

	@Autowired
	CasopisService casopisService;

	@Autowired
	CasopisRepository casopisRepository;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() throws Exception{
		KeyStore clientStore = KeyStore.getInstance("JKS");
		clientStore.load(new FileInputStream("src/main/resources/identity.jks"), "secret".toCharArray());
		KeyStore trustStore = KeyStore.getInstance("JKS");
		trustStore.load(new FileInputStream("src/main/resources/truststore.jks"), "secret".toCharArray());

		SSLContextBuilder sslContextBuilder = new SSLContextBuilder();
		sslContextBuilder.setProtocol("TLS");
		sslContextBuilder.loadKeyMaterial(clientStore, "secret".toCharArray());
		sslContextBuilder.loadTrustMaterial(trustStore,null);

		SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build());
		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(sslConnectionSocketFactory)
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
		requestFactory.setConnectTimeout(10000); // 10 seconds
		requestFactory.setReadTimeout(10000); // 10 seconds

		return new RestTemplate(requestFactory);
	}
}
