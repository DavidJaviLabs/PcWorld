package src;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

public class EnviarCorreo 
{
    private String numeroAleatorio;
    private static EnviarCorreo instancia;
    
    public EnviarCorreo()
    {
        generarNumeroAleatorio();
    }
    
    public static EnviarCorreo getInstancia()
    {
        if(instancia == null)
        {
            instancia = new EnviarCorreo();
        }
        return instancia;
    }
    
    public static EnviarCorreo crearNuevaInstancia() 
    {
        instancia = null;
        return getInstancia();
    }
    
    public String getNumeroAleatorio() 
    {
        return this.numeroAleatorio;
    }
    
    public String generarNumeroAleatorio() 
    {
        Random random = new Random();
        this.numeroAleatorio = String.format("%06d", random.nextInt(1000000));
        System.out.println(numeroAleatorio);
        
        return numeroAleatorio;
    }
    
    public void transfer_to_email (String Correo)
    {        
        String correoEnvia = "***********";
        String contraseña = "***********";

        // Generar un identificador único para el botón
        String botonId = UUID.randomUUID().toString();

        // Mensaje con formato HTML
        String mensaje = "<html>"
                       + "<body>"
                       + "<h2>¡Código de seguridad!</h2>"
                       + "<p>Estimado " + InterfazPrincipal.getInstancia().getUser() + ", </p>"
                       + "<p>Aquí tienes tu código de seguridad:</p>"
                       + "<p style='font-size: 20px; font-weight: bold; color: blue;'>" + numeroAleatorio + "</p>"
                       + "<p>Por favor, no compartas este código con nadie.</p>"
                       + "<p>¡Gracias!</p>"
                       + "</body>"
                       + "</html>";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session sesion = Session.getDefaultInstance(props);
        MimeMessage mail = new MimeMessage(sesion);

        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(Correo)); 
            mail.setSubject("Código de seguridad para el cambio de contraseña"); 
            mail.setContent(mensaje, "text/html"); // Establecer el contenido HTML

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia, contraseña);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO)); 
            transporte.close();
            System.out.println("El correo se envio correctamente");
        } catch (Exception ex) {
            System.out.println("Error de envio de correo...\n" + ex);
        }   
    }
}
