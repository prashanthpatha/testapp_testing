package rally.integration.slack.controller;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import rally.integration.slack.service.SlackService;
import rally.integration.slack.util.Parser;

@RestController
@RequestMapping("/api")
public class IntegrationController {
	
	public static final Logger logger = LoggerFactory.getLogger(IntegrationController.class);
	
	@RequestMapping(value = "/changeEvent", method = RequestMethod.POST) 
	public void itemChanged(@RequestBody String eventJson, UriComponentsBuilder ucBuilder) throws ClientProtocolException, IOException {
		logger.info("\nBegin : *********** Change item in AC *********\n");
		logger.info(eventJson);
		logger.info("\nEnd : *********** Change item in AC *********\n");
		
		String messageToPost = Parser.getMessageToPost(eventJson);
		
		SlackService slackService = new SlackService();
		slackService.postMessageToSlack(messageToPost);		
	}
	
	/*public static void main(String[] args) throws ClientProtocolException, IOException {
		
		FileInputStream fis = new FileInputStream(new IntegrationController().getClass().getClassLoader().getResource("event.json").getFile());
		
		String eventJson = new Scanner(fis).useDelimiter("\\A").next();
		
		new IntegrationController().itemChanged(eventJson, null);
	}*/
}
