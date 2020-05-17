import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;


public class prueba {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ConexionSCORM mySCORM;
			
			String path = "C:\\Users\\Administrador\\Documents\\Java\\PruebaSCORM\\MySCORM",
					tituloGeneral = "Mi primer SCORM",
					descripGeneral = "Se genera el paquete SCORM desde codigo",
					tituloSesion = "Titulo de la sesion",
					descripSesion = "Descripcion de la sesion";
		
			String idActividad1 = "Actividad1",
					tituloActividad1 = "Esta es la actividad 1",
					descripActividad1 = "Esta actividad contiene un audio",
					idActividad2 = "Actividad2",
					tituloActividad2 = "Esta es la actividad 2",
					descripActividad2 = "Esta actividad contiene un video",
					idActividad3 = "Actividad3",
					tituloActividad3 = "Esta es la actividad 3",
					descripActividad3 = "Esta actividad contiene un apk y un documento",
					idActividad4 = "Actividad4",
					tituloActividad4 = "Esta es la actividad 4",
					descripActividad4 = "Esta actividad contiene una imagen, un apk y un documento";
			
			List<ConexionSCORM.Recurso> recursos1 = new ArrayList<>(Arrays.asList(
					new ConexionSCORM.Recurso("Mi audio","Escuchar el siguiente audio", "content/act1/audio.mp3","audio/mp3","My audio.mp3")));	
			List<ConexionSCORM.Recurso> recursos2 = new ArrayList<>(Arrays.asList(
					new ConexionSCORM.Recurso("Mi video","Ver el siguiente video", "content/video.mp4","video/mp4","My video.mp4")));
			List<ConexionSCORM.Recurso> recursos3 = new ArrayList<>(Arrays.asList(
					new ConexionSCORM.Recurso("Mi app","Descargar la siguiente app", "content/app.apk","app/apk","My app.apk"),
					new ConexionSCORM.Recurso("Mi documento","Llenar el siguiente documento", "content/documento.docx","dcumento/docx","My document.docx")));
			List<ConexionSCORM.Recurso> recursos4 = new ArrayList<>(Arrays.asList(
					new ConexionSCORM.Recurso("Mi imagen","Ver la siguiente imagen", "content/imagen.png","imagen/jpg","My imagen.png"),
					new ConexionSCORM.Recurso("Mi app","Descargar la siguiente app", "content/app.apk","app/apk","My app.apk"),
					new ConexionSCORM.Recurso("Mi documento","Llenar el siguiente documento", "content/documento.docx","dcumento/docx","My document.docx")));
			
			mySCORM = new ConexionSCORM(tituloGeneral, descripGeneral, path);
			mySCORM.setInfo(tituloSesion, descripSesion);
			mySCORM.addActividad(idActividad1, tituloActividad1, descripActividad1, recursos1);
			mySCORM.addActividad(idActividad2, tituloActividad2, descripActividad2, recursos2);
			mySCORM.addActividad(idActividad3, tituloActividad3, descripActividad3, recursos3);
			mySCORM.addActividad(idActividad4, tituloActividad4, descripActividad4, recursos4);
			mySCORM.generar();
			
			System.out.println("Se ha generado su paquete SCORM");
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
