package Domain.Transactions;

import java.util.ArrayList;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.AlgoritmeClustering;
import Domain.Model.CoefficientCalinskiHarabaszStrategy;
import Domain.Model.CoefficientClusterStrategy;
import Domain.Model.CoefficientDunnStrategy;
import Domain.Model.CoefficientSilouheteStrategy;
import Domain.Model.DistanceEuclideanStrategy;
import Domain.Model.DistanceManhattanStrategy;
import Domain.Model.DistanceStrategy;
import Domain.Model.Enquesta;
import Domain.Model.K_meansPlusPlus;
import Domain.Model.K_meansRandom;
import Domain.Model.Kmedoids;
import Domain.Model.Pregunta;
import Domain.Model.RespostaEnquesta;

public class TxPlot2D implements Transaction{
    
    private int idEnquesta;

    private Integer k = null;
    private double thresholdRatio;
    
    private String algorithmName;
    private String distanceStrategyName;
    private String coefficientStrategyName;

    private ArrayList<RespostaEnquesta>[] clusters;
    private ArrayList<RespostaEnquesta> respostes;
    private AlgoritmeClustering algoritme;

    public TxPlot2D(int idEnquesta, String algorithmName, int k, String distanceStrategyName, String coefficientStrategyName) {
        this.idEnquesta = idEnquesta;
        this.algorithmName = algorithmName;
        this.distanceStrategyName = distanceStrategyName;
        this.coefficientStrategyName = coefficientStrategyName;
        this.k = k;
    }

    public TxPlot2D(int idEnquesta, String algorithmName, double thresholdRatio, String distanceStrategyName, String coefficientStrategyName) {
        this.idEnquesta = idEnquesta;
        this.algorithmName = algorithmName;
        this.distanceStrategyName = distanceStrategyName;
        this.coefficientStrategyName = coefficientStrategyName;
        this.thresholdRatio = thresholdRatio;
    }

    public void execute(){
        IEnquestaCtrl ectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = ectrl.GetEnquesta(idEnquesta);
        respostes = e.getAllRespostes();

        algoritme = switch (algorithmName) {
            case "K_MEANS_RANDOM" -> new K_meansRandom();
            case "K_MEANS_PLUS_PLUS" -> new K_meansPlusPlus();
            case "K_MEDOIDS" -> new Kmedoids();
            default ->
                    throw new IllegalArgumentException("Algoritme no reconegut, ha de ser K_MEANS_RANDOM, K_MEANS_PLUS_PLUS o K_MEDOIDS");
        };

        DistanceStrategy distance = switch(distanceStrategyName){
            case "DISTANCIA_MANHATTAN" -> DistanceManhattanStrategy.getInstance();
            case "DISTANCIA_EUCLIDEANA" -> DistanceEuclideanStrategy.getInstance();
            default ->
                    throw new IllegalArgumentException("Distancia no reconeguda, ha de ser DISTANCIA_MANHATTAN o DISTANCIA_EUCLIDEANA");
        };

        CoefficientClusterStrategy coefficient = switch(coefficientStrategyName){
            case "COEFICIENT_DE_SILHOUETE" -> CoefficientSilouheteStrategy.getInstance();
            case "COEFICIENT_DE_CALINSKI" -> CoefficientCalinskiHarabaszStrategy.getInstance();
            case "COEFICIENT_DE_DUNN" -> CoefficientDunnStrategy.getInstance();
            default ->
                    throw new IllegalArgumentException("Coeficient no reconegut, ha de ser COEFICIENT_DE_SILHOUETE, COEFICIENT_DE_CALINSKI o COEFICIENT_DE_DUNN");
        };

        algoritme.setDistanceStrategy(distance);
        algoritme.setCoefficientClusterStrategy(coefficient);

        if(k == null) clusters = algoritme.computeBestAlgorithm(respostes, thresholdRatio);
        else clusters = algoritme.computeAlgorithm(k, respostes);
    }

    public double[][] getPlot2D(){
        return algoritme.get2DPlot(respostes, clusters);
    }

    public double getCoefficient(){
        return algoritme.coefficientCluster(clusters);
    }

    public int getK(){
        if(k == null) return clusters.length;
        else return this.k;
    }

    public String[] getCentroidsStrings(){
        IEnquestaCtrl enq_ctrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta enq = enq_ctrl.GetEnquesta(idEnquesta);
        ArrayList<RespostaEnquesta> centroids = algoritme.getCentroids();
        String[] centroidsStrings = new String[centroids.size()];

        for(int i = 0; i < centroids.size(); ++i){

            RespostaEnquesta r = centroids.get(i);
            String[] valuesString = r.getRespostesString();
            centroidsStrings[i] = "";

            for (int j = 0; j < enq.numPreguntes(); ++j) {
                Pregunta p = enq.getPregunta(j);

                centroidsStrings[i] += p.toString();
                centroidsStrings[i] += "Resposta: " + valuesString[j] + "\n";
            }
        }
        return centroidsStrings;
    }

}
