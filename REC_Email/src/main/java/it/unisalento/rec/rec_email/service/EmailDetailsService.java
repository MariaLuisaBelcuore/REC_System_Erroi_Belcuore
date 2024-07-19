package it.unisalento.rec.rec_email.service;

import it.unisalento.rec.rec_email.domain.Email;
import it.unisalento.rec.rec_email.domain.StatusEmail;
import it.unisalento.rec.rec_email.dto.EmailBillDTO;
import it.unisalento.rec.rec_email.dto.EmailDTO;
import it.unisalento.rec.rec_email.dto.EmailListDTO;
import it.unisalento.rec.rec_email.exceptions.EmailNotFoundException;
import it.unisalento.rec.rec_email.repositories.EmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class EmailDetailsService {
    @Autowired
    EmailRepository emailRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PdfService pdfService;
    private static final Logger log = LoggerFactory.getLogger(EmailDetailsService.class);

    public Email createEmail(EmailDTO emailDTO) {
        Email email = new Email();
        BeanUtils.copyProperties(emailDTO, email);
        email.setSendDateEmail(LocalDateTime.now());
        return email;
    }

    public void sendEmail(Email email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("recsystem2024@gmail.com");
            message.setTo(email.getEmailTo());
            message.setSubject(email.getSubject());
            message.setText(email.getText());
            javaMailSender.send(message);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (Exception e) {
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(email);
        }
    }

    public void sendEmailWithAttachment(Email email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom(email.getEmailFrom());
            helper.setTo(email.getEmailTo());
            helper.setSubject(email.getSubject());
            helper.setText(email.getText());

            if (email.getAttachment() != null) {
                ByteArrayResource byteArrayResource = new ByteArrayResource(email.getAttachment());
                helper.addAttachment("invoice.pdf", byteArrayResource);
            }

            javaMailSender.send(mimeMessage);
            email.setStatusEmail(StatusEmail.SENT);
        } catch (Exception e) {
            email.setStatusEmail(StatusEmail.ERROR);
        } finally {
            emailRepository.save(email);
        }
    }

    public EmailListDTO searchAll() throws EmailNotFoundException {
        EmailListDTO emailListDTO = new EmailListDTO();
        ArrayList<EmailDTO> emails = new ArrayList<>();
        emailListDTO.setList(emails);

        List<Email> list =  emailRepository.findAll();
        if (list.isEmpty()){
            throw new EmailNotFoundException("No email found");
        }
        for(Email email : list){
            EmailDTO emailDTO = new EmailDTO();
            BeanUtils.copyProperties(email, emailDTO);
            emails.add(emailDTO);
        }
        return emailListDTO;
    }

    public EmailDTO search(String id) throws EmailNotFoundException {
        if (!emailRepository.existsById(id)) {
            throw new EmailNotFoundException("Email not found");
        }
        Email email = emailRepository.findById(id).orElseThrow();
        EmailDTO emailDTO = new EmailDTO();
        BeanUtils.copyProperties(email, emailDTO);
        return emailDTO;
    }

    public EmailListDTO searchAllEmailTo(String emailTo) throws EmailNotFoundException {
        EmailListDTO emailListDTO = new EmailListDTO();
        ArrayList<EmailDTO> emails = new ArrayList<>();
        emailListDTO.setList(emails);

        List<Email> list =  emailRepository.findByEmailTo(emailTo);

        if (list.isEmpty()){
            throw new EmailNotFoundException("No emails sent to " + emailTo);
        }

        for(Email email : list){
            EmailDTO emailDTO = new EmailDTO();
            BeanUtils.copyProperties(email, emailDTO);
            emails.add(emailDTO);
        }
        return emailListDTO;
    }

    public String deleteEmail(String id) throws EmailNotFoundException{
        if (!emailRepository.existsById(id)) {
            throw new EmailNotFoundException("Email not found");
        }
        emailRepository.deleteById(id);
        return id;
    }

    public String deleteAllEmailTo(String emailTo) throws EmailNotFoundException{
        if (!emailRepository.existsByEmailTo(emailTo)) {
            throw new EmailNotFoundException("No emails sent to " + emailTo);
        }
        emailRepository.deleteByEmailTo(emailTo);
        return emailTo;
    }

    public void sendEmailBill(EmailBillDTO emailBillDTO) throws IOException {
        try {
            Email email = new Email();
            email.setOwnerRef("recsystem2024@gmail.com");
            email.setEmailFrom("recsystem2024@gmail.com");
            email.setEmailTo(emailBillDTO.getClientEmail());
            email.setSubject("Submission Task Bill");
            email.setText(
                    "Dear " + emailBillDTO.getClientEmail() + ",\n\n" +
                            "We are pleased to inform you that we have successfully processed your recent task submission. " +
                            "Please find attached the invoice for the services rendered.\n\n" +
                            "Task Name: " + emailBillDTO.getName() + "\n" +
                            "Description: " + emailBillDTO.getDescription() + "\n" +
                            "Cost: $" + emailBillDTO.getCost() + "\n\n" +
                            "Thank you for your business. If you have any questions or need further assistance, " +
                            "please do not hesitate to contact us.\n\n" +
                            "Best regards,\n" +
                            "The RecSystem Team"
            );
            email.setSendDateEmail(LocalDateTime.now());

            byte[] pdfBytes = pdfService.generateInvoice(emailBillDTO);
            email.setAttachment(pdfBytes);

            sendEmailWithAttachment(email);

        } catch (MailException | IOException e) {
            log.error("Errore durante l'invio dell'email o generazione del PDF: ", e);
            throw e;
        }
    }
}
