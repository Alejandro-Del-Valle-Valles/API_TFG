package com.tfg.API_TFG.core.event;

public record CompraEmailEvent(String to, String subject, String htmlBody) {
}

