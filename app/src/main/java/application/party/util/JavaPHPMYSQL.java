package application.party.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

//He tenido q descargarme la librería desde aqui
// https://code.google.com/p/json-simple/downloads/detail?name=json-simple-1.1.1.jar&can=2&q=
//luego meter el jar en la capeta de proyecto; Project Structure-> app (modules) Dependencis add (2da opcion y pones la ruta)
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class JavaPHPMYSQL {

    /**
     * @param args the command line arguments
     */

    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String SERVER_PATH = "http://fiesta1.esy.es/";

    // ENVIA UN JSON CON LAS VARIABLES AL PHP Y EJECUTA UNA CONSULTA SQL , NO RECOGE VALORES
    //SE PUEDE USAR PARA INSERTAR, DELETE, ...
    public static void insercion(String jsonString, String nombreScript) {
        try {
            //Codificar el json a URL
            jsonString = URLEncoder.encode(jsonString, "UTF-8");
            //Generar la URL
            String url = SERVER_PATH+nombreScript;
            //Creamos un nuevo objeto URL con la url donde queremos enviar el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            //Creamos los parametros para enviar
            String urlParameters = "json="+jsonString;
            // Enviamos los datos por POST
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();
            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println(response);
            //cerramos la conexión
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //ENVIA VARIABLES EN JSON, EJECUTA SCRIPT Y RECUPERA DATOS EN STRING
    //CUANDO RECUPERA LOS DATOS HAY QUE LLAMAR A UN METODO MOSTRAR, EJEMPLO: getVehiculoByUserId
    public static String getDataFromFilter(String jsonString, String nombreScript){
        //jdcc primero enviamos las variables en el json
        StringBuffer response = null;

        try {
            //Codificar el json a URL
            jsonString = URLEncoder.encode(jsonString, "UTF-8");
            //Generar la URL
            String url = SERVER_PATH+nombreScript;
            //Creamos un nuevo objeto URL con la url donde queremos enviar el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            //Creamos los parametros para enviar
            String urlParameters = "json="+jsonString;
            // Enviamos los datos por POST
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();


            //jdcc: ahora recuperamos los datos
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println("Respuesta del servidor: "+response);
            System.out.println();
            //cerramos la conexión
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return response.toString();
    }

    public static void insertarRuta(String Origen, String Destino,String HoraInicio, String Fecha, String Comentario, int idTransportista) {
        //Creamos un objeto JSON
        JSONObject jsonObj = new JSONObject();
        //Añadimos el nombre, apellidos y email del usuario
        //es IMPORTANTE que pongamos lo que esta entre comillas igual que la columna de la BBDD
        jsonObj.put("idRuta", 0);
        jsonObj.put("Favorita", false);
        jsonObj.put("Origen", Origen);
        jsonObj.put("Destino", Destino);
        jsonObj.put("HoraInicio", HoraInicio);
        jsonObj.put("Fecha", Fecha);
        jsonObj.put("Comentario", Comentario);
        jsonObj.put("idTransportista", idTransportista);

        //Creamos una lista para almacenar el JSON
        List l = new LinkedList();
        l.addAll(Arrays.asList(jsonObj));
        //Generamos el String JSON
        String jsonString = JSONValue.toJSONString(l);
        System.out.println("JSON GENERADO:");
        System.out.println(jsonString);
        System.out.println("");

        insercion(jsonString, "insertarRuta.php");
    }
    public static void borrarRuta(int idRuta){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idRuta", idRuta);
        List l = new LinkedList();
        l.addAll(Arrays.asList(jsonObject));

        String jsonString = JSONValue.toJSONString(l);

        insercion(jsonString, "borrarRuta.php"); //puede usar insercion porque solo recibe una variable y ejecuta una query
    }


    //JDCC: ADRIAN A PARTIR DE AQUI TIENES EJEMPLOS DE METODOS PARA RECIBIR OBJETOS.
    //LA IDEA ES HACERLO MEJOR Q EASYTRANSPORT, asi que buscar por google encriptacion de json
    //https://www.google.es/search?q=json+android+php+encriptar&oq=json+android+php+encriptar&aqs=chrome..69i57j0l5.14828j0j7&sourceid=chrome&es_sm=122&ie=UTF-8
    //LA IDEA ES CREAR UN SCRIPT PARA CONEXION QUE LLAME A METODOS DE OTRO SCRIPT,
    //PARA HACER MENOS SCRIPTS, SE PODRIA PASAR LA SENTENCIA SQL AL SCRIPT, (asi no tendriamos un script por cada query)



/*
    public static ArrayList<Vehiculo> getVehiculoByUserId(int idUsuario){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("idUsuario", idUsuario);
        List l = new LinkedList();
        l.addAll(Arrays.asList(jsonObject));

        String jsonString = JSONValue.toJSONString(l);
        //el script obtiene la variable idUsuario hace consulta y recupera datos
        String json= getDataFromFilter(jsonString, "getVehiculoByUserId.php");
        return mostrarVehiculos(json);
    }

    public static String getAllRutas(){

        StringBuffer response = null;

        try {
            //Generar la URL
            String url = SERVER_PATH+"getAllRutas.php";
            //Creamos un nuevo objeto URL con la url donde pedir el JSON
            URL obj = new URL(url);
            //Creamos un objeto de conexión
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            //Añadimos la cabecera
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Enviamos la petición por POST
            con.setDoOutput(true);
            //Capturamos la respuesta del servidor
            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            //Mostramos la respuesta del servidor por consola
            System.out.println("Respuesta del servidor: "+response);
            System.out.println();
            //cerramos la conexión
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static ArrayList<Acuerdo> mostrarAcuerdos(String json){
        System.out.println("INFORMACIÓN OBTENIDA DE LA BASE DE DATOS:");
        //Crear un Objeto JSON a partir del string JSON
        ArrayList listaAcuerdos = new ArrayList();
        Object jsonObject =JSONValue.parse(json.toString());
        //Convertir el objeto JSON en un array
        JSONArray array=(JSONArray)jsonObject;
        //Iterar el array y extraer la información
        for(int i=0;i<array.size();i++){
            JSONObject row =(JSONObject)array.get(i);
            String idAcuerdo = row.get("idAcuerdo").toString();
            String Precio = row.get("Precio").toString();
            String Comentario = row.get("Comentario").toString();
            String Estado = row.get("Estado").toString();
            String idRuta = row.get("idUsuario").toString(); //se mete a null porque se sabe de que ruta es
            String Punto_recogida = row.get("Punto_recogida").toString();
            String Punto_entrega = row.get("Punto_entrega").toString();
            String Leido = row.get("Leido").toString();

            // Acuerdo acuerdo = new Acuerdo(Integer.parseInt(idAcuerdo),Double.parseDouble(Precio),Comentario ,Estado,null,Punto_recogida,Punto_entrega,Leido);

            //  listaAcuerdos.add(acuerdo);

        }
        return listaAcuerdos;
    }


    public static ArrayList<Ruta> mostrarAllRutas(String json){
        System.out.println("INFORMACIÓN OBTENIDA DE LA BASE DE DATOS:");
        //Crear un Objeto JSON a partir del string JSON
        ArrayList listaRutas = new ArrayList();
        Object jsonObject =JSONValue.parse(json.toString());
        //Convertir el objeto JSON en un array
        JSONArray array=(JSONArray)jsonObject;
        //Iterar el array y extraer la información
        for(int i=0;i<array.size();i++){
            JSONObject row =(JSONObject)array.get(i);
            String idRuta = row.get("idRuta").toString();
            String Origen = row.get("Origen").toString();
            String Destino = row.get("Destino").toString();
            String HoraInicio = row.get("HoraInicio").toString();
            String Fecha = row.get("Fecha").toString();
            String Comentario = row.get("Comentario").toString();
            String idTransportista = row.get("idTransportista").toString();

            Ruta ruta = new Ruta(Integer.parseInt(idRuta),Origen,Destino,HoraInicio,Fecha,Comentario);

            listaRutas.add(ruta);
        }
        return listaRutas;
    }*/

}



