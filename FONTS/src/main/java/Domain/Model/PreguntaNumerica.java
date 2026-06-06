package Domain.Model;

import Domain.Exceptions.RespostaNoValida;

/**
 * Class representing a numeric question type
 */
public class PreguntaNumerica extends TipusPregunta {
    private static PreguntaNumerica instance = null;
    private PreguntaNumerica() {
        setCode("NUMERICA");
    }

    /**
     * Get the singleton instance of PreguntaNumerica
     * @return The singleton instance of PreguntaNumerica
     */
    public static PreguntaNumerica getInstance() {
        if (instance == null) {
            instance = new PreguntaNumerica();
        }
        return instance;
    }

    @Override
    public void checkOpcions() {
        throw new UnsupportedOperationException("Les preguntes numèriques no tenen opcions.");
    }

    /**
     * Generate a specific answer for numeric question
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param isBlank Indicates if the answer is blank (non answered)
     * @param p The question instance
     * @return A RespostaAPregunta instance representing the parsed answer
     * @throws RespostaNoValida If the answer is not a valid number, or out of range
     */
    @Override
    protected RespostaAPregunta<Double> generaEspecific(String resposta, int ordrePregunta, boolean isBlank, Pregunta p) throws RespostaNoValida {
        if(isBlank) {
             // skip cast, generate a blank answer
            return new RespostaAPreguntaNumerica(ordrePregunta);
        }
        try {
            Double valor = Double.parseDouble(resposta);
            return new RespostaAPreguntaNumerica(valor, ordrePregunta);
        } catch (NumberFormatException e) {
            throw new RespostaNoValida("La resposta proporcionada no és un número vàlid.");
        }
    }

    /**
     * Change the state of the question to numeric open answer
     * @param p An instance of Pregunta (the caller's 'this')
     */
    @Override
    public void setPreguntaRespostaObertaNumerica(Pregunta p) {
        // Ja està en aquest estat
    }
}
