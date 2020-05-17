package com.example.euprofesor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.*;

/**
 * Clase que se encarga de crear el SCORM con dos paginas html
 * index.html que es la principal a mostrar
 * content.html es la pagina donde se muestran las actividades con sus recursos
 * las constantes tienen la plantilla necesario para realizar la construccionn de la pagina
 * los elementos que se agregan una o mas veces son las etiquetas que en la plantilla estan comentariadas
 * Ex: <!--etiqueta-->
 * por lo que dicha etiqueta se reemplaza por la etiqueta ya construida
 * Tambien, estan los elementos que se cambian una vez que son items dentro de la etiqueta
 * Ex: [elemento]
 * por lo que se debe reemplazar los elementos por lo que se desea mostrar
 */
public class ConexionSCORM {

	private final String index = "<!DOCTYPE html>\r\n" +
			"<html>\r\n" + 
			"<head>\r\n" + 
			"    <meta charset='utf-8'>\r\n" + 
			"    <title>Inicio</title>\r\n" + 
			"    <meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'>\r\n" + 
			"    <link rel='shortcut icon' href='img/icon.svg'>\r\n" + 
			"    <link href='components/jquery/css/custom/jquery-ui.min.css' rel='stylesheet' type='text/css'>\r\n" + 
			"    <link href='css/scormplayer.css' rel='stylesheet' type='text/css'>\r\n" +
			"    <script src='components/jquery/jquery.min.js'></script>\r\n" + 
			"    <script src='components/jquery/jquery-ui.min.js'></script>\r\n" + 
			"    <script src='js/app.js'></script>\r\n" + 
			"    <script src='js/lang.es.js'></script>\r\n" + 
			"    <script src='components/scorm/player.js'> </script>\r\n" +
			"</head>\r\n" + 
			"<body data-autoload='false' data-display-window='modal' data-window-width='100' data-window-height='100'>\r\n" + 
			"    <div id='body'>\r\n" + 
			"        <div id='content'>\r\n" + 
			"            <h1> [titulo] </h1>\r\n" + 
			"            <p>\r\n" + 
			"                [descripcion] \r\n" + 
			"            </p>\r\n" + 
			"            <div id='play_scorm'></div>\r\n" + 
			"        </div>\r\n" + 
			"    </div>\r\n" + 
			"</body>\r\n" + 
			"</html>\r\n";
	
	private Content content;
	private String titulo, descripcion, path;

	/**
	 * @param titulo  de la pagina principal
	 * @param descripcion de la pagina principal
	 * @param path donde se encuentra el paquete SCORM (la estructura inicial)
	 */
	public ConexionSCORM(String titulo, String descripcion,String path) {
		this.titulo = titulo;
		this.descripcion = descripcion;
		this.path = path;
	}

	/**
	 * Información de la pagina principal de las actividades
	 * @param titulo general
	 * @param descripcion general
	 */
	public void setInfo(String titulo, String descripcion) {
		content = new Content(titulo, descripcion);
	}

	/**
	 * Los elementos para construir la actividad en la pagina
	 * Subclase Actividad
	 * @param id identificador, internamente son para las etiquetas
	 * @param titulo se muestra para identificar la actividad
	 * @param descactividad descripcion de lo que se debe hacer
	 * @param recursos la lista de los recusos que tiene la actividad (Subclase Recurso)
	 */
	public void addActividad(String id, String titulo, String descactividad, List<Recurso> recursos) {
		content.actividades.add(new Actividad(id,titulo,descactividad,recursos));
	}

	/**
	 * Metodo para crear las paginas html y deja el paquete comprimido, listo para subir
	 * @return retorna el path del .zip del SCORM
	 * @throws FileNotFoundException Si no se encuentran los archivos en la carpeta SCORM
	 * @throws ZipException Cualquier excepcion al comprimir el paquete
	 */
	public String generar(String id) throws FileNotFoundException, ZipException {
		//Inicializar las variables con la plantilla
		String index = this.index,
				html = content.html,
				head = content.head,
				body = content.body, 
				header = content.header, 
				menu = content.menu, 
				main = content.main, 
				section = content.section, 
				actividad = content.actividad, 
				recurso = content.recursootro,
				footer = content.footer,
				extra = content.extra;
		
		//Adecuar index.html
		index = index.replace("[titulo]", titulo);
		index = index.replace("[descripcion]", descripcion);
		
		//Adecuar content.html
		for (Actividad i : content.actividades) {
			actividad = content.actividad;
			menu = content.menu;
			//Agregar la informacion del elemento menu para la actividad i
			menu = menu.replace("[actividad]", i.titulo);
			menu = menu.replace("[idactividad]", i.id);
			//Agregar la informacion de la seccion de la actividad i
			actividad = actividad.replace("[idactividad]", i.id);
			actividad = actividad.replace("[actividad]", i.titulo);
			actividad = actividad.replace("[descactividad]", i.descactividad);
			for(Recurso j : i.recursos) {
				//Adecuar segun el tipo de recurso
				switch (j.type.split("/")[0]) {
				case "audio":
					recurso = content.recursoaudio;
					break;
				case "video":
					recurso = content.recursovideo;
					break;
				case "imagen":
					recurso = content.recursoimagen;
					break;
				default:
					recurso = content.recursootro;
					break;
				}
				//Llenar el recurso j con su informacion
				recurso = recurso.replace("[recurso]", j.titulo);
				recurso = recurso.replace("[descrecurso]", j.descrecurso);
				recurso = recurso.replace("[path]", j.path);
				recurso = recurso.replace("[type]", j.type);
				recurso = recurso.replace("[name]", j.name);
				//Agregar el recurso j a la actividad i (se vuelve agregar la etiqueta recurso para seguir añadiendo)
				actividad = actividad.replace("<!--recurso-->", recurso+" <!--recurso--> \r\n");
			}
			//Se agrega el menu de la actividad i para que aparezca en la barra de navegacion
			header = header.replace("<!--item-->", menu+" <!--item--> \r\n");
			//Agregar la seccion de la actividad i que ya tiene toda su informacion para ser mostrada
			main = main.replace("<!--sectionactividad-->",actividad+" <!--sectionactividad--> \r\n");
		}
		//Se llena la informacion de la seccion principal
		section = section.replace("[titulo]", content.titulo);
		section = section.replace("[descripcion]", content.descripcion);
		//Se agrega la seccion principal para mostrar
		main = main.replace("<!--sectioninicio-->", section);
		//Se agregan los elementos en su orden que van construyendo la pagina de forma incremental
		body = body.replace("<!--header-->", header);
		body = body.replace("<!--main-->", main);
		body = body.replace("<!--footer-->", content.footer);
		body = body.replace("<!--extra-->", content.extra);
		html = html.replace("<!--head-->", content.head);
		html = html.replace("<!--body-->", body);
		
		//Crear el archivo index.html y escribir el contenido
		PrintWriter indexhtml = new PrintWriter(new File(path+"/index.html"));
		indexhtml.print(index);
		indexhtml.close();
		
		//Crear el archivo content.html y escribir el contenido
		PrintWriter contenthtml = new PrintWriter(new File(path+"/content.html"));
		contenthtml.print(html);
		contenthtml.close();
		
		//Generar el .zip
		String scorm = path;
		String scormZip = path+id+".zip";
		convertToZip(scorm, scormZip);
		
		return scormZip;
	}

	/**
	 * Metodo para comprimir cualquier carpeta utilizando la libreria Zip4j
	 * @param input la ubicacion de la carpeta a comprimir
	 * @param output la ubicacion donde quedara la carpeta comprimida
	 * @throws ZipException Cualquier excepcion al comprimir
	 */
	public void convertToZip(String input, String output) throws ZipException {
		ZipFile zip = new ZipFile(output);               
	    ZipParameters parameters = new ZipParameters();
        parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
        parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);       
	    zip.addFolder(input, parameters);
	}

	/**
	 * Clase interna privada que contiene toda la plantilla de la pagina content.html y su informacion principal
	 */
	private class Content{

		private final String html = "<!DOCTYPE html>\r\n" + 
				"<html>\r\n" + 
				"<!--head--> \r\n" + 
				"<!--body--> \r\n" +
				"</html>";
		private final String head = "<head>\r\n" + 
				"    <title>Actividades de la sesion</title>\r\n" + 
				"\r\n" + 
				"\r\n" + 
				"    <meta charset='utf-8'>\r\n" + 
				"    <meta name='viewport' content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no'>\r\n" + 
				"\r\n" + 
				"    <link rel='shortcut icon' href='img/icon.svg'>\r\n" + 
				"    <link href='components/jquery/css/custom/jquery-ui.min.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/ionicons/css/ionicons.min.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/pit/css/jpit_quiz.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/pit/css/jpit_mark.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/pit/css/jpit_wordpuzzle.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/pit/css/jpit_crossword.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/pit/css/jpit_zoom.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link href='components/csscircle/circle.min.css' rel='stylesheet' type='text/css'>\r\n" + 
				"    <link rel='stylesheet' href='components/twentytwenty/css/twentytwenty.css' type='text/css' media='screen' />\r\n" + 
				"    <link rel='stylesheet' href='components/mediaelementjs/mediaelementplayer.css' type='text/css' />\r\n" + 
				"\r\n" + 
				"    <script src='components/jquery/jquery.min.js'></script>\r\n" + 
				"    <script src='components/jquery/jquery-ui.min.js'></script>\r\n" + 
				"    <script type='text/javascript' src='components/jquery-mobile/jquery.ui.touch-punch.min.js'></script>\r\n" + 
				"    <script src='components/twentytwenty/js/jquery.event.move.js' type='text/javascript'></script>\r\n" + 
				"    <script src='components/twentytwenty/js/jquery.twentytwenty.js' type='text/javascript'></script>\r\n" + 
				"    <script src='components/mediaelementjs/mediaelement-and-player.min.js' type='text/javascript'></script>\r\n" + 
				"    <script src='components/maphilight/jquery.maphilight.min.js' type='text/javascript'></script>\r\n" + 
				"    <script src='components/interact/interact.min.js'></script>\r\n" + 
				"    <script src='components/pit/jpit_api.js'></script>\r\n" + 
				"    <script src='components/pit/utilities/jpit_utilities.js'></script>\r\n" + 
				"    <script src='components/pit/activity/jpit_activity.js'></script>\r\n" + 
				"    <script src='components/pit/resources/jpit_resource.js'></script>\r\n" + 
				"    <script src='components/pit/activity/quiz/jpit_activity_quiz.js'></script>\r\n" + 
				"    <script src='components/pit/activity/mark/jpit_activity_mark.js'></script>\r\n" + 
				"    <script src='components/pit/activity/wordpuzzle/jpit_activity_wordpuzzle.js'></script>\r\n" + 
				"    <script src='components/pit/activity/droppable/jpit_activity_droppable.js'></script>\r\n" + 
				"    <script src='components/pit/activity/crossword/jpit_activity_crossword.js'></script>\r\n" + 
				"    <script src='components/pit/activity/cloze/jpit_activity_cloze.js'></script>\r\n" + 
				"    <script src='components/pit/activity/form/jpit_activity_form.js'></script>\r\n" + 
				"    <script src='components/pit/activity/sortable/jpit_activity_sortable.js'></script>\r\n" + 
				"    <script src='components/pit/activity/check/jpit_activity_check.js'></script>\r\n" + 
				"    <script src='components/pit/resources/movi/jpit_resource_movi.js'></script>\r\n" + 
				"    <script src='components/pit/resources/zoom/jpit_resource_zoom.js'></script>\r\n" + 
				"    <script src='js/app.js'></script>\r\n" + 
				"    <script src='js/init.js'></script>\r\n" + 
				"    <script src='js/lang.es.js'></script>\r\n" + 
				"    <script src='js/lib.js'></script>\r\n" + 
				"    <script src='js/mobilelib.js'></script>\r\n" + 
				"    <script src='js/stories.js'></script>\r\n" + 
				"    <script src='components/scorm/SCORM_12_APIWrapper.js'> </script>\r\n" + 
				"    <script src='components/scorm/scorm_api.js'> </script>\r\n" + 
				"    \r\n" + 
				"    <style type='text/css'>\r\n" + 
				"\r\n" + 
				"        body {\r\n" + 
				"            background-color: rgba(242, 242, 242, 0.7);\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        body.loading {\r\n" + 
				"            text-align: center;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        body.loading:after {\r\n" + 
				"            content: ';\r\n" + 
				"            background: url(img/loading.gif) no-repeat bottom center;\r\n" + 
				"            width: 130px;\r\n" + 
				"            height: 30px;\r\n" + 
				"            display: block;\r\n" + 
				"            margin: auto;\r\n" + 
				"            padding-bottom: 20px;\r\n" + 
				"        }\r\n" + 
				"\r\n" + 
				"        body.loading #body {\r\n" + 
				"            display: none;\r\n" + 
				"        }\r\n" + 
				"    </style>\r\n" + 
				"</head>";
		private final String body = "<body class='loading' data-debug='true' data-model='scorm' data-display-mode='onlypages' data-approve-limit='70' data-activities-percentaje='60' data-mobile-mode='512'>\r\n" + 
				"    <div id='body' class='not_print'>\r\n" + 
				"        <div id='not_scorm_msg' style='display: none;'></div>\r\n" + 
				"        <!--header--> \r\n" + 
				"        <!--main--> \r\n" + 
				"        <!--footer--> \r\n" + 
				"    </div>\r\n" + 
				"    <!--extra--> \r\n" + 
				"</body>";
		private final String header = "<header>\r\n" + 
				"            <h1>Actividades de la sesion</h1>\r\n" + 
				"            <nav label='Principal' class='horizontal main' data-offset='true'>\r\n" + 
				"                <menu>\r\n" + 
				"                    <menuitem label='Presentacion' data-page='pag-inicio'></menuitem>\r\n" + 
				"                    <!--item--> \r\n" + 
				"                </menu>\r\n" + 
				"            </nav>\r\n" + 
				"            <h2 class='page-title'></h2>\r\n" + 
				"        </header>";
		private final String menu = "<menuitem label=' [actividad] ' data-page='[idactividad]'></menuitem>\r\n" + 
				"                    ";
		private final String main = "<main style='display: none;'>\r\n" + 
				"			<!--sectioninicio--> \r\n" + 
				"            <!--sectionactividad--> \r\n" + 
				"        </main>";
		private final String section = "<section id='pag-inicio'>\r\n" + 
				"                <div class='subpage'>\r\n" + 
				"                    <h3> [titulo] </h3>\r\n" + 
				"                    <p>\r\n" + 
				"                        [descripcion] \r\n" + 
				"                    </p>\r\n" + 
				"                </div>\r\n" + 
				"            </section>";
		private final String actividad = "<section id='[idactividad]'>\r\n" + 
				"                <div class='subpage'>\r\n" + 
				"                    <h3> [actividad] </h3>\r\n" + 
				"                    <p>\r\n" + 
				"                    	[descactividad] \r\n" + 
				"                    </p>\r\n" + 
				"                    <!--recurso--> \r\n" + 
				"                    <div class='instruction' type='info'>Haga clic en boton para finalizar la actividad</div>\r\n" + 
				"                    <div class='left'>\r\n" + 
				"                        <span title='Finalizar' class='icon_large icon_link icon_green ion-checkmark-circled jpit-activities-view' data-act-id='[idactividad]' data-act-title='[actividad]'></span>\r\n" +
				"                    </div>\r\n" + 
				"                </div>\r\n" + 
				"            </section> \r\n" + 
				"             ";
		private final String recursoaudio = "<div class='box-text important' label=' [recurso] '>\r\n" +
				"                        <p>\r\n" +
				"                            [descrecurso] \r\n" +
				"                        </p>\r\n" +
				"                        <p>\r\n" +
				"                            <audio controls>\r\n" +
				"                                <source src=' [path] ' type=' [type] '>\r\n" +
				"                                Su navegador no soporta el componente de audio\r\n" +
				"                            </audio>\r\n" +
				"                            <div class='about'>\r\n" +
				"	                            <a href='[path]' download='[name]'>\r\n" +
				"									Descargar recurso\r\n" +
				"								</a>\r\n" +
				"                            </div>\r\n" +
				"                        </p>\r\n" +
				"                    </div> \r\n";
		private final String recursovideo = "<div class='box-text important' label=' [recurso] '>\r\n" +
				"                        <p>\r\n" +
				"                            [descrecurso] \r\n" +
				"                        </p>\r\n" +
				"                        <p>\r\n" +
				"                            <div class='videobox'>\r\n" +
				"                                <video controls>\r\n" +
				"                                    <source src=' [path] ' type=' [type] '>\r\n" +
				"                                    El reproductor de video no es soportado por el navegador\r\n" +
				"                               	</video>\r\n" +
				"                            </div>\r\n" +
				"                            <div class='about'>\r\n" +
				"	                            <a href='[path]' download='[name]'>\r\n" +
				"									Descargar recurso\r\n" +
				"								</a>\r\n" +
				"                            </div>\r\n" +
				"                        </p>\r\n" +
				"                    </div> \r\n";
		private final String recursoimagen = "<div class='box-text important' label=' [recurso] '>\r\n" +
				"                        <p>\r\n" +
				"                            [descrecurso] \r\n" +
				"                        </p>\r\n" +
				"                        <p>\r\n" +
				"                            <div class='expand-image' data-src=' [path] ' title=' [name] '></div>\r\n" +
				"                            <div class='about'>\r\n" +
				"	                            <a href='[path]' download='[name]'>\r\n" +
				"									Descargar recurso\r\n" +
				"								</a>\r\n" +
				"                            </div>\r\n" +
				"                        </p>\r\n" +
				"                    </div>\r\n" +
				"                    ";
		private final String recursootro = "<div class='box-text important' label=' [recurso] '>\r\n" +
				"                        <p>\r\n" +
				"                            [descrecurso] \r\n" +
				"                        </p>\r\n" +
				"                        <p>\r\n" +
				"                            <div class='about'>\r\n" +
				"	                            <a href='[path]' download='[name]'>\r\n" +
				"									Descargar recurso\r\n" +
				"								</a>\r\n" +
				"                            </div>\r\n" +
				"                        </p>\r\n" +
				"                    </div>\r\n" +
				"                    ";
		private final String footer = "<footer class='not_print'>\r\n" +
				"            <div class='measuring-progress'></div>\r\n" + 
				"\r\n" + 
				"            <nav label='Caracter�sticas globales' class='globals'>\r\n" + 
				"                <menu>\r\n" + 
				"                    <menuitem data-global-id='results'><i title='Progreso' class='ion-clipboard tooltip' data-position-at='top'></i></menuitem>\r\n" + 
				"                    <menuitem data-global-id='return'>\r\n" + 
				"                        <i title='Maximizar' class='ion-arrow-expand tooltip maximize' data-position-at='top'></i>\r\n" + 
				"                        <i title='Minimizar' class='ion-arrow-shrink tooltip minimize' data-position-at='top'></i>\r\n" + 
				"                    </menuitem>\r\n" + 
				"                    <menuitem data-global-id='close_all'><i title='Regresar' class='ion-reply tooltip' data-position-at='top'></i></menuitem>\r\n" + 
				"                </menu>\r\n" + 
				"            </nav>\r\n" + 
				"\r\n" + 
				"            <div id='subpages_menu'>\r\n" + 
				"                <div class='button' previous-page></div>\r\n" + 
				"                <ul class='button' subpages-menu></ul>\r\n" + 
				"                <div class='button' next-page></div>\r\n" + 
				"            </div>\r\n" + 
				"\r\n" + 
				"            <div id='page_number'></div>\r\n" + 
				"        </footer>";
		private final String extra = "<div id='results_page' style='display:none;'>\r\n" + 
				"        <h2>Resumen de su avance en la lectura del documento</h2>\r\n" + 
				"        <h3>Paginas visitadas</h3>\r\n" + 
				"        <p>En verde, aquellas paginas que ya ha visitado.</p>\r\n" + 
				"        <div id='results_page_visited' class='container'></div>\r\n" + 
				"        <br class='clear' />\r\n" + 
				"        <h3>Actividades de aprendizaje</h3>\r\n" + 
				"        <div id='results_page_activities'></div>\r\n" + 
				"    </div>\r\n" + 
				"\r\n" + 
				"    <div id='printent_content' style='display:none;'>\r\n" + 
				"        <div class='content'></div>\r\n" + 
				"        <div class='button_container'>\r\n" + 
				"            <button class='general button' onclick='window.print();'>Imprimir</button>\r\n" + 
				"            <button id='printent_back' class='general button' >Volver</button>\r\n" + 
				"        </div>\r\n" + 
				"    </div>";
		
		private String titulo, descripcion;
		private List<Actividad> actividades;

		/**
		 * Se agrega la informacion de la ventana principal de las actividades
		 * @param titulo de la ventana principal de las actividades
		 * @param descripcion de la ventana principal de las actividades
		 */
		public Content(String titulo, String descripcion) {
			this.titulo = titulo;
			this.descripcion = descripcion;
			actividades = new ArrayList<>();
		}
	}

	/**
	 * Clase interna para el manejo de las actividades a mostrar
	 */
	private static class Actividad{
		
		private String id, titulo, descactividad;
		private List<Recurso> recursos;

		/**
		 * Informacion de la actividad
		 * @param id identificador para las etiquetas
		 * @param titulo a mostrar en la seccion
		 * @param descactividad descripcion de la actividad
		 * @param recursos todos los recursos de la actividad
		 */
		public Actividad(String id, String titulo, String descactividad, List<Recurso> recursos) {
			this.id = id;
			this.titulo = titulo;
			this.descactividad = descactividad;
			this.recursos = recursos;
		}
		
	}

	/**
	 * Clase interna para el manejo de la información de los recursos para mostrar
	 */
	public static class Recurso{
		
		private String titulo, descrecurso, path, type, name;

		/**
		 * Toda la informacion para crear la etiqueta del recurso
		 * @param titulo a mostrar
		 * @param descrecurso descripcion del recurso
		 * @param path para que funcione debe estar en la carpeta content del paquete SCORM
		 *             se pasa la ruta /content/name.ext
		 * @param type los tipos a mostrar son: audio/* , video/* , imagen/* ...
		 * @param name el nombre que se desea dar al momento de descargar el recurso
		 */
		public Recurso(String titulo, String descrecurso, String path, String type, String name) {
			this.titulo = titulo;
			this.descrecurso = descrecurso;
			this.path = path;
			this.type = type;
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
