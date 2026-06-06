package Domain.Model;

/**
 * Class representing an open answer question type
 */
public class PreguntaOberta extends TipusPregunta {
    private static PreguntaOberta instance = null;
    private PreguntaOberta() {
        setCode("OBERTA");
    }

    /**
     * Get the singleton instance of PreguntaOberta
     * @return The singleton instance of PreguntaOberta
     */
    public static PreguntaOberta getInstance() {
        if (instance == null) {
            instance = new PreguntaOberta();
        }
        return instance;
    }

    @Override
    public void checkOpcions() {
        throw new UnsupportedOperationException("Les preguntes obertes no tenen opcions.");
    }

    /**
     * Change the state of the question to open answer
     * @param p An instance of Pregunta (the caller's 'this')
     */
    @Override
    public void setPreguntaRespostaOberta(Pregunta p) {
        // Ja està en aquest estat
    }

    /**
     * Generate a specific answer for open answer question
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param isBlank Indicates if the answer is blank (non answered)
     * @param p The question instance
     * @return A RespostaAPregunta instance representing the parsed answer
     */
    @Override
    protected RespostaAPregunta<String> generaEspecific(String resposta, int ordrePregunta, boolean isBlank, Pregunta p) {
        if(isBlank) {
            return new RespostaAPreguntaOberta(ordrePregunta);
        }
        return new RespostaAPreguntaOberta(resposta, ordrePregunta);
    }

}
