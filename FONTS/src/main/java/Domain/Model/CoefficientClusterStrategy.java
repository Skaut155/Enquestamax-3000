package Domain.Model;

import java.util.ArrayList;

public interface CoefficientClusterStrategy {

    public static final DistanceEuclideanStrategy distance = DistanceEuclideanStrategy.getInstance();

    /**
     * Coefficient to avaluate the clustering, 
     * @param clusters result of the clustering
     * @return coefficient of the clusters
     */
    public double calcularCoefficient(ArrayList<RespostaEnquesta>[] clusters);

    /**
     * Acceptable threshold for the coefficient
     * @return acceptable threshold value
     */
    public double acceptableThreshold();
}
