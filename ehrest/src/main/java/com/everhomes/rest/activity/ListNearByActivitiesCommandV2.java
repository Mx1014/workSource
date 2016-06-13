package com.everhomes.rest.activity;


/**
 * 
 * @author Administrator
 * <ul>
 * <li>community_id:小区id</li>
 * <li>anchor：分页</li>
 * </ul>
 *
 */

public class ListNearByActivitiesCommandV2 {
	
		private Long community_id;
		
		private Long anchor;

		public Long getAnchor() {
			return anchor;
		}

		public void setAnchor(Long anchor) {
			this.anchor = anchor;
		}

		public Long getCommunity_id() {
			return community_id;
		}

		public void setCommunity_id(Long community_id) {
			this.community_id = community_id;
		}
		
		

}
