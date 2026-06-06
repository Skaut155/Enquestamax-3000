package Domain.Model;

import java.util.ArrayList;

/**
 * Strategy to calculate the Dunn Coefficient for clustering evaluation
 */
public class CoefficientDunnStrategy implements CoefficientClusterStrategy {

    private static CoefficientDunnStrategy instance;

    /**
     * Get the singleton instance of CoefficientDunnStrategy
     * @return singleton instance of CoefficientDunnStrategy
     */
    public static CoefficientDunnStrategy getInstance() {
        if (instance == null) {
            instance = new CoefficientDunnStrategy();
        }
        return instance;
    }
    
    /**
     * Dunn Coefficient to evaluate the clustering
     * @param clusters result of the clustering
     * @return Dunn coefficient of clusters
     */
    @Override
    public double calcularCoefficient(ArrayList<RespostaEnquesta>[] clusters) {        
        if (clusters.length == 1)
            throw new IllegalArgumentException("Ha d'haver-hi mínim 2 clusters per calcular Dunn");

        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            if (cluster.isEmpty())
                throw new IllegalArgumentException("No pot haver-hi clusters buits");
        }

        // Compute minimum inter-cluster distance
        double minInterClusterDist = minInterClusterDistance(clusters);

        // Compute maximum intra-cluster distance (diameter)
        double maxIntraClusterDist = maxIntraClusterDistance(clusters);

        // Dunn Index = min inter-cluster distance / max intra-cluster distance
        if (maxIntraClusterDist == 0) return Double.POSITIVE_INFINITY;
        
        return minInterClusterDist / maxIntraClusterDist;
    }

    /**
     * Compute the minimum distance between any two clusters
     * @param clusters Array of clusters
     * @return Minimum inter-cluster distance
     */
    private double minInterClusterDistance(ArrayList<RespostaEnquesta>[] clusters) {
        double minDist = Double.POSITIVE_INFINITY;

        for (int i = 0; i < clusters.length; i++) {
            for (int j = i + 1; j < clusters.length; j++) {
                double dist = clusterDistance(clusters[i], clusters[j]);
                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }

        return minDist;
    }

    /**
     * Compute the distance between two clusters (minimum distance between any two points)
     * @param cluster1 First cluster
     * @param cluster2 Second cluster
     * @return Distance between the two clusters
     */
    private double clusterDistance(ArrayList<RespostaEnquesta> cluster1, ArrayList<RespostaEnquesta> cluster2) {
        double minDist = Double.POSITIVE_INFINITY;

        for (RespostaEnquesta resposta1 : cluster1) {
            for (RespostaEnquesta resposta2 : cluster2) {
                double dist = distance.calcularDistRespostesEnquesta(resposta1, resposta2);
                if (dist < minDist) {
                    minDist = dist;
                }
            }
        }

        return minDist;
    }

    /**
     * Compute the maximum intra-cluster distance (diameter) across all clusters
     * @param clusters Array of clusters
     * @return Maximum intra-cluster distance
     */
    private double maxIntraClusterDistance(ArrayList<RespostaEnquesta>[] clusters) {
        double maxDiameter = 0.0;

        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            double diameter = clusterDiameter(cluster);
            if (diameter > maxDiameter) {
                maxDiameter = diameter;
            }
        }

        return maxDiameter;
    }

    /**
     * Compute the diameter of a cluster (maximum distance between any two points)
     * @param cluster Cluster to compute diameter for
     * @return Diameter of the cluster
     */
    private double clusterDiameter(ArrayList<RespostaEnquesta> cluster) {
        double maxDist = 0.0;

        for (int i = 0; i < cluster.size(); i++) {
            for (int j = i + 1; j < cluster.size(); j++) {
                double dist = distance.calcularDistRespostesEnquesta(
                    cluster.get(i), cluster.get(j)
                );
                if (dist > maxDist) {
                    maxDist = dist;
                }
            }
        }

        return maxDist;
    }

    /**
     * Acceptable threshold for the Dunn coefficient
     * @return acceptable threshold value
     */
    @Override
    public double acceptableThreshold() {
        return 1.0;
    }
}