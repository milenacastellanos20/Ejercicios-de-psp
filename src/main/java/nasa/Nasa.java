package nasa;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Nasa {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Introduce tu fecha de nacimiento (YYYY-MM-DD): ");
        String fecha = sc.nextLine();


        String urlApi = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY&date=" + fecha;

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(urlApi))
                    .GET()
                    .build();


            HttpResponse<String> respuesta = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (respuesta.statusCode() == 200) {
                String json = respuesta.body();

                String url = extraerValor(json, "url");
                String titulo = extraerValor(json, "title");
                String explicacion = extraerValor(json, "explanation");

                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
                String nombreFichero = timestamp + "MilenaCastellanosVillanueva.md";

                String contenidoMd = "Título: " + titulo + "\nURL: " + url + "\nExplicación: " + explicacion;
                guardarYMostrar(nombreFichero, contenidoMd);

            } else {
                System.out.println("Error en la petición. Código: " + respuesta.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extraerValor(String json, String clave) {
        try {
            int inicio = json.indexOf("\"" + clave + "\":\"") + clave.length() + 4;
            int fin = json.indexOf("\"", inicio);
            return json.substring(inicio, fin);
        } catch (Exception e) {
            return "No encontrado";
        }
    }

    private static void guardarYMostrar(String nombre, String contenido) throws IOException {
        FileWriter fw = new FileWriter(nombre);
        fw.write(contenido);
        fw.close();

        System.out.println("\n Contenido del fichero: " + nombre + ": ");
        System.out.println(contenido);
        System.out.println("===================");
    }
}
