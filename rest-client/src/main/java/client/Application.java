package client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import client.dto.AccessToken;
import client.sts.TokenFetcher;

@ComponentScan
@EnableAutoConfiguration
public class Application implements CommandLineRunner {
	private static final Logger logger = Logger.getLogger(Application.class);

	@Autowired
	private TokenFetcher tokenFetcher;

	@Autowired
	private RestTemplate restTemplate;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	public void run(String... args) throws Exception {
		

		System.setProperty("javax.net.ssl.trustStore", "/home/eva/ffproject/medcom-oio-idws-rest-java/rest-client/src/main/resources/trust.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "Test1234");
		
		// get the access token
		AccessToken accessToken = tokenFetcher.getAccessToken("urn:medcom:videoapi");

		// setup request Authorization header
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Holder-of-key " + accessToken.getToken());

		// call service
		ResponseEntity<String> restServicResponse = restTemplate.exchange("https://videoapi.test-vdxapi.vconf.dk/videoapi/meetings?from-start-time=2018-01-01T03:12:30 -0100&to-start-time=2023-12-31T03:12:30 -0100", HttpMethod.GET, new HttpEntity<>("", headers), String.class);

		// should print out "Hello John"
//		logger.info(restServicResponse.getBody());
	}
}
