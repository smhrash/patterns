package com.upgrad.patterns.Strategies;

import com.upgrad.patterns.Config.RestServiceGenerator;
import com.upgrad.patterns.Entity.JohnHopkinResponse;
import com.upgrad.patterns.Interfaces.IndianDiseaseStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class JohnHopkinsStrategy implements IndianDiseaseStat {

	private Logger logger = LoggerFactory.getLogger(JohnHopkinsStrategy.class);
	private RestTemplate restTemplate;
	@Value("${config.john-hopkins-url}")
	private String baseUrl;

	public JohnHopkinsStrategy() {
		restTemplate = RestServiceGenerator.GetInstance();
	}

	@Override
	public String GetActiveCount() {
		try {
			JohnHopkinResponse[] responses = getJohnHopkinResponses();
			if (responses != null) {
				Double sum = Arrays.stream(responses)
						.filter(response -> "India".equals(response.getCountry()) && response.getStats() != null)
						.map(response -> response.getStats().getConfirmed())
						.reduce(0.0, Double::sum);
				return String.valueOf(Math.round(sum));
			} else {
				logger.error("Received null response from John Hopkins API");
				// Return a default or error value
				return "Data not available";
			}
		} catch (Exception e) {
			logger.error("Error fetching John Hopkins data", e);
			// Return a default or error value
			return "Error fetching data";
		}
	}

	private JohnHopkinResponse[] getJohnHopkinResponses() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");

		HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

		try {
			return restTemplate.exchange(
					baseUrl, HttpMethod.GET, entity,
					JohnHopkinResponse[].class).getBody();
		} catch (Exception e) {
			logger.error("API call to JohnHopkins failed", e);
			return null;
		}
	}
}
