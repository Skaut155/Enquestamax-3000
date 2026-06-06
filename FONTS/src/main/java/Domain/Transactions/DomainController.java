package Domain.Transactions;

/**
 * DomainController acts as a facade for domain operations
 */
public class DomainController {

    /**
     * Creates a new enquesta.
     *
     * @param nomEnquesta Name of the enquesta.
     * @return The ID of the created enquesta.
     */
    public int crearEnquesta(String nomEnquesta) {
        TxCrearEnquesta t = new TxCrearEnquesta(nomEnquesta);
        t.execute();

        return t.result;
    }

    /**
     * Deletes an enquesta by its ID.
     *
     * @param idEnquesta ID of the enquesta to delete.
     */
    public void eiminarEnquesta(String nomAdmin, int idEnquesta) {
        Transaction t = new TxEliminarEnquesta(nomAdmin, idEnquesta);
        t.execute();
    }

    /**
     * Registers a new administrator.
     *
     * @param nomAdmin    Name of the administrator.
     * @param contrasenya Password of the administrator.
     */
    public void registrarAdmin(String nomAdmin, String contrasenya) {
        Transaction t = new TxRegistrarAdmin(nomAdmin, contrasenya);
        t.execute();
    }

    /**
     * Retrieves the IDs of responses for a given enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     * @return Array of response IDs.
     */
    public int[] getIdsRespostes(int idEnquesta) {
        TxGetRespostesEnquesta t = new TxGetRespostesEnquesta(idEnquesta);
        t.execute();
        // Convert ArrayList<Integer> to int[]
        return t.result.stream().mapToInt(Integer::valueOf).toArray();
    }

    /**
     * Adds a new question to an enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     */
    public void afegirPregunta(int idEnquesta) {
        Transaction t = new TxAfegirPregunta(idEnquesta);
        t.execute();
    }

    /**
     * Deletes a question from an enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     * @param ordre      Order of the question to delete.
     */
    public void eliminarPregunta(int idEnquesta, int ordre) {
        Transaction t = new TxEliminarPregunta(idEnquesta, ordre);
        t.execute();
    }

    /**
     * Changes the type of a question in an enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     * @param ordre      Order of the question.
     * @param nouTipus   New type for the question.
     */
    public void canviarTipusPregunta(int idEnquesta, int ordre, String nouTipus) {
        Transaction t = new TxCanviarTipusPregunta(idEnquesta, ordre, nouTipus);
        t.execute();
    }

    /**
     * Modifies the text of a question in an enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     * @param ordre      Order of the question.
     * @param nouText    New text for the question.
     */
    public void modificarPregunta(int idEnquesta, int ordre, String nouText) {
        Transaction t = new TxModificarPregunta(idEnquesta, ordre, nouText);
        t.execute();
    }

    /**
     * Adds a response option to a question in an enquesta.
     *
     * @param idEnquesta ID of the enquesta.
     * @param ordre      Order of the question.
     * @param textOpcio  Text of the new response option.
     */
    public void afegirOpcioResposta(int idEnquesta, int ordre, String textOpcio) {
        Transaction t = new TxAfegirOpcioResposta(idEnquesta, ordre, textOpcio);
        t.execute();
    }

    /**
     * Modifies a response option of a question in an enquesta.
     *
     * @param idEnquesta    ID of the enquesta.
     * @param ordrePregunta Order of the question.
     * @param ordreOpcio    Order of the response option.
     * @param textOpcio     New text for the response option.
     */
    public void modificarOpcioResposta(int idEnquesta, int ordrePregunta, int ordreOpcio, String textOpcio) {
        Transaction t = new TxModificarOpcioResposta(idEnquesta, ordrePregunta, ordreOpcio, textOpcio);
        t.execute();
    }

    /**
     * Deletes a response option from a question in an enquesta.
     *
     * @param idEnquesta   ID of the enquesta.
     * @param odrePregunta Order of the question.
     * @param ordreOpcio   Order of the response option to delete.
     */
    public void eliminarOpcioResposta(int idEnquesta, int odrePregunta, int ordreOpcio) {
        Transaction t = new TxEliminarOpcioResposta(idEnquesta, odrePregunta, ordreOpcio);
        t.execute();
    }

    /**
     * Changes the mandatory status of a question in an enquesta.
     *
     * @param idEnquesta    ID of the enquesta.
     * @param ordrePregunta Order of the question.
     * @param obligatoria   Whether the question is mandatory.
     */
    public void canviarObligatorietatPregunta(int idEnquesta, int ordrePregunta, boolean obligatoria) {
        Transaction t = new TxObligatorietatPregunta(idEnquesta, ordrePregunta, obligatoria);
        t.execute();
    }

    /**
     * Clones an enquesta.
     *
     * @param idEnquesta ID of the enquesta to clone.
     */
    public void clonarEnquesta(int idEnquesta, String nomAdmin) {
        Transaction t = new TxClonarEnquesta(idEnquesta, nomAdmin);
        t.execute();
    }

    /**
     * Inicia sessió amb un administrador existent.
     *
     * @param nomAdmin    Nom de l'administrador.
     * @param contrasenya Contrasenya de l'administrador.
     * @return true si l'inici de sessió és correcte, false altrament.
     */
    public boolean iniciarSessio(String nomAdmin, String contrasenya) {
        Transaction t = new TxIniciarSessio(nomAdmin, contrasenya);
        try {
            t.execute();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * Retorna una representació textual de l'enquesta amb el seu estat actual.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return String amb la informació de l'enquesta.
     */
    public String mostrarEnquesta(int idEnquesta) {
        Transaction t = new TxMostrarEnquesta(idEnquesta);
        t.execute();
        return ((TxMostrarEnquesta) t).result;
    }

    /**
     * Retorna un llistat de totes les enquestes disponibles.
     *
     * @param nomAdmin Nom de l'administrador.
     * @return String amb el llistat d'enquestes.
     */
    public String getLlistatEnquestes(String nomAdmin) {
        TxGetLlistatEnquestes t = new TxGetLlistatEnquestes(nomAdmin);
        t.execute();
        return t.result;
    }

    public String[] getPreguntesEnquesta(int idEnquesta) {
        Transaction t = new TxGetPreguntesEnquesta(idEnquesta);
        t.execute();

        return ((TxGetPreguntesEnquesta) t).result;
    }


    /**
     * Guarda totes les respostes d'una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param respostes  Array de respostes en format String.
     */
    public void guardarRespostes(int idEnquesta, String[] respostes) {
        Transaction t = new TxNovaRespostaEnquesta(idEnquesta, respostes);
        t.execute();
    }

    /**
     * Consulta una resposta específica d'una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param idResposta ID de la resposta.
     * @return String amb la informació de la resposta.
     */
    public String consultarResposta(int idEnquesta, int idResposta) {
        TxConsultaRespostaAEnquesta t = new TxConsultaRespostaAEnquesta(idEnquesta, idResposta);
        t.execute();
        return t.result;
    }

    /**
     * Calcula els clústers per una enquesta utilitzant un algoritme específic i un nombre de clústers donat.
     *
     * @param idEnquesta    ID de l'enquesta.
     * @param algorithmName Nom de l'algoritme de clustering.
     * @param k             Nombre de clústers.
     * @return Matriu amb els resultats del clustering.
     */
    public int[][] computeClusterAlgorithm(int idEnquesta, String algorithmName, int k) {
        TxComputeClusterAlgorithm t = new TxComputeClusterAlgorithm(idEnquesta, algorithmName, k);
        t.execute();
        return t.result;
    }

    /**
     * Calcula el nombre òptim de clústers per una enquesta utilitzant un algoritme específic i un llindar de ràtio.
     *
     * @param idEnquesta    ID de l'enquesta.
     * @param algorithmName Nom de l'algoritme de clustering.
     * @param thresholdRatio Ràtio de llindar per determinar el nombre òptim de clústers.
     * @return Matriu amb els resultats del clustering amb el nombre òptim de clústers.
     */
    public int[][] computeClusterAlgorithmOptimalK(int idEnquesta, String algorithmName, double thresholdRatio) {
        TxComputeClusterAlgorithmOptimalK t = new TxComputeClusterAlgorithmOptimalK(idEnquesta, algorithmName, thresholdRatio);
        t.execute();
        return t.result;
    }

    /**
     * Cancela l'edició d'una enquesta, revertint els canvis no guardats.
     *
     * @param idEnquesta ID de l'enquesta.
     */
    public void cancelarEdicioEnquesta(int idEnquesta) {
        Transaction t = new TxCancelarEdicioEnquesta(idEnquesta);
        t.execute();
    }

    /**
     * Guarda els canvis realitzats en una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     */
    public void guardarEdicioEnquesta(int idEnquesta, String nomAdmin) {
        Transaction t = new TxGuardarEnquesta(idEnquesta, nomAdmin);
        t.execute();
    }

    /**
     * Elimina un administrador del sistema.
     *
     * @param nomAdmin Nom de l'administrador a eliminar.
     */
    public void eliminarAdmin(String nomAdmin) {
        Transaction t = new TxEliminarAdmin(nomAdmin);
        t.execute();
    }

    /**
     * Modifica el nom d'una enquesta.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param nouNom     Nou nom per a l'enquesta.
     */
    public void setEnquestaNom(int idEnquesta, String nouNom) {
        Transaction t = new TxModificarNomEnquesta(idEnquesta, nouNom);
        t.execute();
    }

    /**
     * Retorna el codi del tipus de pregunta per a una pregunta específica d'una enquesta.
     *
     * @param idEnquesta     ID de l'enquesta.
     * @param ordrePregunta  Ordre de la pregunta dins de l'enquesta.
     * @return Codi del tipus de pregunta.
     */
    public String getCodiTipusPregunta(int idEnquesta, int ordrePregunta) {
        TxGetCodiTipusPregunta t = new TxGetCodiTipusPregunta(idEnquesta, ordrePregunta);
        t.execute();
        return t.result;
    }

    /**
     * Retorna el títol d'una pregunta específica d'una enquesta.
     *
     * @param idEnquesta     ID de l'enquesta.
     * @param ordrePregunta  Ordre de la pregunta dins de l'enquesta.
     * @return Títol de la pregunta.
     */
    public String getTitolPregunta(int idEnquesta, int ordrePregunta) {
        TxGetTitolPregunta t = new TxGetTitolPregunta(idEnquesta, ordrePregunta);
        t.execute();
        return t.result;
    }

    /**
     * Retorna si una pregunta específica d'una enquesta és obligatòria.
     *
     * @param idEnquesta     ID de l'enquesta.
     * @param ordrePregunta  Ordre de la pregunta dins de l'enquesta.
     * @return true si la pregunta és obligatòria, false altrament.
     */
    public boolean getObligatorietatPregunta(int idEnquesta, int ordrePregunta) {
        TxGetObligatorietatPregunta t = new TxGetObligatorietatPregunta(idEnquesta, ordrePregunta);
        t.execute();
        return t.result;
    }

    /**
     * Retorna el nom d'una enquesta donat el seu ID.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Nom de l'enquesta.
     */
    public String getNomEnquesta(int idEnquesta) {
        TxGetNomEnquesta t = new TxGetNomEnquesta(idEnquesta);
        t.execute();
        return t.result;
    }

    /**
     * Retorna les opcions de resposta d'una pregunta específica d'una enquesta.
     *
     * @param idEnquesta     ID de l'enquesta.
     * @param ordrePregunta  Ordre de la pregunta dins de l'enquesta.
     * @return Array de textos de les opcions de resposta.
     */
    public String[] getTextOpcionsPregunta(int idEnquesta, int ordrePregunta) {
        TxGetTextOpcionsPregunta t = new TxGetTextOpcionsPregunta(idEnquesta, ordrePregunta);
        t.execute();
        return t.result;
    }

    /**
     * Retorna el nombre de preguntes d'una enquesta donat el seu ID.
     *
     * @param idEnquesta ID de l'enquesta.
     * @return Nombre de preguntes de l'enquesta.
     */
    public int getNumPreguntesEnquesta(int idEnquesta) {
        TxGetNumPreguntesEnquesta t = new TxGetNumPreguntesEnquesta(idEnquesta);
        t.execute();
        return t.result;
    }

    /**
     * Exporta la resposta d'una enquesta a un fitxer en una ruta especificada.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param idResposta ID de la resposta.
     * @param path       Ruta on s'exportarà la resposta.
     */
    public void exportarRespostaEnquesta(int idEnquesta, int idResposta, String path) {
        Transaction t = new TxExportarResposta(idEnquesta, path, idResposta);
        t.execute();
    }

    /**
     * Importa una resposta d'una enquesta des d'un fitxer en una ruta especificada.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param path       Ruta d'on s'importaran les respostes.
     */
    public void importarRespostaEnquesta(int idEnquesta, String path) {
        Transaction t = new TxImportarResposta(idEnquesta, path);
        t.execute();
    }

    /**
     * Importa una enquesta des d'un fitxer en una ruta especificada.
     *
     * @param path      Ruta d'on s'importarà l'enquesta.
     * @param nomAdmin  Nom de l'administrador que importa l'enquesta.
     */
    public void importarEnquesta(String path, String nomAdmin) {
        Transaction t = new TxImportarEnquesta(path, nomAdmin);
        t.execute();
    }

    /**
     * Exporta una enquesta a un fitxer en una ruta especificada.
     *
     * @param idEnquesta ID de l'enquesta.
     * @param finalPath  Ruta on s'exportarà l'enquesta.
     */
    public void exportarEnquesta(int idEnquesta, String finalPath) {
        Transaction t = new TxExportarEnquesta(idEnquesta, finalPath);
        t.execute();
    }

    TxPlot2D txplot = null;
    /*
        * Calcula el plot 2D per una enquesta donada, un algoritme específic i un nombre de clústers donat.
        * @param idEnquesta    ID de l'enquesta.
        * @param algorithmName Nom de l'algoritme de clustering.
        * @param k             Nombre de clústers.
     */
    public void calcPlot2DGivenK(int idEnquesta, String algorithmName, int k, String distanceStrategyName, String coefficientStrategyName) {
        txplot = new TxPlot2D(idEnquesta, algorithmName, k, distanceStrategyName, coefficientStrategyName);
        txplot.execute();
    }

    /*
     * Calcula el plot 2D per una enquesta donada, un algoritme específic i un llindar de ràtio.
     * @param idEnquesta    ID de l'enquesta.
     * @param algorithmName Nom de l'algoritme de clustering.
     * @param thresholdRatio Ràtio de llindar per determinar el nombre òptim de clústers.
     */
    public void calcPlot2DOptimalK(int idEnquesta, String algorithmName, double thresholdRatio, String distanceStrategyName, String coefficientStrategyName) {
        txplot = new TxPlot2D(idEnquesta, algorithmName, thresholdRatio, distanceStrategyName, coefficientStrategyName);
        txplot.execute();
    }

    /*
     * Retorna el coeficient del plot calculat.
     * @return Coeficient del plot.
     */
    public double getPlotCoefficient() {
        if (txplot == null) {
            throw new IllegalStateException("Cal calcular el plot abans d'obtenir el coeficient.");
        }
        return txplot.getCoefficient();
    }

    /*
     * Retorna els punts del plot calculat.
     * @return Matriu de punts del plot.
     */
    public double[][] getPlotPoints(){
        if (txplot == null) {
            throw new IllegalStateException("Cal calcular el plot abans d'obtenir els punts.");
        }
        return txplot.getPlot2D();
    }

    public int getKOptimal() {
        if (txplot == null) {
            throw new IllegalStateException("Cal calcular el plot abans d'obtenir el k òptim.");
        }
        return txplot.getK();
    }

    public String[] getCentroidsPlot2D() {
        if (txplot == null) {
            throw new IllegalStateException("Cal calcular el plot abans d'obtenir els centroids.");
        }
        return txplot.getCentroidsStrings();
    }
}
