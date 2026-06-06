package Presentation.Controllers;

import Domain.Transactions.DomainController;
import Presentation.Views.VistaPrincipal;
import Presentation.Views.VistaRespostes;
import Presentation.Views.VistaRespondre;
import Presentation.Views.VistaAnalitzar;

/**
 * Controlador de presentació que gestiona la interacció entre la vista i el controlador de domini.
 */
public class CtrlPresentation {
    /** Controlador de domini */
    private final DomainController dc;
    /** Vista principal de l'aplicació */
    private final VistaPrincipal vistaPrincipal;
    /** Nom de l'usuari actual que ha iniciat sessió */
    private String usuariActual;

    /**
     * Constructor de la classe CtrlPresentation.
     * Inicialitza el controlador de domini i la vista principal.
     */
    public CtrlPresentation() {
        dc = new DomainController();
        vistaPrincipal = new VistaPrincipal(this);
        usuariActual = null;
    }

    /**
     * Inicialitza la presentació fent visible la vista principal.
     */
    public void inicializarPresentacion() {
        vistaPrincipal.hacerVisible();
    }

    /**
     * Registra un nou administrador amb el nom d'usuari i contrasenya proporcionats.
     *
     * @param nomAdmin Nom de l'administrador
     * @param contrasenya Contrasenya de l'administrador
     * @return true si el registre és exitós, false en cas contrari
     */
    public boolean registrarAdmin(String nomAdmin, String contrasenya) {
        try {
            dc.registrarAdmin(nomAdmin, contrasenya);
            usuariActual = nomAdmin;
            vistaPrincipal.actualitzarEstatSessio();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Inicia sessió per a un usuari amb el nom d'usuari i contrasenya proporcionats.
     *
     * @param nomUsuari Nom de l'usuari
     * @param contrasenya Contrasenya de l'usuari
     * @return true si la sessió s'inicia correctament, false en cas contrari
     */
    public boolean iniciarSessio(String nomUsuari, String contrasenya) {
        boolean success = dc.iniciarSessio(nomUsuari, contrasenya);
        if (success) {
            usuariActual = nomUsuari;
            vistaPrincipal.actualitzarEstatSessio();
        }
        return success;
    }

    /**
     * Crea una nova enquesta amb el nom proporcionat.
     *
     * @param nomEnquesta Nom de l'enquesta a crear
     * @return Identificador de l'enquesta creada
     */
    public int crearEnquesta(String nomEnquesta) {
        return dc.crearEnquesta(nomEnquesta);
    }

    /**
     * Retorna un mapa d'identificadors i noms d'enquestes per a l'usuari actual.
     *
     * @return Mapa amb ID d'enquesta com a clau i nom d'enquesta com a valor
     */
    public java.util.Map<Integer, String> getEnquestes() {
        String llistat = dc.getLlistatEnquestes(usuariActual);
        java.util.Map<Integer, String> enquestes = new java.util.HashMap<>();

        if (llistat == null || llistat.isEmpty()) {
            return enquestes;
        }

        String[] lines = llistat.split("\n");
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(", Nom: ");
            String idPart = parts[0].replace("ID: ", "").trim();
            String namePart = parts[1].trim();
            int id = Integer.parseInt(idPart);
            enquestes.put(id, namePart);
        }
        return enquestes;
    }

    /**
     * Obre la vista per editar una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta a editar
     */
    public void editarEnquesta(int idEnquesta) {
        vistaPrincipal.openEditSurvey(idEnquesta);
    }

    /**
     * Esborra una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta a esborrar
     */
    public void esborrarEnquesta(int idEnquesta) {
        dc.eiminarEnquesta(usuariActual, idEnquesta);
    }

    /**
     * Obre la vista per veure les respostes d'una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta
     */
    public void veureRespostesEnquesta(int idEnquesta) {
        // Get the survey name
        java.util.Map<Integer, String> enquestes = getEnquestes();
        String nomEnquesta = enquestes.getOrDefault(idEnquesta, "Encuesta #" + idEnquesta);

        // Open the responses view
        VistaRespostes vistaRespostes = new VistaRespostes(this, idEnquesta, nomEnquesta);
        vistaRespostes.hacerVisible();
    }

    /**
     * Obre la vista per respondre una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta a respondre
     */
    public void respondreEnquesta(int idEnquesta) {
        java.util.Map<Integer, String> enquestes = getEnquestes();
        String nomEnquesta = enquestes.getOrDefault(idEnquesta, "Encuesta #" + idEnquesta);
        VistaRespondre vistaRespondre = new VistaRespondre(this, idEnquesta, nomEnquesta);
        vistaRespondre.hacerVisible();
    }

    /**
     * Retorna les preguntes d'una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @return Array de preguntes
     */
    public String[] getPreguntesEnquesta(int idEnquesta) {
        return dc.getPreguntesEnquesta(idEnquesta);
    }

    /**
     * Guarda les respostes d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param respostes Array de respostes a guardar
     */
    public void guardarRespostes(int idEnquesta, String[] respostes) {
        dc.guardarRespostes(idEnquesta, respostes);
    }

    /**
     * Analitza les respostes d'una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta a analitzar
     */
    public void analitzarEnquesta(int idEnquesta) {
        java.util.Map<Integer, String> enquestes = getEnquestes();
        String nomEnquesta = enquestes.getOrDefault(idEnquesta, "Encuesta #" + idEnquesta);
        VistaAnalitzar vistaAnalitzar = new VistaAnalitzar(this, idEnquesta, nomEnquesta);
        vistaAnalitzar.hacerVisible();
    }

    /**
     * Clona una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta a clonar
     */
    public void clonarEnquesta(int idEnquesta) {
        dc.clonarEnquesta(idEnquesta, usuariActual);
    }

    /**
     * Elimina l'usuari actual i tanca la sessió.
     */
    public void eliminarUsuariActual() {
        dc.eliminarAdmin(usuariActual);
        usuariActual = null;
        vistaPrincipal.actualitzarEstatSessio();
    }

    /**
     * Retorna els identificadors de les respostes d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @return Array d'identificadors de respostes
     */
    public int[] getIdsRespostes(int idEnquesta) {
        return dc.getIdsRespostes(idEnquesta);
    }

    /**
     * Consulta la resposta d'una enquesta donat el seu identificador i el de la resposta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param idResposta Identificador de la resposta
     * @return Resposta com a cadena
     */
    public String consultarResposta(int idEnquesta, int idResposta) {
        return dc.consultarResposta(idEnquesta, idResposta);
    }

    /**
     * Tanca la sessió de l'usuari actual.
     */
    public void tancarSessio() {
        usuariActual = null;
        vistaPrincipal.actualitzarEstatSessio();
    }

    /**
     * Retorna l'usuari actual que ha iniciat sessió.
     *
     * @return Nom de l'usuari actual
     */
    public String getUsuariActual() {
        return usuariActual;
    }

    /**
     * Comprova si hi ha una sessió iniciada.
     *
     * @return true si hi ha una sessió iniciada, false en cas contrari
     */
    public boolean sessioIniciada() {
        return usuariActual != null;
    }

    /**
     * Retorna el nombre de preguntes d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @return Nombre de preguntes
     */
    public int getNumPreguntesEnquesta(int idEnquesta) {
        return dc.getNumPreguntesEnquesta(idEnquesta);
    }

    /**
     * Elimina una pregunta d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param i Ordre de la pregunta dins l'enquesta
     */
    public void eliminarPregunta(int idEnquesta, int i) {
        dc.eliminarPregunta(idEnquesta, i);
    }

    /**
     * Afegeix una nova pregunta a una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     */
    public void afegirPregunta(int idEnquesta) {
        dc.afegirPregunta(idEnquesta);
    }

    /**
     * Canvia el tipus d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @param tipusStr Nou tipus de la pregunta com a cadena
     */
    public void canviarTipusPregunta(int idEnquesta, int ordre, String tipusStr) {
        dc.canviarTipusPregunta(idEnquesta, ordre, tipusStr);
    }

    /**
     * Retorna el codi del tipus de pregunta per a una pregunta específica dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param i Ordre de la pregunta dins l'enquesta
     * @return Codi del tipus de pregunta
     */
    public String getCodiTipusPregunta(int idEnquesta, int i) {
        return dc.getCodiTipusPregunta(idEnquesta, i);
    }

    /**
     * Cancela l'edició d'una enquesta, descartant els canvis realitzats.
     *
     * @param idEnquesta Identificador de l'enquesta
     */
    public void cancelarEdicioEnquesta(int idEnquesta) {
        dc.cancelarEdicioEnquesta(idEnquesta);
    }

    /**
     * Guarda els canvis realitzats en l'edició d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     */
    public void guardarEdicioEnquesta(int idEnquesta) {
        dc.guardarEdicioEnquesta(idEnquesta, usuariActual);
    }

    /**
     * Retorna el nom d'una enquesta donat el seu identificador.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @return Nom de l'enquesta
     */
    public String getNomEnquesta(int idEnquesta) {
        return dc.getNomEnquesta(idEnquesta);
    }

    /**
     * Retorna el títol d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param i Ordre de la pregunta dins l'enquesta
     * @return Títol de la pregunta
     */
    public String getTitolPregunta(int idEnquesta, int i) {
        return dc.getTitolPregunta(idEnquesta, i);
    }

    /**
     * Canvia el títol d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @param text Nou títol de la pregunta
     */
    public void canviarTitolPregunta(int idEnquesta, int ordre, String text) {
        dc.modificarPregunta(idEnquesta, ordre, text);
    }

    /**
     * Afegeix una nova opció de resposta a una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @param optText Text de l'opció de resposta a afegir
     */
    public void afegirOpcioResposta(int idEnquesta, int ordre, String optText) {
        dc.afegirOpcioResposta(idEnquesta, ordre, optText);
    }

    /**
     * Modifica el text d'una opció de resposta d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordrePregunta Ordre de la pregunta dins l'enquesta
     * @param ordreOpcio Ordre de l'opció de resposta dins la pregunta
     * @param optText Nou text de l'opció de resposta
     */
    public void modificarOpcioResposta(int idEnquesta, int ordrePregunta, int ordreOpcio, String optText) {
        dc.modificarOpcioResposta(idEnquesta, ordrePregunta, ordreOpcio, optText);
    }

    /**
     * Retorna les opcions de resposta d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param i Ordre de la pregunta dins l'enquesta
     * @return Array de cadenes amb les opcions de resposta
     */
    public String[] getTextOpcionsPregunta(int idEnquesta, int i) {
        return dc.getTextOpcionsPregunta(idEnquesta, i);
    }

    /**
     * Esborra una opció de resposta d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @param ordreOpcio Ordre de l'opció de resposta dins la pregunta
     */
    public void esborrarOpcioResposta(int idEnquesta, int ordre, int ordreOpcio) {
        dc.eliminarOpcioResposta(idEnquesta, ordre, ordreOpcio);
    }

    /**
     * Calcula els clusters d'una enquesta utilitzant un algoritme de clustering específic i un valor k donat.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param algorithmName Nom de l'algoritme de clustering
     * @param k Nombre de clusters
     * @return Matriu amb els resultats del clustering
     */
    public int[][] computeClusterAlgorithm(int idEnquesta, String algorithmName, int k) {
        return dc.computeClusterAlgorithm(idEnquesta, algorithmName, k);
    }

    /**
     * Calcula el valor òptim de k per a un algoritme de clustering donat una enquesta i un llindar.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param algorithmName Nom de l'algoritme de clustering
     * @param thresholdRatio Ratio de llindar per determinar k òptim
     * @return Matriu amb els resultats del clustering per al k òptim
     */
    public int[][] computeClusterAlgorithmOptimalK(int idEnquesta, String algorithmName, double thresholdRatio) {
        return dc.computeClusterAlgorithmOptimalK(idEnquesta, algorithmName, thresholdRatio);
    }

    /**
     * Canvia l'obligatorietat d'una pregunta dins una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @param isObligatoria true si la pregunta ha de ser obligatòria, false en cas contrari
     */
    public void canviarObligatorietatPregunta(int idEnquesta, int ordre, boolean isObligatoria) {
        dc.canviarObligatorietatPregunta(idEnquesta, ordre, isObligatoria);
    }

    /**
     * Retorna si una pregunta és obligatòria.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param ordre Ordre de la pregunta dins l'enquesta
     * @return true si la pregunta és obligatòria, false en cas contrari
     */
    public boolean getObligatorietatPregunta(int idEnquesta, int ordre) {
        return dc.getObligatorietatPregunta(idEnquesta, ordre);
    }

    /**
     * Exporta una enquesta a un fitxer.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param path Ruta del fitxer on s'exportarà l'enquesta
     */
    public void exportarEnquesta(int idEnquesta, String path) {
       dc.exportarEnquesta(idEnquesta, path);
    }

    /**
     * Importa una enquesta des d'un fitxer.
     *
     * @param path Ruta del fitxer des d'on s'importarà l'enquesta
     */
    public void importarEnquesta(String path) {
        dc.importarEnquesta(path, usuariActual);
    }

    /**
     * Importa una resposta d'una enquesta des d'un fitxer.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param path Ruta del fitxer des d'on s'importarà la resposta
     */
    public void importarResposta(int idEnquesta, String path) {
        dc.importarRespostaEnquesta(idEnquesta, path);
    }

    /**
     * Exporta una resposta d'una enquesta a un fitxer.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param idResposta Identificador de la resposta
     * @param path Ruta del fitxer on s'exportarà la resposta
     */
    public void exportarResposta(int idEnquesta, int idResposta, String path) {
        dc.exportarRespostaEnquesta(idEnquesta, idResposta, path);
    }

    /**
     * Canvia el nom d'una enquesta.
     *
     * @param idEnquesta Identificador de l'enquesta
     * @param nomEnquesta Nou nom de l'enquesta
     */
    public void canviarNomEnquesta(int idEnquesta, String nomEnquesta) {
        dc.setEnquestaNom(idEnquesta, nomEnquesta);
    }

    /**
     * Calcula el plot 2D per una enquesta donada, un algoritme específic i un valor k.
     * @param idEnquesta    ID de l'enquesta.
     * @param algorithmName Nom de l'algoritme de clustering.
     * @param k             Nombre de clústers (k).
     * @param distanceStrategyName Nom de l'estratègia de distància.
     * @param coefficientStrategyName Nom de l'estratègia de càlcul del
     */
    public void calcPlot2DGivenK(int idEnquesta, String algorithmName, int k, String distanceStrategyName, String coefficientStrategyName) {
        dc.calcPlot2DGivenK(idEnquesta, algorithmName, k, distanceStrategyName, coefficientStrategyName);
    }

    /**
     * Calcula el plot 2D per una enquesta donada, un algoritme específic i un llindar de ràtio.
     * @param idEnquesta    ID de l'enquesta.
     * @param algorithmName Nom de l'algoritme de clustering.
     * @param thresholdRatio Ràtio de llindar per determinar el nombre òptim de clústers.
     * @param distanceStrategyName Nom de l'estratègia de distància.
     * @param coefficientStrategyName Nom de l'estratègia de càlcul del
     */
    public void calcPlot2DOptimalK(int idEnquesta, String algorithmName, double thresholdRatio, String distanceStrategyName, String coefficientStrategyName) {
        dc.calcPlot2DOptimalK(idEnquesta, algorithmName, thresholdRatio, distanceStrategyName, coefficientStrategyName);
    }

    /**
     * Retorna el coeficient del plot calculat.
     * @return Coeficient del plot.
     */
    public double getPlotCoefficient() {
        return dc.getPlotCoefficient();
    }

    /**
     * Retorna els punts del plot calculat.
     * @return Matriu de punts del plot.
     */
    public double[][] getPlotPoints(){
        return dc.getPlotPoints();
    }

    /**
     * Retorna el nombre òptim de clústers (k) del plot calculat.
     * @return Nombre òptim de clústers (k).
     */
    public int getKOptimal() {
        return dc.getKOptimal();
    }

    /**
     * Retorna els centroids del plot calculat.
     * @return Matriu de cadenes amb els centroids del plot.
     */
    public String[] getCentroidsPlot2D() {
        return dc.getCentroidsPlot2D();
    }
}