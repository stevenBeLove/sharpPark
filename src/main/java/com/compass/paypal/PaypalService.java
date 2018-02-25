package com.compass.paypal;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Refund;
import com.paypal.api.payments.Sale;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

@Service
public class PaypalService {
	
	@Autowired
    private APIContext apiContext;

	/**
	 * 
	 * @param total
	 * @param currency
	 * @param method 设置为paypal选择PayPal付款方式。
	 * @param intent sale。立即进行快速结帐付款  authorize。暂时搁置资金以便以后付款。 order。表示买方已经同意将来购买。资金被授权并在晚些时候被捕获，而没有搁置资金。
	 * @param description
	 * @param cancelUrl
	 * @param successUrl
	 * @return
	 * @throws PayPalRESTException
	 */
    public Payment createPayment(
            Double total, 
            String currency, 
            PaypalPaymentMethod method, 
            PaypalPaymentIntent intent, 
            String description, 
            String cancelUrl, 
            String successUrl) throws PayPalRESTException{
        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method.toString()); 

        Payment payment = new Payment();
        payment.setIntent(intent.toString());
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        return payment.execute(apiContext, paymentExecute);
    }
    
    public Refund executeRefund(String id,String refundMoney,String currency) throws PayPalRESTException{
    	Refund refund = new Refund();
    	Amount amount = new Amount();
    	amount.setTotal(refundMoney);
    	amount.setCurrency(currency);
    	refund.setAmount(amount);
    	Sale sale = new Sale();
    	sale.setId(id);
    	return sale.refund(apiContext, refund);
    }
}
