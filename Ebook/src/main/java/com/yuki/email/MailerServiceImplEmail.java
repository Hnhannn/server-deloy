package com.yuki.email;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailerServiceImplEmail implements MailService {
    private static final Logger logger = LoggerFactory.getLogger(MailerServiceImplEmail.class);

    JavaMailSender sender;

    public MailerServiceImplEmail(JavaMailSender sender) {
        this.sender = sender;
    }

    @Override
    public void send(MailInfo mail) {
        // Tạo message
        MimeMessage message = sender.createMimeMessage();
        // Sử dụng Helper để thiết lập các thông tin cần thiết cho message
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(mail.getFrom());
            helper.setTo(mail.getTo());
            helper.setSubject(mail.getSubject());
            helper.setText(mail.getBody(), false);
            helper.setReplyTo(mail.getFrom());
            String[] cc = mail.getCc();
            if (cc != null && cc.length > 0) {
                helper.setCc(cc);
            }
            String[] bcc = mail.getBcc();
            if (bcc != null && bcc.length > 0) {
                helper.setBcc(bcc);
            }
            String[] attachments = mail.getAttachments();
            if (attachments != null && attachments.length > 0) {
                for (String path : attachments) {
                    File file = new File(path);
                    if (file.exists()) {
                        helper.addAttachment(file.getName(), file);
                    } else {
                        logger.error("Attachment file not found");
                    }
                }
            }
            logger.info("Kết nối với máy chủ email và gửi message");
            sender.send(message);
            logger.info("Gửi email thành công đến: {}", mail.getTo());
        } catch (MessagingException e) {
            e.printStackTrace();
            logger.error("Lỗi khi gửi email đến: {}", mail.getTo(), e);
        }
    }

    @Override
    public void send(String to, String subject, String body) {
        this.send(new MailInfo(to, subject, body));
    }

    List<MailInfo> list = new ArrayList<>();

    @Override
    public void queue(MailInfo mail) {
        list.add(mail);
    }

    @Override
    public void queue(String to, String subject, String body) {
        queue(new MailInfo(to, subject, body));
    }

    @Scheduled(fixedDelay = 5000)
    public void run() {
        while (!list.isEmpty()) {
            MailInfo mail = list.remove(0);
            try {
                this.send(mail);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
