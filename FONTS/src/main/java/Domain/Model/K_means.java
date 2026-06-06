package Domain.Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * K_means clustering algorithm for survey responses ("RespostaEnquesta").
 */
public abstract class K_means extends AlgoritmeClustering {
    private static final double maxDist = 1e-3;
    private static final int maxIter = 1000;
    private ArrayList<RespostaEnquesta> centroidsResult;

    /**
     * Initialize firsts centroids for k_means
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @return array of k different centroids
     */
    public abstract ArrayList<RespostaEnquesta> initialize(int k, ArrayList<RespostaEnquesta> respostes);
   
    /**
     * Compute the clustering with k_means algorithm
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @return set of k clusters of RespostaEnquesta
     */
    @Override
    protected ArrayList<RespostaEnquesta>[] computeClustering(int k, ArrayList<RespostaEnquesta> respostes) {
        ArrayList<RespostaEnquesta> centroids = initialize(k, respostes);
        
        return calcularRec(k, respostes, centroids, 0);
    }

    @Override
    public ArrayList<RespostaEnquesta> getCentroids() {
        return centroidsResult;
    }

    /**
     * Compute the clustering recursively with k_means algorithm
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @param centroids precalculated k centroids
     * @return set of k clusters of RespostaEnquesta
     */
    private ArrayList<RespostaEnquesta>[] calcularRec(int k, ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta> centroids, int iter) {
        // Create k clusters
        ArrayList<RespostaEnquesta>[] clusters = new ArrayList[k];
        for (int i = 0; i < k; i++) clusters[i] = new ArrayList<RespostaEnquesta>();

        // Assign each RespostaAPregunta at the nearest cluster
        assignClusters(respostes, centroids, clusters);
        
        // Handle empty clusters before checking convergence
        boolean hadEmptyClusters = handleEmptyClusters(clusters, respostes, centroids);

        if(iter >= maxIter) return clusters;

        // Recompute centroids
        ArrayList<RespostaEnquesta> newCentroids = computeCentroids(clusters, centroids, respostes);
        this.centroidsResult = newCentroids;


        // Check if the algorithm has converged (only if no empty clusters were handled)
        if(!hadEmptyClusters && sameCentroids(centroids, newCentroids)) { // Convergence point
            return clusters;
        } else {
            return calcularRec(k, respostes, newCentroids, iter+1); // Continue computing with new centroids
        }
    }

    /**
     * Checks if the two Centrois have all the same RespostaEnquesta's
     * @param c1 first centroid
     * @param c2 second centroid
     * @return true if all the RespostaEnquesta have the same content in c1 and c2, false otherwise 
     */
    private boolean sameCentroids(ArrayList<RespostaEnquesta> oldC, ArrayList<RespostaEnquesta> newC) {
        for (int i = 0; i < oldC.size(); i++) {
            double dist = distRespostesEnquesta(oldC.get(i), newC.get(i));
            if (dist > maxDist) return false;
        }
        return true;
    }

    /**
     * Find the nearest centroid
     * @param r a particular answer
     * @param centroids centroid of all the answers
     * @return position of the nearest centroid to r
     */
    private Integer nearestCentroid(RespostaEnquesta r, ArrayList<RespostaEnquesta> centroids) {
        Double minDist = Double.MAX_VALUE; // Has to be a value bigger than any possible distance (1.0)
        int nearest = -1;
        for(int i = 0; i < centroids.size(); i++) {
            double dist = distRespostesEnquesta(r, centroids.get(i));
            if(dist < minDist) {
                minDist = dist;
                nearest = i;
            }
        }
        return nearest;
    }

    /**
     * Assign each answer at the cluster at the same position of the nearest centroid  
     * @param respostes set of RespostaEnquesta of the same Enquesta
     * @param centroids set of RespostaEnquesta of the same Enquesta from respostes
     * @param clusters empty set
     */
    private void assignClusters(ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta> centroids, ArrayList<RespostaEnquesta>[] clusters) {
        for(RespostaEnquesta r : respostes) {
            int c = nearestCentroid(r, centroids);
            clusters[c].add(r);            
        }
    }

    /**
     * Compute the centroids of a clustering
     * @param clusters set of all the clusters of RespostaEnquesta of the same Enquesta
     * @return array of centroids of each cluster
     */
        private ArrayList<RespostaEnquesta> computeCentroids(ArrayList<RespostaEnquesta>[] clusters,
          ArrayList<RespostaEnquesta> oldCentroids, ArrayList<RespostaEnquesta> allRespostes) {
        
        ArrayList<RespostaEnquesta> newCentroids = new ArrayList<RespostaEnquesta>();

        for(int i = 0; i < clusters.length; i++) {
            ArrayList<RespostaEnquesta> cluster = clusters[i];
            RespostaEnquesta centroid = computeCentroid(cluster);
            newCentroids.add(centroid);
        }
        return newCentroids;
    }

    /**
     * Handle empty clusters by reassigning points from the largest cluster
     * @param clusters all current clusters
     * @param allRespostes all survey responses
     * @param centroids current centroids
     * @return true if any empty clusters were handled, false otherwise
     */
    private boolean handleEmptyClusters(ArrayList<RespostaEnquesta>[] clusters, 
         ArrayList<RespostaEnquesta> allRespostes, ArrayList<RespostaEnquesta> centroids) {
        
        boolean hadEmpty = false;
        
        for(int i = 0; i < clusters.length; i++) {
            if(clusters[i].size() == 0) {
                hadEmpty = true;
                
                // Find the largest cluster
                int largestClusterId = 0;
                int maxSize = 0;
                for(int j = 0; j < clusters.length; j++) {
                    if(clusters[j].size() > maxSize) {
                        maxSize = clusters[j].size();
                        largestClusterId = j;
                    }
                }
                
                // Find the point in the largest cluster that is farthest from its centroid
                ArrayList<RespostaEnquesta> largestCluster = clusters[largestClusterId];
                RespostaEnquesta largestCentroid = centroids.get(largestClusterId);
                
                double maxDist = -1;
                int farthestPointId = 0;
                
                for(int j = 0; j < largestCluster.size(); j++) {
                    RespostaEnquesta r = largestCluster.get(j);
                    double dist = distRespostesEnquesta(r, largestCentroid);
                    if(dist > maxDist) {
                        maxDist = dist;
                        farthestPointId = j;
                    }
                }
                
                // Move the farthest point to the empty cluster
                RespostaEnquesta resposta = largestCluster.remove(farthestPointId);
                clusters[i].add(resposta);
                
                // Update the centroid for the empty cluster
                centroids.set(i, resposta);
            }
        }
        
        return hadEmpty;
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
        for(int j = 0; j < respostes.length; ++j) {
            RespostaAPregunta r = respostes[j].get(0).calcularCentroide(respostes[j]);
            centroid.addRespostaAPregunta(r);
        }

        return centroid;
    }

    /**
     * Group the answers according to the questions
     * @param cluster cluster of RespostaEnquesta of the same Enquesta
     * @return matrix of RespostaAPregunta transposed of the cluster
     */
    private ArrayList<RespostaAPregunta<?>>[] groupByPregunta(ArrayList<RespostaEnquesta> cluster){

        int numRespostes = cluster.size();

        ArrayList<RespostaAPregunta> firstRespostes = cluster.get(0).getRespostes();

        int numPreguntes = firstRespostes.size();

        ArrayList<RespostaAPregunta<?>>[] respostes = (ArrayList<RespostaAPregunta<?>>[]) new ArrayList<?>[numPreguntes];
        for (int j = 0; j < numPreguntes; ++j) respostes[j] = new ArrayList<>();

        // Group the answer according to the questions
        for(int i = 0; i < numRespostes; ++i) {
            RespostaEnquesta resposta = cluster.get(i);
            for(int j = 0; j < numPreguntes; ++j){
                respostes[j].add(resposta.getRespostaAPregunta(j));
            }
        }

        return respostes;
    }
}