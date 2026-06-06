package Domain.Model;

import java.util.ArrayList;

/**
 * Strategy to calculate the Calinski-Harabasz Coefficient for clustering evaluation
 */
public class CoefficientCalinskiHarabaszStrategy implements CoefficientClusterStrategy {

    private static CoefficientCalinskiHarabaszStrategy instance;

    /**
     * Get the singleton instance of CoefficientCalinskiHarabaszStrategy
     * @return singleton instance of CoefficientCalinskiHarabaszStrategy
     */
    public static CoefficientCalinskiHarabaszStrategy getInstance() {
        if (instance == null) {
            instance = new CoefficientCalinskiHarabaszStrategy();
        }
        return instance;
    }
    
    /**
     * Calinski-Harabasz Coefficient to evaluate the clustering
     * @param clusters result of the clustering
     * @return Calinski-Harabasz coefficient of clusters
     */
    @Override
    public double calcularCoefficient(ArrayList<RespostaEnquesta>[] clusters) {
        if (clusters.length == 1)
            throw new IllegalArgumentException("Ha d'haver-hi mínim 2 clusters per calcular Calinski-Harabasz");

        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            if (cluster.isEmpty())
                throw new IllegalArgumentException("No pot haver-hi clusters buits");
        }

        // Total number of points
        int n = 0;
        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            n += cluster.size();
        }

        // Number of clusters
        int k = clusters.length;

        // Compute global centroid
        RespostaEnquesta globalCentroid = computeGlobalCentroid(clusters);

        // Compute between-cluster sum of squares (BCSS)
        double bcss = computeBetweenClusterSS(clusters, globalCentroid);

        // Compute within-cluster sum of squares (WCSS)
        double wcss = computeWithinClusterSS(clusters);

        // Calinski-Harabasz Index = (BCSS / (k - 1)) / (WCSS / (n - k))
        if (wcss == 0) return Double.POSITIVE_INFINITY;

        return (bcss / (k - 1)) / (wcss / (n - k));
    }

    /**
     * Compute the global centroid of all clusters
     * @param clusters Array of clusters
     * @return Global centroid
     */
    private RespostaEnquesta computeGlobalCentroid(ArrayList<RespostaEnquesta>[] clusters) {
        ArrayList<RespostaEnquesta> allRespostes = new ArrayList<>();
        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            allRespostes.addAll(cluster);
        }
        return computeCentroid(allRespostes);
    }

    /**
     * Compute the centroids of a single cluster
     * @param cluster cluster of RespostaEnquesta of the same Enquesta
     * @return centroid (median) of the cluster
     */
    private RespostaEnquesta computeCentroid(ArrayList<RespostaEnquesta> cluster) {        
        ArrayList<RespostaAPregunta<?>>[] respostes = groupByPregunta(cluster);
        RespostaEnquesta centroid = new RespostaEnquesta();
        centroid.associaContainer(cluster.get(0).getContainer());

        // Compute the centroid for each question
        for (int j = 0; j < respostes.length; ++j) {
            RespostaAPregunta r = respostes[j].get(0).calcularCentroide(respostes[j]);
            centroid.addRespostaAPregunta(r);
        }

        return centroid;
    }

    /**
     * Group responses by question
     * @param cluster cluster of RespostaEnquesta
     * @return Array of ArrayLists, each containing responses to the same question
     */
    private ArrayList<RespostaAPregunta<?>>[] groupByPregunta(ArrayList<RespostaEnquesta> cluster) {        
        int numPreguntes = cluster.get(0).getRespostes().size();
        ArrayList<RespostaAPregunta<?>>[] respostes = new ArrayList[numPreguntes];
        
        for (int i = 0; i < numPreguntes; i++) {
            respostes[i] = new ArrayList<>();
        }
        
        for (RespostaEnquesta re : cluster) {
            ArrayList<RespostaAPregunta> respostesPreguntes = re.getRespostes();
            for (int i = 0; i < respostesPreguntes.size(); i++) {
                respostes[i].add(respostesPreguntes.get(i));
            }
        }
        
        return respostes;
    }

    /**
     * Compute between-cluster sum of squares (BCSS)
     * @param clusters Array of clusters
     * @param globalCentroid Global centroid of all data
     * @return Between-cluster sum of squares
     */
    private double computeBetweenClusterSS(ArrayList<RespostaEnquesta>[] clusters, RespostaEnquesta globalCentroid) {
        double bcss = 0.0;

        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            RespostaEnquesta clusterCentroid = computeCentroid(cluster);
            double dist = distance.calcularDistRespostesEnquesta(clusterCentroid, globalCentroid);
            bcss += cluster.size() * dist * dist;
        }

        return bcss;
    }

    /**
     * Compute within-cluster sum of squares (WCSS)
     * @param clusters Array of clusters
     * @return Within-cluster sum of squares
     */
    private double computeWithinClusterSS(ArrayList<RespostaEnquesta>[] clusters) {
        double wcss = 0.0;

        for (ArrayList<RespostaEnquesta> cluster : clusters) {
            RespostaEnquesta clusterCentroid = computeCentroid(cluster);
            
            for (RespostaEnquesta resposta : cluster) {
                double dist = distance.calcularDistRespostesEnquesta(resposta, clusterCentroid);
                wcss += dist * dist;
            }
        }

        return wcss;
    }

    /**
     * Acceptable threshold for the Calinski-Harabasz coefficient
     * @return acceptable threshold value
     */
    @Override
    public double acceptableThreshold() {
        return 10.0;
    }
}