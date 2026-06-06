package Domain.Model;

/**
 * Abstract class RespostaAPreguntaSeleccio represents a selection question answer
 * @param <T> type of the response
 */
public abstract class RespostaAPreguntaSeleccio<T> extends RespostaAPregunta<T> {    
    /**
     * Constructor of RespostaAPreguntaSeleccio
     * @param opcionsSeleccionades set of order of Opcio that has been selected
     * @param ordrePregunta order of Pregunta that it answers
     * @param cs the strategy used to calculate the centroide
     */
    RespostaAPreguntaSeleccio(T opcionsSeleccionades, int ordrePregunta, CentroideStrategy cs) {
        super(opcionsSeleccionades, ordrePregunta, cs);
        setCode("PREGUNTA_SELECCIO");
    }
}