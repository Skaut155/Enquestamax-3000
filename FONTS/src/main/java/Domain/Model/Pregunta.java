package Domain.Model;

import Domain.Exceptions.OpcioNoExisteix;
import Domain.Exceptions.OrdreIncorrecte;
import Domain.Exceptions.RespostaNoValida;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Class Pregunta represents a question in a survey (Enquesta)
 */
public class Pregunta{
    private String titol;
    private boolean obligatoria;
    private int ordre;
    private TipusPregunta state;
    private ArrayList<Opcio> opcions;

    /**
     * Constructor for Pregunta
     */
    public Pregunta() {
        this.titol = "Introdueix una pregunta";
        this.obligatoria = false;
        this.ordre = -1;
        this.state = PreguntaSeleccioUnicaNoOrdenada.getInstance();
        this.opcions = new ArrayList<>();
    }

    /**
     * Constructor for Pregunta with specified type
     * @param state The type of the question
     */
    public Pregunta(TipusPregunta state) {
        this();
        this.state = state;
    }

    /**
     * Set if the question is mandatory
     * @param ordre The order of the question inside the Enquesta
     * @return true if successful
     * @throws OrdreIncorrecte if the order is negative
     */
    public boolean setOrdre(int ordre) throws OrdreIncorrecte {
        checkOrdre(ordre);
        this.ordre = ordre;
        return true;
    }

    /**
     * Get the order of the question
     * @return order
     */
    public int getOrdre() {
        return this.ordre;
    }

    /**
     * Get the value of titol
     * @return titol
     */
    public String getTitol() {
        return titol;
    }

    /**
     * Set the value of titol
     * @param titol The new title
     * @return true if successful
     */
    public boolean setTitol(String titol) {
        if (titol == null) {
            throw new IllegalArgumentException("El títol no pot ser nul");
        }
        if (titol.isBlank()) {
            throw new IllegalArgumentException("El títol no pot estar en blanc");
        }
        this.titol = titol;
        return true;
    }


    /**
     * Set if the question is mandatory
     * @param obligatoria true if mandatory, false if optional
     */
    public void setObligatoria(boolean obligatoria) {
        this.obligatoria = obligatoria;
    }

    /**
     * Get if the question is optional
     * @return optional (true) or mandatory (false)
     */
    public boolean isOptional() {
        return !obligatoria;
    }

    /**
     * Get the options of the question
     * @return list of options
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     */
    public ArrayList<Opcio> getOpcions() throws UnsupportedOperationException {
        state.checkOpcions();
        // sort opcions by order
        opcions.sort(Comparator.comparingInt(Opcio::getOrdre));
        return new ArrayList<>(opcions);
    }

    /**
     * Remove an option from the question
     * @param ordre order of the option to remove
     * @return True if success
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     * @throws OpcioNoExisteix if the option does not exist
     */
    public boolean eliminarOpcio(int ordre) throws UnsupportedOperationException, OpcioNoExisteix {
        state.checkOpcions();
        if (!opcions.removeIf(opcio -> opcio.getOrdre() == ordre)) {
            throw new OpcioNoExisteix("La opció amb ordre "+ ordre+" no existeix");
        }
        // reorder remaining options
        for (int i = 0; i < opcions.size(); i++) {
            opcions.get(i).setOrdre(i);
        }

        return true;
    }

    /**
     * Get the number of options
     * @return number of options
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     */
    public int numOpcions() throws UnsupportedOperationException {
        state.checkOpcions();
        return opcions.size();
    }

    /**
     * Add a new option with the given text
     * @param textOpcio text of the new option
     * @return true if successful
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     */
    public boolean afegirOpcio(String textOpcio) throws  UnsupportedOperationException {
        state.checkOpcions();
        int ordreNou = opcions.size();
        Opcio novaOpcio = new Opcio(ordreNou, textOpcio);
        return opcions.add(novaOpcio);
    }

    /**
     * Modify the text of an option, delete the option if the new text is empty
     * @param ordrePregunta order of the option to modify
     * @param textOpcio new text of the option
     * @return true if successful
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     * @throws OpcioNoExisteix if the option does not exist
     */
    public boolean modificarOpcio(int ordrePregunta, String textOpcio) throws UnsupportedOperationException, OpcioNoExisteix {
        state.checkOpcions();
        if(textOpcio.trim().isEmpty()) {
            return eliminarOpcio(ordrePregunta);
        }
        for (Opcio o : opcions) {
            if (o.getOrdre() == ordrePregunta) {
                return o.setText(textOpcio);
            }
        }
        throw new OpcioNoExisteix("La opció amb ordre "+ ordrePregunta+" no existeix");
    }

    /**
     * Set the type of the question
     * @param tipus type of the question
     * @return true if successful
     */
    public boolean setTipusPregunta(TipusPregunta tipus) {
        this.state = tipus;
        return true;
    }

    /**
     * Get the type of the question
     * @return the type instance
     */
    public TipusPregunta getTipusPregunta() {
        return this.state;
    }

    /**
     * Check if the order is valid
     * @param ordre order to check
     * @throws OrdreIncorrecte if the order is negative
     */
    private void checkOrdre(int ordre) throws OrdreIncorrecte {
        if(ordre < 0) {
            throw new OrdreIncorrecte("L'ordre de la pregunta ha de ser un enter no negatiu.");
        }
    }

    /**
     * String representation of the question
     * @return string representation with title, type, mandatory status and its options
     */
    @Override
    public String toString() {
        String result = "[#"+(ordre+1)+"] " + titol +  (obligatoria ? "*" : " ") +
                " - tipus: " + state.getCode() + "\n";

        for (Opcio o : opcions) {
            result += "\t" + o.toString() + "\n";
        }
        return result;
    }

    /**
     * Generate a response for the question from a string response
     * @param resposta The answer string (unparsed)
     * @return the generated response of type RespostaAPregunta
     * @throws RespostaNoValida If the answer is not valid for this question type, out of range, or duplicate selections
     */
    public RespostaAPregunta<?> generaResposta(String resposta) throws RespostaNoValida {
        return state.generaResposta(resposta, this.ordre, this);
    }
}
