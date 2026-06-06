package Domain.Model;

import Domain.Exceptions.RespostaNoValida;

/**
 * Singleton class representing a single choice ordered question type
 */
public class PreguntaSeleccioUnicaOrdenada extends TipusPregunta {
    private static PreguntaSeleccioUnicaOrdenada instance = null;
    private PreguntaSeleccioUnicaOrdenada() {
        setCode("UNICA_ORDENADA");
    }

    /**
     * Get the singleton instance of PreguntaSeleccioUnicaOrdenada
     * @return The singleton instance of PreguntaSeleccioUnicaOrdenada
     */
    public static PreguntaSeleccioUnicaOrdenada getInstance() {
        if (instance == null) {
            instance = new PreguntaSeleccioUnicaOrdenada();
        }
        return instance;
    }

    @Override
    public void checkOpcions() {
        // Les preguntes de selecció única ordenada tenen opcions, per tant, no cal fer res aquí.
    }

    /**
     * Change the state of the question to single choice ordered
     * @param p An instance of Pregunta (the caller's 'this')
     */
    @Override
    public void setPreguntaSeleccioUnicaOrdenada(Pregunta p){
        // Ja està en aquest estat
    }

    /**
     * Generate a specific answer for single choice ordered question
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
            return new RespostaAPreguntaOrdenada(ordrePregunta);
        }
        try {
            Integer seleccionada = Integer.parseInt(resposta.trim());
            if(seleccionada < 1 || seleccionada > p.numOpcions()){
                throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció única ordenada.");
            }
            return new RespostaAPreguntaOrdenada(seleccionada-1, ordrePregunta);
        } catch (NumberFormatException e) {
            throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció única ordenada.");
        }
    }
}
