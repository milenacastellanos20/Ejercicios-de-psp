package ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileInputStream;
import java.io.InputStream;

public class SubidaArchivosFTP {

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
                cliente.enterLocalPassiveMode();

                // SUBIDA ARCHIVO .MD
                cliente.setFileType(FTP.ASCII_FILE_TYPE);

                InputStream mdInput = new FileInputStream(
                        "src/main/resources/prueba.md"
                );

                boolean mdSubido = cliente.storeFile(
                        "md" + usuario + "/prueba.md",
                        mdInput
                );

                mdInput.close();
                System.out.println("Archivo .md subido: " + mdSubido);

                // SUBIDA ARCHIVO .JPG
                cliente.setFileType(FTP.BINARY_FILE_TYPE);

                InputStream imgInput = new FileInputStream(
                        "src/main/resources/foto.jpg"
                );

                boolean imgSubida = cliente.storeFile(
                        "fotos" + usuario + "/foto.jpg",
                        imgInput
                );

                imgInput.close();
                System.out.println("Archivo .jpg subido: " + imgSubida);


            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
