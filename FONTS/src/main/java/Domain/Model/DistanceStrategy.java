package Domain.Model;

import java.util.ArrayList;

/**
 * Strategy interface for calculating the distances between two RespostaEnquesta
 */
public abstract class DistanceStrategy {
    
    /**
     * Distance between 2 RespostaEnquesta of the same Enquesta
     * @param r1 first RespostaEnquesta without empty answers
     * @param r2 second RespostaEnquesta without empty answers
     * @return (double) distance between the 2 RespostaEnquesta
     * @throws IllegalArgumentException if r1 and r2 have different order of types of RespostaAPregunta
     */
    public double calcularDistRespostesEnquesta(RespostaEnquesta r1, RespostaEnquesta r2){
        if(r1 == null || r2 == null)
            throw new IllegalArgumentException("Les respostes no poden ser null");
        
        ArrayList<RespostaAPregunta> respostes1 = r1.getRespostes();
        ArrayList<RespostaAPregunta> respostes2 = r2.getRespostes();

        int size = respostes1.size();
        
        if(size != respostes2.size()){
            throw new IllegalArgumentException("Les respostes han de tenir el mateix nombre de RespostaAPregunta");
        }

        return calcularDistCorrectRespostesEnquesta(respostes1, respostes2);
    }

    /**
     * Distance between 2 arrays of RespostaAPregunta with the same size
     * @param r1 first RespostaEnquesta without empty answers
     * @param r2 second RespostaEnquesta without empty answers
     * @return (double) distance between the 2 RespostaEnquesta
     * @throws IllegalArgumentException if r1 and r2 have different order of types of RespostaAPregunta
     */
    protected abstract double calcularDistCorrectRespostesEnquesta(ArrayList<RespostaAPregunta> r1, ArrayList<RespostaAPregunta> r2);

    /**
     * Compute between 2 RespostaAPregunta of the same Enquesta of the same type
     * @param a first RespostaAPregunta
     * @param b second RespostaAPregunt
     * @return (double) distance between the 2 RespostaEnquesta
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static double computeDistanceSameType(RespostaAPregunta<?> a, RespostaAPregunta<?> b) {
        // Both have the same runtime type, so the cast is safe
        return ((RespostaAPregunta) a).distRespostesPregunta(b);
    }
}
