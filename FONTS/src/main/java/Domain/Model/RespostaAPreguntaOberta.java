package Domain.Model;

/**
 * Class RespostaAPreguntaOberta represents an open question answer
 */
public class RespostaAPreguntaOberta extends RespostaAPregunta<String> {

    /**
     * Constructor RespostaAPreguntaOberta empty
     * @param ordrePregunta order of the Pregunta that it answers
     */
    public RespostaAPreguntaOberta(int ordrePregunta){
        super("", ordrePregunta, CentroidePreguntaObertaStrategy.getInstance());
        setCode("OBERTA");
    }

    /**
     * Constructor for RespostaAPreguntaOberta
     * @param resposta string answered
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaOberta(String resposta, int ordrePregunta) {
        super(resposta, ordrePregunta, CentroidePreguntaObertaStrategy.getInstance());
        setCode("OBERTA");
    }

    /**
     * String representation of the answer
     * @return String with the answer
     */
    @Override
    public String getStringResposta() {
        if(isEmpty()) return "No hi ha resposta";
        return super.getResposta();
    }

    /**
     * True if the answer is empty, false otherwise
     * @return resposta == null or empty string
     */
    @Override
    public boolean isEmpty(){
        return super.getResposta() == null || super.getResposta() == "";
    }

    /**
     * Distance between this RespostaAPreguntaOberta and another RespostaAPreguntaOberta
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPreguntaOberta and r
     */
    @Override
    public double distRespostesPregunta(RespostaAPregunta<String> r) {
        String s1 = this.getResposta();
        RespostaAPreguntaOberta r2 = (RespostaAPreguntaOberta) r;
        String s2 = r2.getResposta();

        int len1 = s1.length();
        int len2 = s2.length();
        
        int maxLen = Math.max(len1, len2);
        int lenDiff = Math.abs(len1 - len2);
        
        double dist = distLevenshtein(s1, s2, len1, len2);
        return (dist - lenDiff) / (maxLen - lenDiff);
    }

    /**
     * Levenshtein distance between 2 strings - Space optimized O(min(len1,len2))
     */
    private double distLevenshtein(String s1, String s2, int len1, int len2) {
        // Ensure s1 is the shorter string to save memory
        if (len1 > len2) {
            String tmp = s1; s1 = s2; s2 = tmp;
            int t = len1; len1 = len2; len2 = t;
        }
        
        double[] prev = new double[len1 + 1];
        double[] curr = new double[len1 + 1];
        
        // Initialize base case
        for (int i = 0; i <= len1; i++) {
            prev[i] = i;
        }
        
        // Fill the arrays row by row
        for (int j = 1; j <= len2; j++) {
            curr[0] = j;
            char c2 = s2.charAt(j - 1);
            
            for (int i = 1; i <= len1; i++) {
                char c1 = s1.charAt(i - 1);
                double cost = (c1 == c2) ? 0.0 : 1.0;
                curr[i] = Math.min(
                    Math.min(curr[i - 1] + 1, prev[i] + 1),
                    prev[i - 1] + cost
                );
            }
            
            // Swap arrays
            double[] tmp = prev;
            prev = curr;
            curr = tmp;
        }
        
        return prev[len1];
    }
}
