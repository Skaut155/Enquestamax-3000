package Domain.Model;

/**
 * Abstract class RespostaAPreguntaUnica represents a single-choice question answer
 */
public abstract class RespostaAPreguntaUnica extends RespostaAPreguntaSeleccio<Integer> {

    /**
     * Constructor of RespostaAPreguntaUnica
     * @param opcionsSeleccionades set of order of Opcio with only one option selected
     * @param ordrePregunta order of Pregunta that it answers
     * @param cs the strategy used to calculate the centroide
     */
    public RespostaAPreguntaUnica(Integer opcionsSeleccionades, int ordrePregunta, CentroideStrategy cs) {
        super(opcionsSeleccionades, ordrePregunta, cs);
        setCode("PREGUNTA_UNICA");
    }
}