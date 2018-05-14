package ac.integration.util;

import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ac.integration.controller.IntegrationController;

/**
 * Helper for parsing the json and getting required data from it
 * 
 * @author patpr14
 *
 */

public class Helper {
	
	public static final Logger logger = LoggerFactory.getLogger(Helper.class);
	
	Properties slackProperties;
	
	public static String getMessageToPostToSlack(String eventJson) throws JsonProcessingException, IOException {
		String messageToPost = "";
		
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode rootNode = objectMapper.readTree(eventJson.getBytes()).path("message");
		
		String detailLink = rootNode.path("detail_link").textValue();
		String action = rootNode.path("action").asText();
		
		String username = rootNode.path("transaction").path("user").path("username").asText();
		
		String projectName = rootNode.path("project").path("name").asText();
		
		String storyName = "";
		String storyFormattedID = "";
		String newScheduledState = "";
		String oldScheduledState = "";
		
		Iterator<JsonNode> state = rootNode.path("state").elements();
		
		while(state.hasNext()) {
			JsonNode jsonNode = state.next();
			
			
			if(jsonNode.path("name").asText().equals("Name")) {
				storyName = jsonNode.path("value").asText();
			}
			
			if(jsonNode.path("name").asText().equals("FormattedID")) {
				storyFormattedID = jsonNode.path("value").asText();
			}
			
			if(jsonNode.path("name").asText().equals("ScheduleState")) {
				newScheduledState = jsonNode.path("value").path("name").asText();
			}			
		}
		
		Iterator<JsonNode> changes = rootNode.path("changes").elements();
		
		while(changes.hasNext()) {
			JsonNode jsonNode = changes.next();
			
			
			if(jsonNode.path("name").asText().equals("ScheduleState")) {
				oldScheduledState = jsonNode.path("old_value").path("name").asText();
			}		
		}
		
		messageToPost = "User Story ( " + storyFormattedID + " ) `" + action + "`" + " by " + username +
					"\n_*Scheduled State*_ changed :  ~" + oldScheduledState + "~  " + newScheduledState +
					"\n\n*Story details* : " +
					"\nName = " + storyName + 
					"\nID = " + storyFormattedID +
					"\nNew Scheduled State = " + newScheduledState +
					"\n\nProject = " + projectName +
					"\n\n Click on below link for more details on this story : \n" + detailLink;
		
		return messageToPost;
	}
	
	public Properties getProperties(String path) {
		slackProperties = new Properties();
		
		try {
			slackProperties.load(getClass().getClassLoader().getResourceAsStream(path));
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		
		return slackProperties;
	}
	
	public String extractAndSaveIncomingWebhookURL(String json) {
		String value = "";
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readTree(json.getBytes());
			String incomingWebhookURL = jsonNode.path("incoming_webhook").path("url").asText();	
			
			logger.info("Got webhook : " + incomingWebhookURL);
			
			String hookValue = slackProperties.getProperty("incoming_webhook");
			if(hookValue != null) {
				hookValue.concat(",").concat(incomingWebhookURL);
			} else {
				hookValue = incomingWebhookURL;
			}
			slackProperties.setProperty("incoming_webhook", hookValue);
			
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		
		return value;
	}
	
	public String[] getIncomingWebhookURLs() {
		String hookValue = slackProperties.getProperty("incoming_webhook");
		return hookValue.split(",");	
	}

}
