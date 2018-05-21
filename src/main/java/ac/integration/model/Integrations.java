package ac.integration.model;

import java.util.LinkedHashSet;

public class Integrations {
	
	LinkedHashSet<Integration> integrations = new LinkedHashSet<>();
	
	public void add(Integration integraiton) {
		integrations.add(integraiton);
	}
	
	public LinkedHashSet<Integration> getIntegrations() {
		return integrations;
	}
	
	
}
