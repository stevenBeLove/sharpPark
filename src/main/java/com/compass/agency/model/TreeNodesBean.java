package com.compass.agency.model;

public class TreeNodesBean {

		private String id;  
	    private String pId;  
	    private String name;  
	    private int isParent;
	    private int open;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getpId() {
			return pId;
		}
		public void setpId(String pId) {
			this.pId = pId;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getOpen() {
			return open;
		}
		public void setOpen(int open) {
			this.open = open;
		}
		public int getIsParent() {
			return isParent;
		}
		public void setIsParent(int isParent) {
			this.isParent = isParent;
		} 
	    
		
	    
}
