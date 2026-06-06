package Data.Persistance;

import Domain.Adapters.IRespostesIO;
import Domain.Model.*;
import com.google.gson.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class RespostesIO implements IRespostesIO {

    /**
     * Exports a RespostaEnquesta to a JSON file.
     * @param resposta the RespostaEnquesta object to export.
     * @param rutaFitxer the path to the JSON file.
     */
    @Override
    public void exportResposta(RespostaEnquesta resposta, String rutaFitxer) {
        ensureDirExists(rutaFitxer);

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        String jsonString = gson.toJson(resposta);

        try (FileWriter fileWriter = new FileWriter(rutaFitxer)) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Imports a RespostaEnquesta from a JSON file.
     * @param rutaFitxer the path to the JSON file.
     * @return the imported RespostaEnquesta object.
     */
    @Override
    public Domain.Model.RespostaEnquesta importResposta(String rutaFitxer) {
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
                .registerTypeAdapter(RespostaAPregunta.class, respostaAPreguntaDeser)
                .create();

        try (FileReader reader = new FileReader(rutaFitxer)) {
            return gson.fromJson(reader, RespostaEnquesta.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /*
        * Ensures that the directory for the given file path exists.
     */
    private void ensureDirExists(String dirPath) {
        File dir = new File(dirPath).getParentFile();
        if (!dir.exists()) {
            throw new RuntimeException("the specified path does not exist: " + dirPath);
        }
    }
}
