package analizadorSintactico.backend;

import analizadorLexico.frontend.Analizador;
import analizadorLexico.frontend.AreaTexto;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author fabricio
 */
public class Archivos {

    private Analizador analizador = null;
    private AreaTexto areaTexto = null;

    public Archivos(Analizador analizador, AreaTexto areaTexto) {
        this.analizador = analizador;
        this.areaTexto = areaTexto;
    }

    public void escribirArchivo() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("TXT, HTML", "txt", "html");
        fc.setFileFilter(filtro);

        //Abrimos la ventana, guardamos la opcion seleccionada por el usuario
        int seleccion = fc.showSaveDialog(analizador);

        //Si el usuario, pincha en aceptar
        if (seleccion == JFileChooser.APPROVE_OPTION) {
            //Seleccionamos el fichero
            File fichero = fc.getSelectedFile();
            //Ecribe la ruta del fichero seleccionado en el campo de texto
            System.out.println(fichero.getAbsolutePath());
            iniciarProceso(fichero);
        }
    }

    private void iniciarProceso(File file){
        AutomataPila ap = new AutomataPila(areaTexto.getMat().getListaTokens());
        ap.iniciarAPD();
        if (ap.getErrores().isEmpty()){
            crearArchivo(ap.getTextoSalida(), file);
        }
    }
    
    private void crearArchivo(String salida, File direccion){
        FileWriter fichero = null;
        PrintWriter pw = null;
        try {
            fichero = new FileWriter(direccion);
            pw = new PrintWriter(fichero);
            pw.println(salida);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(null != fichero){
                    fichero.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
