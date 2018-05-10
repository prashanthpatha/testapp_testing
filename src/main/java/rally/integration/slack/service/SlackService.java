package rally.integration.slack.service;

import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class SlackService {
	
	public static final String SLACK_INCOMING_WEBHOOK = "https://hooks.slack.com/services/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";	
	
	public void postMessageToSlack(String message) throws ClientProtocolException, IOException {
		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(SLACK_INCOMING_WEBHOOK);
		StringEntity entity = new StringEntity("{\"text\" : \""+ message + "\"}");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpClient.execute(httpPost);
	}
}
