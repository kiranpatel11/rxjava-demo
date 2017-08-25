package com.att.idp.dashboard.client;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.att.idp.dashboard.model.Cart;
import com.att.idp.dashboard.model.CartDetails;
import com.att.idp.dashboard.model.CatalogItem;

import io.reactivex.Observable;

@Component
public class CartServiceClient {

	private static final Logger logger = LoggerFactory.getLogger(CartServiceClient.class);
	
	@Value("${api.endpoint.cart-api}")
	private String cartEndpoint;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private CatalogServiceClient catalogServiceClient;
	
	//private RestTemplate restTemplate = new RestTemplate();
	/**
	 * Retrieves Cart and its item details from catalog service. 
	 * 	
	 * @param id
	 * @return
	 */
	public Observable<CartDetails> getCart(String id) {			
		return getLeanCart(id)
				.flatMap(cart -> catalogServiceClient.getCatalogItem(cart.getSkus()),
					(cart, catalogItems) -> combineCartAndCatalogItems(cart, catalogItems)
				);
	}
	
	/**
	 * Returns lean cart as 
	 * {
	 * 		"customerId" : "1234567890",
	 * 		"lastModified" : 1503609025,
	 * 		"skus" : ["23456", "12345" , "35345"]
	 * }
	 * 
	 * 
	 * @return
	 */
	private Observable<Cart> getLeanCart(String id) {
	
		return Observable.<Cart> create(sub -> {
			Cart cart = restTemplate					
					.getForObject(UriComponentsBuilder.fromUriString(cartEndpoint).toUriString(), Cart.class);										
			sub.onNext(cart);
			sub.onComplete();
		})
		.doOnNext(c -> logger.info("cart ids retreived successfully..."))
		.doOnError(e -> logger.error("An ERROR occurred while retrieving the cart ids.", e));						
	}
			
	/**
	 * 
	 * @param cart
	 * @param catalogItems
	 * @return
	 */
	private CartDetails combineCartAndCatalogItems(Cart cart, List<CatalogItem> catalogItems) {
		CartDetails cartDetails = new CartDetails();
		cartDetails.setCustomerId(cart.getCustomerId());
		cartDetails.setLastModified(cart.getLastModified());
		cartDetails.setItems(catalogItems);
		return cartDetails;
	} 

}
