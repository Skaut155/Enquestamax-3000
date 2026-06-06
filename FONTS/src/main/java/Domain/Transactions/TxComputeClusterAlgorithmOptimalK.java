package Domain.Transactions;

import Domain.Controllers.IEnquestaCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.*;

import java.util.ArrayList;

public class TxComputeClusterAlgorithmOptimalK implements Transaction {
    private String algorithmName;
    private double thresholdRatio;
    private int idEnquesta;

    public int[][] result;

    public TxComputeClusterAlgorithmOptimalK(int idEnquesta, String algorithmName, double thresholdRatio) {
        this.idEnquesta = idEnquesta;
        this.algorithmName = algorithmName;
        this.thresholdRatio = thresholdRatio;
    }

    public void execute() {
        IEnquestaCtrl ectrl = CtrlFactory.getInstance().GetEnquestaCtrl();
        Enquesta e = ectrl.GetEnquesta(idEnquesta);
        ArrayList<RespostaEnquesta> respostes = e.getAllRespostes();

        AlgoritmeClustering algoritme = switch (algorithmName) {
            case "K_MEANS_RANDOM" -> new K_meansRandom();
            case "K_MEANS_PLUS_PLUS" -> new K_meansPlusPlus();
            case "K_MEDOIDS" -> new Kmedoids();
            default ->
                    throw new IllegalArgumentException("Algoritme no reconegut, ha de ser K_MEANS_RANDOM, K_MEANS_PLUS_PLUS o K_MEDOIDS");
        };

        ArrayList<RespostaEnquesta>[] clusters = algoritme.computeBestAlgorithm(respostes, thresholdRatio);

        // Convert to array of arrays of IDs
        result = new int[clusters.length][];
        for (int i = 0; i < clusters.length; i++) {
            result[i] = new int[clusters[i].size()];
            for (int j = 0; j < clusters[i].size(); j++) {
                result[i][j] = clusters[i].get(j).getId();
            }
        }
    }
}