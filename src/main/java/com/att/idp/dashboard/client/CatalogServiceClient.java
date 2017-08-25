package com.att.idp.dashboard.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.att.idp.dashboard.model.CatalogItem;

import io.reactivex.Observable;

@Component
public class CatalogServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(CatalogServiceClient.class);
	
	@Value("${api.endpoint.catalog-api}")
	private String catalogEndpoint;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * Returns catalogItems as array
	 *
	 * @param skus
	 * @return
	 */
	Observable<List<CatalogItem>> getCatalogItem(List<String> skus) {
		return Observable.<List<CatalogItem>> create(sub -> {

			ResponseEntity<List<CatalogItem>> catalogItemsResponse = restTemplate
					.exchange(catalogEndpoint, HttpMethod.GET, null, new ParameterizedTypeReference<List<CatalogItem>>(){});
			sub.onNext(catalogItemsResponse.getBody());
			sub.onComplete();
		})
		.doOnNext(c -> logger.info("catalogitems retreived successfully..."))
		.doOnError(e -> logger.error("An ERROR occurred while retrieving the catalog items.", e));		
	}	

}
