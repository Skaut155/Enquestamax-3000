package Domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Centroide strategy for ordered qualitative question responses (RespostaAPreguntaNoOrdenada).
 */
public class CentroidePreguntaNoOrdenadaStrategy implements CentroideStrategy {
   private static CentroidePreguntaNoOrdenadaStrategy instance;

    /**
     * Get the singleton instance of CentroidePreguntaNoOrdenadaStrategy
     * @return singleton instance of CentroidePreguntaNoOrdenadaStrategy
     */
    public static CentroidePreguntaNoOrdenadaStrategy getInstance() {
        if (instance == null) {
            instance = new CentroidePreguntaNoOrdenadaStrategy();
        }
        return instance;
    }

    /**
     * Centroide of a set of RespostaAPregunta of the class RespostaAPreguntaNoOrdenada
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    @Override
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes) {
        if(respostes.size() == 0) throw new IllegalArgumentException("Ha d'haver-hi minim una resposta");

        Map<Integer, Integer> freqOpcions = new HashMap<Integer, Integer>();
        
        for(RespostaAPregunta<?> resposta : respostes){
            if(!(resposta instanceof RespostaAPreguntaNoOrdenada))
                throw new IllegalArgumentException("Resposta no és de tipus no-ordenada");

            RespostaAPreguntaNoOrdenada respostaNoOrdenada = (RespostaAPreguntaNoOrdenada) resposta;
            Integer opcio = respostaNoOrdenada.getResposta();   
            
            // Recalculate frequencies of options
            freqOpcions.put(opcio, freqOpcions.getOrDefault(opcio, 0) + 1);
        }
        
        return new RespostaAPreguntaNoOrdenada(maxFreq(freqOpcions), respostes.get(0).getOrdrePregunta());
    }
    
}
