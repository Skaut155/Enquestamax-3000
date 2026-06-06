package Domain.Model;

/**
 * Class representing an option for questions with predefined answers
 */
public class Opcio{
    private int ordre;
    private String text;

    /**
     * Constructor for Opcio
     * @param ordre The order of the option inside the question
     * @param text The value of the option
     * @throws IllegalArgumentException if ordre is negative or text is null/blank
     */
    public Opcio(int ordre, String text) throws IllegalArgumentException {
        checkOrdre(ordre);
        checkText(text);
        this.ordre = ordre;
        this.text = text;
    }

    /**
     * Get the order of the option
     * @return ordre
     */
    public int getOrdre() { return ordre; }

    /**
     * Get the text of the option
     * @return text
     */
    public String getText() { return text; }

    /**
     * Set the order of the option
     * @param ordre The new order
     * @throws IllegalArgumentException if ordre is negative
     */
    public boolean setOrdre(int ordre) throws IllegalArgumentException {
        checkOrdre(ordre);
        this.ordre = ordre;
        return true;
    }

    /**
     * Set the text of the option
     * @param text The new value for the option
     * @return true if successful
     */
    public boolean setText(String text) {
        checkText(text);
        this.text = text;
        return true;
    }

    /**
     * String representation of the option
     * @return String in the format "[ordre] text"
     */
    @Override
    public String toString() {
        return "[" + (ordre+1) + "] " + text;
    }


    private static void checkText(String text) {
        if(text == null || text.isBlank()) throw new IllegalArgumentException("El text no pot ser buit");
    }

    private static void checkOrdre(int ordre) {
        if (ordre < 0) throw new IllegalArgumentException("L'ordre no pot ser negatiu");
    }
}

