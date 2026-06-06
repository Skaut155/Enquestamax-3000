package Domain.Adapters;

import Domain.Model.RespostaEnquesta;

public interface IRespostesIO {
    /**
     * Exports a survey response to a file.
     * @param resposta the RespostaEnquesta object to export.
     * @param rutaFitxer the file path where the response will be saved.
     */
    void exportResposta(RespostaEnquesta resposta, String rutaFitxer);
    /**
     * Imports a survey response from a file.
     * @param rutaFitxer the file path from which the response will be loaded.
     * @return the imported RespostaEnquesta object.
     */
    RespostaEnquesta importResposta(String rutaFitxer);
}
