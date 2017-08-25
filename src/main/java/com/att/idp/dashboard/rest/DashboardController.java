package com.att.idp.dashboard.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.att.idp.dashboard.model.Dashboard;
import com.att.idp.dashboard.service.DashboardService;

@RestController
public class DashboardController {
	
	private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	private DashboardService dashboardService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/dashboard/{customerId}", produces = "application/json")
	public DeferredResult<Dashboard> getDashboard(@PathVariable String customerId) {
		
		//DeferredResult with timeout
		//DeferredResult will be populated later by async threads
		final DeferredResult<Dashboard> deferedResult = new DeferredResult<Dashboard>((long) 30000);

		dashboardService.getCustomerDashboard(customerId)
			.subscribe(result -> deferedResult.setResult(result), error -> deferedResult.setErrorResult(error));
		
		logger.info("Returning DeferredResult " + deferedResult);		
		return deferedResult; 		
	}
}
