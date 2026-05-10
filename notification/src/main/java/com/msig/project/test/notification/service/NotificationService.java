package com.msig.project.test.notification.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log =
            LoggerFactory.getLogger(
                    NotificationService.class
            );

    private final JavaMailSender mailSender;

    public NotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOrderNotification(String message) {

        sendEmail(
                "Order Created",
                message
        );
    }

    public void sendPaymentNotification(
            String message
    ) {

        sendEmail(
                "Payment Success",
                message
        );
    }

    private void sendEmail(
            String subject,
            String message
    ) {

        try {

            log.info(
                    "========== SEND EMAIL =========="
            );

            SimpleMailMessage mail =
                    new SimpleMailMessage();

        //     mail.setTo(
        //             "test@gmail.com"
        //     );

        //     mail.setSubject(
        //             subject
        //     );

        //     mail.setText(
        //             message
        //     );

        //     mailSender.send(mail);

            log.info(
                    "EMAIL SUCCESSFULLY SENT"
            );

        } catch (MailException e) {

            log.error(
                    "FAILED SEND EMAIL : {}",
                    e.getMessage(),
                    e
            );
        }
    }
}