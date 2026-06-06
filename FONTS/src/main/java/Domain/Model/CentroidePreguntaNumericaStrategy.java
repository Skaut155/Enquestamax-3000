package Domain.Model;

import java.util.ArrayList;

/**
 * Centroide strategy for quantitative question responses (RespostaAPreguntaNumerica).
 */
public class CentroidePreguntaNumericaStrategy implements CentroideStrategy {
   private static CentroidePreguntaNumericaStrategy instance;

    /**
     * Get the singleton instance of CentroidePreguntaNumericaStrategy
     * @return singleton instance of CentroidePreguntaNumericaStrategy
     */
    public static CentroidePreguntaNumericaStrategy getInstance() {
        if (instance == null) {
            instance = new CentroidePreguntaNumericaStrategy();
        }
        return instance;
    }

    /**
     * Centroide of a set of RespostaAPregunta of the class RespostaAPreguntaNumerica
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    @Override
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes) {
        if(respostes.size() == 0) throw new IllegalArgumentException("Ha d'haver-hi minim una resposta");
        
        double sum = 0;
        for(RespostaAPregunta<?> resposta : respostes){
            if(!(resposta instanceof RespostaAPreguntaNumerica))
                throw new IllegalArgumentException("Resposta no és de tipus numèrica");

            RespostaAPreguntaNumerica respostaNumerica = (RespostaAPreguntaNumerica) resposta;
            Double num = respostaNumerica.getResposta();  

            sum += num;
        }

        return new RespostaAPreguntaNumerica(sum/respostes.size(), respostes.get(0).getOrdrePregunta());
    }
    
}