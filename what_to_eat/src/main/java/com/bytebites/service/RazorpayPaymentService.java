package com.bytebites.service;

import com.bytebites.config.RazorpayConfig;
import com.razorpay.Order;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import org.json.JSONObject;

public class RazorpayPaymentService {

    private final String keyId;
    private final String keySecret;
    private final RazorpayClient client;
    private String lastPaymentLinkId;

    public RazorpayPaymentService() throws RazorpayException {
        keyId = RazorpayConfig.KEY_ID;
        keySecret = RazorpayConfig.KEY_SECRET;
        client = new RazorpayClient(keyId, keySecret);
    }

    public String getKeyId() {
        return keyId;
    }

    public String createOrder(
            double amountInRupees,
            String receipt) throws RazorpayException {

        if (amountInRupees <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be positive.");
        }

        JSONObject orderRequest = new JSONObject()
                .put("amount", Math.round(amountInRupees * 100))
                .put("currency", "INR")
                .put("receipt", receipt)
                .put("payment_capture", 1);

        Order order = client.orders.create(orderRequest);

        return order.get("id").toString();
    }

    public String createPaymentLink(
            double amountInRupees,
            String description,
            String customerName,
            String customerEmail,
            String customerContact) throws RazorpayException {

        if (amountInRupees <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be positive.");
        }

        JSONObject linkRequest = new JSONObject()
                .put(
                        "amount",
                        Math.round(amountInRupees * 100))
                .put(
                        "currency",
                        "INR")
                .put(
                        "description",
                        description)
                .put(
                        "reference_id",
                        "bytebites_" + System.currentTimeMillis())
                .put(
                        "customer",
                        new JSONObject()
                                .put("name", customerName)
                                .put("email", customerEmail)
                                .put("contact", customerContact));

        PaymentLink paymentLink =
                client.paymentLink.create(linkRequest);

        lastPaymentLinkId =
                paymentLink.get("id").toString();

        return paymentLink.get("short_url").toString();
    }

    public String getLastPaymentLinkId() {
        return lastPaymentLinkId;
    }

    public String getPaymentLinkStatus(
            String paymentLinkId) throws RazorpayException {

        if (paymentLinkId == null ||
                paymentLinkId.trim().isEmpty()) {
            return "";
        }

        PaymentLink paymentLink =
                client.paymentLink.fetch(paymentLinkId);

        Object status =
                paymentLink.get("status");

        if (status == null) {
            return "";
        }

        return status.toString();
    }

    public boolean isPaymentCompleted(
            String paymentLinkId) throws RazorpayException {

        String status =
                getPaymentLinkStatus(paymentLinkId);

        return "paid".equalsIgnoreCase(status);
    }

    public boolean verifyPayment(
            String orderId,
            String paymentId,
            String signature)
            throws RazorpayException {

        JSONObject attributes =
                new JSONObject()
                        .put(
                                "razorpay_order_id",
                                orderId)
                        .put(
                                "razorpay_payment_id",
                                paymentId)
                        .put(
                                "razorpay_signature",
                                signature);

        return Utils.verifyPaymentSignature(
                attributes,
                keySecret);
    }
}