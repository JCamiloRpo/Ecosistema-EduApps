package com.example.euprofesor;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

public class ConexionFTPS {

    private FTPSClient client; // Objecto de la libreria
    private String ftps; //Se almacenará la direccion Ip
    private String user; //El usuario del servidor FTP
    private String pass; //La contraseña correspondiente al usuario

    /**
     * @param user Nombre de usuario.
     * @param pass Contrasena.
     * @param ftps Host a conectar.
     */
    public ConexionFTPS(String ftps, String user, String pass) {
        client = new FTPSClient();
        this.ftps = ftps;
        this.user = user;
        this.pass = pass;
    }

    /**
     * Establece una conexion SFTP.
     *
     */
    public void connect() throws Exception {
        if(isConnected())
            return;

        client.connect(ftps); //Metodo para conectar con el servidor
        if(!client.login(user,pass)) //Metodo para realizar el logeo
            throw new Exception("No se pudo conectar con el servidor SFTP.");

        client.execPBSZ(0);
        client.execPROT("P"); //Configurar nivel de proteccion

        client.setFileType(FTP.BINARY_FILE_TYPE);//Elegir el tipo de archivo(para subir o descargar)
        client.enterLocalPassiveMode(); //Entrar en modo pasivo
    }

    /**
     * Cierra la sesion SFTP.
     *
     * @throws IOException Cuando hay un error en cerrar la sesion.
     */
    public void disconnect() throws IOException {
        client.logout();
        client.disconnect();
    }

    /**
     * Sube un archivo al servidor FTP usando el protocolo SFTP.
     *
     * @param ftpPath  Ruta donde se agregará el archivo en el servidor.
     * @param fileName Nombre que tendra el archivo en el servidor.
     * @param localFile Ruta (incluye en nombre), en disco, del archivo a subir.
     *
     */
    public void addFile(String ftpPath, String fileName, String localFile) throws Exception{
        if(!isConnected())
            throw new Exception("Debe conectarse al servidor SFTP.");

        client.changeWorkingDirectory(ftpPath+"/");// Nos ubicamos en el directorio del FTP.

        InputStream tmp = new FileInputStream(localFile); //Crear un obtjeto del archivo a subir

        DecimalFormat format = new DecimalFormat("#.00");
        long tInicio = System.currentTimeMillis();// Metodo para iniciar el tiempo de ejecucion en milisegundos

        boolean r = client.storeFile(fileName,tmp); //Metodo para subir el archivo al servidor

        long tFinal = System.currentTimeMillis(); //Metodo para finalizar el tiempo de ejecucion en milisegundos
        long tDiferencia = tFinal - tInicio; // Se logra la diferiencia entre long inicio y long final
        double segundosTranscurridos = tDiferencia/1000.0; // Convertir a segundos
        float length= new File(localFile).length(); //Obtener el tamaño del archivo a enviar
        double lengthtotal = length/(1024*1024); //Convertir en MB
        double velocidad = lengthtotal/segundosTranscurridos; // Hallar velocidad
        double anchobanda = velocidad*8;// Hallar ancho de banda
        String[] args = {"Carga.txt","Reporte de cargar de: "+fileName,
                "Ancho de banda: "+format.format(anchobanda)+" Mb/s","Tamaño archivo: "+format.format(lengthtotal)+" MB",
                "Duracion: "+format.format(segundosTranscurridos)+" s","Velocidad promedio: "+format.format(velocidad)+" MB/s"};
        report(args);
        if(!r) throw new Exception("No se pudo subir el archivo: "+fileName+" al servidor FTPS");
    }

    /**
     * Descarga un archivo del servidor FTP usando el protocolo SFTP.
     *
     * @param localFile Ruta donde se descargará el archivo en el disco.
     * @param fileName Nombre que tendrá el archivo en el disco.
     * @param ftpPath Ruta (incluye en nombre), en el servidor, del archivo a  desacargar.
     *
     */
    public void downloadFile(String localFile, String fileName, String ftpPath)throws Exception {
        if(!isConnected())
            throw new Exception("Debe conectarse al servidor SFTP.");

        File folder = new File(localFile); //Se obbtiene el directorio donde se desea almacenar el archivo
        if(!folder.exists()) //Si no existe crea los directorios que necesita
            if(!folder.mkdirs())
                throw new Exception("No se pudo crear el directorio a descargar el archivo");

        OutputStream tmp = new FileOutputStream(localFile+"/"+fileName); //Crea el archivo que se guardará

        DecimalFormat format = new DecimalFormat("#.00");
        long tInicio = System.currentTimeMillis();// Metodo para iniciar el tiempo de ejecucion en milisegundos

        boolean r = client.retrieveFile(ftpPath,tmp); // Se guarda el archivo del servidor en el celular
        tmp.close();

        long tFinal = System.currentTimeMillis(); //Metodo para finalizar el tiempo de ejecucion en milisegundos
        long tDiferencia = tFinal - tInicio; // Se logra la diferiencia entre long inicio y long final
        double segundosTranscurridos = tDiferencia/1000.0; // Convertir a segundos
        float length= new File(localFile+"/"+fileName).length(); //Obtener el tamaño del archivo a enviar
        double lengthtotal = length/(1024*1024); //Convertir en MB
        double velocidad = lengthtotal/segundosTranscurridos; // Hallar velocidad
        double anchobanda = velocidad*8;// Hallar ancho de banda
        String[] args = {"Descarga.txt","Reporte de cargar de: "+fileName,
                "Ancho de banda: "+format.format(anchobanda)+" Mb/s","Tamaño archivo: "+format.format(lengthtotal)+" MB",
                "Duracion: "+format.format(segundosTranscurridos)+" s","Velocidad promedio: "+format.format(velocidad)+" MB/s"};
        report(args);
        if(!r) throw new Exception("No se pudo descargar el archivo: "+fileName+" del servidor FTPS");
    }

    /**
     * @param args Cada posicion corresponde a una linea a escribir en un archivo que se llama args[0]
     * @throws IOException
     */
    private void report(String[] args) throws IOException {
        String local ="storage/emulated/0/FTP/Reportes", name=args[0]; //El primer argumento corresponde al nombre del archivo que se va a escribir
        File folder = new File(local); //Se crea el objeto para crear los directorios necesarios
        if(!folder.exists())//Solo se crean si no existe
            if(!folder.mkdirs())
                return;
        File arch = new File(local+"/"+name);//Se crea el archivo en el directorio especificado
        if(!arch.exists())//Si no existe se crea el archivo
            if(!arch.createNewFile())
                return;
        BufferedWriter writer = new BufferedWriter(new FileWriter(arch,true));
        for(int i=1;i<args.length;i++)
            writer.write(args[i]+"\n");
        writer.write("\n");
        writer.close();
    }

    public boolean isConnected(){
        return client.isConnected();
    }
}
