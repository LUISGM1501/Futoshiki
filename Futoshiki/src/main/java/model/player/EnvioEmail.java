package model.player;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EnvioEmail{

    public static void sendEmail(String email, String token, String usuario) {

        String from = "foogles05@gmail.com";
        String host = "smtp.gmail.com";
        int port = 587;


        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(properties, new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("foogles05@gmail.com", "vrdw dkgh hhzl sswl");
            }
        });

        try {

            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(from));

            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            message.setSubject("Recuperación de Contraseña");

            message.setText("\tDe parte del equipo del juego Futoshiki,esperamos tenga un buen día/tarde" +
                    "\n\nAca se envia el token de recuperación para el usuario " + usuario +
                    " con el cual podra reestablecer su contraseña:\n " + token + "\n\nEsperamos que le vaya bien, y disfrute jugando futoshiki!!");

            Transport.send(message);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}
