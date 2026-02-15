package Pruebas;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Cliente {

    public static void main(String[] args) {
        String servidor = "192.168.0.2";
        int puerto = 21;
        String usuario = "8845456";
        String password = "febrero";

        FTPClient cliente = new FTPClient();

        try {
            cliente.connect(servidor, puerto);

            int respuesta = cliente.getReplyCode();
            if (!FTPReply.isPositiveCompletion(respuesta)) {
                System.out.println("Error al conectar al servidor FTP");
                cliente.disconnect();
                return;
            }

            boolean loginCorrecto = cliente.login(usuario, password);

            if (loginCorrecto) {
                System.out.println("Login correcto en el servidor FTP");

                apartado1(cliente);
                apartado2(cliente);
                apartado3(cliente);
                apartado4(cliente, "objetivo.txt");

            } else {
                System.out.println("Login incorrecto");
            }
            cliente.enterLocalPassiveMode();
            cliente.logout();
            cliente.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void apartado1(FTPClient cliente) throws IOException {
        cliente.changeWorkingDirectory("8845456");
        String[] directorios = cliente.listNames();
        Collections.sort(Arrays.asList(directorios));
        cliente.changeWorkingDirectory(directorios[0]);
        System.out.println("Fichero subido? " + subirFichero(cliente, System.getProperty("user.dir"), "subida.md"));
        mostrarInfo(cliente, "subida.md");
        System.out.println("Fichero descargado? " + descargarFichero(cliente, System.getProperty("user.dir"), "subida.md"));
        leerFichero("subida.md");
    }

    public static boolean subirFichero(FTPClient cliente, String directorio, String Nombrefichero) throws IOException {
        cliente.setFileType(FTP.ASCII_FILE_TYPE);
        File fichero = new File(directorio + "/" + Nombrefichero);
        FileWriter fw = new FileWriter(fichero, true);
        fw.write("Milena\n");
        fw.write("Castellanos\n");
        fw.write("Adyso");
        fw.close();

        String ficheroRemoto = Nombrefichero;
        InputStream input = new FileInputStream(fichero);
        boolean upload = cliente.storeFile(ficheroRemoto, input);
        input.close();
        return upload;
    }

    public static void mostrarInfo(FTPClient ftp, String nombreFichero) throws IOException {
        FTPFile[] archivos = ftp.listFiles();
        for (FTPFile archivo : archivos) {
            if (archivo.getName().equals(nombreFichero)) {
                System.out.println(archivo.getSize());
            }

        }

    }

    public static boolean descargarFichero(FTPClient cliente, String directorio, String Nombrefichero) throws IOException {
        cliente.setFileType(FTP.ASCII_FILE_TYPE);
        File fichero = new File(directorio + "/" + Nombrefichero);
        String ficheroRemoto = Nombrefichero;
        OutputStream output = new FileOutputStream(fichero);
        boolean download = cliente.retrieveFile(ficheroRemoto, output);
        output.close();
        return download;
    }

    public static void leerFichero(String nombreFichero) throws IOException {
        FileReader fr = new FileReader(nombreFichero);
        BufferedReader br = new BufferedReader(fr);
        String linea;
        while ((linea = br.readLine()) != null) {
            System.out.println(linea);
        }
        br.close();
    }

    public static void apartado2(FTPClient cliente) throws IOException {
        cliente.changeWorkingDirectory("/vicente/medina");
        double suma = 0;
        double cantidad = 0;
        double media = 0;
        double mayor = 0;
        double menor = 0;
        String nombreArchivo = "";
        String nombreMenor = "";

        FTPFile[] files = cliente.listFiles();
        for (FTPFile file : files) {
            cantidad++;
            suma += file.getSize();
            if (mayor < file.getSize()) {
                mayor = file.getSize();
                nombreArchivo = file.getName();
            } else if (menor > file.getSize()) {
                menor = file.getSize();
                nombreMenor = file.getName();
            }
        }

        media = suma / cantidad;
        System.out.println("Media: " + media);
        System.out.println("Nombre: " + nombreArchivo + " tamaño: " + cantidad);

        File nuevo = new File("temporal.md");
        FileInputStream fis = new FileInputStream(nombreArchivo);
        FileOutputStream fos = new FileOutputStream(nuevo);
        BufferedWriter bw = new BufferedWriter(new FileWriter(nuevo, true));
        byte[] buffer = new byte[10];
        int leidos = fis.read(buffer);
        if (leidos > 0) {
            fos.write(buffer, 0, leidos);
        }
        bw.newLine();
        bw.write(nombreArchivo);

        bw.close();
        fis.close();
        fos.close();

        System.out.println("Fichero subido? " + subirFichero(cliente, System.getProperty("user.dir"), "temporal.md"));
        cliente.rename("temporal.md", "datosmayor.md");

        cliente.deleteFile(nombreMenor);
    }

    public static void apartado3(FTPClient cliente) throws IOException {
        cliente.changeWorkingDirectory("/");
        cliente.changeWorkingDirectory("/guillermo");
        cliente.makeDirectory("examen");
        cliente.changeWorkingDirectory("examen");

        FTPFile[] files = cliente.listFiles();
        List<String> info = new ArrayList<>();
        for (FTPFile file : files) {
            if (file.getSize() > 2500) {
                info.add(file.getName() + " " + file.getUser() + "  " + file.getSize());
            }
        }
        Collections.sort(info);
        File file = new File("3a.md");
        BufferedWriter bw = new BufferedWriter(new FileWriter(file, true));
        for (String i : info) {
            bw.write(i);
        }
        List<String> info2 = new ArrayList<>();
        File file2 = new File("3b.md");
        for (FTPFile f : files) {
            info2.add(f.getName() + " " + f.getSize() + " " + f.getTimestamp().getTime() + f.getUser());
        }
        Collections.sort(info2, Collections.reverseOrder());
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(file2, true));
        for (String in : info2) {
            bw2.write(in);
        }
    }

    public static void apartado4(FTPClient ftpClient, String nombreObjetivo) throws Exception {
        ftpClient.changeWorkingDirectory("/");
        FTPFile[] directoriosRaiz = ftpClient.listDirectories("/");

        System.out.println("--- Iniciando escaneo de directorios virtuales ---");

        for (FTPFile carpeta : directoriosRaiz) {
            String nombreCarpeta = carpeta.getName();


            if (ftpClient.changeWorkingDirectory("/" + nombreCarpeta)) {

                FTPFile[] archivosInternos = ftpClient.listFiles();

                for (FTPFile archivo : archivosInternos) {
                    if (archivo.isFile() && archivo.getName().equalsIgnoreCase(nombreObjetivo)) {
                        System.out.println(">> Fichero '" + nombreObjetivo + "' encontrado en: /" + nombreCarpeta);
                    }
                }

                ftpClient.changeWorkingDirectory("/");
            }
        }
    }
}
