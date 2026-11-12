package util;

import java.util.Properties;
import java.util.Random;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtils {

    // IMPORTANTE: Altere esta senha imediatamente no seu painel do Google!
    private static final String REMETENTE = "keypasso.oficial@gmail.com";
    private static final String SENHA_APP = "tutmvfwgjmgeicbp"; 

    private static Session criarSessao() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        
        // Usando SSL na porta 465 (Evita bloqueios de rede/firewall)
     // Configuração segura usando TLS/STARTTLS na porta 587
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMETENTE, SENHA_APP);
            }
        });

        // Exibe o log detalhado de comunicação com o Gmail no console do Eclipse
        session.setDebug(true);

        return session;
    }

    public static String gerarCodigo() {
        return String.valueOf(100000 + new Random().nextInt(900000));
    }

    public static boolean enviarCodigoRecuperacao(String emailDestino, String codigo) {
        String assunto = "KeyPasso - Código para Redefinição de Senha";
        String html = "<div style='font-family: Arial; padding: 20px; background-color: #0F141C; color: #FFFFFF; border-radius: 10px;'>"
                + "<h2 style='color: #0088FF;'>Recuperação de Senha</h2>"
                + "<p>Recebemos uma solicitação para redefinir a senha da sua conta KeyPasso.</p>"
                + "<p>Seu código de verificação é:</p>"
                + "<h1 style='color: #00D4FF; letter-spacing: 5px; background: #0A0E16; padding: 10px; text-align: center; border-radius: 6px;'>" + codigo + "</h1>"
                + "<p style='color: #A0AFC8; font-size: 12px;'>Se você não solicitou essa alteração, ignore este e-mail.</p>"
                + "</div>";

        return enviarEmail(emailDestino, assunto, html);
    }

    public static boolean enviarEmailBoasVindas(String destinatario, String nomeUsuario) {
        String assunto = "Bem-vindo ao KeyPasso! 🚀";
        String corpoHTML = "<html>"
            + "<body style='background-color: #0A0E18; color: #FFFFFF; font-family: Segoe UI, sans-serif; padding: 20px;'>"
            + "<div style='max-width: 450px; margin: auto; background-color: #0F141C; padding: 25px; border-radius: 12px; border: 1px solid #1E293B; text-align: center;'>"
            + "<h2 style='color: #0088FF; margin-bottom: 10px;'>Boas-vindas ao KeyPasso! 🚀</h2>"
            + "<p style='color: #A0AFC8; font-size: 14px;'>Olá, <b>" + nomeUsuario + "</b>!</p>"
            + "<p style='color: #A0AFC8; font-size: 14px;'>Seu cadastro foi realizado com sucesso. Agora você tem acesso ao seu gerenciador seguro de senhas.</p>"
            + "<hr style='border: 0; border-top: 1px solid #1E293B; margin: 20px 0;'>"
            + "<p style='color: #64748B; font-size: 12px;'>Se você não realizou este cadastro, desconsidere este e-mail.</p>"
            + "</div>"
            + "</body>"
            + "</html>";

        return enviarEmail(destinatario, assunto, corpoHTML);
    }

    public static boolean enviarEmail(String destinatario, String assunto, String corpoHTML) {
        try {
            Message message = new MimeMessage(criarSessao());
            message.setFrom(new InternetAddress(REMETENTE, "KeyPasso Security"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);
            message.setContent(corpoHTML, "text/html; charset=utf-8");

            Transport.send(message);
            System.out.println("[SUCESSO] E-mail enviado com sucesso para: " + destinatario);
            return true;
        } catch (Exception e) {
            System.err.println("[ERRO CRÍTICO NO ENVIO DE E-MAIL]");
            e.printStackTrace();
            return false;
        }
    }
}