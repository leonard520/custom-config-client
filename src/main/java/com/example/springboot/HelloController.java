package com.example.springboot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import net.minidev.json.JSONObject;
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
	* This is the example that user can read the properties from Config Server.
	*/
	@Value("${hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds}")
	private int timeout;

	@Value("${spring.cloud.config.uri}")
	private String configServerEndpoint;

	@Value("${spring.application.name}")
	private String appName;

	private String defaultLabel = "master";
	private String defaultProfile = "default";

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

	/**
	 * This is the example code that how to fetch the xml from Config Server.
	 * Please prepare the log.xml in advance.
	 */
	@GetMapping(value = "/getXml", produces = "application/xml")
	public String getXml() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/xml");
		headers.set("Authorization", "Bearer " + AccessTokenManager.getToken());
		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<String> response = restTemplate.exchange(String.format("%s/%s/%s/%s/%s",
			configServerEndpoint,
			appName,
			defaultProfile,
			defaultLabel,
			"log.xml"),
			HttpMethod.GET, entity, String.class);

		return response.hasBody() ? response.getBody() : "The response doesn't have a body";
	}

	/**
	 * This is the example code that how to fetch the yml from Config Server.
	 */
	@GetMapping("/getYml")
	public String getYaml() throws IOException {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Accept", "application/json");
		headers.set("Authorization", "Bearer " + AccessTokenManager.getToken());
		HttpEntity entity = new HttpEntity(headers);

		HttpEntity<String> response = restTemplate.exchange(String.format("%s/%s/%s.%s",
			configServerEndpoint,
			appName,
			defaultProfile,
			"yml"),
			HttpMethod.GET, entity, String.class);

		return response.hasBody() ? response.getBody() : "The response doesn't have a body";
	}
}
