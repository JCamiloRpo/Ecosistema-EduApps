package com.example.apppruebarendimiento;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConexionFTP {
    FTPClient Client; // Objecto de la libreria
    String Ftp; //Se almacenará la direccion Ip
    String User; //El usuario del servidor FTP
    String Pas; //La contraseña correspondiente al usuario

    public ConexionFTP(String ftp, String user, String pas){
        Ftp = ftp;
        User = user;
        Pas = pas;
        Client = new FTPClient();
    }

    public String getFtp() {
        return Ftp;
    }

    public String getUser() {
        return User;
    }

    public String getPas() {
        return Pas;
    }

    public void setFtp(String ftp) {
        Ftp = ftp;
    }

    public void setUser(String user) {
        User = user;
    }

    public void setPas(String pas) {
        Pas = pas;
    }

    public boolean Conectar() throws IOException {
        Client.connect(Ftp); //Metodo para conectar con el servidor
        Client.changeWorkingDirectory("/");
        return Client.login(User,Pas); //Metodo para realizar el logeo
    }

    public boolean Desconectar() throws IOException {
        boolean r =Client.logout(); //Metodo para deslogearse
        Client.disconnect(); //Metodo para desconectar
        return r;
    }

    public boolean Cargar(File file) throws IOException {
        Client.setFileType(FTP.BINARY_FILE_TYPE); //Elegir el tipo de archivo a cargar
        Client.enterLocalPassiveMode(); //Entrar en modo pasivo
        InputStream fis = new FileInputStream(file.getAbsolutePath()); //Crear un obtjeto del archivo a subir
        // Guardando el archivo en el servidor
        boolean r = Client.storeFile(file.getName(),fis); //Metodo para subir el archivo al servidor
        float length= file.length(); //Obtener el tamaño del archivo a enviar
        length /=(1024*1024); //Convertir en MB
        String[] args = {"Carga.txt","Reporte de cargar","Ancho de banda: ","Tamaño archivo: "+length+" MB","Duracion: "+" s","Velocidad promedio: "+" MB/s"};
        Reporte(args);
        return r;
    }

    public boolean Descargar(String remote, String local, String name) throws IOException {
        File folder = new File(local); //Se recibe el directorio donde se desea almacenar el archivo
        if(!folder.exists()) //Si no existe crea los directorios que necesita
            if(!folder.mkdirs())
                return false;
        OutputStream out = new FileOutputStream(local+"/"+name); //Crea el archivo que se guardará
        boolean r = Client.retrieveFile(remote,out); // Se guarda el archivo del servidor en el celular
        out.close();
        float length= new File(local+"/"+name).length(); //Obtener el tamaño del archivo que se descargo
        length /=(1024*1024); //Convertir en MB
        String[] args = {"Descarga.txt","Reporte de descarga","Ancho de banda: ","Tamaño archivo: "+length+" MB","Duracion :"+" s" ,"Velocidad promedio: "+" MB/s"};
        Reporte(args);
        return r;
    }

    public void Reporte(String[] args) throws IOException {
        String local ="storage/emulated/0/FTP/Reportes", name=args[0]; //El primer argumento corresponde al nombre del archivo que se va a escribir
        File folder = new File(local); //Se crea el objeto para crear los directorios necesarios
        if(!folder.exists())//Solo se crean si no existe
            if(!folder.mkdirs())
                return;
        File arch = new File(local+"/"+name);//Se crea el archivo en el directorio especificado
        if(!arch.exists())//Si no existe se crea el archivo
            if(!arch.createNewFile())
                return;
        BufferedWriter bw = new BufferedWriter(new FileWriter(arch,true));
        for(int i=1;i<args.length;i++)
            bw.write(args[i]+"\n");
        bw.write("\n");
        bw.close();
    }

    public String getStatus() throws IOException {
        return Client.getStatus();
    }

    public boolean isConnected(){
        return Client.isConnected();
    }
}
