package com.everhomes.richtext;

public interface RichTextProvider {
	
	RichText findRichText(String ownerType, Long ownerId, String resourceType, Integer namespaceId);
	RichText getDefaultRichText(String resourceType);
	
	void createRichText(RichText richText);
	void updateRichText(RichText richText);

}
