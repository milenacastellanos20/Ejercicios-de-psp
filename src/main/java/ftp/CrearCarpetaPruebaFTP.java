package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CrearCarpetaPruebaFTP {

    public static void main(String[] args) {

        String servidor = System.getenv("FTP_SERVER");
        int puerto = Integer.parseInt(System.getenv("FTP_PORT"));
        String usuario = System.getenv("FTP_USER");
        String password = System.getenv("FTP_PASSWORD");

        FTPClient cliente = new FTPClient();

        try {
            cliente.connect(servidor, puerto);

            if (!FTPReply.isPositiveCompletion(cliente.getReplyCode())) {
                cliente.disconnect();
                return;
            }

            if (cliente.login(usuario, password)) {
                cliente.changeWorkingDirectory("/" + usuario);
                String carpeta = "prueba" + usuario;
                boolean creada = cliente.makeDirectory(carpeta);

                System.out.println("Carpeta " + carpeta + " creada: " + creada);
            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

