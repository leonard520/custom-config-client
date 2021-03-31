package com.example.springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import retrofit2.http.Url;

@RestController
public class HelloController {

	/**
	* This is the example that user can fetch the configuration from config server.
	*/
	@Value("${hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds}")
	private int timeout;

	@Value("${spring.cloud.config.uri}")
	private String configServerEndpoint;

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@GetMapping("/config")
	public String getConfig() {
		return String.valueOf(timeout);
	}

	@GetMapping(value = "/getXml", produces = "application/xml")
	public String getXml() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/xml");
		headers.set("Authorization", "Bearer " + AccessTokenManager.getToken());
		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<String> response = restTemplate.exchange(configServerEndpoint + "/gateway/default/master/log.xml",
			HttpMethod.GET, entity, String.class);
		return response.getBody();
	}
}
