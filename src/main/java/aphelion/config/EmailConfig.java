package aphelion.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.nio.charset.StandardCharsets;

@Configuration
@PropertySource("classpath:email.properties")
public class EmailConfig {
    @Value("${mail.username}")
    private String emailLogin;
    @Value("${mail.password}")
    private String emailPassword;
    @Value("${mail.protocol}")
    private String emailProtocol;
    @Value("${mail.host}")
    private String emailHost;
    @Value("${mail.port}")
    private int emailPort;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(emailLogin);
        javaMailSender.setPassword(emailPassword);
        javaMailSender.setProtocol(emailProtocol);
        javaMailSender.setHost(emailHost);
        javaMailSender.setPort(emailPort);
        javaMailSender.setDefaultEncoding(StandardCharsets.UTF_8.name());
        return javaMailSender;
    }
}
