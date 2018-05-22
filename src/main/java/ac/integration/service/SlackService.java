package ac.integration.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Properties;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import ac.integration.model.Integration;
import ac.integration.model.Integrations;
import ac.integration.util.Helper;

/**
 * Service specifically for handling slack related calls
 * 
 * @author patpr14
 *
 */

public class SlackService {
	
	public static final Logger logger = LoggerFactory.getLogger(SlackService.class);

	//public static final String SLACK_INCOMING_WEBHOOK = "https://hooks.slack.com/services/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";

	public static final String SLACK_OAUTH_ACCESS_URL = "https://slack.com/api/oauth.access";
	public static final String SLACK_POSTMESSAGE_URL = "https://slack.com/api/chat.postMessage";

	Helper helper;

	public SlackService() {
		helper = new Helper();
	}

	public void postMessageToSlackChannel(String message, Integration integration) {
		logger.info("message : " + message);
		logger.info("Posting to channel : " + integration.getChannel());
		
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(SLACK_POSTMESSAGE_URL);
		
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode node = objectMapper.createObjectNode();
		node.put("token", integration.getAccessToken());
		node.put("channel", integration.getChannelID());
		node.put("text", message);
		
		StringEntity entity = new StringEntity(node.toString(),"UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		try {
			httpClient.execute(httpPost);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void postMessageToSlackChannels(String message) throws ClientProtocolException, IOException {
		LinkedHashSet<Integration> integrations = helper.getIntegrations();
		
		integrations.forEach((integration) -> {
			postMessageToSlackChannel(message, integration);
		});
	}

	public void getSlackResources(String code) throws URISyntaxException, ClientProtocolException, IOException {

		Properties properties = helper.getProperties("slack.properties");

		HttpClient httpClient = HttpClientBuilder.create().build();
		URIBuilder uriBuilder = new URIBuilder(SLACK_OAUTH_ACCESS_URL);
		uriBuilder.setParameter("client_id", properties.getProperty("client_id"))
				.setParameter("client_secret", properties.getProperty("client_secret")).setParameter("code", code);
		// .setParameter("redirect_uri", "");

		HttpGet httpGet = new HttpGet(uriBuilder.build());
		
		logger.info("URI for getting resources : " + uriBuilder.toString());	
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		
		ResponseHandler<String> handler = new BasicResponseHandler();
		String responseBody = handler.handleResponse(httpResponse);
		int responseCode = httpResponse.getStatusLine().getStatusCode();
		
		logger.info("\n*********** Begin : OAuth Access response *************\n");
		logger.info("\nresponse code : " + responseCode + "\n");
		logger.info(responseBody);
		logger.info("\n************* End : OAuth Access response ***********\n");
		
		//helper.extractAndSaveIncomingWebhookURL(responseBody);
		helper.extractAndSaveSlackResources(responseBody);
	}
}
