package Domain.Model;

import java.util.ArrayList;
import java.util.Random;

/**
 * K-means clustering algorithm with random initialization of centroids.
 */
public class K_meansRandom extends K_means {
    
    /**
     * Deffault constructor K_meansRandom
     */
    public K_meansRandom(){}

    /**
     * Initialize firsts centroids with random algorithm
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @return array of k different random centroids
     */
    @Override
    public ArrayList<RespostaEnquesta> initialize(int k, ArrayList<RespostaEnquesta> respostes) {
        Random rand = new Random();
        ArrayList<RespostaEnquesta> centroids = new ArrayList<RespostaEnquesta>();
        ArrayList<Integer> selectedIndices = new ArrayList<Integer>();
        while(centroids.size() < k) {
            int index = rand.nextInt(respostes.size());
            if(!selectedIndices.contains(index)) {
                centroids.add(respostes.get(index));
                selectedIndices.add(index);
            }
        }
        return centroids;        
    }
}