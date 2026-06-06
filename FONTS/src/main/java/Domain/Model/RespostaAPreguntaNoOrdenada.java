package Domain.Model;

/**
 * Class RespostaAPreguntaNoOrdenada that represents an answer to a single choice unordered question
 */
public class RespostaAPreguntaNoOrdenada extends RespostaAPreguntaUnica {
    /**
     * Constructor RespostaAPreguntaNoOrdenada empty
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaNoOrdenada(int ordrePregunta){
        super(-1 , ordrePregunta, CentroidePreguntaNoOrdenadaStrategy.getInstance());
        setCode("UNICA_NO_ORDENADA");
    }

    /**
     * Constructor of RespostaAPreguntaNoOrdenada
     * @param opcionsSeleccionades set of order of Opcio with only one option selected
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaNoOrdenada(Integer opcionsSeleccionades, int ordrePregunta) {
        super(opcionsSeleccionades, ordrePregunta, CentroidePreguntaNoOrdenadaStrategy.getInstance());
        setCode("UNICA_NO_ORDENADA");
    }

    /**
     * String representation of the answer
     * @return String with the selected option (1-based index)
     */
    @Override
    public String getStringResposta() {
        if(isEmpty()) return "No hi ha resposta";
        Integer opcioIndex = super.getResposta() + 1;
        return opcioIndex.toString();
    }

    /**
     * True if the answer is empty, false otherwise
     * @return resposta == null or empty string
     */
    @Override
    public boolean isEmpty() {
        return super.getResposta() == null || super.getResposta() == -1;
    }

    /**
     * Distance between this RespostaAPreguntaNoOrdenada and another RespostaAPreguntaNoOrdenada
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPreguntaNoOrdenada and r
     */
    @Override
    public double distRespostesPregunta(RespostaAPregunta<Integer> r) {
        RespostaAPreguntaNoOrdenada r2 = (RespostaAPreguntaNoOrdenada) r;

        if(this.getResposta() == r2.getResposta()) return 0.0;
        else return 1.0;
    }
}