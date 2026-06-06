package Domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Centroide strategy for ordered qualitative question responses (RespostaAPreguntaNoOrdenada).
 */
public class CentroidePreguntaOrdenadaStrategy implements CentroideStrategy {
    private static CentroidePreguntaOrdenadaStrategy instance;

    /**
     * Get the singleton instance of CentroidePreguntaOrdenadaStrategy
     * @return singleton instance of CentroidePreguntaOrdenadaStrategy
     */
    public static CentroidePreguntaOrdenadaStrategy getInstance() {
        if (instance == null) {
            instance = new CentroidePreguntaOrdenadaStrategy();
        }
        return instance;
    }

    /**
     * Centroide of a set of RespostaAPregunta of the class RespostaAPreguntaOrdenada
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    @Override
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes) {
        if(respostes.size() == 0) throw new IllegalArgumentException("Ha d'haver-hi minim una resposta");
        
        Map<Integer, Integer> freqOpcions = new HashMap<Integer, Integer>();
        
        for(RespostaAPregunta<?> resposta : respostes){
            if(!(resposta instanceof RespostaAPreguntaOrdenada))
                throw new IllegalArgumentException("Resposta no és de tipus ordenada");

            RespostaAPreguntaOrdenada respostaOrdenada = (RespostaAPreguntaOrdenada) resposta;
            Integer opcio = respostaOrdenada.getResposta();
            
            // Recalculate frequencies of options
            freqOpcions.put(opcio, freqOpcions.getOrDefault(opcio, 0) + 1);
        }
        
        return new RespostaAPreguntaOrdenada(maxFreq(freqOpcions), respostes.get(0).getOrdrePregunta());
    }
    
}