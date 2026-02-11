package ftp;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.OutputStream;

public class DescargarArchivosFTP {

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

                // DESCARGA ARCHIVO .MD
                cliente.setFileType(FTP.ASCII_FILE_TYPE);

                String rutaLocalMd = "src/main/resources/descargas" + usuario + "/prueba.md";
                OutputStream salidaMd = new FileOutputStream(rutaLocalMd);

                boolean mdDescargado = cliente.retrieveFile(
                        "md" + usuario + "/prueba.md",
                        salidaMd
                );

                salidaMd.close();
                System.out.println("Archivo .md descargado: " + mdDescargado);

                // DESCARGA ARCHIVO .JPG

                cliente.setFileType(FTP.BINARY_FILE_TYPE);

                String rutaLocalImg = "src/main/resources/descargas" + usuario + "/foto.jpg";
                OutputStream salidaImg = new FileOutputStream(rutaLocalImg);

                boolean imgDescargada = cliente.retrieveFile(
                        "fotos" + usuario + "/foto.jpg",
                        salidaImg
                );

                salidaImg.close();
                System.out.println("Archivo .jpg descargado: " + imgDescargada);


            }

            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
