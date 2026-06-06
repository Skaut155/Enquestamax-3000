package Domain.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Class Kmedoids implements the algorithm k-medoids to distribute the answers in k clusters. The algorithm selects k medoids (random answers from the survey) and distributes the answers in k clusters, each one in the cluster corresponding to the closest medoid. Afterward, new medoids are selected via PAM Optimization and convergence is checked (medoids no longer change). If false, the process is repeated. Otherwise, the k clusters with the answers are returned.
 */
public class Kmedoids extends AlgoritmeClustering {
	private int k;
	private ArrayList<RespostaEnquesta> respostes;
	private RespostaEnquesta[] medoids;
	private ArrayList<RespostaEnquesta>[] clusters;

    /**
     * Constructor for Kmedoids
     */
    public Kmedoids() {
    }

    /**
     * Calculate how to divide the answers in k groups (clusters) by similarity (distance between them)
     * @param k Number of clusters to distribute the answers in
     * @param respostes List of answers to the survey
     * @return Array (clusters) of ArrayList of RespostaEnquesta (list of elements in each cluster)
     */
    @Override
    public ArrayList<RespostaEnquesta>[] computeClustering(int k, ArrayList<RespostaEnquesta> respostes){
        this.respostes = respostes;
        this.k = k;
        return calcularClusters();
    }

	@Override
    public ArrayList<RespostaEnquesta> getCentroids() {
		ArrayList<RespostaEnquesta> centroides = new ArrayList<>();
		for (RespostaEnquesta medoid : medoids) {
			centroides.add(medoid);
		}
        return centroides;
    }

    /**
         * Initialize the k medoids, choosing k random answers.
         */
	private void initializeMedoids(){
		this.clusters = new ArrayList[k];
		this.medoids = new RespostaEnquesta[k];
		Random rand = new Random();
		int randomInt;
		int i = 0;
		while(i < k){
			randomInt = rand.nextInt(respostes.size());
			if (!Arrays.asList(medoids).contains(respostes.get(randomInt))){
				medoids[i] = respostes.get(randomInt);
				++i;
			}
		}
	}
	
	/**
         * Initialize medoids and assign clusters iteratively, optimizing them via PAM Optimization until they have converged.
         * @return Array (clusters) of ArrayList<RespostaEnquesta> (list of elements in the cluster)
         */
	private ArrayList<RespostaEnquesta>[] calcularClusters(){
		initializeMedoids();
		boolean converged = false;
		while (!converged){
			assignClusters();
			RespostaEnquesta[] newMedoids = pamOptimization();
			converged = checkConvergence(newMedoids);
			this.medoids = newMedoids;
		}
		return this.clusters;
	}
	 
	/**
	  * Given the current medoids, the answers are assigned to their corresponding cluster. It is done by calculating the distance to the medoids, and adding the answer to the cluster corresponding to the closest medoid.
	  */
	private void assignClusters(){
		Arrays.fill(clusters, null);
		for (int i = 0; i < k; ++i){
			clusters[i] = new ArrayList<>();
		}
        for (RespostaEnquesta resposte : respostes) {
            double minDistance = -1;
            int closestCluster = 0;
            for (int j = 0; j < k; ++j) {
                double dist = distRespostesEnquesta(resposte, medoids[j]);
                if ((minDistance == -1) || (dist < minDistance)) {
                    minDistance = dist;
                    closestCluster = j;
                }
            }
            // Add to the closest cluster only once
            clusters[closestCluster].add(resposte);
        }
        // Fix empty clusters by moving elements from the largest clusters
        fixEmptyClusters();
	}

	/**
	 * Ensures no cluster is empty by moving elements from the largest cluster to empty ones.
	 */
	private void fixEmptyClusters(){
		for (int i = 0; i < k; ++i){
			if (clusters[i].isEmpty()){
				// Find the largest cluster
				int largestClusterIdx = 0;
				for (int j = 1; j < k; ++j){
					if (clusters[j].size() > clusters[largestClusterIdx].size()){
						largestClusterIdx = j;
					}
				}
				// Move the farthest element from the largest cluster to the empty one
				if (clusters[largestClusterIdx].size() > 1){
					int farthestIdx = 0;
					double maxDist = -1;
					for (int j = 0; j < clusters[largestClusterIdx].size(); ++j){
						double dist = distRespostesEnquesta(medoids[largestClusterIdx], clusters[largestClusterIdx].get(j));
						if (dist > maxDist){
							maxDist = dist;
							farthestIdx = j;
						}
					}
					RespostaEnquesta moved = clusters[largestClusterIdx].remove(farthestIdx);
					clusters[i].add(moved);
					medoids[i] = moved;
				}
			}
		}
	}
	 
	/**
         * Iteratively chooses new medoids and changes them if the cost improves.
         * @return Array of the k new better medoids
         */
	private RespostaEnquesta[] pamOptimization(){
		RespostaEnquesta[] newMedoids = Arrays.copyOf(medoids, k);
		double bestCost = calculateTotalCost(medoids);
		for (int i = 0; i < k; ++i){
			for (RespostaEnquesta j : respostes){
				if (!Arrays.asList(medoids).contains(j)){
					RespostaEnquesta[] newMedoidsAux = Arrays.copyOf(medoids, k);
					newMedoidsAux[i] = j;
					double newCost = calculateTotalCost(newMedoidsAux);
					if (newCost < bestCost){
						bestCost = newCost;
						newMedoids = newMedoidsAux;
					}
				}
			}
		}
		return newMedoids;
	}

	/**
         * Calculates the total cost of the current clusters.
         * @param medoids Medoids used to calculate the distance.
         * @return Total cost of the answers in the clusters
         */
	private double calculateTotalCost(RespostaEnquesta[] medoids){
		double totalCost = 0;
		for (int i = 0; i < k; ++i){
			for (int j = 0; j < clusters[i].size(); ++j){
				totalCost += distRespostesEnquesta(medoids[i], clusters[i].get(j));
			}
		}
		return totalCost;
	}
	 
	/**
         * Checks if the medoids have converged, meaning the new and optimized ones are not different from the last ones.
         * @return True if new medoids have converged, false otherwise.
         */
	private boolean checkConvergence(RespostaEnquesta[] newMedoids){
		for (int i = 0; i < k; ++i){
			if (medoids[i] != newMedoids[i]){
				return false;
			}
		}
		return true;
	}
	
}
