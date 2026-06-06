package Domain.Model;

import Domain.Exceptions.PreguntaNoExisteix;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a survey ("Enquesta") containing a list of questions and a container for responses.
 *
 * Each Enquesta has a unique id, a name, creation date, an ordered list of Pregunta instances
 * and a RespostesContainer that holds submitted responses.
 */
public class Enquesta{
    /**
     * Unique identifier for this Enquesta.
     */
    private int id;

    /**
     * Name/title of the Enquesta.
     */
    private String nom;

    /**
     * Timestamp when the Enquesta was created.
     */
    private LocalDateTime dataCreacio;

    /**
     * Ordered list of questions in the survey.
     */
    private ArrayList<Pregunta> preguntes;

    /**
     * Container holding responses submitted to this survey.
     */
    private RespostesContainer respostesContainer;

    /**
     * Constructs a new Enquesta with the given name.
     * The constructor assigns a unique id, sets the creation timestamp, and initializes
     * the question list and responses container.
     *
     * @param nom the name/title of the survey
     */
    public Enquesta(String nom)
    {
        this.nom = nom;
        this.dataCreacio = LocalDateTime.now();
        this.preguntes = new ArrayList<>();
        this.respostesContainer = new RespostesContainer(this);
    }

    /**
     * Sets the unique identifier for this survey.
     *
     * @param id the survey id to set
     */
    public void setId(int id)
    {
        this.id = id;
    }

    /**
     * Returns the name of the survey.
     *
     * @return the survey name
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets the name of the survey.
     *
     * @param nom new survey name
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Returns the unique identifier of this survey.
     *
     * @return survey id
     */
    public int  getId() {
        return id;
    }

    /**
     * Returns the creation date and time of the survey.
     *
     * @return creation timestamp
     */
    public LocalDateTime getDate() {
        return dataCreacio;
    }

    /**
     * Returns the question at the given order/index.
     *
     * @param ordrePregunta zero-based index of the question
     * @return the Pregunta at the specified index
     * @throws PreguntaNoExisteix if the index is out of bounds (negative or >= number of questions)
     */
    public Pregunta getPregunta(int ordrePregunta) throws PreguntaNoExisteix {
        if (ordrePregunta >= preguntes.size()) {
            throw new PreguntaNoExisteix("No existeix una instància de Pregunta amb ordre " + ordrePregunta + " Dins l'enquesta " + nom + " id:" + id);
        }
        if (ordrePregunta < 0) {
            throw new PreguntaNoExisteix("L'ordre de la pregunta no pot ser menor que 0");
        }

        return preguntes.get(ordrePregunta);
    }

    /**
     * Adds a question to the end of the question list and sets its ordre to the correct index.
     *
     * @param p question to add
     */
    public void afegirPregunta(Pregunta p) {
        preguntes.add(p);
        p.setOrdre(preguntes.size()-1);
    }

    /**
     * Swaps the positions of two questions identified by their indices and updates their ordre values.
     *
     * @param i index of the first question
     * @param j index of the second question
     */
    public void swapPreguntes(int i, int j) {
        Pregunta p1 = preguntes.get(i);
        Pregunta p2 = preguntes.get(j);
        p1.setOrdre(j);
        p2.setOrdre(i);
        Collections.swap(preguntes, i, j);
    }

    /**
     * Removes the question at the specified index.
     *
     * @param ordrePregunta zero-based index of the question to remove
     * @throws PreguntaNoExisteix if the index is out of bounds (negative or >= number of questions)
     */
    public void eliminarPregunta(int ordrePregunta) throws PreguntaNoExisteix {
        if (ordrePregunta >= preguntes.size()) {
            throw new PreguntaNoExisteix("No existeix una instància de Pregunta amb ordre " + ordrePregunta + " Dins l'enquesta " + nom + " id:" + id);
        }
        if (ordrePregunta < 0) {
            throw new PreguntaNoExisteix("L'ordre de la pregunta no pot ser menor que 0");
        }

        preguntes.remove(ordrePregunta);

        // Update ordre of remaining questions
        for (int i = ordrePregunta; i < preguntes.size(); i++) {
            preguntes.get(i).setOrdre(i);
        }
    }

    /**
     * Adds a new response to this survey's responses container.
     * @return the unique id of the newly created response
     */
    public int afegirNovaResposta() {
        RespostaEnquesta re = new RespostaEnquesta();
        respostesContainer.addResposta(re);
        return re.getId();
    }

    /**
     * Removes the response with the specified id from the responses container.
     *
     * @param idResposta identifier of the response to remove
     */
    public void eliminaResposta(int idResposta) {
        respostesContainer.removeResposta(idResposta);
    }

    /**
     * Returns the number of questions in this survey.
     *
     * @return number of questions
     */
    public int numPreguntes() {
        return preguntes.size();
    }

    /**
     * Returns a list of response ids recorded for this survey.
     *
     * @return list of response ids
     */
    public ArrayList<Integer> getIdsRespostes() {
        return respostesContainer.getIdsRespostes();
    }

    /**
     * Returns the response with the specified id.
     *
     * @param id response identifier
     * @return the corresponding RespostaEnquesta, or null if not found
     */
    public RespostaEnquesta getResposta(int id) {
        return respostesContainer.getResposta(id);
    }

    /**
     * Returns all responses recorded for this survey as an array.
     *
     * @return array of all RespostaEnquesta instances
     */
    public ArrayList<RespostaEnquesta> getAllRespostes() {
        ArrayList<Integer> respostes = respostesContainer.getIdsRespostes();
        ArrayList<RespostaEnquesta> respostesArray = new ArrayList<>();
        for (int i = 0; i < respostes.size(); i++) {
            respostesArray.add(respostesContainer.getResposta(respostes.get(i)));
        }
        return respostesArray;
    }

    /**
     * Returns a string representation of the survey, including its id, creation date,
     * and details of each question.
     *
     * @return string representation of the Enquesta
     */
    @Override
    public String toString() {
        StringBuilder s =
                new StringBuilder("Enquesta \"" + nom + "\"\n"
                        + "------------------------------------------\n"
                        + "-id: " + id + "\n"
                        + "-Data de creacio: " + dataCreacio.toString() + "\n"
                        + "----------------------------------------\n");

        for (Pregunta p : preguntes) {
            s.append(p.toString()).append("\n");
        }

        return s.toString();
    }


    /**
     * Deletes all responses recorded for this survey.
     */
    public void esborrarRespostes() {
        respostesContainer.EsborrarRespostes();
    }

    /**
     * Returns the responses container associated with this survey.
     *
     * @return the RespostesContainer instance
     */
    public RespostesContainer getRespostesContainer() {
        return respostesContainer;
    }
}