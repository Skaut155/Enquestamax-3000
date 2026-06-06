package Domain.Model;

/**
 * Class RespostaAPreguntaOrdenada represents a single choice ordered question answer
 */
public class RespostaAPreguntaOrdenada extends RespostaAPreguntaUnica {

    /**
     * Constructor RespostaAPreguntaOrdenada empty
     * @param ordrePregunta Order of the question in the survey
     */
    public RespostaAPreguntaOrdenada(int ordrePregunta){
        super(-1, ordrePregunta, CentroidePreguntaOrdenadaStrategy.getInstance());
        setCode("UNICA_ORDENADA");
    }

    /**
     * Constructor of RespostaAPreguntaOrdenada
     * @param opcionsSeleccionades set of order of Opcio that has been selected
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaOrdenada(Integer opcionsSeleccionades, int ordrePregunta) {
        super(opcionsSeleccionades, ordrePregunta, CentroidePreguntaOrdenadaStrategy.getInstance());
        setCode("UNICA_ORDENADA");
    }

    /**
     * Get string representation of the answer
     * @return answer as a string
     */
    @Override
    public String getStringResposta() {
        if (this.isEmpty()) return "No hi ha resposta";
        Integer opcioIndex = super.getResposta() + 1;
        return Integer.toString(opcioIndex);
    }
    
    /**
     * True if the answer is empty, false otherwise
     * @return resposta == null or -1
     */
    @Override
    public boolean isEmpty(){
        return super.getResposta() == null || super.getResposta() == -1;
    }

    /**
     * Distance between this RespostaAPreguntaOrdenada and another RespostaAPreguntaOrdenada
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPreguntaOrdenada and r
     */
    @Override
    public double distRespostesPregunta(RespostaAPregunta<Integer> r){
    	double s1 = this.getResposta();
    	double s2 = r.getResposta();
    	int st = this.getNumOpcionsPregunta();
    	return Math.abs(s1 - s2) / (st - 1);
    }
    
}
