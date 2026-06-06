package Domain.Model;

import java.util.ArrayList;

/**
 * Represents a survey response (RespostaEnquesta) which groups answers
 * to the individual questions of an associated survey container.
 *
 * <p>This class holds a list of {@code RespostaAPregunta} objects and
 * delegates some aggregate computations (like min/max) to the
 * associated {@code RespostesContainer}.</p>
 */
public class RespostaEnquesta {
    private transient RespostesContainer container;

    private int id;
    private ArrayList<RespostaAPregunta<?>> respostes;

    /**
     * Creates an empty RespostaEnquesta with no associated container
     * and an empty list of question responses.
     */
    public RespostaEnquesta() {
        this.respostes = new ArrayList<RespostaAPregunta<?>>();
    }

    /**
     * Associates this RespostaEnquesta with a {@link RespostesContainer}.
     * The container provides access to question metadata and aggregate
     * calculations for a given question index.
     *
     * @param container the container that contains question definitions and helper methods
     */
    public void associaContainer(RespostesContainer container) {
        this.container = container;
    }

    /**
     * Returns the identifier of this survey response.
     *
     * @return the id of the RespostaEnquesta
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the identifier for this RespostaEnquesta.
     *
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the associated {@code RespostesContainer}.
     *
     * @return the container linked to this RespostaEnquesta
     */
    public RespostesContainer getContainer() {
        if (container == null) {
            throw new NullPointerException("No container associated with this RespostaEnquesta");
        }
        return container;
    }

    /**
     * Delegates to the associated {@code RespostesContainer} to calculate
     * the maximum value for the specified question order/index.
     *
     * @param ordrePregunta the zero-based order/index of the question
     * @return the maximum value for the question as provided by the container
     */
    public double calcMax(int ordrePregunta) {
        return container.calcMax(ordrePregunta);
    }

    /**
     * Delegates to the associated {@code RespostesContainer} to calculate
     * the minimum value for the specified question order/index.
     *
     * @param ordrePregunta the zero-based order/index of the question
     * @return the minimum value for the question as provided by the container
     */
    public double calcMin(int ordrePregunta) {
        return container.calcMin(ordrePregunta);
    }

    /**
     * Finds and returns the response object for a given question order/index.
     * If no response for the specified question is present, this method
     * returns {@code null}.
     *
     * @param ordrePregunta the zero-based order/index of the question
     * @return the {@code RespostaAPregunta} for the requested question, or {@code null}
     *         if not found
     */
    public RespostaAPregunta getRespostaAPregunta(int ordrePregunta) {
        for (RespostaAPregunta<?> rap : respostes) {
           if (rap.getOrdrePregunta() == ordrePregunta) { // Todo: Marc: RespostaAPregunta.GetOrdrePregunta no existeix
                return rap;
           }
        }
        return null; // or throw an exception if not found
    }

    /**
     * Returns a shallow copy of the internal list of responses. The returned
     * list is a separate {@code ArrayList} instance, but the contained
     * {@code RespostaAPregunta} objects are the same references as stored
     * internally.
     *
     * @return a copy of the internal list of responses
     */
    public ArrayList<RespostaAPregunta> getRespostes(){
        return new ArrayList<>(respostes);
    }

    /**
     * Retrieves the {@code Pregunta} metadata for the given question
     * order/index from the associated container.
     *
     * @param ordrePregunta the zero-based order/index of the question
     * @return the {@code Pregunta} object from the container
     * @throws NullPointerException if no container has been associated
     */
    public Pregunta getPregunta(int ordrePregunta) {
        return container.getPregunta(ordrePregunta);
    }

    /**
     * Adds a response for a single question to this {@code RespostaEnquesta}.
     *
     * @param rap the response to add (must not be {@code null})
     */
    public void addRespostaAPregunta(RespostaAPregunta<?> rap) {
        rap.setRespostaEnquesta(this);
        respostes.add(rap);
    }

    /**
     * Returns an array of string representations of the answers
     * contained in this {@code RespostaEnquesta}.
     *
     * @return an array of strings representing each answer
     */
    public String[] getRespostesString() {
        String[] respostesStr = new String[respostes.size()];
        for (int i = 0; i < respostes.size(); i++) {
            respostesStr[i] = respostes.get(i).getStringResposta();
        }
        return respostesStr;
    }
}
