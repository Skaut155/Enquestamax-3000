package Domain.Adapters;

import Domain.Model.Enquesta;

/**
 * Interface for survey (enquesta) persistence operations.
 */
public interface IEnquestaPersistance {
    /**
     * Saves a survey (enquesta).
     * @param enquesta the Enquesta object to save.
     * @return the path to the saved JSON file.
     */
    public String saveEnquesta(Enquesta enquesta);

    /**
     * Loads a survey (enquesta) by its ID.
     * @param idEnquesta the ID of the survey to load.
     * @return the Enquesta object if found, null otherwise.
     */
    public Enquesta loadEnquesta(int idEnquesta);

    /**
     * Loads a survey (enquesta) from a specified file path.
     * @param pathToFile the path to the JSON file containing the survey data.
     * @return the Enquesta object if found, null otherwise.
     */
    public Enquesta loadEnquestaByPath(String pathToFile);

    /**
     * Deletes a survey (enquesta) by its ID.
     * @param idEnquesta the ID of the survey to delete.
     * @return true if the survey was successfully deleted, false otherwise.
     */
    public boolean deleteEnquesta(int idEnquesta);

    /**
     * Checks if a survey (enquesta) with the given ID exists.
     * @param idEnquesta the ID of the survey to check.
     * @return true if the survey exists, false otherwise.
     */
    public boolean existsEnquesta(int idEnquesta);

    /**
     * Gets the last used survey (enquesta) ID.
     * @return the last used survey ID.
     */
    public int getLastId();

    /**
     * Gets the file path for a given survey (enquesta) ID.
     * @param idEnquesta the ID of the survey.
     * @return the file path as a String.
     */
    public String getPathOfEnquesta(int idEnquesta);
}
