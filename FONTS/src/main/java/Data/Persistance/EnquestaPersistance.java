package Data.Persistance;

import Domain.Adapters.IEnquestaPersistance;
import Domain.Model.*;
import com.google.gson.*;

import java.io.*;
import java.time.LocalDateTime;

/**
 * Implementation of survey (enquesta) persistence operations using JSON files.
 */
public class EnquestaPersistance implements IEnquestaPersistance {
    /**
     * Directory to store survey (enquesta) JSON files.
     */
    private static final String DATA_DIR = "./.data/enquestes";

    /**
     * Ensures that the data directory exists; creates it if it doesn't.
     */
    private void ensureDataDirExists() {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves a survey (enquesta).
     * @param enquesta the Enquesta object to save.
     * @return the path to the saved JSON file.
     */
    public String saveEnquesta(Enquesta enquesta) {
        ensureDataDirExists();

        // Java 21 safe LocalDateTime adapter
        JsonSerializer<LocalDateTime> ldtSer = (src, typeOfSrc, context) -> context.serialize(src.toString());
        JsonDeserializer<LocalDateTime> ldtDeser = (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString());

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, ldtSer)
                .registerTypeAdapter(LocalDateTime.class, ldtDeser)
                .setPrettyPrinting()
                .create();
        String jsonString = gson.toJson(enquesta);

        String fPath = getPath(enquesta.getId());
        try (FileWriter fileWriter = new FileWriter(fPath)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return fPath;
    }

    /**
     * Loads a survey (enquesta) by its ID.
     * @param idEnquesta the ID of the survey to load.
     * @return the Enquesta object if found, null otherwise.
     */
    public Enquesta loadEnquesta(int idEnquesta) {
        return loadEnquestaByPath(getPath(idEnquesta));
    }

    /**
     * Loads a survey (enquesta) from a specified file path.
     * @param pathToFile the path to the JSON file containing the survey data.
     * @return the Enquesta object if found, null otherwise.
     */
    public Enquesta loadEnquestaByPath(String pathToFile) {
        File file = new File(pathToFile);
        if (!file.exists()) {
            return null;
        }

        JsonSerializer<LocalDateTime> ldtSer = (src, typeOfSrc, context) -> context.serialize(src.toString());
        JsonDeserializer<LocalDateTime> ldtDeser = (json, typeOfT, context) -> LocalDateTime.parse(json.getAsString());

        // Custom deserializer to reconstruct the correct TipusPregunta singleton from its code
        JsonDeserializer<TipusPregunta> tipusPreguntaDeser = (json, typeOfT, context) -> {
            JsonObject obj = json.getAsJsonObject();
            JsonElement codeElem = obj.get("code");
            if (codeElem == null || codeElem.isJsonNull()) {
                return PreguntaSeleccioUnicaNoOrdenada.getInstance(); // default type
            }
            String code = codeElem.getAsString();
            switch (code) {
                case "UNICA_ORDENADA":
                    return PreguntaSeleccioUnicaOrdenada.getInstance();
                case "UNICA_NO_ORDENADA":
                    return PreguntaSeleccioUnicaNoOrdenada.getInstance();
                case "MULTIPLE":
                    return PreguntaSeleccioMultiple.getInstance();
                case "OBERTA":
                    return PreguntaOberta.getInstance();
                case "NUMERICA":
                    return PreguntaNumerica.getInstance();
                default:
                    return PreguntaSeleccioUnicaNoOrdenada.getInstance();
            }
        };

        JsonDeserializer<RespostaAPregunta<?>> respostaAPreguntaDeser = (json, typeOfT, context) -> {
            JsonObject obj = json.getAsJsonObject();
            // We expect a 'code' discriminator that identifies the concrete type
            JsonElement codeElem = obj.get("code");
            if (codeElem == null || codeElem.isJsonNull()) {
                // Fallback: return null or throw; here we choose null to avoid breaking deserialization
                return null;
            }
            String code = codeElem.getAsString();

            // Get common fields
            JsonElement ordreElem = obj.get("ordrePregunta");
            if (ordreElem == null || ordreElem.isJsonNull()) {
                return null;
            }
            int ordrePregunta = ordreElem.getAsInt();

            // Get the response value based on type
            JsonElement respostaElem = obj.get("resposta");

            switch (code) {
                case "NUMERICA":
                    if (respostaElem != null && !respostaElem.isJsonNull()) {
                        double valorNumeric = respostaElem.getAsDouble();
                        return new RespostaAPreguntaNumerica(valorNumeric, ordrePregunta);
                    }
                    return new RespostaAPreguntaNumerica(ordrePregunta);

                case "OBERTA":
                    if (respostaElem != null && !respostaElem.isJsonNull()) {
                        String valorOberta = respostaElem.getAsString();
                        return new RespostaAPreguntaOberta(valorOberta, ordrePregunta);
                    }
                    return new RespostaAPreguntaOberta(ordrePregunta);

                case "MULTIPLE":
                    if (respostaElem != null && !respostaElem.isJsonNull() && respostaElem.isJsonArray()) {
                        java.util.ArrayList<Integer> opcionsMultiples = new java.util.ArrayList<>();
                        for (JsonElement elem : respostaElem.getAsJsonArray()) {
                            opcionsMultiples.add(elem.getAsInt());
                        }
                        return new RespostaAPreguntaMultiple(opcionsMultiples, ordrePregunta);
                    }
                    return new RespostaAPreguntaMultiple(ordrePregunta);

                case "UNICA_ORDENADA":
                    if (respostaElem != null && !respostaElem.isJsonNull()) {
                        int valorOrdenada = respostaElem.getAsInt();
                        if(valorOrdenada != -1)
                            return new RespostaAPreguntaOrdenada(valorOrdenada, ordrePregunta);
                        else
                            return new RespostaAPreguntaOrdenada(ordrePregunta);
                    }
                    return new RespostaAPreguntaOrdenada(ordrePregunta);

                case "UNICA_NO_ORDENADA":
                    if (respostaElem != null && !respostaElem.isJsonNull()) {
                        int valorNoOrdenada = respostaElem.getAsInt();
                        if(valorNoOrdenada != -1)
                            return new RespostaAPreguntaNoOrdenada(valorNoOrdenada, ordrePregunta);
                        else
                            return new RespostaAPreguntaNoOrdenada(ordrePregunta);
                    }
                    return new RespostaAPreguntaNoOrdenada(ordrePregunta);

                default:
                    return null;
            }
        };

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, ldtSer)
                .registerTypeAdapter(LocalDateTime.class, ldtDeser)
                .registerTypeAdapter(TipusPregunta.class, tipusPreguntaDeser)
                .registerTypeAdapter(RespostaAPregunta.class, respostaAPreguntaDeser)
                .create();

        try (FileReader reader = new FileReader(file)) {
            Enquesta e = gson.fromJson(reader, Enquesta.class);
            // iterate through all RespostaEnquesta and set their parent RepostesContainer
            RespostesContainer respostesContainer = e.getRespostesContainer();
            for (RespostaEnquesta re : e.getAllRespostes()) {
                for(RespostaAPregunta<?> rap : re.getRespostes()) {
                    rap.setRespostaEnquesta(re);
                }
                re.associaContainer(respostesContainer);

            }
            respostesContainer.associaEnquesta(e);

            return e;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Deletes a survey (enquesta) by its ID.
     * @param idEnquesta the ID of the survey to delete.
     * @return true if the survey was successfully deleted, false otherwise.
     */
    public boolean deleteEnquesta(int idEnquesta) {
        File file = new File(getPath(idEnquesta));
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * Checks if a survey (enquesta) with the given ID exists.
     * @param idEnquesta the ID of the survey to check.
     * @return true if the survey exists, false otherwise.
     */
    public boolean existsEnquesta(int idEnquesta) {
        File file = new File(getPath(idEnquesta));
        return file.exists();
    }

    /**
     * Gets the last used survey (enquesta) ID.
     * @return the last used ID, or 0 if no surveys exist.
     */
    public int getLastId(){
        ensureDataDirExists();
        File dir = new File(DATA_DIR);
        String[] files = dir.list();
        int maxId = 0;
        if (files != null) {
            for (String fileName : files) {
                if (fileName.startsWith("enquesta-") && fileName.endsWith(".json")) {
                    String idStr = fileName.substring(9, fileName.length() - 5);
                    int id = Integer.parseInt(idStr);
                    if (id > maxId) {
                        maxId = id;
                    }
                }
            }
        }
        return maxId;
    }

    /** Returns the file path for a given survey (enquesta) ID.
     * @param idEnquesta the ID of the survey.
     * @return the file path as a String.
     */
    public String getPathOfEnquesta(int idEnquesta) {
            return getPath(idEnquesta);
    }

    /**
     * Gets the file path for a given survey (enquesta) ID.
     * @param idEnquesta the ID of the survey.
     * @return the file path as a String.
     */
    private static String getPath(int idEnquesta) {
        return DATA_DIR + "/enquesta-" + idEnquesta + ".json";
    }
}
