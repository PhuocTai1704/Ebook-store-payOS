package com.dangphuoctai.Ebook_BE.service;

import com.dangphuoctai.Ebook_BE.payloads.EmailDetails;

public interface EmailService {
    // Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);
}
