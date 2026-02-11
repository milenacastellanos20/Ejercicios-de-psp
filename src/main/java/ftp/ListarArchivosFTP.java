package ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

public class ListarArchivosFTP {

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

                System.out.println("Listado del directorio: " + cliente.printWorkingDirectory());

                FTPFile[] archivos = cliente.listFiles();

                for (FTPFile archivo : archivos) {

                    String tipo;
                    if (archivo.isDirectory()) {
                        tipo = "Directorio";
                    } else if (archivo.isFile()) {
                        tipo = "Archivo";
                    } else {
                        tipo = "Otro";
                    }

                    System.out.println(
                            "Nombre: " + archivo.getName() +
                                    " | Tipo: " + tipo +
                                    " | Tamaño: " + archivo.getSize() + " bytes"
                    );
                }
            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
