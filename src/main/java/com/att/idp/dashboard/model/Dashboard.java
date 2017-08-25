package com.att.idp.dashboard.model;

import java.io.Serializable;

public class Dashboard implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Account account;
	
	private CartDetails cartDetails;
	
	public Dashboard(Account account, CartDetails cartDetails) {
		super();
		this.account = account;
		this.cartDetails = cartDetails;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public CartDetails getCartDetails() {
		return cartDetails;
	}

	public void setCartDetails(CartDetails cartDetails) {
		this.cartDetails = cartDetails;
	}

}
