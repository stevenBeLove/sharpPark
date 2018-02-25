package com.compass.paypal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
public class PaypalConfig {
	
	private String CLIENT_ID = "Ab7pri_GacGwZi3s6AqAx0xaWgsV0TCYe05VxqwyXpP1v9u7DMiRJv9bQrwtRVLnbKfKw-d4KzM2NCy_";
	private String CLIENT_SECRET = "EB-n9sQoM0Ljbf7vF7GnAjQdn6XtBAlQMjnyZekyUY8G9KUjeW8gPa0Bbp7hijgV3paSgIs1R1h6Jszr";
	private String MODE = "sandbox";
	
    @Bean
    public APIContext apiContext() throws PayPalRESTException{
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return apiContext;
    }
}
