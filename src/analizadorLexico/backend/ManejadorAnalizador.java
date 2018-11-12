package analizadorLexico.backend;

import analizadorLexico.frontend.AreaTexto;
import analizadorLexico.frontend.Analizador;
import analizadorLexico.frontend.archivos.GuardadorArchivo;
import analizadorLexico.backend.archivos.ManejadorArchivos;
import analizadorLexico.frontend.archivos.SeleccionadorArchivo;
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author fabricio
 */
public class ManejadorAnalizador {

    static final String NEW_TAB = "new tab";
    static final String TIPO_TXT = ".txt";
    private Analizador analizador = null;

    public ManejadorAnalizador(Analizador analizador) {
        this.analizador = analizador;
        
    }

    //Metodo encargado de abrir una ventana en blanco
    public void agregarVentana() {
        AreaTexto at = new AreaTexto();
        analizador.jTabbedPane.add(NEW_TAB, at);
        estadoGuardar();
    }

    //Metodo encargado de agregar una nueva ventana con el texto cargado colocando como
    //nombre de la ventana el path absoluta de tal
    public void agregarVentana(String path, String texto) {
        AreaTexto at = new AreaTexto();
        at.getjEditorPane1().setText(texto);
        analizador.jTabbedPane.add(path, at);
        at.getMat().iniciarAutomata();
    }

    //Metodo encargado de abrir una nueva ventana colocandole como texto inicial el seleccionado
    public void abrirDocumento(int i) {
        boolean error = false;
        SeleccionadorArchivo sa = new SeleccionadorArchivo(analizador, true);
        File archivo = null;
        sa.setVisible(true);
        if (sa.getFile() != null) {
            archivo = sa.getFile();
            if (archivo.getAbsolutePath().endsWith(TIPO_TXT)) {
                for (int j = 0; j < i; j++) {
                    if (analizador.jTabbedPane.getTitleAt(j).equals(archivo.getAbsolutePath())) {
                        JOptionPane.showMessageDialog(analizador, "El archivo ya se encuentra abierto", "Informacion", JOptionPane.WARNING_MESSAGE);
                        error = true;
                        break;
                    }
                }
                if (!error) {
                    try {
                        ManejadorArchivos ma = new ManejadorArchivos();
                        agregarVentana(archivo.getAbsolutePath(), ma.obtenerTextoDeArchivoEnHtml(archivo));
                    } catch (FileNotFoundException e) {
                        JOptionPane.showMessageDialog(analizador, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(analizador, "Unicamente se puede cargar archivo de texto \""
                        + TIPO_TXT + "\"", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    //Metodo encargado de cerrar una ventanva, verificando que se hayan guardado los cambios o no
    public void cerrarVentana(int ventana) {
        AreaTexto at = (AreaTexto) analizador.jTabbedPane.getSelectedComponent();
        if (at.isModificado()) {
            int respuesta = JOptionPane.showConfirmDialog(analizador, "¿Desea guardar los cambios?",
                    "Alerta!", JOptionPane.YES_NO_CANCEL_OPTION);
            if (respuesta == JOptionPane.NO_OPTION) {
                analizador.jTabbedPane.remove(ventana);
                estadoGuardar();
            } else if (respuesta == JOptionPane.YES_OPTION) {
                guardarArchivo(ventana);
                analizador.jTabbedPane.remove(ventana);
                estadoGuardar();
            }
        } else {
            analizador.jTabbedPane.remove(ventana);
            estadoGuardar();
        }
    }

    public void guardarArchivo(int i) {
        if (analizador.jTabbedPane.getTitleAt(i).equals(NEW_TAB)) {
            guardarArchivoComo(i, 1);
        } else {
            guardarComo(analizador.jTabbedPane.getTitleAt(i));
        }
    }

    public void guardarArchivoComo(int i, int opcion) {
        GuardadorArchivo ga = new GuardadorArchivo(analizador, true);
        ga.setVisible(true);
        if (ga.getFile() != null) {
            if (!ga.getFile().exists()) {
                guardarComo(ga.getFile().getAbsolutePath());
                if (opcion == 1) {
                    analizador.jTabbedPane.setTitleAt(i, ga.getFile().getAbsolutePath());
                }
            } else {
                JOptionPane.showMessageDialog(analizador, "No es posible guardar el documento, pues ya existe");
            }
        }
    }

    //Metodo que muestra un dialogo que contiene la informacion del desarrollador
    public void mostrarInformacionDesarrollador() {
        ImageIcon desarrollador = new ImageIcon("desarrollador.png");
        String informacion = "";
        informacion = "                     Analizador Lexico\n\n"
                + "                      Desarrollado por:\n"
                + "            Ivan Fabricio Racancoj García\n"
                + "                            201731115\n4to Semestre Ing. Sistemas CUNOC - USAC";
        JOptionPane.showMessageDialog(analizador, informacion, "About...", JOptionPane.INFORMATION_MESSAGE, desarrollador);
    }

    //Metodo encargado de guardar en un archivo con nombre del path recibido como parametro y escribiendo
    //en el documento el texto contenido en el jEditorPanel del la tabla actual o abierta
    private void guardarComo(String path) {
        File file = new File(path);
        AreaTexto at = (AreaTexto) analizador.jTabbedPane.getSelectedComponent();
        if (file != null) {
            try {
                ManejadorArchivos ma = new ManejadorArchivos();
                ma.escribirArchivoTexto(file, at.getMat().getPlainText());
                at.setModificado(false);
                JOptionPane.showMessageDialog(analizador, "Se ha guardado correctamente el archivo \"" + path
                        + "\"", "Accion exitosa", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Metodo que cambia el estado de los botones "guardar" y "guardar como" dependiendo de las tablas abiertas
    private void estadoGuardar() {
        boolean estado = true;
        if (analizador.jTabbedPane.getComponentCount() < 1) {
            estado = false;
        } else {
            estado = true;
        }
        analizador.jMenuItemSave.setEnabled(estado);
        analizador.jMenuItemSaveAs.setEnabled(estado);
        analizador.jMenuItemCloseTab.setEnabled(estado);
    }

    public void mostrarTokens() {
        AreaTexto at = (AreaTexto) analizador.jTabbedPane.getSelectedComponent();
        at.mostrarTokens();
    }

    public void buscarCadena() {
        String respuesta = JOptionPane.showInputDialog(analizador, "Escriba la cadena "
                + "a buscar", "Busqueda", JOptionPane.QUESTION_MESSAGE);
        if (respuesta != null) {
            if (!respuesta.isEmpty()) {
                AreaTexto at = (AreaTexto) analizador.jTabbedPane.getSelectedComponent();
                at.getMat().subrayarTexto(respuesta);
            }
        }
    }

    public void cerrarVentanas(int tamaño) {
        for (int i = tamaño - 1; i >= 0; i--) {
            cerrarVentana(i);
        }
        if (analizador.getComponentCount() <= 0) {
            System.exit(0);
        }
    }

    public void habilitarAPD(){
        if(analizador.jTabbedPane.getComponentCount() > 0){
            analizador.jMenuItemIniciarAPD.setEnabled(true);
        } else {
            analizador.jMenuItemIniciarAPD.setEnabled(false);
        }
    }
    
}
