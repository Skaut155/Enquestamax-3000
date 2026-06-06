package Domain.Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * K_means++ clustering algorithm for survey responses ("RespostaEnquesta").
 */
public class K_meansPlusPlus extends K_means {
    
    /**
     * Deffault constructor K_meansPlusPlus
     */
    public K_meansPlusPlus(){}

    /**
     * Initialize firsts centroids with k-means++ algorithm 
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @return array of k different centroids choosen by k-means++
     */
    @Override
    public ArrayList<RespostaEnquesta> initialize(int k, ArrayList<RespostaEnquesta> respostes) {          
        Random rand = new Random();
        ArrayList<RespostaEnquesta> centroids = new ArrayList<RespostaEnquesta>();
        
        // Choose first random centroid
        int firstIndex = rand.nextInt(respostes.size());
        centroids.add(respostes.get(firstIndex));

        while(centroids.size() < k) {
            // Compute the distance between each answer and the nearest centroid
            double[] minDists = new double[respostes.size()];
            double totalDist = minDists(respostes, centroids, minDists);

            // Choose the next centroid with probability of squared distance
            int c = selectNextCentroid(minDists, rand.nextDouble() * totalDist);
            centroids.add(respostes.get(c));
        }
        return centroids;
    }

    /**
     * Squared distance between each answer and the nearest centroid and the total sum
     * @param respostes answers
     * @param centroids centroids
     * @param minDists empty set completed with the squared distance between each answer and the nearest centroid
     * @return sum of the distances
     */
    private double minDists(ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta> centroids, double[] minDists) {
        double totalDist = 0.0;
        for(int i = 0; i < respostes.size(); i++) {
            double minDist = Double.MAX_VALUE; //minDist > 1.0
            for(RespostaEnquesta centroid : centroids) {
                double dist = distRespostesEnquesta(respostes.get(i), centroid);
                if(dist < minDist) {
                    minDist = dist;
                }
            }
            minDists[i] = minDist * minDist;
            totalDist += minDists[i];
        }
        return totalDist;
    }

    /**
     * Choose the next centroid with probability of the squared ditances
     * @param sqDists squared distances
     * @param dist random distance choosed
     * @return index i of the centroid. First index that fulfills: sum(j in 0..i) sqDists[j] >= dist
     */
    private int selectNextCentroid(double[] sqDists, double dist){
        double acumulativeDist = 0.0;
        int i = 0;
        boolean found = false;
        for(i = 0; !found; i++) {
            acumulativeDist += sqDists[i];
            if(acumulativeDist >= dist) found = true;
        }
        return i-1;
    }


} 