package com.example.euestudiante;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

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
     * @param URL localizacion del recurso
     */
    public static String downloadData(String URL) throws Exception {
        BufferedReader in;
        StringBuffer response;
        String inputLine;

        HttpsURLConnection con = setHtpps(URL);
        con.setRequestMethod("GET");
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
            throw new Exception("No se puede pueden obtener los datos de: "+URL);
    }

    private static HttpsURLConnection setHtpps(String urlString) throws Exception {
        try
        {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            InputStream caInput = new BufferedInputStream(MainActivity.context.getAssets().open("server.cer"));
            Certificate ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

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
            throw new Exception("Error en el certificado");
        }
    }
}
