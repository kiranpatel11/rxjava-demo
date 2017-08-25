package com.att.idp.dashboard.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * Wrapper Object for Cart and Product details
 * 
 * @author kp7466
 *
 */
public class Cart implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String customerId;

	private Date lastModified;	
	
	private List<String> skus;

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public List<String> getSkus() {
		return skus;
	}

	public void setSkus(List<String> skus) {
		this.skus = skus;
	}

}
