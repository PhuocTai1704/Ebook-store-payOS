package com.dangphuoctai.Ebook_BE.controller;

import lombok.RequiredArgsConstructor;
import vn.payos.type.Webhook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dangphuoctai.Ebook_BE.service.PayOSService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private PayOSService payOSService;

    @PostMapping("/payments/webhook")
    public ResponseEntity<String> paymentPayOS(@RequestBody Webhook body) {
        payOSService.verifiedPaymentWithPayOS(body);

        return ResponseEntity.ok("Received webhook");
    }

    @PostMapping("/payments/webhook/confirm")
    public ResponseEntity<String> confirmWebhook(@RequestBody String webhookUrl) {

        payOSService.confirmWebhook(webhookUrl);

        return ResponseEntity.ok("Webhook confirmed successfully");
    }

}
