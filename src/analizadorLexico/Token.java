package analizadorLexico;

/**
 *
 * @author fabricio
 */
public class Token {

private TipoToken tipoToken;
private String lexema;
private int colummna;
private int fila;

    public Token(TipoToken tt, String lexema, int colummna, int fila) {
        this.tipoToken = tt;
        this.lexema = lexema;
        this.colummna = colummna;
        this.fila = fila;
    }

    public TipoToken getTipoToken() {
        return tipoToken;
    }

    public void setTipoToken(TipoToken tt) {
        this.tipoToken = tt;
    }

    public String getLexema() {
        return lexema;
    }

    public void setLexema(String lexema) {
        this.lexema = lexema;
    }

    public int getColummna() {
        return colummna;
    }

    public void setColummna(int colummna) {
        this.colummna = colummna;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }



}
