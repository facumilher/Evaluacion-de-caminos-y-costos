public class NodoGrafo implements Comparable<Integer> {
    String nodo;
    int horaInicio;
    int horaFin;
    GrafoLA.NodoArista arista;
    NodoGrafo sigNodo;

    NodoGrafo() {
    }

    @Override
    public int compareTo(Integer horafin2) {        
        return Integer.compare(this.horaFin, horafin2);
    }
}