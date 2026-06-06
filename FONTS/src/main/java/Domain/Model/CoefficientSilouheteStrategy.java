package Domain.Model;

import java.util.ArrayList;

public class CoefficientSilouheteStrategy implements CoefficientClusterStrategy{

    private static CoefficientSilouheteStrategy instance;

    /**
     * Get the singleton instance of CoefficientSilouheteStrategy
     * @return singleton instance of CoefficientSilouheteStrategy
     */
    public static CoefficientSilouheteStrategy getInstance() {
        if (instance == null) {
            instance = new CoefficientSilouheteStrategy();
        }
        return instance;
    }
    
    /**
     * Silhouette Coefficient to avaluate the clustering
     * @param clusters result of the clustering
     * @return Silhouette coefficient of clusters
     */
    @Override
    public double calcularCoefficient(ArrayList<RespostaEnquesta>[] clusters) {
        if(clusters.length == 0) 
            throw new IllegalArgumentException("Ha d'haver-hi mínim 1 cluster");

        double s = 0.0;
        int numRespostes = 0;
        
        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            if(cluster.isEmpty())
                throw new IllegalArgumentException("No pot haver-hi clusters buits");
            
            for (RespostaEnquesta respostaI : cluster) {
                numRespostes++;

                // Compute a(i)
                double a = innerMeanDistance(respostaI, cluster);

                // Compute b(i)
                double b = outerMeanDistance(respostaI, clusters, cluster);

                // Silhouette for this point
                double si = 0.0;
                if (Math.max(a, b) > 0) si = (b - a) / Math.max(a, b);

                s += si;
            }
        }
        return s / numRespostes;
    }

    /**
     * Mean distance between a RespostaEnquesta and all the elements in a cluster
     * @param respostaI RespostaEnquesta 
     * @param cluster Cluster that respostaI belongs to
     * @return Mean distance between respostaI and all elements in cluster.
     */
    private double innerMeanDistance(RespostaEnquesta respostaI, ArrayList<RespostaEnquesta> cluster) {
        double a = 0.0;
        if (cluster.size() > 1) {
            for (RespostaEnquesta respostaA : cluster) {
                if (respostaI != respostaA)
                    a += distance.calcularDistRespostesEnquesta(respostaI, respostaA);
            }
            a /= (cluster.size() - 1);
        }
        return a;
    }

    /**
     * Minimum mean distance between a RespostaEnquesta and an ArrayList of clusters
     * @param respostaI RespostaEnquesta
     * @param clusters Array of clusters
     * @param cluster Cluster that belongs respostaI
     * @return Minimum mean distance between respostaI and all the elements of each cluster
     */
    private double outerMeanDistance(RespostaEnquesta respostaI, ArrayList<RespostaEnquesta>[] clusters, ArrayList<RespostaEnquesta> cluster){
        double b = Double.POSITIVE_INFINITY;
        for (ArrayList<RespostaEnquesta> otherCluster : clusters) {
            if (otherCluster == cluster) continue;

            double sumDist = 0.0;
            for (RespostaEnquesta respostaB : otherCluster) {
                sumDist += distance.calcularDistRespostesEnquesta(respostaI, respostaB);
            }
            
            double avgDist = sumDist / otherCluster.size();
            if (avgDist < b) b = avgDist;
        }
        return b;
    }

    /**
     * Acceptable threshold for the silhouette coefficient
     * @return acceptable threshold value
     */
    @Override
    public double acceptableThreshold() {
        return 0.5;
    }
}
