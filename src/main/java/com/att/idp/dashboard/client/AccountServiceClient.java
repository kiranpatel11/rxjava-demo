package com.att.idp.dashboard.client;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.idp.dashboard.JsonUtils;
import com.att.idp.dashboard.model.Account;

import io.netty.buffer.ByteBuf;
import io.reactivex.Observable;
import io.reactivex.netty.RxNetty;

@Component
public class AccountServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(AccountServiceClient.class);
	
	@Value("${api.endpoint.account-api}")
	private String accountEndpoint;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Observable<Account> getAccount(String id) {
	
		return Observable.<Account> create(sub -> {
			Account account = restTemplate					
					.getForObject(UriComponentsBuilder.fromUriString(accountEndpoint).toUriString(), Account.class);										
			sub.onNext(account);
			sub.onComplete();
		})
		.doOnNext(c -> logger.info("Account retreived successfully..."))
		.doOnError(e -> logger.error("An ERROR occurred while retrieving the Account.", e));						
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	public Observable<Account> getAccountNetty(String id) {
		
		return Observable.just(
				RxNetty.createHttpGet(UriComponentsBuilder.fromUriString(accountEndpoint).toUriString())
				.doOnNext(c -> logger.info("Account retreived successfully using Netty..."))
				.doOnError(e -> logger.error("An ERROR occurred while retrieving the Account using Netty...", e))						
				.flatMap(response -> response.getContent())
				.map((data) -> toAccount(data))
				.toBlocking().single());
	}
	
	private Account toAccount(ByteBuf data) {
		//avoiding a null return
		Account account = new Account();	
	
		String str = data.toString(Charset.defaultCharset());
		//logger.info("Raw response...." + str);
		try {
			account = JsonUtils.toPojo(str, Account.class);
		} catch (Exception e) {								
			logger.error("Error Parsing content , "  + e.toString());
		}
		return account;				
	}
	
}
