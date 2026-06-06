package Domain.Model;

import Domain.Exceptions.EnquestaNoConteResposta;

import java.util.ArrayList;

/**
 * Container for storing responses (RespostaEnquesta) associated with an Enquesta.
 * Provides methods to add and retrieve responses and perform simple aggregations.
 */
public class RespostesContainer {
    /**
     * The survey this container belongs to.
     */
    private transient Enquesta enquesta;

    /**
     * Monotonic identifier used when assigning ids to responses.
     */
    private int lastId;

    /**
     * List of responses stored in this container.
     */
    private ArrayList<RespostaEnquesta> respostes;

    /**
     * Constructs a new RespostesContainer for the given Enquesta.
     *
     * @param enquesta the survey to which stored responses belong
     */
    public RespostesContainer(Enquesta enquesta) {
        lastId = 0;
        respostes = new ArrayList<RespostaEnquesta>();
        this.enquesta = enquesta;
    }

    /**
     * Associates this container with an Enquesta.
     *
     * @param enquesta the survey to associate with
     */
    public void associaEnquesta(Enquesta enquesta) {
        this.enquesta = enquesta;
    }

    /**
     * Adds a response to this container, assigns it a unique id and associates it back to this container.
     *
     * @param resposta the response to add
     */
    public void addResposta(RespostaEnquesta resposta)
    {
        respostes.add(resposta);
        resposta.setId(lastId++);
        resposta.associaContainer(this);
    }

    /**
     * Returns the list of ids of responses currently stored in this container.
     *
     * @return an ArrayList of Integer ids
     */
    public ArrayList<Integer> getIdsRespostes()
    {
        ArrayList<Integer> ids = new ArrayList<Integer>();
        for (RespostaEnquesta r : respostes)
        {
            ids.add(r.getId());
        }
        return ids;
    }

    /**
     * Retrieves a response by its id.
     *
     * @param id the id of the response to retrieve
     * @return the RespostaEnquesta with the given id
     * @throws EnquestaNoConteResposta if no response with the given id exists in this container
     */
    public RespostaEnquesta getResposta(int id)
    {
        for (RespostaEnquesta r : respostes)
        {
            if (r.getId() == id) return r;
        }

        throw new EnquestaNoConteResposta("L'enquesta no conte una resposta amb id: " + id);
    }

    /**
     * Calculates the maximum numeric answer for a question identified by its order.
     * NOTE: Implementation depends on numeric-answer types; current method is a stub.
     *
     * @param ordrePregunta the order/index of the question in the survey
     * @return the maximum numeric value found for the question, or -Double.MAX_VALUE if none
     */
    public double calcMax(int ordrePregunta)
    {
        double max = -Double.MAX_VALUE;
        for (RespostaEnquesta r : respostes)
        {
            RespostaAPregunta<Double> rapn = r.getRespostaAPregunta(ordrePregunta);

            if (rapn.isEmpty()) continue;

            if (rapn.getResposta() > max)
            {
                max = rapn.getResposta();
            }
        }

        return max;
    }

    /**
     * Calculates the minimum numeric answer for a question identified by its order.
     * NOTE: Implementation depends on numeric-answer types; current method is a stub.
     *
     * @param ordrePregunta the order/index of the question in the survey
     * @return the minimum numeric value found for the question, or Double.MAX_VALUE if none
     */
    public double calcMin(int ordrePregunta)
    {
        double min = Double.MAX_VALUE;
        for (RespostaEnquesta r : respostes)
        {
            RespostaAPregunta<Double> rapn = r.getRespostaAPregunta(ordrePregunta);

            if (rapn.isEmpty()) continue;

            if (rapn.getResposta() < min)
            {
                min = rapn.getResposta();
            }
        }

        return min;
    }

    /**
     * Returns the question (Pregunta) corresponding to the given order/index in the associated Enquesta.
     *
     * @param ordrePregunta the order/index of the question
     * @return the Pregunta at the specified order
     */
    public Pregunta getPregunta(int ordrePregunta) {
        return enquesta.getPregunta(ordrePregunta);
    }

    /**
     * Removes all responses from this container.
     */
    public void EsborrarRespostes() {
        respostes.clear();
        lastId = 0;
    }

    /**
     * Removes the response with the specified id from this container.
     *
     * @param idResposta the id of the response to remove
     */
    public void removeResposta(int idResposta) {
        boolean b = respostes.removeIf(r -> r.getId() == idResposta);
        if (!b) {
            throw new EnquestaNoConteResposta("L'enquesta no conte una resposta amb id: " + idResposta);
        }
    }
}
