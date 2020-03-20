package com.example.apppruebarendimiento;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
        return r;
    }

    public boolean Descargar(String remote, String local) throws IOException {
        OutputStream out = new FileOutputStream(local);
        boolean r = Client.retrieveFile(remote,out);
        out.close();
        return r;
    }

    public void Reporte(){

    }

    public String getStatus() throws IOException {
        return Client.getStatus();
    }

    public boolean isConnected(){
        return Client.isConnected();
    }
}
