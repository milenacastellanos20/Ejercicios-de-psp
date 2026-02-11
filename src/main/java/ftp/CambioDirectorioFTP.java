package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CambioDirectorioFTP {

    public static void main(String[] args) {

        String servidor = System.getenv("FTP_SERVER");
        int puerto = Integer.parseInt(System.getenv("FTP_PORT"));
        String usuario = System.getenv("FTP_USER");
        String password = System.getenv("FTP_PASSWORD");

        FTPClient cliente = new FTPClient();

        try {
            cliente.connect(servidor, puerto);

            int respuesta = cliente.getReplyCode();
            if (!FTPReply.isPositiveCompletion(respuesta)) {
                System.out.println("No se pudo conectar al servidor");
                cliente.disconnect();
                return;
            }

            if (cliente.login(usuario, password)) {
                cliente.changeWorkingDirectory("/Milena"); //hay que cambiar manualmente al directorio por como Filezilla gestiona la raíz virtual

                System.out.println("Directorio actual: " + cliente.printWorkingDirectory());

                boolean cambiado = cliente.changeWorkingDirectory("/Castellanos");

                if (cambiado) {
                    System.out.println("Cambio de directorio realizado");
                } else {
                    System.out.println("No se ha podido acceder al directorio de otro usuario");
                }

            } else {
                System.out.println("Login incorrecto");
            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
