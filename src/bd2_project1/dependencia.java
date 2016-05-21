package bd2_project1;

/**
 *
 * @author elco45
 */
public class dependencia {
    private String ladoIzquierdo;
    private String ladoDerecho;
    
    public dependencia() {
    }

    public dependencia( String ladoIzquierdo,String ladoDerecho) {
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }

    public String getLadoDerecho() {
        return ladoDerecho;
    }

    public void setLadoDerecho(String ladoDerecho) {
        this.ladoDerecho = ladoDerecho;
    }

    public String getLadoIzquierdo() {
        return ladoIzquierdo;
    }

    public void setLadoIzquierdo(String ladoIzquierdo) {
        this.ladoIzquierdo = ladoIzquierdo;
    }

    @Override
    public String toString() {
        return "dependencia{" + "ladoDerecho=" + ladoDerecho + ", ladoIzquierdo=" + ladoIzquierdo + '}';
    }
    
    
}
