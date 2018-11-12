package analizadorLexico;

/**
 *
 * @author fabricio
 */
public class ErrorLexema {

private String nombreLexema;
private int columna;
private int fila;

    public ErrorLexema(String nombreLexema, int columna, int fila) {
        this.nombreLexema = nombreLexema;
        this.columna = columna;
        this.fila = fila;
    }

    public String getNombreLexema() {
        return nombreLexema;
    }

    public void setNombreLexema(String nombreLexema) {
        this.nombreLexema = nombreLexema;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }
    
}
