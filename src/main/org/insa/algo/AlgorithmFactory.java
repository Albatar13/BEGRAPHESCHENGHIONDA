package org.insa.algo;

import java.lang.reflect.Constructor;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;

/**
 * Factory class used to register and retrieve algorithms based on their common
 * ancestor and name.
 *
 */
public class AlgorithmFactory {

    // Map between algorithm names and class.
    private final static Map<Class<? extends AbstractAlgorithm<?>>, Map<String, Class<? extends AbstractAlgorithm<?>>>> ALGORITHMS = new IdentityHashMap<>();

    static {
        // Register weakly-connected components algorithm:
        registerAlgorithm(WeaklyConnectedComponentsAlgorithm.class, "WCC basic",
                WeaklyConnectedComponentsAlgorithm.class);

        // Register shortest path algorithm:
        registerAlgorithm(ShortestPathAlgorithm.class, "Bellman-Ford", BellmanFordAlgorithm.class);
        registerAlgorithm(ShortestPathAlgorithm.class, "Dijkstra", DijkstraAlgorithm.class);
        registerAlgorithm(ShortestPathAlgorithm.class, "A*", AStarAlgorithm.class);

        // Register your algorithms here:
        // registerAlgorithm(CarPoolingAlgorithm.class, "My Awesome Algorithm",
        // MyCarPoolingAlgorithm.class);
    }

    /**
     * Register the given algorithm class with the given name as a child class of
     * the given base algorithm.
     * 
     * @param baseAlgorithm Base algorithm class that corresponds to the newly
     *        registered algorithm class (e.g., generic algorithm class for the
     *        problem).
     * @param name Name for the registered algorithm class.
     * @param algoClass Algorithm class to register.
     */
    public static void registerAlgorithm(Class<? extends AbstractAlgorithm<?>> baseAlgorithm,
            String name, Class<? extends AbstractAlgorithm<?>> algoClass) {
        if (!ALGORITHMS.containsKey(baseAlgorithm)) {
            ALGORITHMS.put(baseAlgorithm, new LinkedHashMap<>());
        }
        ALGORITHMS.get(baseAlgorithm).put(name, algoClass);
    }

    /**
     * Create an instance of the given algorithm class using the given input data.
     * Assuming algorithm correspond to a class "Algorithm", this function returns
     * an object equivalent to `new Algorithm(data)`.
     * 
     * @param algorithm Class of the algorithm to create.
     * @param data Input data for the algorithm.
     * 
     * @return A new instance of the given algorithm class using the given data.
     * 
     * @throws Exception if something wrong happens when constructing the object,
     *         i.e. the given input data does not correspond to the given algorithm
     *         and/or no constructor that takes a single parameter of type
     *         (data.getClass()) exists.
     */
    public static AbstractAlgorithm<?> createAlgorithm(
            Class<? extends AbstractAlgorithm<?>> algorithm, AbstractInputData data)
            throws Exception {
        // Retrieve the set of constructors for the given algorithm class.
        Constructor<?>[] constructors = algorithm.getDeclaredConstructors();

        // Within this set, find the constructor that can be called with "data" (only).
        AbstractAlgorithm<?> constructed = null;
        for (Constructor<?> c: constructors) {
            Class<?>[] params = c.getParameterTypes();
            if (params.length == 1 && params[0].isAssignableFrom(data.getClass())) {
                c.setAccessible(true);
                constructed = (AbstractAlgorithm<?>) c.newInstance(new Object[] { data });
                break;
            }
        }
        return constructed;
    }

    /**
     * Return the algorithm class corresponding to the given base algorithm class
     * and name. The algorithm must have been previously registered using
     * registerAlgorithm.
     * 
     * @param baseAlgorithm Base algorithm class for the algorithm to retrieve.
     * @param name Name of the algorithm to retrieve.
     * 
     * @return Class corresponding to the given name.
     * 
     * @see #registerAlgorithm
     */
    public static Class<? extends AbstractAlgorithm<?>> getAlgorithmClass(
            Class<? extends AbstractAlgorithm<?>> baseAlgorithm, String name) {
        return ALGORITHMS.get(baseAlgorithm).get(name);
    }

    /**
     * Return the list of names corresponding to the registered algorithm classes
     * for the given base algorithm class.
     * 
     * @param baseAlgorithm Base algorithm class for the algorithm class names to
     *        retrieve.
     * 
     * @return Names of the currently registered algorithms.
     * 
     * @see #registerAlgorithm
     */
    public static Set<String> getAlgorithmNames(
            Class<? extends AbstractAlgorithm<?>> baseAlgorithm) {
        if (!ALGORITHMS.containsKey(baseAlgorithm)) {
            return new TreeSet<>();
        }
        return ALGORITHMS.get(baseAlgorithm).keySet();
    }
}
