import java.util.LinkedList;
import java.util.List;

public class GrafoLA{
    NodoGrafo origen;

    public GrafoLA() {
    }

    public void inicializarGrafo() {
        this.origen = null;
    }

    public void agregarVertice(String v, int horaInicio, int horaFin) {
        NodoGrafo aux = new NodoGrafo();
        aux.nodo = v;
        aux.arista = null;
        aux.sigNodo = this.origen;
        aux.horaInicio = horaInicio;
        aux.horaFin = horaFin;
        this.origen = aux;
    }

    public void agregarArista(String v1, String v2, int tiempo, Double distancia) {
        NodoGrafo n1 = this.Vert2Nodo(v1);
        NodoGrafo n2 = this.Vert2Nodo(v2);
        if(existeArista(v1, v2)){
            GrafoLA.NodoArista aux1;
            for(aux1 = n1.arista; !aux1.nodoDestino.nodo.equals(v2) ; aux1 = aux1.sigArista){
            }
            aux1.distancias.add(distancia);
            aux1.tiempos.add(tiempo);
        }
        else {
            GrafoLA.NodoArista aux = new GrafoLA.NodoArista();
            aux.distancias.add(distancia);
            aux.tiempos.add(tiempo);
            aux.nodoDestino = n2;
            aux.sigArista = n1.arista;
            n1.arista = aux;
        }
        
    }

    private NodoGrafo Vert2Nodo(String v) {
        NodoGrafo aux;
        for(aux = this.origen; aux != null && !aux.nodo.equals(v); aux = aux.sigNodo) {
        }
        return aux;
    }

    public void eliminarVertice(String v) {
        if (this.origen.nodo == v) {
            this.origen = this.origen.sigNodo;
        }

        for(NodoGrafo aux = this.origen; aux != null; aux = aux.sigNodo) {
            this.EliminarAristaNodo(aux, v);
            if (aux.sigNodo != null && aux.sigNodo.nodo == v) {
                aux.sigNodo = aux.sigNodo.sigNodo;
            }
        }
    }

    private void EliminarAristaNodo(NodoGrafo nodo, String v) {
        GrafoLA.NodoArista aux = nodo.arista;
        if (aux != null) {
            if (aux.nodoDestino.nodo == v) {
                nodo.arista = aux.sigArista;
            } else {
                while(aux.sigArista != null && aux.sigArista.nodoDestino.nodo != v) {
                    aux = aux.sigArista;
                }

                if (aux.sigArista != null) {
                    aux.sigArista = aux.sigArista.sigArista;
                }
            }
        }

    }
    public List<NodoGrafo> verticesN() {
        List<NodoGrafo> c = new LinkedList<>();
        for(NodoGrafo aux = this.origen; aux != null; aux = aux.sigNodo) {
            c.add(aux);
        }
        return c;
    }

    public LinkedList<String> vertices() {
        LinkedList<String> c = new LinkedList<>();
        for(NodoGrafo aux = this.origen; aux != null; aux = aux.sigNodo) {
            c.add(aux.nodo);
        }
        return c;
    }

    public void eliminarArista(String v1, String v2) {
        NodoGrafo n1 = this.Vert2Nodo(v1);
        this.EliminarAristaNodo(n1, v2);
    }

    public boolean existeArista(String v1, String v2) {
        NodoGrafo n1 = this.Vert2Nodo(v1);

        NodoArista aux;
        for(aux = n1.arista; aux != null && !aux.nodoDestino.nodo.equals(v2); aux = aux.sigArista) {
        }

        return aux != null;
    }

    public LinkedList<Double> distanciaArista(String v1, String v2){
        String a; String b;
        if (existeArista(v1, v2)){
            a = v1;
            b = v2;
        }
        else{
            a = v2;
            b = v1;
        }
        NodoGrafo n1 = this.Vert2Nodo(a);
        GrafoLA.NodoArista aux;
        for(aux = n1.arista; !aux.nodoDestino.nodo.equals(b); aux = aux.sigArista) {
        }
        LinkedList<Double> retorno = (LinkedList)aux.distancias.clone();
        return retorno;
    }

    public LinkedList<Integer> tiempoArista(String v1, String v2){
        String a; String b;
        if (existeArista(v1, v2)){
            a = v1;
            b = v2;
        }
        else{
            a = v2;
            b = v1;
        }
        NodoGrafo n1 = this.Vert2Nodo(a);
        GrafoLA.NodoArista aux;
        for(aux = n1.arista; !aux.nodoDestino.nodo.equals(b); aux = aux.sigArista) {
        }
        LinkedList<Integer> retorno = (LinkedList)aux.tiempos.clone();
        return retorno;
    }
    public int horaInicioVertice(String x){
        NodoGrafo n = this.Vert2Nodo(x);
        int retorno = n.horaInicio;
        return retorno;
    }
    public int horaFinVertice(String x){
        NodoGrafo n = this.Vert2Nodo(x);
        int retorno = n.horaFin;
        return retorno;
    }

    class NodoArista {
        LinkedList<Double> distancias = new LinkedList<>();
        LinkedList<Integer> tiempos = new LinkedList<>();
        NodoGrafo nodoDestino;
        NodoArista sigArista;

        NodoArista() {
        }
    }
}