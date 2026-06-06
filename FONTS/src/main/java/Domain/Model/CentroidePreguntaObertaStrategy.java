package Domain.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Centroide strategy for open question responses (RespostaAPreguntaOberta).
 */
public class CentroidePreguntaObertaStrategy implements CentroideStrategy {
    private static CentroidePreguntaObertaStrategy instance;

    /**
     * Get the singleton instance of CentroidePreguntaObertaStrategy
     * @return singleton instance of CentroidePreguntaObertaStrategy
     */
    public static CentroidePreguntaObertaStrategy getInstance() {
        if(instance == null) {
            instance = new CentroidePreguntaObertaStrategy();
        }
        return instance;
    }

    /**
     * Centroide of a set of RespostaAPregunta of the class RespostaAPreguntaOberta
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    @Override
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes) {
        if(respostes.size() == 0) throw new IllegalArgumentException("Ha d'haver-hi minim una resposta");

        Map<String, Integer> paraulesFreq = new HashMap<String, Integer>();
        
        for(RespostaAPregunta<?> resposta : respostes) {
            // Assegurar que la resposta és de tipus oberta
            if(!(resposta instanceof RespostaAPreguntaOberta))
                throw new IllegalArgumentException("Resposta no és de tipus oberta");

            // Obtenir les paraules freqüents de la resposta
            RespostaAPreguntaOberta respostaOberta = (RespostaAPreguntaOberta) resposta;
            String textResposta = respostaOberta.getResposta();
            ArrayList<String> paraules = getParaulesFrequents(textResposta);

            // Actualitzar el mapa de freqüències
            addSubsetsParaules(paraules, paraulesFreq);
        }

        return new RespostaAPreguntaOberta(maxFreq(paraulesFreq), respostes.get(0).getOrdrePregunta());
    }

    /**
     * Get all the semantic filtered words 
     * @param resposta string with words
     * @return Array with each position a semantic filtered word of resposta
     */
    private ArrayList<String> getParaulesFrequents(String resposta) {
        String[] paraulesArray = resposta.split(" ");

        ArrayList<String> paraules = new ArrayList<String>();
        for(String paraula : paraulesArray) {
            String filteredParaula = filterParaula(paraula);
            if(filteredParaula == "") continue;
            paraules.add(filteredParaula);
        }

        return paraules;
    }

    /**
     * Filtered word
     * @param paraula word to filter
     * @return word with only the letters and converted all to lower case or empty string if there are less than 3 letters
     */
    private String filterParaula(String paraula) {
        // Delete non alfabetical characters and lower case all
        String filteredParaula = paraula.replaceAll("[^a-zA-Z]", "").toLowerCase();

        if(filteredParaula.length() < 3) return "";
        return filteredParaula;
    }

    /**
     * Add to a map of frequencies the frequency of all the posbile combinations of an Array of words
     * @param paraules array of words
     * @param paraulesFreq map of frequencies of words
     */
    private void addSubsetsParaules(ArrayList<String> paraules, Map<String, Integer> paraulesFreq) {
        for(String paraula : paraules) {
            paraulesFreq.put(paraula, paraulesFreq.getOrDefault(paraula, 0) + 1);
            for(String altraParaula : paraules) {
                if(!paraula.equals(altraParaula)) {
                    String combinacio = paraula + " " + altraParaula;
                    paraulesFreq.put(combinacio, paraulesFreq.getOrDefault(combinacio, 0) + 1);
                }
            }
        }
    }
    
}
