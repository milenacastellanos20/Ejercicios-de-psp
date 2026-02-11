package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CambioADirectorioPropioFTP {

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

                System.out.println("Directorio inicial: " + cliente.printWorkingDirectory());

                boolean cambiado = cliente.changeWorkingDirectory("/Milena");

                if (cambiado) {
                    System.out.println("Cambio correcto al directorio del usuario");
                    System.out.println("Directorio actual: " + cliente.printWorkingDirectory());
                } else {
                    System.out.println("No se pudo cambiar al directorio del usuario");
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
