package Domain.Model;

import java.util.ArrayList;

/**
 * Class RespostaAPreguntaMultiple that represents an answer to a multiple choice question
 */
public class RespostaAPreguntaMultiple extends RespostaAPreguntaSeleccio<ArrayList<Integer>> {
    /**
     * Constructor RespostaAPreguntaMultiple empty
     * @param ordrePregunta order of the Pregunta that it answers
     */
    public RespostaAPreguntaMultiple(int ordrePregunta){
        super(new ArrayList<Integer>(), ordrePregunta, CentroidePreguntaMultipleStrategy.getInstance());
        setCode("MULTIPLE");
    }

    /**
     * Constructor of RespostaAPreguntaMultiple
     * @param opcionsSeleccionades set of order of Opcio with options selected
     * @param ordrePregunta order of Pregunta that it answers
     */
    public RespostaAPreguntaMultiple(ArrayList<Integer> opcionsSeleccionades, int ordrePregunta) {
        super(opcionsSeleccionades, ordrePregunta, CentroidePreguntaMultipleStrategy.getInstance());
        setCode("MULTIPLE");
    }

    /**
     * String representation of the answer
     * @return String with the selected options (1-based index) separated by spaces
     */
    @Override
    public String getStringResposta() {
        if(isEmpty()) return "No hi ha resposta";
        String respostaString = "";
        for(Integer opcio : super.getResposta()) {
            Integer opcioIndex = opcio + 1;
            respostaString += opcioIndex.toString() + " ";
        }
        return respostaString;
    }

    /**
     * True if the answer is empty, false otherwise
     * @return resposta == null or empty array
     */
    @Override
    public boolean isEmpty() {
        return super.getResposta() == null || super.getResposta().isEmpty();
    }

    /**
     * Distance between this RespostaAPreguntaMultiple and another RespostaAPreguntaMultiple
     * @param r the other RespostaAPregunta
     * @return distance between this RespostaAPreguntaMultiple and r
     */
    @Override
    public double distRespostesPregunta(RespostaAPregunta<ArrayList<Integer>> r) {
        ArrayList<Integer> o1 = this.getResposta(); 

        RespostaAPreguntaMultiple r2 = (RespostaAPreguntaMultiple) r;
        ArrayList<Integer> o2 = r2.getResposta();

        double interseccio = 0.0;
        for(Integer opcio : o1)  if(o2.contains(opcio)) interseccio += 1.0;
        
        return 1 - (interseccio / (o1.size() + o2.size() - interseccio));
    }
}