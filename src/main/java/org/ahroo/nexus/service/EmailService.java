package org.ahroo.nexus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private final JavaMailSender mailSender;

    public void sendTokenAsEmail(String to, UUID tokenId) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setSubject("Activate your nexus account");
        mailMessage.setText("Use this token to activate the account: %s".formatted(tokenId));

        mailSender.send(mailMessage);

        // security risk to store token. disable in production
        log.trace("Email sent to {} with token {}", to, tokenId);
    }
}
