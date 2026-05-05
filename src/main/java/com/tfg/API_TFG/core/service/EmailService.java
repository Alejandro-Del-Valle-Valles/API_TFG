package com.tfg.API_TFG.core.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

/**
 * Service to handle transactional emails using EmailJS REST API.
 * This approach bypasses SMTP restrictions and uses Strict Mode authentication.
 */
@Service
public class EmailService {

    private final String serviceId;
    private final String templateId;
    private final String publicKey;
    private final String privateKey;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public EmailService(@Value("${emailjs.service.id}") String serviceId,
                        @Value("${emailjs.template.id}") String templateId,
                        @Value("${emailjs.public.key}") String publicKey,
                        @Value("${emailjs.private.key}") String privateKey) {
        this.serviceId = serviceId;
        this.templateId = templateId;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        // Instantiated directly to avoid Spring context injection issues
        this.objectMapper = new ObjectMapper();
        this.httpClient = HttpClient.newHttpClient();
    }

    /**
     * Sends an HTML email via HTTP POST to the EmailJS API.
     *
     * @param to Destination email address.
     * @param subject Email subject.
     * @param htmlBody HTML content.
     */
    public void enviarCompraHtml(String to, String subject, String htmlBody) {
        try {
            // 1. Prepare template parameters
            Map<String, String> templateParams = Map.of(
                    "to_email", to,
                    "subject", subject,
                    "html_body", htmlBody
            );

            // 2. Prepare main JSON payload including the Private Key (accessToken)
            Map<String, Object> payload = Map.of(
                    "service_id", serviceId,
                    "template_id", templateId,
                    "user_id", publicKey,
                    "accessToken", privateKey,
                    "template_params", templateParams
            );

            // 3. Convert Map to JSON String
            String requestBody = objectMapper.writeValueAsString(payload);

            // 4. Build HTTP request
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.emailjs.com/api/v1.0/email/send"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            // 5. Send request
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("EmailJS returned status code " + response.statusCode() + ": " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Error sending email via EmailJS API", e);
        }
    }
}