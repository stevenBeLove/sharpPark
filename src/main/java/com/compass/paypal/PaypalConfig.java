package com.compass.paypal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {
	
	private String CLIENT_ID = "*******";
	private String CLIENT_SECRET = "*******";
	private String MODE = "sandbox";
	
    @Bean
    public APIContext apiContext() throws PayPalRESTException{
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return apiContext;
    }
}
