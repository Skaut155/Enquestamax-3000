package Domain.Model;

import Domain.Exceptions.RespostaNoValida;

import java.util.ArrayList;

/**
 * Class representing multiple choice questions
 */
public class PreguntaSeleccioMultiple extends TipusPregunta {
    private static PreguntaSeleccioMultiple instance = null;
    private PreguntaSeleccioMultiple() {
        setCode("MULTIPLE");
    }

    /**
     * Get the singleton instance of PreguntaSeleccioMultiple
     * @return The singleton instance of PreguntaSeleccioMultiple
     */
    public static PreguntaSeleccioMultiple getInstance() {
        if (instance == null) {
            instance = new PreguntaSeleccioMultiple();
        }
        return instance;
    }

    @Override
    public void checkOpcions() {
        // Les preguntes de selecció múltiple tenen opcions, per tant, no cal fer res aquí.
    }

    /**
     * Change the state of the question to multiple choice
     * @param p An instance of Pregunta (the caller's 'this')
     */
    @Override
    public void setPreguntaSeleccioMultiple(Pregunta p) {
        // Ja està en aquest estat
    }

    /**
     * Generate a specific answer for multiple choice questions
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param isBlank Indicates if the answer is blank
     * @param p The question instance requesting the answer generation
     * @return A RespostaAPregunta instance representing the parsed answer
     * @throws RespostaNoValida If the answer is not valid for this question type, or out of range, or duplicate selections
     */
    @Override
    protected RespostaAPregunta<ArrayList<Integer>> generaEspecific(String resposta, int ordrePregunta, boolean isBlank, Pregunta p) throws RespostaNoValida {
        // skip validation if empty
        if(isBlank){
            return new RespostaAPreguntaMultiple(ordrePregunta);
        }
        String[] parts = resposta.split(" ");
        ArrayList<Integer> seleccionades = new ArrayList<>();
        for (String part : parts) {
            try {
                Integer o_id = Integer.parseInt(part.trim())-1;
                // validate option range
                if(o_id < 0 || o_id >= p.numOpcions()){
                    throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció múltiple.");
                }
                if(seleccionades.contains(o_id)){
                    throw new RespostaNoValida("La resposta proporcionada ja està marcada");
                }
                seleccionades.add((o_id));
            } catch (NumberFormatException e) {
                throw new RespostaNoValida("La resposta proporcionada no és vàlida per a una pregunta de selecció múltiple.");
            }
        }
        return new RespostaAPreguntaMultiple(seleccionades, ordrePregunta);
    }
}
