package Domain.Model;

import java.util.ArrayList;

/**
 * Manhattan distance strategy implementation
 */
public class DistanceManhattanStrategy extends DistanceStrategy {

    private static DistanceManhattanStrategy instance;

    /**
     * Get the singleton instance of DistanceManhattanStrategy
     * @return singleton instance of DistanceManhattanStrategy
     */
    public static DistanceManhattanStrategy getInstance() {
        if (instance == null) {
            instance = new DistanceManhattanStrategy();
        }
        return instance;
    }
    
    /**
     * Manhattan distance between 2 arrays of RespostaAPregunta with the same size
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

            double newDist = DistanceStrategy.computeDistanceSameType(a, b);
            dist += newDist;
        }
        return dist / size;
    }
}
