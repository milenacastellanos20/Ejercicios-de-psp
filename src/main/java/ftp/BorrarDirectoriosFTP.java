package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class BorrarDirectoriosFTP {

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



                String[] carpetas = {
                        "md" + usuario,
                        "fotos" + usuario,
                        "borrar" + usuario
                };

                for (String carpeta : carpetas) {
                    boolean borrada = cliente.removeDirectory(carpeta);
                    System.out.println("Borrar " + carpeta + ": " + borrada);
                }
            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
