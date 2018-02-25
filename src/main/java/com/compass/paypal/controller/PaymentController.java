package com.compass.paypal.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.compass.paypal.PaypalPaymentIntent;
import com.compass.paypal.PaypalPaymentMethod;
import com.compass.paypal.PaypalService;
import com.compass.paypal.URLUtils;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.Refund;
import com.paypal.base.rest.PayPalRESTException;

@Controller
public class PaymentController {
	
	public static final String PAYPAL_SUCCESS_URL = "/pay/success.do";
    public static final String PAYPAL_CANCEL_URL = "/pay/cancel.do";
    
    private Logger  log = LoggerFactory.getLogger(PaymentController.class);
    
    @Autowired
    private PaypalService paypalService;

    @RequestMapping(value="/pay/index.do",method = RequestMethod.GET)
    public String index(){
        return "paypal/index";
    }

    @RequestMapping(method = RequestMethod.POST, value = "pay/topay.do")
    public String pay(HttpServletRequest request){
        String cancelUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_CANCEL_URL;
        String successUrl = URLUtils.getBaseURl(request) + "/" + PAYPAL_SUCCESS_URL;
        try {
        	String currency = request.getParameter("currency");
        	String amount = request.getParameter("amount");
        	BigDecimal totalAmount = BigDecimal.ZERO;
        	if(StringUtils.isNotBlank(amount)){
        		totalAmount = new BigDecimal(amount);
        	}
            Payment payment = paypalService.createPayment(
            		totalAmount.doubleValue(), 
            		currency, 
                    PaypalPaymentMethod.paypal, 
                    PaypalPaymentIntent.sale,
                    "payment description", 
                    cancelUrl, 
                    successUrl);
            for(Links links : payment.getLinks()){
                if(links.getRel().equals("approval_url")){
                    return "redirect:" + links.getHref();
                }
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_CANCEL_URL)
    public String cancelPay(){
        return "paypal/index";
    }

    @RequestMapping(method = RequestMethod.GET, value = PAYPAL_SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId,Model model){
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            log.info("payment:"+payment);
            if(payment.getState().equals("approved")){
            	Amount amount = payment.getTransactions().get(0).getAmount();
            	model.addAttribute("total", amount.getTotal());
            	model.addAttribute("currency", amount.getCurrency());
            	model.addAttribute("saleId",payment.getTransactions().get(0).getRelatedResources().get(0).getSale().getId());
            	model.addAttribute("message","支付成功，付款金额："+amount.getTotal()+",币种："+amount.getCurrency());
            }else{
            	model.addAttribute("message","支付失败");
            }
            return "paypal/index";
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        return "redirect:/";
    }
    
    
    @RequestMapping(method= RequestMethod.POST,value="/pay/payRefund.do")
    public String payRefund(@RequestParam("id") String id,@RequestParam("refundAmount") String refundAmount,Model model){
    	try {
    		Refund refund = paypalService.executeRefund(id, refundAmount, "USD");
    		if("completed".equals(refund.getState())){
    			model.addAttribute("saleId",id);
            	model.addAttribute("message","退款成功，退款金额："+refundAmount);
    		}
    		log.info("refund:"+refund);
		} catch (Exception e) {
			log.error(e.getMessage());
			model.addAttribute("saleId",id);
        	model.addAttribute("message","退款失败,："+e.getMessage());
		}
    	return "paypal/index";
    }
}
