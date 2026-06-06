package Domain.Model;

import Domain.Exceptions.RespostaNoValida;

/**
 * Class representing a single choice unordered question type
 */
public class PreguntaSeleccioUnicaNoOrdenada extends TipusPregunta {
    private static PreguntaSeleccioUnicaNoOrdenada instance = null;
    private PreguntaSeleccioUnicaNoOrdenada() {
        setCode("UNICA_NO_ORDENADA");
    }

    /**
     * Get the singleton instance of PreguntaSeleccioUnicaNoOrdenada
     * @return The singleton instance of PreguntaSeleccioUnicaNoOrdenada
     */
    public static PreguntaSeleccioUnicaNoOrdenada getInstance() {
        if (instance == null) {
            instance = new PreguntaSeleccioUnicaNoOrdenada();
        }
        return instance;
    }

    @Override
    public void checkOpcions() {
        // Les preguntes de selecció única no ordenada tenen opcions, per tant, no cal fer res aquí.
    }

    /**
     * Change the state of the question to single choice unordered
     * @param p An instance of Pregunta (the caller's 'this')
     */
    @Override
    public void setPreguntaSeleccioUnicaNoOrdenada(Pregunta p) {
        // Ja està en aquest estat
    }

    /**
     * Generate a specific answer for single choice unordered question
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param isBlank Indicates if the answer is blank (non answered)
     * @param p The question instance
     * @return A RespostaAPregunta instance representing the parsed answer
     * @throws RespostaNoValida If the answer is not valid for this question type, out of range, or not a number
     */
    @Override
    protected RespostaAPregunta<Integer> generaEspecific(String resposta, int ordrePregunta, boolean isBlank, Pregunta p) throws RespostaNoValida {
        // skip validation if empty
        if(isBlank){
            return new RespostaAPreguntaNoOrdenada(ordrePregunta);
        }
        try {
            Integer seleccionada = Integer.parseInt(resposta.trim());
            if(seleccionada < 1 || seleccionada > p.numOpcions()){
                throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció única no ordenada.");
            }
            return new RespostaAPreguntaNoOrdenada(seleccionada-1, ordrePregunta);
        } catch (NumberFormatException e) {
            throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció única no ordenada.");
        }
    }
}
