package com.att.idp.dashboard;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.att.idp.dashboard.client.AccountServiceClient;
import com.att.idp.dashboard.client.CartServiceClient;
import com.att.idp.dashboard.client.CatalogServiceClient;
import com.att.idp.dashboard.service.DashboardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

@RunWith(SpringRunner.class)
//@SpringBootTest
@ContextConfiguration(classes = {DashboardService.class, CartServiceClient.class, AccountServiceClient.class, CatalogServiceClient.class, RestTemplate.class})
public class DashboardApplicationTests {

	private static final Logger logger = LoggerFactory.getLogger(DashboardApplicationTests.class);

	/*@Test
	public void contextLoads() {
	}*/
	@Autowired
	private DashboardService service;
	
	@Test
	public void testFlatMap() throws InterruptedException {
		
		Observable<Integer> o = Observable.just(1,2,3);
		
		o.flatMap(i -> Observable.just(i*i))
		.subscribe(item -> logger.info("item" + item));
		
		logger.info("Waiting for 3 secs");
		Thread.sleep(3000);
	}
	
	@Test
	public void testFlatMap2() throws InterruptedException {
		
		Observable<Integer> o = Observable.just(1,2,3);
		
		o.flatMap(i -> Observable.just(i*i))
		.subscribeOn(Schedulers.computation())
		.subscribe(item -> logger.info("item" + item));
		
		logger.info("Waiting for 3 secs");
		Thread.sleep(3000);
	}


	@Test
	public void testGetDashboard() throws InterruptedException {

		service.getCustomerDashboard("dummy")
				.subscribe(item -> logger.info(JsonUtils.toJson(item)));
 
		logger.info("Waiting for 3 secs");
		Thread.sleep(3000);
	}

}
