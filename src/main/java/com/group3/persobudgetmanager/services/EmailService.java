package com.group3.persobudgetmanager.services;
import com.group3.persobudgetmanager.models.EmailDetails;
public interface EmailService {
    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
}
