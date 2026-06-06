package Domain.Model;

import java.util.ArrayList;

/**
 * Euclidean distance strategy implementation
 */
public class DistanceEuclideanStrategy extends DistanceStrategy {
    
    private static DistanceEuclideanStrategy instance;

    /**
     * Get the singleton instance of DistanceEuclideanStrategy
     * @return singleton instance of DistanceEuclideanStrategy
     */
    public static DistanceEuclideanStrategy getInstance() {
        if (instance == null) {
            instance = new DistanceEuclideanStrategy();
        }
        return instance;
    }

    /**
     * Euclidean distance between 2 arrays of RespostaAPregunta with the same size
     * @param respostes1 first RespostaEnquesta without empty answers
     * @param respostes2 second RespostaEnquesta without empty answers
     * @return (double) distance between the 2 RespostaEnquesta
     * @throws IllegalArgumentException if respostes1 and respostes2 have different order of types of RespostaAPregunta
     */
    @Override
    public double calcularDistCorrectRespostesEnquesta(ArrayList<RespostaAPregunta> respostes1, ArrayList<RespostaAPregunta> respostes2){
        int size = respostes1.size();

        double dist = 0;
        for (int i = 0; i < size; i++) {
            RespostaAPregunta<?> a = respostes1.get(i);
            RespostaAPregunta<?> b = respostes2.get(i);

            if (a.getClass() != b.getClass())
                throw new IllegalArgumentException("Tipus incompatibles");

            double d = DistanceStrategy.computeDistanceSameType(a, b);
            dist += d * d;
        }
        return Math.sqrt(dist) / size;
    }
}
