package Domain.Model;

import java.util.ArrayList;
import java.util.Map;

/**
 * Strategy interface for calculating the centroid of a set of survey question responses ("RespostaAPregunta").
 */
public interface CentroideStrategy {

    /**
     * Centroide of a set of RespostaAPregunta of the same type
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes);
    
    /**
     * Most common element in a map of frequencies
     * @param <T> type of element
     * @param freqElements map key elements, value frequency of the element
     * @return key with the highest value
     */
    default public <T> T maxFreq(Map<T, Integer> freqElements){
        T elemFreq = freqElements.keySet().iterator().next();
        int maxFreq = -1;
        for(T elem : freqElements.keySet()){
            int freq = freqElements.get(elem);
            if(freq > maxFreq){
                maxFreq = freq;
                elemFreq = elem;
            }
        }
        return elemFreq;
    }
}
