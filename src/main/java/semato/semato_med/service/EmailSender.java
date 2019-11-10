package semato.semato_med.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    public void send(SimpleMailMessage msg){
        javaMailSender.send(msg);
    }

    public void send(MimeMessage msg){
        javaMailSender.send(msg);
    }

}
