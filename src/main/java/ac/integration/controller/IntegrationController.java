package ac.integration.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import ac.integration.service.SlackService;
import ac.integration.util.Helper;

/**
 * This controller can listen to events in AC , handle redirect requests from other 3rd party services etc
 *  
 * @author patpr14
 *
 */

@RestController
@RequestMapping("/api")
public class IntegrationController {
	
	public static final Logger logger = LoggerFactory.getLogger(IntegrationController.class);
	
	SlackService slackService;
	
	public IntegrationController() {
		slackService = new SlackService();
	}
	
	@RequestMapping(value = "/changeEvent", method = RequestMethod.POST) 
	public void itemChanged(@RequestBody String eventJson, UriComponentsBuilder ucBuilder) throws ClientProtocolException, IOException {
		logger.info("\nBegin : *********** Change item in AC *********\n");
		logger.info(eventJson);
		logger.info("\nEnd : *********** Change item in AC *********\n");
		
		String messageToPost = Helper.getMessageToPostToSlack(eventJson);
		
		slackService.postMessageToSlackChannels(messageToPost);		
	}
	
	@RequestMapping(value = "/oAuthSuccess")
	public void oAuthSuccess(@RequestParam String code) throws ClientProtocolException, URISyntaxException, IOException {
		logger.info("code received : <<" + code + ">>");
		slackService.getSlackResources(code);
	}
	
	/* method to test the apiChangeEvent * 
	 * public static void main(String[] args) throws ClientProtocolException, IOException {
		
		FileInputStream fis = new FileInputStream(new IntegrationController().getClass().getClassLoader().getResource("event.json").getFile());
		
		String eventJson = new Scanner(fis).useDelimiter("\\A").next();
		
		new IntegrationController().itemChanged(eventJson, null);
	}*/
	
	
}
