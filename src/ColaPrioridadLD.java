import java.util.LinkedList;

public class ColaPrioridadLD{
    ColaPrioridadLD.NodoPrioridad primero;

    public ColaPrioridadLD() {
    }

    public void inicializarCola() {
        this.primero = null;
    }

    public void acolarPrioridad(String x, Double prioridadNodo, LinkedList<String> caminoPadre, LinkedList<Double> distanciaPadre, LinkedList<Integer> tiempoPadre) {
        ColaPrioridadLD.NodoPrioridad nuevo = new ColaPrioridadLD.NodoPrioridad();
        nuevo.prioridad = prioridadNodo;
        nuevo.info = x;
        nuevo.caminos = caminoPadre;
        nuevo.distancias = distanciaPadre;
        nuevo.tiempos = tiempoPadre;
       
        if (!this.colaVacia() && prioridadNodo >= this.primero.prioridad) {
            ColaPrioridadLD.NodoPrioridad aux;
            for(aux = this.primero; aux.sig != null && aux.sig.prioridad <= prioridadNodo; aux = aux.sig) {
            }

            nuevo.sig = aux.sig;
            aux.sig = nuevo;
        } else {
            nuevo.sig = this.primero;
            this.primero = nuevo;
        }

    }

    public void desacolar() {
        this.primero = this.primero.sig;
    }

    public boolean colaVacia() {
        return this.primero == null;
    }

    public ColaPrioridadLD.NodoPrioridad primero() {
        return this.primero;
    }

    public Double prioridadPrimero() {
        return this.primero.prioridad;
    }

    public class NodoPrioridad {
        String info;
        Double prioridad;
        LinkedList<String> caminos = new LinkedList<>();
        LinkedList<Double> distancias = new LinkedList<>();
        LinkedList<Integer> tiempos = new LinkedList<>();
        ColaPrioridadLD.NodoPrioridad sig;

        private NodoPrioridad() {
        }
    }
}