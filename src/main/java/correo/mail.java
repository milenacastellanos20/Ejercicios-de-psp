package correo;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Properties;
import java.util.Scanner;

public class mail {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //apartado1 - recoleccion de datos
        System.out.println("PROGRAMA 1: ENVÍO PERSONALIZADO");
        System.out.print("Introduce el destinatario: (email) ");
        String destinatario = sc.nextLine();
        System.out.print("Introduce el destinatario oculto (BCC): ");
        String destinatarioOculto = sc.nextLine();
        System.out.print("Introduce el asunto del mensaje: ");
        String asunto = sc.nextLine();
        System.out.print("Introduce el cuerpo del correo: ");
        String cuerpo = sc.nextLine();
        System.out.print("Introduce la ruta del archivo adjunto (Opcional): ");
        String ruta = sc.nextLine();
        envioScanner(destinatario, destinatarioOculto, asunto, cuerpo, ruta);

        //apartado2 - envio html con los 3 adjuntos
        System.out.println("\nPROGRAMA 2: ENVÍO HTML AUTOMÁTICO");
        envioHTML();

        //apartado3 - listado ordenado
        System.out.println("PROGRAMA 3: LISTADO DE MENSAJES");
        System.out.print("Filtrar por Asunto: ");
        String filtroAsunto = sc.nextLine();
        System.out.print("Filtrar por Remitente: ");
        String filtroRemitente = sc.nextLine();
        listadoOrdenado(filtroAsunto, filtroRemitente);


    }

    //apartado1 - envio con el scanner
    private static void envioScanner(String to, String bcc, String sub, String body, String path) {
        try {
            Properties prop = datosServidor();
            Session session = crearSession(prop);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(prop.getProperty("mail.smtp.username")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            if (!bcc.isEmpty()) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
            }
            message.setSubject(sub);

            Multipart multi = new MimeMultipart();

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multi.addBodyPart(textPart);

            if (!path.isEmpty()) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(new File(path));
                multi.addBodyPart(attachPart);
            }

            message.setContent(multi);
            Transport.send(message);
            System.out.println("Programa 1: Enviado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //apartado2 - envio html con los 3 adjuntos
    private static void envioHTML() {
        try {
            Properties prop = datosServidor();
            Session session = crearSession(prop);
            Message message = new MimeMessage(session);
            message.setSubject("Ejercicio 2: HTML y 3 Adjuntos");
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("milenacastellanos3940@gmail.com"));

            Multipart multi = new MimeMultipart();

            // Parte HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            String html = "<h1>Práctica Milena</h1><p>Mensaje con <b>HTML</b> y archivos.</p>";
            htmlPart.setContent(html, "text/html; charset=utf-8");
            multi.addBodyPart(htmlPart);

            // 3 Adjuntos de distinto tipo
            String[] archivos = {"documento.pdf", "imagen.jpg", "notas.txt"};
            for (String ruta : archivos) {
                File f = new File(ruta);
                if (f.exists()) {
                    MimeBodyPart p = new MimeBodyPart();
                    p.attachFile(f);
                    multi.addBodyPart(p);
                }
            }

            message.setContent(multi);
            Transport.send(message);
            System.out.println("Programa 2: Enviado con éxito.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //apartado3 - listado ordenado
    private static void listadoOrdenado(String fAsunto, String fRemitente) {
        try {
            Properties prop = datosServidorImap();
            Session session = crearSessionImap(prop);
            Store store = session.getStore("imap");
            store.connect(prop.getProperty("mail.imap.host"), prop.getProperty("mail.imap.username"), prop.getProperty("mail.imap.password"));

            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] mensajes = inbox.getMessages();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            int cont = 0;

            for (int i = mensajes.length - 1; i >= 0 && cont < 10; i--) {
                Message m = mensajes[i];
                String asunto = m.getSubject() != null ? m.getSubject() : "";
                String de = m.getFrom()[0].toString();

                if (asunto.contains(fAsunto) && de.contains(fRemitente)) {
                    System.out.println("Fecha: " + sdf.format(m.getSentDate()));
                    System.out.println("Remitente: " + de);
                    System.out.println("Asunto: " + asunto);

                    if (m.isMimeType("text/plain")) {
                        String[] lineas = m.getContent().toString().split("\n");
                        System.out.println("L1: " + (lineas.length > 0 ? lineas[0].trim() : ""));
                        System.out.println("L2: " + (lineas.length > 1 ? lineas[1].trim() : ""));
                    }
                    System.out.println("-----------------------------------");
                    cont++;
                }
            }
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //configuracion del servidor
    private static Properties datosServidor() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.username", "8845456@alu.murciaeduca.es");
        prop.put("mail.smtp.password", "xivo vdlu spvt zrpz");
        return prop;
    }

    private static Properties datosServidorImap() {
        Properties prop = new Properties();
        prop.put("mail.imap.host", "imap.gmail.com");
        prop.put("mail.imap.port", "993");
        prop.put("mail.imap.ssl.enable", "true");
        prop.put("mail.imap.username", "8845456@alu.murciaeduca.es");
        prop.put("mail.imap.password", "xivo vdlu spvt zrpz");
        return prop;
    }

    private static Session crearSession(Properties prop) {
        return Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.smtp.username"),
                        prop.getProperty("mail.smtp.password"));
            }
        });
    }

    private static Session crearSessionImap(Properties prop) {
        return Session.getInstance(prop, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(prop.getProperty("mail.imap.username"),
                        prop.getProperty("mail.imap.password"));
            }
        });
    }
}