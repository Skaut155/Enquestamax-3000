package Data.Persistance;

import Domain.Adapters.IAdminPersistance;
import Domain.Model.Admin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Implementation of admin persistence operations using JSON files.
 */
public class AdminPersistance implements IAdminPersistance {
    /**
     * Directory to store admin JSON files.
     */
    private static final String DATA_DIR = "./.data/admins";

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
     * Saves an admin.
     * @param admin the Admin object to save.
     */
    public void saveAdmin(Admin admin) {
        ensureDataDirExists();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonString = gson.toJson(admin);

        try (FileWriter fileWriter = new FileWriter(DATA_DIR + "/" + admin.getNom()+".json")) {
            fileWriter.write(jsonString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads an admin by their name.
     * @param nomAdmin the name of the admin to load.
     * @return the Admin object if found, null otherwise.
     */
    public Admin loadAdmin(String nomAdmin) {
        File file = new File(DATA_DIR + "/" + nomAdmin + ".json");
        if (!file.exists()) {
            return null;
        }

        Gson gson = new GsonBuilder().create();

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, Admin.class);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * Checks if an admin with the given name exists.
     * @param nomAdmin the name of the admin to check.
     * @return true if the admin exists, false otherwise.
     */
    public boolean existsAdmin(String nomAdmin) {
        File file = new File(DATA_DIR + "/" + nomAdmin + ".json");
        return file.exists();
    }

    /**
     * Deletes an admin by their name.
     * @param nomAdmin the name of the admin to delete.
     * @return true if the admin was successfully deleted, false otherwise.
     */
    public boolean deleteAdmin(String nomAdmin) {
        File file = new File(DATA_DIR + "/" + nomAdmin + ".json");
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }
}
