import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class Main {





    public static Double greedy(GrafoLA g){

        List<NodoGrafo> vertices = g.verticesN();;
        vertices.removeIf(nodo->nodo.nodo.equalsIgnoreCase("A"));
        Collections.sort(vertices, new Comparator<NodoGrafo>() {

            @Override
            public int compare(NodoGrafo o1, NodoGrafo o2) {
                return o1.compareTo(o2.horaFin) ;
            }
            
        });
        String ultimo = "A";
        int suma = 0;
        while (!vertices.isEmpty()){
            String x = ((LinkedList<NodoGrafo>) vertices).pollFirst().nodo;
            suma += mejorArista(g, ultimo, x);
            ultimo = x;
        }
        return suma + mejorArista(g, ultimo, "A");
    }
    public static Boolean Poda (Double c, Double cota){
        if (c > cota) return true;
        else return false;
    }
    
    public static Double mejorArista(GrafoLA g, String a, String b){
        LinkedList<Double> lista = g.distanciaArista(a, b);
        Double mejor = lista.pollFirst();
        while (!lista.isEmpty()){
            Double x = lista.pollFirst();
            if (x < mejor)
                mejor = x;
        }
        return mejor;
    }
    
    public static Boolean condicionHoraria(GrafoLA grafo, String x, LinkedList<Integer> tiempoPadre){
        int tiempoTotal=0;
        ListIterator<Integer> iter = tiempoPadre.listIterator();
        while (iter.hasNext()){
            tiempoTotal += iter.next();
        }
        if(grafo.horaFinVertice(x) > tiempoTotal && tiempoTotal > grafo.horaInicioVertice(x)) return true;
        else return false;
    }
  
    public static Boolean hijoBueno(GrafoLA grafo, ColaPrioridadLD c, Double cota, String x, Double prioridad, LinkedList<String> caminoPadre, LinkedList<Integer> tiempoPadre){
        if(condicionHoraria(grafo, x, tiempoPadre) && !Poda(prioridad, cota)) return true;
        else return false;
    }

    public static Double cotaInferior(GrafoLA grafo, String x, LinkedList<String> caminoPadre, LinkedList<Double> distanciaPadre){
        ListIterator<Double> iter = distanciaPadre.listIterator();
        Double distanciaTotal = 0.00;
        while(iter.hasNext()) distanciaTotal += iter.next();

        LinkedList<String> pendientes = grafo.vertices();
        pendientes.removeAll(caminoPadre);
        Double prim = costoPrim(grafo, pendientes);

        LinkedList<String> mejorConexion = menorConexion(x, pendientes, grafo);
        Double menorArista = Double.valueOf(mejorConexion.pollLast());

        LinkedList<String> mejorConexionA = menorConexion("A", pendientes, grafo);
        Double menorAristaA = Double.valueOf(mejorConexionA.pollLast());

        return(distanciaTotal + prim + menorArista + menorAristaA);
    }
    
    public static Double costoPrim(GrafoLA grafo, LinkedList<String> pendientes){
        Double prim = 0.0;
        String nodoActual = pendientes.pollFirst();
        while(pendientes.size() > 1){
            LinkedList<String> mejorConexion = menorConexion(nodoActual, pendientes, grafo);
            nodoActual = mejorConexion.pollFirst();
            prim += Double.valueOf(mejorConexion.pollFirst());
            pendientes.remove(nodoActual);
        }
        return prim;
    }

    public static LinkedList<String> menorConexion(String x, LinkedList<String> lista, GrafoLA grafo){
        ListIterator<String> iter = lista.listIterator();
        Double mejorDist = 10000.01;
        String mejorNodo = x;
        while(iter.hasNext()){
            String aux = iter.next();
            Double conexion = mejorArista(grafo, x, aux);
            if(conexion <= mejorDist) {
                mejorDist = conexion;
                mejorNodo = aux;
            }
        }
        LinkedList<String> retorno = new LinkedList<>();
        retorno.add(mejorNodo);
        retorno.add(String.valueOf(mejorDist));
        
        return retorno;
    }

    public static void generarHijos(GrafoLA grafo, ColaPrioridadLD cola, ColaPrioridadLD.NodoPrioridad nodo, Double cota){
        LinkedList<String> pendientes = grafo.vertices();
        pendientes.removeAll(nodo.caminos);
        
        ListIterator<String> iter = pendientes.listIterator();

        while(iter.hasNext()){
            String candidato = iter.next();
            LinkedList<Double> listaDistancias = grafo.distanciaArista(nodo.info, candidato);
            LinkedList<Integer> listaTiempos = grafo.tiempoArista(nodo.info, candidato);
            ListIterator<Double> distIter = listaDistancias.listIterator();
            ListIterator<Integer> tiempoIter = listaTiempos.listIterator();
            
            LinkedList<String> nodoCamino = nodo.caminos;
            LinkedList<Double> nodoDistancia = nodo.distancias;
            LinkedList<Integer> nodoTiempo = nodo.tiempos;

            while(distIter.hasNext()){
                Double nuevaDist = distIter.next();
                int nuevoTiempo = tiempoIter.next();
                LinkedList<String> caminoPadre = (LinkedList)nodoCamino.clone();
                caminoPadre.add(candidato);               
                LinkedList<Double> distanciaPadre = (LinkedList)nodoDistancia.clone();
                distanciaPadre.add(nuevaDist);
                LinkedList<Integer> tiempoPadre = (LinkedList)nodoTiempo.clone();
                tiempoPadre.add(nuevoTiempo);
                
                Double prioridad = cotaInferior(grafo, candidato, caminoPadre, distanciaPadre);
                
                if(hijoBueno(grafo, cola, cota, candidato, prioridad, caminoPadre, tiempoPadre)){
                    cola.acolarPrioridad(candidato, prioridad, caminoPadre, distanciaPadre, tiempoPadre);
                }
            }
        }
    }
    
    public static void imprimirCola(ColaPrioridadLD cola){
        
    }




    public static void main(String[] args) throws FileNotFoundException {
       System.out.println("a");
        try{ 


            /// CREAMOS EL GRAFO Y LO RELLENAMOS CON LOS DATOS DE LOS ARCHIVOS

            GrafoLA grafo = new GrafoLA(); grafo.inicializarGrafo();
            
            BufferedReader datosClientes = new BufferedReader(new FileReader("DatosClientes.txt"));
            String lineaDatosClientes = datosClientes.readLine();
            int cantClientes = -1;

            while (lineaDatosClientes != null){
                String[] datos1 = lineaDatosClientes.split("\t");

                if (datos1[0].equals("A")){
                    grafo.agregarVertice("A", 0, 0);
                }
                else if (!datos1[0].equals("Cliente")){
                    grafo.agregarVertice(datos1[0], Integer.parseInt(datos1[4])*60, Integer.parseInt(datos1[5])*60);
                }

                cantClientes ++;
                lineaDatosClientes = datosClientes.readLine();
            }
            datosClientes.close();

            BufferedReader caminos = new BufferedReader(new FileReader("Caminos.txt"));
            String lineaCaminos = caminos.readLine();

            while (lineaCaminos != null){
                String[] datos2 = lineaCaminos.split("\t");

                if (!datos2[0].equals("origen")){
                    datos2[3] = datos2[3].replaceAll(",", ".");
                    grafo.agregarArista(datos2[0], datos2[1], Integer.parseInt(datos2[2]), Double.parseDouble(datos2[3]));
                }

                lineaCaminos = caminos.readLine();
            }
            caminos.close();

            /// CREAMOS LA LISTA DE NODOS VIVOS Y LE AGREGAMOS EL PRIMER NODO (CENTRO DE DISTRIBUCIÓN)

            ColaPrioridadLD listaNodosVivos = new ColaPrioridadLD(); listaNodosVivos.inicializarCola();
            LinkedList<String> caminoPadre = new LinkedList<>(); caminoPadre.add("A");
            LinkedList<Double> distanciaPadre = new LinkedList<>(); distanciaPadre.add(0.0);
            LinkedList<Integer> tiempoPadre = new LinkedList<>(); tiempoPadre.add(480);
            listaNodosVivos.acolarPrioridad("A", cotaInferior(grafo, "A", caminoPadre, distanciaPadre), caminoPadre, distanciaPadre, tiempoPadre);

            Double cotaSuperior = greedy(grafo);
            LinkedList<String> mejorSolucionCamino = new LinkedList<>();
            LinkedList<Integer> mejorSolucionTiempo = new LinkedList<>();
            LinkedList<Double> mejorSolucionDistancia = new LinkedList<>();

            while(!listaNodosVivos.colaVacia()){
                ColaPrioridadLD.NodoPrioridad nodo = listaNodosVivos.primero();
                listaNodosVivos.desacolar();
                
                if(nodo.caminos.size() == cantClientes){
                    ListIterator<Double> iter = distanciaPadre.listIterator();
                    Double distanciaTotal = 0.00;

                    while(iter.hasNext()) distanciaTotal += iter.next();
                    distanciaTotal += mejorArista(grafo, "A", nodo.info);
                    
                    if(cotaSuperior > distanciaTotal){
                        cotaSuperior = distanciaTotal;
                        mejorSolucionCamino = nodo.caminos;
                        mejorSolucionTiempo = nodo.tiempos;
                        mejorSolucionDistancia = nodo.distancias;
                    }
                }
                else {
                    generarHijos(grafo, listaNodosVivos, nodo, cotaSuperior);
                } 
            }
            System.out.println(mejorSolucionCamino);
            System.out.println(mejorSolucionTiempo);
            System.out.println(mejorSolucionDistancia);
            
        }
        catch(IOException excepcion){
            excepcion.printStackTrace();
        } 
    }
}