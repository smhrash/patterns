package com.upgrad.patterns.Strategies;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.upgrad.patterns.Config.RestServiceGenerator;
import com.upgrad.patterns.Entity.JohnHopkinResponse;
import com.upgrad.patterns.Interfaces.IndianDiseaseStat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

	private JohnHopkinResponse[] getJohnHopkinResponses() throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		ClassPathResource resource = new ClassPathResource("JohnHopkins.json");
		return new JohnHopkinResponse[]{objectMapper.readValue(resource.getFile(), JohnHopkinResponse.class)};
	}
}
