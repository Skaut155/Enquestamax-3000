package Domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a clustering algorithm for survey responses (RespostaEnquesta).
 * The algorithm can compute clusters based on a specified number of clusters (k)
 * or determine the best k.
 * 
 * The algorithm holds the original and changed responses to handle missing data imputation.
 */
public abstract class AlgoritmeClustering {

    private Map<RespostaEnquesta, RespostaEnquesta> originalToImputed;
    private DistanceStrategy distanceStrategy;
    private CoefficientClusterStrategy coefficientClusterStrategy;

    /**
     * Deffault constructor
     * Manhattan distance and coefficient Silhouete
     */
    public AlgoritmeClustering(){
        distanceStrategy = DistanceManhattanStrategy.getInstance();
        coefficientClusterStrategy = CoefficientSilouheteStrategy.getInstance();
    }

    /**
     * Set the strategy to compute the distance
     * @param distanceStrategy strategy to compute the distance
     */
    public void setDistanceStrategy(DistanceStrategy distanceStrategy){
        this.distanceStrategy = distanceStrategy;
    }

    /**
     * Set the strategy to compute the quality of the cluster
     * @param coefficientClusterStrategy coefficient to compute the quality of the cluster
     */
    public void setCoefficientClusterStrategy(CoefficientClusterStrategy coefficientClusterStrategy){
        this.coefficientClusterStrategy = coefficientClusterStrategy;
    }

    /**
     * Get the distance strategy
     * @return distance strategy
     */
    public DistanceStrategy getDistanceStrategy(){
        return this.distanceStrategy;
    }

    /**
     * Get the coefficient cluster strategy
     * @return coefficient cluster strategy
     */
    public CoefficientClusterStrategy getCoefficientClusterStrategy(){
        return this.coefficientClusterStrategy;
    }
   
    /**
     * Compute the algorithm
     * @param k number of clusters
     * @param respostes  set of minimum k RespostaEnquesta and all of the same Enquesta
     * @return set of k clusters of RespostaEnquesta
     */
    public ArrayList<RespostaEnquesta>[] computeAlgorithm(int k, ArrayList<RespostaEnquesta> respostes){
        if (respostes == null || respostes.isEmpty())
            throw new IllegalArgumentException("Respostes no pot ser null ni buida");
        if ((k < 1) || (k > respostes.size())){
			throw new IllegalArgumentException("El nombre de clústers k ha de ser major que 0 i menor o igual al total de respostes de l'enquesta");
		}
        
        assureRespostesSameSize(respostes);

        ArrayList<RespostaEnquesta> imputedRespostes = imputeNulls(respostes);

        ArrayList<RespostaEnquesta>[] bestClusters = computeClustering(k, imputedRespostes);
        double maxCoef = coefficientCluster(bestClusters);
        
        for(int i = 0; maxCoef < coefficientClusterStrategy.acceptableThreshold() && i < 10; ++i){

            ArrayList<RespostaEnquesta>[] clusters = computeClustering(k, imputedRespostes);
            double coef = coefficientCluster(clusters);
            
            if(coef > maxCoef){
                maxCoef = coef;
                bestClusters = clusters;
            }
        }
        
        restoreNulls(bestClusters);
        
        return bestClusters;
    }



    /**
     * Compute the clustering for the best k based on the coefficient
     * @param respostes set of minimum k RespostaEnquesta and all of the same Enquesta
     * @param thresholdRatio the threshold ratio to detect the elbow (recommended 0.9)
     * @return the last clustering that improves the coefficient score by more than the thresholdRatio
     */
    public ArrayList<RespostaEnquesta>[] computeBestAlgorithm(ArrayList<RespostaEnquesta> respostes, double thresholdRatio){
        if (respostes == null || respostes.isEmpty())
            throw new IllegalArgumentException("Respostes no pot ser null ni buida");
        
        if(thresholdRatio <= 0.0 || thresholdRatio >= 1.0)
            throw new IllegalArgumentException("El thresholdRatio ha d'estar entre 0 i 1 (exclusius)");

        assureRespostesSameSize(respostes);

        ArrayList<RespostaEnquesta> imputedRespostes = imputeNulls(respostes);
        ArrayList<RespostaEnquesta>[] clusters = computeBestClustering(imputedRespostes, thresholdRatio);
        restoreNulls(clusters);
        return clusters;
    }

    /**
     * Assure that all RespostaEnquesta have the same number of RespostaAPregunta
     * @param respostes
     */
    private void assureRespostesSameSize(ArrayList<RespostaEnquesta> respostes){
        int size = respostes.get(0).getRespostes().size();

        for(RespostaEnquesta resposta : respostes){
            if(resposta.getRespostes().size() != size)
                throw new IllegalArgumentException("Totes les RespostaEnquesta han de tenir el mateix nombre de respostes");
        }
    }

    /**
     * Compute the clustering for the best k based on the coefficient of Silhouete
     * @param respostes set of minimum k RespostaEnquesta without nulls and all of the same Enquesta
     * @param thresholdRatio the threshold ratio to detect the elbow (recommended 0.9)
     * @return the last clustering that improves the coefficient score by more than the thresholdRatio
     */
    private ArrayList<RespostaEnquesta>[] computeBestClustering(ArrayList<RespostaEnquesta> respostes, double thresholdRatio){
        CoefficientSilouheteStrategy silouheteStrategy = CoefficientSilouheteStrategy.getInstance();

        ArrayList<RespostaEnquesta>[] bestCluster = computeClustering(1, respostes);
        double prevCoefficient = silouheteStrategy.calcularCoefficient(bestCluster);
        double prevSlope = 0.0;

        for(int k = 2; k <= respostes.size(); k++){
            ArrayList<RespostaEnquesta>[] cluster = computeClustering(k, respostes);
            double coefficient = silouheteStrategy.calcularCoefficient(cluster);
            double slope = coefficient - prevCoefficient;

            if(prevSlope != 0){
                double ratio = Math.abs(slope / prevSlope);
                if (ratio > thresholdRatio) break;
            }

            prevCoefficient = coefficient;
            prevSlope = slope;
            bestCluster = cluster;
        }

        return bestCluster;
    }

    /**
     * Compute the clustering
     * @param k number of clusters
     * @param respostes set of minimum k RespostaEnquesta without nulls and all of the same Enquesta
     * @return set of k clusters of RespostaEnquesta
     */
    protected abstract ArrayList<RespostaEnquesta>[] computeClustering(int k, ArrayList<RespostaEnquesta> respostes);

    /**
     * Change null answers with the centroid of all the others answers and stores the changed answers 
     * @param respostes answers
     */
    private ArrayList<RespostaEnquesta> imputeNulls(ArrayList<RespostaEnquesta> respostes){
        
        int numRespostes = respostes.size();

        int numPreguntes = respostes.get(0).getRespostes().size();
        if(numPreguntes < 1) throw new IllegalArgumentException("Ha d-haver-hi mínim 1 RespostaAPregunta");

        @SuppressWarnings("unchecked")
        ArrayList<RespostaAPregunta<?>>[] notNullRespostesAPregunta = 
            (ArrayList<RespostaAPregunta<?>>[]) new ArrayList<?>[numPreguntes];

        for (int j = 0; j < numPreguntes; ++j) {
            notNullRespostesAPregunta[j] = new ArrayList<>();
        }

        RespostaAPregunta<?>[][] transposedRespostes = new RespostaAPregunta<?>[numPreguntes][numRespostes];
        Boolean[][] isNull = new Boolean[numRespostes][numPreguntes];

        // Separate null and not-null answers
        for(int i = 0; i < numRespostes; ++i){
            ArrayList<RespostaAPregunta> respostesAPreguntes = respostes.get(i).getRespostes();
            
            for(int j = 0; j < numPreguntes; ++j){
                RespostaAPregunta<?> r = respostesAPreguntes.get(j);
                if(!r.isEmpty()) notNullRespostesAPregunta[j].add(r);
                isNull[i][j] = r.isEmpty();
                
                transposedRespostes[j][i] = r;               
            }
        }

        //Compute the centroid for each question without nulls
        RespostaAPregunta<?>[] centroids = new RespostaAPregunta[numPreguntes];

        for(int j = 0; j < numPreguntes; ++j){
            centroids[j] = notNullRespostesAPregunta[j].get(0).calcularCentroide(notNullRespostesAPregunta[j]);
        }

        originalToImputed = new HashMap<>();
        ArrayList<RespostaEnquesta> imputedRespostes = new ArrayList<>();

        //Replace nulls with centroids
        for(int i = 0; i < numRespostes; ++i){    
            RespostaEnquesta original = respostes.get(i);

            RespostaEnquesta imputed = new RespostaEnquesta();
            imputed.setId(original.getId());
            imputed.associaContainer(original.getContainer());
            
            Boolean respostaWithNull = false;        
            for(int j = 0; j < numPreguntes; ++j){
                if(!isNull[i][j]) imputed.addRespostaAPregunta(transposedRespostes[j][i]);
                else{
                    imputed.addRespostaAPregunta(centroids[j]);
                    respostaWithNull = true;
                }
            }

            if(respostaWithNull){
                originalToImputed.put(original, imputed);
                imputedRespostes.add(imputed);
            }
            else imputedRespostes.add(original);
        }

        return imputedRespostes;
    }

    /**
     * Restore null answers to the cluster
     * @param clusters clusters
     */
    private void restoreNulls(ArrayList<RespostaEnquesta>[] clusters){

        for (Map.Entry<RespostaEnquesta, RespostaEnquesta> entry : originalToImputed.entrySet()) {
            RespostaEnquesta original = entry.getKey();
            RespostaEnquesta imputed = entry.getValue();

            // Replace imputed object with original in every cluster
            for (ArrayList<RespostaEnquesta> list : clusters) {
                int index = list.indexOf(imputed);
                if (index != -1) {
                    list.set(index, original);
                }
            }
        }
    }

    /**
     * Quality of the cluster
     * @param cluster clusters
     * @return (double) quality of the cluster
     */
    public double coefficientCluster(ArrayList<RespostaEnquesta>[] cluster){
        return coefficientClusterStrategy.calcularCoefficient(cluster);
    }

    /**
     * Distance between 2 RespostaEnquesta of the same Enquesta
     * @param r1 first RespostaEnquesta without empty answers
     * @param r2 second RespostaEnquesta without empty answers
     * @return (double) distance between the 2 RespostaEnquesta
     * @throws IllegalArgumentException if r1 and r2 have different order of types of RespostaAPregunta
     */
    protected double distRespostesEnquesta(RespostaEnquesta r1, RespostaEnquesta r2){
        return distanceStrategy.calcularDistRespostesEnquesta(r1, r2);
    }

    /**
     * 2D plot of the clustering using MDS and SMACOF algorithm with clustering information as third dimension
     * @param respostes input of the clustering
     * @param clusters output of the clustering
     * @return 2D plot coordinates for each RespostaEnquesta with clustering information as third dimension
     */
    public double[][] get2DPlot(ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta>[] clusters){
        if(clusters == null) throw new IllegalArgumentException("El cluster està buit");
        if(respostes == null || respostes.size() == 0) throw new IllegalArgumentException("No hi ha cap resposta.");

        double[][] plot = plot2D(respostes, 1000, 1e-12, 10, clusters);
        normalize(plot);

        double[][] finalPlot = addClusteringToPlot(plot, respostes, clusters);
        return finalPlot;
    }


    /**
     * 2D plot of the clustering using MDS and SMACOF algorithm
     * @param respostes input of the clustering
     * @param maxIter maximum number of iterations
     * @param tol tolerance for convergence
     * @param restarts number of restarts
     * @param clusters output of the clustering
     * @return 2D plot coordinates for each RespostaEnquesta
     */
    private double[][] plot2D(ArrayList<RespostaEnquesta> respostes,
            int maxIter, double tol,  int restarts, ArrayList<RespostaEnquesta>[] clusters) {
       
       // compute target distance matrix (Manhattan/normalized via your function)
        double[][] delta = distanceMatrix(respostes);

        double bestStress = Double.POSITIVE_INFINITY;
        double[][] bestX = null;

        for (int run = 0; run < Math.max(1, restarts); run++) {
            double[][] X = init2D(respostes, clusters, run);
            
            double[][] finalX = SMACOF(X, delta, maxIter, tol);
            double stress = computeStress(delta, distanceEuclidianMatrix(finalX));

            if (stress < bestStress) {
                bestStress = stress;
                bestX = finalX;
            }
        }

        return bestX;
    }

    /**
     * SMACOF (Scaling by MAjorizing a COmplicated Function) algorithm for MDS optimization 
     * @param X initial 2D coordinates matrix
     * @param delta target distance matrix
     * @param maxIter maximum number of iterations before stopping
     * @param tol relative tolerance for convergence (stops when stress improvement < tol)
     * @return optimized 2D coordinates matrix after convergence or maxIter reached
     */
    private double[][] SMACOF(double[][] X, double[][] delta, int maxIter, double tol){
        double[][] d = distanceEuclidianMatrix(X);
        double previousStress = Double.POSITIVE_INFINITY;

        for (int iter = 0; iter < maxIter; iter++) {
            double[][] B = gutmannTransform(delta, d);
            double[][] Xnew = newCoordinates(X, B);
            // center after update
            centerCoordinates(Xnew);

            // compute stress using current Euclidean distances (d) or recompute after update
            double[][] dNew = distanceEuclidianMatrix(Xnew);
            double stress = computeStress(delta, dNew);

            // stopping criterion: relative improvement
            if (previousStress != Double.POSITIVE_INFINITY) {
                double rel = (previousStress - stress) / previousStress;
                if (rel > 0 && rel < tol) {
                    X = Xnew;
                    d = dNew;
                    break;
                }
            }

            // update
            X = Xnew;
            d = dNew;
            previousStress = stress;
        }

        return X;
    }

    /**
     * Distance matrix between all RespostaEnquesta
     * @param respostes set of RespostaEnquesta without nulls and all of the same Enquesta
     * @return distance matrix
     */
    private double[][] distanceMatrix(ArrayList<RespostaEnquesta> respostes) {
        int n = respostes.size();
        double[][] distMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = 0.0;
                dist = distRespostesEnquesta(respostes.get(i), respostes.get(j));
                distMatrix[i][j] = dist;
                distMatrix[j][i] = dist;
            }
        }
        return distMatrix;
    }

    /**
     * Euclidian distance matrix between all points in X
     * @param X array of points
     * @return distance matrix
     */
    private static double[][] distanceEuclidianMatrix(double[][] X) {
        int n = X.length;
        double[][] distMatrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double dist = 0.0;
                double dx = X[i][0] - X[j][0];
                double dy = X[i][1] - X[j][1];
                dist = Math.sqrt(dx * dx + dy * dy);
                
                distMatrix[i][j] = dist;
                distMatrix[j][i] = dist;
            }
        }
        return distMatrix;
    }

    
    /**
     * Initialize 2D coordinates in a circular layout with jitter
     * @param respostes input of the clustering
     * @param clusters output of the clustering
     * @param run current run number for seeding jitter
     * @return 2D coordinates for each RespostaEnquesta
     */
    private double[][] init2D(ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta>[] clusters, int run)
    {
        int n = respostes.size();
        int k = clusters.length;
        double[][] X = new double[n][2];

        // Clusters centers in a circle
        double R = 10; 
        double[][] clusterCenters = new double[k][2];

        for (int c = 0; c < k; c++) {
            double angle = 2 * Math.PI * c / k;
            clusterCenters[c][0] = R * Math.cos(angle);
            clusterCenters[c][1] = R * Math.sin(angle);
        }

        for (int i = 0; i < n; i++) {

            // find cluster id
            RespostaEnquesta r = respostes.get(i);
            int c = -1;
            for (int j = 0; j < k; j++) {
                if (clusters[j].contains(r)) c = j;
            }
            if (c == -1) throw new RuntimeException("Resposta no trobada en cap cluster.");

            // Hash
            long seed = (long) run * 1315423911L + i * 2654435761L;

            // Deterministic jitter based on: (run, i) - pseudo-random (deterministic but depending on the run)
            double jitterScale = 0.4;

            double jitterX = (((seed >> 8) & 0xFFFF) / 65535.0 - 0.5) * jitterScale;
            double jitterY = (((seed >> 24) & 0xFFFF) / 65535.0 - 0.5) * jitterScale;

            X[i][0] = clusterCenters[c][0] + jitterX;
            X[i][1] = clusterCenters[c][1] + jitterY;
        }
        centerCoordinates(X);

        return X;
    }


    /**
     * Center the coordinates around the origin
     * @param X array of points
     */
    private static void centerCoordinates(double[][] X) {
        int n = X.length;
        double meanX = 0, meanY = 0;

        for (int i = 0; i < n; i++) {
            meanX += X[i][0];
            meanY += X[i][1];
        }

        meanX /= n;
        meanY /= n;

        for (int i = 0; i < n; i++) {
            X[i][0] -= meanX;
            X[i][1] -= meanY;
        }
    }

    /**
     * Gutmann transform of a distance matrix
     * @param delta distance matrix
     * @param d Euclidian distance matrix
     * @return Gutmann transformed matrix B
     */
    private static double[][] gutmannTransform(double[][] delta, double[][] d) {
        int n = delta.length;
        double[][] B = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    B[i][j] = 0;
                } else if (delta[i][j] == 0) {
                    B[i][j] = 0;
                } else{
                    double dHat = Math.max(d[i][j], 1e-6);  // tiny for safety
                    double ratio = delta[i][j] / dHat;

                    B[i][j] = -ratio;
                }
            }
        }


        // Set diagonal elements, rows sum to 0
        for(int i = 0; i < n; i++) {
            double sum = 0.0;
            for(int j = 0; j < n; j++) sum += B[i][j];
            B[i][i] = -sum;
        }

        return B;
    }

    /**
     * Compute new coordinates using B matrix
     * @param X current coordinates
     * @param B Gutmann transformed matrix
     * @return new coordinates
     */
    private static double[][] newCoordinates(double[][] X, double[][] B) {
        int n = X.length;
        double[][] newX = new double[n][2];

        for(int i = 0; i < n; i++) {
            newX[i][0] = 0.0;
            newX[i][1] = 0.0;

            for(int j = 0; j < n; j++) {
                newX[i][0] += B[i][j] * X[j][0];
                newX[i][1] += B[i][j] * X[j][1];
            }

            double w = -B[i][i];
            if (w < 1e-12) w = 1e-12;

            newX[i][0] /= w;
            newX[i][1] /= w;
        }

        return newX;
    }

    /**
     * Compute stress between target distances and Euclidian distances
     * @param delta distance matrix
     * @param d Euclidian distance matrix
     * @return stress value
     */
    private static double computeStress(double[][] delta, double[][] d) {
        int n = delta.length;
        double stress = 0.0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double diff = delta[i][j] - d[i][j];
                stress += diff * diff;
            }
        }
        return stress;
    }

    /**
     * Normalize the 2D plot to [0,1] range in both dimensions
     * @param X array of points
     */
    private void normalize(double[][] X) {
        
        // Find min and max for each dimension
        double minX = X[0][0], maxX = X[0][0];
        double minY = X[0][1], maxY = X[0][1];
        
        for (int i = 1; i < X.length; i++) {
            if (X[i][0] < minX) minX = X[i][0];
            if (X[i][0] > maxX) maxX = X[i][0];
            if (X[i][1] < minY) minY = X[i][1];
            if (X[i][1] > maxY) maxY = X[i][1];
        }
        
        // Normalize each dimension to [0,1]
        double rangeX = maxX - minX;
        double rangeY = maxY - minY;
        
        for (int i = 0; i < X.length; i++) {
            if (rangeX > 0) X[i][0] = (X[i][0] - minX) / rangeX;
            if (rangeY > 0) X[i][1] = (X[i][1] - minY) / rangeY;
        }
    }

    /**
     * Add clustering information to the plot by adding the number of the corresponding cluster as a third dimension
     * @param plot original 2D plot coordinates
     * @param respostes input of the clustering
     * @param clusters output of the clustering
     * @return adjusted 2D plot coordinates
     */
    private double[][] addClusteringToPlot(double[][] plot, ArrayList<RespostaEnquesta> respostes, ArrayList<RespostaEnquesta>[] clusters) {
        int n = respostes.size();
        int k = clusters.length;
        double[][] adjustedPlot = new double[n][3];

        for (int i = 0; i < n; i++) {
            // find cluster id
            RespostaEnquesta r = respostes.get(i);
            int c = -1;
            for (int j = 0; j < k; j++) {
                if (clusters[j].contains(r)) c = j;
            }

            adjustedPlot[i][0] = plot[i][0];
            adjustedPlot[i][1] = plot[i][1];
            adjustedPlot[i][2] = c; // cluster id as third dimension
        }

        return adjustedPlot;
    }

    /**
     * Get the centroids of the clusters
     * @return list of centroids
     */
    public abstract ArrayList<RespostaEnquesta> getCentroids();  

}
