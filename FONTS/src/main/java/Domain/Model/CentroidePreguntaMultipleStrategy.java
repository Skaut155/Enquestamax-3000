package Domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Centroide strategy for multiple choice question responses ("RespostaAPreguntaMultiple").
 */
public class CentroidePreguntaMultipleStrategy implements CentroideStrategy {
    private static CentroidePreguntaMultipleStrategy instance;

    /**
     * Get the singleton instance of CentroidePreguntaMultipleStrategy
     * @return singleton instance of CentroidePreguntaMultipleStrategy
     */
    public static CentroidePreguntaMultipleStrategy getInstance() {
        if (instance == null) {
            instance = new CentroidePreguntaMultipleStrategy();
        }
        return instance;
    }

    /**
     * Centroide of a set of RespostaAPregunta of the class RespostaAPreguntaMultiple
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    @Override
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes) {
        if(respostes.size() == 0) throw new IllegalArgumentException("Ha d'haver-hi minim una resposta");

        Map<ArrayList<Integer>, Integer> freqOpcions = new HashMap<ArrayList<Integer>, Integer>();

        for(RespostaAPregunta<?> resposta : respostes){
            if(!(resposta instanceof RespostaAPreguntaMultiple))
                throw new IllegalArgumentException("Resposta no és de tipus multiple");

            RespostaAPreguntaMultiple respostaMultiple = (RespostaAPreguntaMultiple) resposta;
            ArrayList<Integer> opcions = respostaMultiple.getResposta();   
            
            // Recalculate frequencies of options
            freqOpcions.put(opcions, freqOpcions.getOrDefault(opcions, 0) + 1);
        }
        // Take the most common option
        return new RespostaAPreguntaMultiple(maxFreq(freqOpcions), respostes.get(0).getOrdrePregunta());
    }

}
