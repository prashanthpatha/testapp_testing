package rally.integration.slack.util;

import java.io.IOException;
import java.util.Iterator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Parser {
	
	public static String getMessageToPost(String eventJson) throws JsonProcessingException, IOException {
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

}
