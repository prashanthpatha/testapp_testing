package ac.integration.model;

public class Integration {
	private String accessToken;
	private String scope;
	private String userID;
	private String teamName;
	private String teamID;
	private String channel;
	private String channelID;
	private String configurationURL;
	
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getScope() {
		return scope;
	}
	public void setScope(String scope) {
		this.scope = scope;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public String getTeamID() {
		return teamID;
	}
	public void setTeamID(String teamID) {
		this.teamID = teamID;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getChannelID() {
		return channelID;
	}
	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}
	public String getConfigurationURL() {
		return configurationURL;
	}
	public void setConfigurationURL(String configurationURL) {
		this.configurationURL = configurationURL;
	}
	
	public String toString() {
		return "accessToken = " + accessToken +
				"scope = " + scope +
				"userID = " + userID +
				"teamName = " + teamName +
				"teamID = " + teamID +
				"channel = " + channel +
				"channelID = " + channelID +
				"configurationURL = " + configurationURL ;
	}
	
}
