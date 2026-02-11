package ftp;

import java.io.File;

public class CrearCarpetaLocal {

    public static void main(String[] args) {

        String usuario = System.getenv("FTP_USER");

        String ruta = "src/main/resources/descargas" + usuario;

        File carpeta = new File(ruta);

        if (carpeta.exists()) {
            System.out.println("La carpeta ya existe: " + carpeta.getPath());
        } else {
            boolean creada = carpeta.mkdir();
            System.out.println("Carpeta creada: " + creada);
        }
    }
}

