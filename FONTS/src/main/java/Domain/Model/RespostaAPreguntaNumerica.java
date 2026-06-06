package Domain.Model;

/**
 * Class RespostaAPreguntaNumerica represents a numeric question answer
 */
public class RespostaAPreguntaNumerica extends RespostaAPregunta<Double> {

    /**
     * Constructor RespostaAPreguntaNumerica empty
     * @param ordrePregunta Order of the question in the survey
     */
    public RespostaAPreguntaNumerica(int ordrePregunta){
       super(null, ordrePregunta, CentroidePreguntaNumericaStrategy.getInstance());
       setCode("NUMERICA");
    }

    /**
     * Constructor for RespostaAPreguntaNumerica
     * @param resposta number answered
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaNumerica(double resposta, int ordrePregunta) {
        super(resposta, ordrePregunta, CentroidePreguntaNumericaStrategy.getInstance());
        setCode("NUMERICA");
    }

    /**
     * Get string representation of the answer
     * @return answer as a string
     */
    @Override
    public String getStringResposta() {
        if (super.isEmpty()) return "No hi ha resposta";
        return String.valueOf(super.getResposta());
    }

    /**
     * Distance between this RespostaAPreguntaNumerica and another RespostaAPreguntaNumerica
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPreguntaNumerica and r
     */
    @Override
    public double distRespostesPregunta(RespostaAPregunta<Double> r){
        if (this.calcMax() == this.calcMin()) return 0.0;
    	return Math.abs(this.getResposta() - r.getResposta()) / ((this.calcMax() - this.calcMin()));
    }
    
}
