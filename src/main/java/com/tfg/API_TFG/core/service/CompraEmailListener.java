package com.tfg.API_TFG.core.service;

import com.tfg.API_TFG.core.event.CompraEmailEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CompraEmailListener {
    private static final Logger logger = LoggerFactory.getLogger(CompraEmailListener.class);

    private final EmailService emailService;

    public CompraEmailListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async("emailTaskExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onCompraEmail(CompraEmailEvent event) {
        try {
            emailService.enviarCompraHtml(event.to(), event.subject(), event.htmlBody());
        } catch (RuntimeException ex) {
            logger.error("Error enviando correo de compra a {}", event.to(), ex);
        }
    }
}

