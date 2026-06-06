package Domain.Model;

import java.util.ArrayList;

/**
 * Abstract class that represents an answer to a question in a survey
 * @param <T> type of the response
 */
public abstract class RespostaAPregunta<T> {

    private String code;
    private T resposta;
    private transient RespostaEnquesta respostaEnquesta;
    private Integer ordrePregunta;
    private CentroideStrategy centroideStrategy;

    /**
     * Constructor for RespostaAPregunta
     * @param ordrePregunta order of Pregunta that it answers
     * @param cs the strategy used to calculate the centroide
     */
    RespostaAPregunta(T resposta, int ordrePregunta, CentroideStrategy cs) {
        this.resposta = resposta;
        this.ordrePregunta = ordrePregunta;
        this.centroideStrategy = cs;
    }

    /**
     * Set code of the RespostaAPregunta
     * @param code code of the RespostaAPregunta
     */
    protected void setCode(String code){
        this.code = code;
    }

    /**
     * Get string representation of the answer
     * @return answer as a string
     */
    public abstract String getStringResposta();

    /**
     * Get answer
     * @return resposta
     */
    public T getResposta() {
        return resposta;
    }

    /**
     * Set value of resposta
     * @param resposta answer
     */
    public void setResposta(T resposta) {
        this.resposta = resposta;
    }

    /**
     * True if the answer is empty, false otherwise
     * @return resposta == null
     */
    public boolean isEmpty(){
        return resposta == null;
    }

    /**
     * Set respostaEnquesa
     * @param respostaEnquesta respostaEnquesta that it belongs
     */
    public void setRespostaEnquesta(RespostaEnquesta respostaEnquesta){
        this.respostaEnquesta = respostaEnquesta;
    }

    /**
     * Get the order of Pregunta that it answers
     * @return ordrePregunta
     */
    public Integer getOrdrePregunta() {
        return ordrePregunta;
    }

    /**
     * Distance between this RespostaAPregunta and another
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPregunta and r
     */
    public abstract double distRespostesPregunta(RespostaAPregunta<T> r);

    /**
     * Centroide of a set of RespostaAPregunta of the same type
     * @param respostes set of RespostaAPregunta
     * @return centroide of respostes
     */
    public RespostaAPregunta<?> calcularCentroide(ArrayList<RespostaAPregunta<?>> respostes){
        return centroideStrategy.calcularCentroide(respostes);
    }

    /**
     * Maximum value of the total of answers at the same question
     * @return maximum value of the total of answers at the same question
     */
    public double calcMax(){
        return respostaEnquesta.calcMax(this.ordrePregunta);
    }

    /**
     * Minimum value of the total of answers at the same question
     * @return minimum value of the total of answers at the same question
     */
    public double calcMin(){
        return respostaEnquesta.calcMin(this.ordrePregunta);
    }

    /**
     * Total number of possible options that has the question
     * @return number of options of Pregunta ordrePregunta of this RespostaAPregunta
     */
    public int getNumOpcionsPregunta(){
        return respostaEnquesta.getPregunta(ordrePregunta).numOpcions();
    }
}
