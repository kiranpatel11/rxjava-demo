package com.att.idp.dashboard.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.att.idp.dashboard.JsonUtils;
import com.att.idp.dashboard.client.AccountServiceClient;
import com.att.idp.dashboard.client.CartServiceClient;
import com.att.idp.dashboard.model.Account;
import com.att.idp.dashboard.model.CartDetails;
import com.att.idp.dashboard.model.Dashboard;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@Service
public class DashboardService {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardService.class);
	
	@Autowired
	private CartServiceClient cartServiceClient;

	@Autowired
	private AccountServiceClient accountServiceClient;
	
	public Observable<Dashboard> getCustomerDashboard(String customerId) {
		
		logger.info("###### ##### Begin Composition of upstream services...................");
		
		//RxNetty client fires the request using dedicated threadpool
		//no need to explicitly call subscribeOn for async processing
		Observable<Account> accountObservable = accountServiceClient.getAccountNetty(customerId);
		
		/**
		 * Alternative option for AccountService: Using RestTempate
		Observable<Account> accountObservable = accountServiceClient.getAccountNetty(customerId);
						.subscribeOn(Schedulers.io());  		
		**/
		
		//SubscribeOn different thread from Schedulers.io
		Observable<CartDetails> cartObservable = cartServiceClient.getCart(customerId)
				.subscribeOn(Schedulers.io());
			
		
		
		return Observable.zip(accountObservable, cartObservable, (a, c) -> combineResults(a, c))
				.doOnNext(r -> logger.info("dashboard created successfully..." + JsonUtils.toJson(r)))
				.doOnError(e -> logger.error("An ERROR occurred while creating dashboard.", e));						
	}
	
	private Dashboard combineResults(Account a, CartDetails c) {
		return new Dashboard(a, c);
	}
	
}
