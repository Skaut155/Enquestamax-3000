package Domain.Model;

import Domain.Exceptions.RespostaNoValida;

/**
 * Abstract class TipusPregunta represents the type of a question in a survey
 */
public abstract class TipusPregunta {
    private String code;

    /**
     * Gets the string representation of the state
     * @return A String of the type's name
     */
    public String getCode(){
        return this.code;
    }

    /**
     * Sets the string representation of the state
     * @param code A String of the type's name
     */
    protected void setCode(String code){this.code = code;}

    /**
     * Check options validity for the question type
     * @throws UnsupportedOperationException if the operation is not supported depending on the question type
     */
    public abstract void checkOpcions() throws UnsupportedOperationException;

    /**
     * Change the type to PreguntaSeleccioUnicaOrdenada
     * @param p An instance of Pregunta (the caller's 'this')
     */
    public void setPreguntaSeleccioUnicaOrdenada(Pregunta p){
        p.setTipusPregunta(PreguntaSeleccioUnicaOrdenada.getInstance());
    }

    /**
     * Change the type to PreguntaRespostaObertaNumerica
     * @param p An instance of Pregunta (the caller's 'this')
     */
    public void setPreguntaRespostaObertaNumerica(Pregunta p){
        p.setTipusPregunta(PreguntaNumerica.getInstance());
    }

    /**
     * Change the type to PreguntaRespostaOberta
     * @param p An instance of Pregunta (the caller's 'this')
     */
    public void setPreguntaRespostaOberta(Pregunta p){
        p.setTipusPregunta(PreguntaOberta.getInstance());
    }

    /**
     * Change the type to PreguntaSeleccioUnicaNoOrdenada
     * @param p An instance of Pregunta (the caller's 'this')
     */
    public void setPreguntaSeleccioUnicaNoOrdenada(Pregunta p){
        p.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
    }

    /**
     * Change the type to PreguntaSeleccioMultiple
     * @param p An instance of Pregunta (the caller's 'this')
     */
    public void setPreguntaSeleccioMultiple(Pregunta p){
        p.setTipusPregunta(PreguntaSeleccioMultiple.getInstance());
    }

    /**
     * Generate a generic answer for the question type
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param p The question instance requesting the answer generation
     * @return A RespostaAPregunta instance representing the parsed answer
     * @throws RespostaNoValida If the answer is not valid for this question type, out of range, or duplicate selections
     */
    public RespostaAPregunta<?> generaResposta(String resposta, int ordrePregunta, Pregunta p) throws RespostaNoValida{
        boolean isOptional = p.isOptional();
        boolean isBlank = false;
        // comprovar no empty i no null en cas de que sigui no oblig.
        if(resposta == null || resposta.isBlank()){
            if(!isOptional){ //mandatory
                throw new RespostaNoValida("La resposta és obligatòria, l'has de respondre.");
            }
            isBlank = true;
        }
        return generaEspecific(resposta, ordrePregunta, isBlank, p);
    }

    /**
     * Generate a specific answer for the question type
     * @param resposta The answer string (unparsed)
     * @param ordrePregunta The question order inside the survey
     * @param isBlank Whether the answer is blank
     * @param p The question instance requesting the answer generation
     * @return A RespostaAPregunta instance representing the parsed answer
     * @throws RespostaNoValida If the answer is not valid for this question type, out of range, or duplicate selections
     */
    protected abstract RespostaAPregunta<?> generaEspecific(String resposta, int ordrePregunta, boolean isBlank, Pregunta p) throws RespostaNoValida;

}
