import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Modelo de una red social mediante un grafo simple, no dirigido y no ponderado.
 * - Vertices: usuarios unicos (identificados por nombre).
 * - Aristas: amistades reciprocas entre dos usuarios distintos.
 * - Reglas del grafo simple: sin lazos, sin aristas duplicadas, sin usuarios inexistentes.
 */
public class RedSocialGrafo {

    // Lista de adyacencia: usuario -> amigos (se conserva el orden de insercion)
    private final Map<String, List<String>> adyacencia = new LinkedHashMap<>();
    private int numAristas = 0;

    /** Registra un usuario nuevo (vertice). Rechaza nombres vacios o repetidos. */
    public boolean agregarUsuario(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            System.out.println("Operacion invalida: el nombre del usuario no puede estar vacio.");
            return false;
        }
        if (adyacencia.containsKey(nombre)) {
            System.out.println("Operacion invalida: el usuario " + nombre + " ya existe.");
            return false;
        }
        adyacencia.put(nombre, new ArrayList<>());
        return true;
    }

    /** Registra una amistad (arista) aplicando las reglas del grafo simple. */
    public boolean agregarAmistad(String a, String b) {
        if (!adyacencia.containsKey(a) || !adyacencia.containsKey(b)) {
            System.out.println("Operacion invalida: usuario inexistente en la red (" + a + ", " + b + ").");
            return false;
        }
        if (a.equals(b)) {
            System.out.println("Operacion invalida: no es posible registrar una relacion de amistad "
                    + "de un usuario consigo mismo (lazo no permitido).");
            return false;
        }
        if (existeRelacion(a, b)) {
            System.out.println("Operacion invalida: la amistad " + a + " - " + b
                    + " ya existe (arista duplicada no permitida).");
            return false;
        }
        adyacencia.get(a).add(b); // no dirigido: se registra en ambos sentidos
        adyacencia.get(b).add(a);
        numAristas++;
        return true;
    }

    /** Devuelve true si existe una arista directa entre a y b. */
    public boolean existeRelacion(String a, String b) {
        return adyacencia.containsKey(a) && adyacencia.get(a).contains(b);
    }

    /** Lista de amigos directos de un usuario. */
    public List<String> vecinos(String usuario) {
        if (!adyacencia.containsKey(usuario)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(adyacencia.get(usuario));
    }

    /** Grado = numero de amistades del usuario. */
    public int grado(String usuario) {
        return adyacencia.containsKey(usuario) ? adyacencia.get(usuario).size() : -1;
    }

    public int numVertices() { return adyacencia.size(); }
    public int numAristas()  { return numAristas; }

    /** Muestra la lista de adyacencia con el grado de cada usuario. */
    public void mostrar() {
        for (Map.Entry<String, List<String>> e : adyacencia.entrySet()) {
            System.out.println(e.getKey() + " (grado " + e.getValue().size() + ") -> " + e.getValue());
        }
    }

    /** Comprueba el teorema del apreton de manos: suma de grados = 2 x aristas. */
    public boolean verificarSumaGrados() {
        int suma = 0;
        for (List<String> amigos : adyacencia.values()) {
            suma += amigos.size();
        }
        System.out.println("Suma de grados = " + suma + " | 2 x aristas = " + (2 * numAristas));
        return suma == 2 * numAristas;
    }

    public static void main(String[] args) {
        RedSocialGrafo red = new RedSocialGrafo();

        // Vertices
        String[] usuarios = {"Mateo", "Sofia", "Lucas", "Valentina", "Juan", "Camila"};
        for (String u : usuarios) {
            red.agregarUsuario(u);
        }

        // Aristas
        red.agregarAmistad("Mateo", "Sofia");
        red.agregarAmistad("Mateo", "Lucas");
        red.agregarAmistad("Sofia", "Lucas");
        red.agregarAmistad("Mateo", "Valentina");
        red.agregarAmistad("Valentina", "Juan");

        System.out.println("=== Red social (V = " + red.numVertices() + ", E = " + red.numAristas() + ") ===");
        red.mostrar();

        System.out.println("\n=== Verificacion de grados ===");
        System.out.println("Suma de grados correcta: " + red.verificarSumaGrados());

        System.out.println("\n=== Consultas existeRelacion ===");
        System.out.println("existeRelacion(Mateo, Valentina) = " + red.existeRelacion("Mateo", "Valentina"));
        System.out.println("existeRelacion(Sofia, Juan)      = " + red.existeRelacion("Sofia", "Juan"));

        System.out.println("\n=== Consultas sobre Mateo ===");
        System.out.println("vecinos(Mateo) = " + red.vecinos("Mateo"));
        System.out.println("grado(Mateo)   = " + red.grado("Mateo"));

        System.out.println("\n=== Amistades que deben rechazarse ===");
        red.agregarAmistad("Mateo", "Mateo");        // lazo
        red.agregarAmistad("Mateo", "Sofia");        // duplicado
        red.agregarAmistad("Mateo", "Pedro");        // usuario inexistente
    }
}
