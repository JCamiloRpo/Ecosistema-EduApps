package com.example.euestudiante;

import org.json.JSONArray;
<<<<<<< HEAD
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.InvalidKeyException;
=======
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

<<<<<<< HEAD
public class ConexionApiRest {

    private String url;

    /**
     * Constructor que recibe el url por del servidor API REST
     * @param url ejemplo https://192.168.0.101/ApiRest/
     */
    public ConexionApiRest(String url){
        this.url = url;
    }

    /**
     * Obtener los datos de una tabla con todas las columnas
     * {
     * {"value1","value2","value3",...}, //Registro uno
     * {"value1","value2","value3",...}, //Registro dos
     * ...}
     * @param table a consultar
     * @return Retorna una matriz que representa la tabla
     */
    public String[][] getData(String table) throws IllegalAccessException, InvalidKeyException, IOException, JSONException {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url+"getData.php?t="+table, "GET"));//Descargo el archivo JSON
        JSONArray tmp = json.getJSONArray("data");
        ncolum = tmp.getJSONObject(0).length()/2;
        nrow = tmp.length();
        strData = new String[nrow][ncolum];
        for(int n=0; n < nrow; n++){//Itero solo la cantidad de registros obtenidos correctament
            JSONObject row = tmp.getJSONObject(n);//Obtengo solo las columnas de un registro
            JSONArray results = row.names(); //Obtengo los nombres de las columnas
            for (int i=0,j=1; i < ncolum; i++,j+=2)
                strData[n][i] = row.getString(results.getString(j)); //Obtengo el valor de la columna
        }
        return  strData;
    }

    /**
     * Obtener los datos de una tabla con las columnas especificadas
     * {
     * {"value1","value2","value3",...}, //Registro uno
     * {"value1","value2","value3",...}, //Registro dos
     * ...}
     * @param table a consultar
     * @param columns separadas por , (column1,column2,column3...)
     * @return Retorna una matriz que representa la tabla
     */
    public String[][] getData(String table, String columns) throws IllegalAccessException, InvalidKeyException, IOException, JSONException {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url+"getData.php?t="+table+"&c="+columns,"GET"));//Descargo el archivo JSON
        JSONArray tmp = json.getJSONArray("data");
        ncolum = tmp.getJSONObject(0).length()/2;
        nrow = tmp.length();
        strData = new String[nrow][ncolum];
        for(int n=0; n < nrow; n++){//Itero solo la cantidad de registros obtenidos correctament
            JSONObject row = tmp.getJSONObject(n);//Obtengo solo las columnas de un registro
            JSONArray results = row.names(); //Obtengo los nombres de las columnas
            for (int i=0,j=1; i < ncolum; i++,j+=2)
                strData[n][i] = row.getString(results.getString(j)); //Obtengo el valor de la columna
        }
        return  strData;
    }

    /**
     * Obtener los datos de una tabla con las columnas especificadas que cumplan una condición
     * {
     * {"value1","value2","value3",...}, //Registro uno
     * {"value1","value2","value3",...}, //Registro dos
     * ...}
     * @param table a consultar
     * @param columns separadas por , (column1,column2,column3...)
     * @param where condicion (ID=0, ID>0 AND ID<20)
     * @return Retorna una matriz que representa la tabla
     */
    public String[][] getData(String table, String columns, String where) throws IllegalAccessException, InvalidKeyException, IOException, JSONException {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url+"getData.php?t="+table+"&c="+columns+"&w="+where,"GET"));//Descargo el archivo JSON
        JSONArray tmp = json.getJSONArray("data");
        ncolum = tmp.getJSONObject(0).length()/2;
        nrow = tmp.length();
        strData = new String[nrow][ncolum];
        for(int n=0; n < nrow; n++){//Itero solo la cantidad de registros obtenidos correctament
            JSONObject row = tmp.getJSONObject(n);//Obtengo solo las columnas de un registro
            JSONArray results = row.names(); //Obtengo los nombres de las columnas
            for (int i=0,j=1; i < ncolum; i++,j+=2)
                strData[n][i] = row.getString(results.getString(j)); //Obtengo el valor de la columna
        }
        return  strData;
    }

    /**
     * Metodo para inserta un registro en una tabla y retorna el resultado (el registro son su ID)
     * @param table a insertar
     * @param columns de la tabla, separados por , (column1,column2,column3)
     * @param values de las columnas separados por , (value1,value2,value3)
     * @return Retorna una matriz que representa la tabla
     */
    public String[][] setData(String table, String columns, String values) throws IllegalAccessException, InvalidKeyException, IOException, JSONException {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url+"postData.php?t="+table+"&c="+columns+"&v="+values,"POST"));//Descargo el archivo JSON
        JSONArray tmp = json.getJSONArray("data");
        ncolum = tmp.getJSONObject(0).length()/2;
        nrow = tmp.length();
        strData = new String[nrow][ncolum];
        for(int n=0; n < nrow; n++){//Itero solo la cantidad de registros obtenidos correctament
            JSONObject row = tmp.getJSONObject(n);//Obtengo solo las columnas de un registro
            JSONArray results = row.names(); //Obtengo los nombres de las columnas
            for (int i=0,j=1; i < ncolum; i++,j+=2)
                strData[n][i] = row.getString(results.getString(j)); //Obtengo el valor de la columna
        }
        return  strData;
    }

    /**
     * Metodo para inserta un registro en una tabla y retorna el resultado (el registro son su ID)
     * @param table a insertar
     * @param column de la tabla (solo una columna)
     * @param value de la columna (solo un valor)
     * @param where condicion para filtrar los registros actualizar
     * @return Retorna una matriz que representa la tabla
     */
    public String[][] updateData(String table, String column, String value, String where) throws IllegalAccessException, InvalidKeyException, IOException, JSONException {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url+"postData.php?t="+table+"&c="+column+"&v="+value+"&w="+where,"POST"));//Descargo el archivo JSON
=======
public abstract class ConexionApiRest {

    /**
     * Obtener los datos en un array donde la primera columnas son los nombres y la segundo el valor
     * {{"column1","value"},
     * {"column2","value"}...)
     * @param url localizacion del recurso
     */
    public static String[][] getData(String url) throws Exception {
        String[][] strData;
        int ncolum,nrow;
        JSONObject json= new JSONObject(downloadData(url));//Descargo el archivo JSON
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        JSONArray tmp = json.getJSONArray("data");
        ncolum = tmp.getJSONObject(0).length()/2;
        nrow = tmp.length();
        strData = new String[nrow][ncolum];
        for(int n=0; n < nrow; n++){//Itero solo la cantidad de registros obtenidos correctament
            JSONObject row = tmp.getJSONObject(n);//Obtengo solo las columnas de un registro
            JSONArray results = row.names(); //Obtengo los nombres de las columnas
            for (int i=0,j=1; i < ncolum; i++,j+=2)
                strData[n][i] = row.getString(results.getString(j)); //Obtengo el valor de la columna
        }
        return  strData;
    }

    /**
     * Descargar todo el JSON
<<<<<<< HEAD
     * @param URL API LINK a descargar
     * @param med el metodo para llamar al URL (GET o POST)
     * @return devuelve el JSON en un string
     */
    private String downloadData(String URL, String med) throws InvalidKeyException, IOException, IllegalAccessException {
=======
     * @param URL localizacion del recurso
     */
    public static String downloadData(String URL) throws Exception {
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        BufferedReader in;
        StringBuffer response;
        String inputLine;

        HttpsURLConnection con = setHtpps(URL);
<<<<<<< HEAD
        con.setRequestMethod(med);
=======
        con.setRequestMethod("GET");
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {

            in = new BufferedReader( new InputStreamReader(con.getInputStream()));
            response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        else
<<<<<<< HEAD
            throw new IllegalAccessException("No se puede pueden obtener los datos de: "+URL);
    }

    /**
     * Se encarga de aceptar el certificado SSL autofirmado
     * @param urlString es la URL del servidor
     * @return la conexion establecida
     */
    private HttpsURLConnection setHtpps(String urlString) throws InvalidKeyException {
=======
            throw new Exception("No se puede pueden obtener los datos de: "+URL);
    }

    private static HttpsURLConnection setHtpps(String urlString) throws Exception {
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        try
        {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

<<<<<<< HEAD
            InputStream caInput = new BufferedInputStream(MainActivity.context.getAssets().open("certificado.cer"));
=======
            InputStream caInput = new BufferedInputStream(MainActivity.context.getAssets().open("server.cer"));
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
            Certificate ca = cf.generateCertificate(caInput);
            //System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            caInput.close();

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);

            URL url = new URL(urlString);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            urlConnection.setSSLSocketFactory(context.getSocketFactory());
            urlConnection.setHostnameVerifier( new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return urlConnection;
        }
        catch (Exception ex)
        {
<<<<<<< HEAD
            throw new InvalidKeyException("Error en el certificado");
=======
            throw new Exception("Error en el certificado");
>>>>>>> f61d18877efb9fc2cbfa1ab33c1305109b1437c0
        }
    }
}
