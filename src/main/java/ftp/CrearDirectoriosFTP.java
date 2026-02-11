package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class CrearDirectoriosFTP {

    public static void main(String[] args) {

        String servidor = System.getenv("FTP_SERVER");
        int puerto = Integer.parseInt(System.getenv("FTP_PORT"));
        String usuario = System.getenv("FTP_USER");
        String password = System.getenv("FTP_PASSWORD");

        FTPClient cliente = new FTPClient();

        try {
            cliente.connect(servidor, puerto);

            if (!FTPReply.isPositiveCompletion(cliente.getReplyCode())) {
                System.out.println("No se pudo conectar");
                cliente.disconnect();
                return;
            }

            if (cliente.login(usuario, password)) {
                cliente.changeWorkingDirectory("/" + usuario);

                String carpetaMD = "md" + usuario;
                String carpetaFotos = "fotos" + usuario;

                boolean mdCreada = cliente.makeDirectory(carpetaMD);
                boolean fotosCreada = cliente.makeDirectory(carpetaFotos);

                System.out.println("Crear carpeta " + carpetaMD + ": " + mdCreada);
                System.out.println("Crear carpeta " + carpetaFotos + ": " + fotosCreada);

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

