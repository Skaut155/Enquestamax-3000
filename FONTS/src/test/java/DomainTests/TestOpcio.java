package DomainTests;

import Domain.Model.Opcio;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestOpcio {
    @Test
    public void gettersReturnInitialValues() {
        Opcio o = new Opcio(1, "Opció 1");
        assertEquals(1, o.getOrdre());
        assertEquals("Opció 1", o.getText());
    }

    @Test
    public void setOrdreUpdatesValue() {
        Opcio o = new Opcio(1, "Text");
        o.setOrdre(10);
        assertEquals(10, o.getOrdre());
    }

    @Test
    public void setTextUpdatesValue() {
        Opcio o = new Opcio(2, "Inicial");
        o.setText("Actualitzat");
        assertEquals("Actualitzat", o.getText());
    }

    @Test
    public void toStringFormatsCorrectly() {
        Opcio o = new Opcio(3, "Color");
        assertEquals("[4] Color", o.toString());
        Opcio o1 = new Opcio(0, "Color");
        assertEquals("[1] Color", o1.toString());
    }

    @Test
    public void doesNotAllowNullTextInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Opcio(5, null));
    }

    @Test
    public void doesNotSupportsNegativeOrdreInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Opcio(-1, "Text"));
    }

    @Test
    public void doesNotAllowBlankTextInConstructor() {
        assertThrows(IllegalArgumentException.class, () -> new Opcio(3, "   "));
    }

    @Test
    public void doesNotAllowNullText() {
        Opcio o = new Opcio(5, "No nul");
        assertThrows(IllegalArgumentException.class, () -> o.setText(null));
    }

    @Test
    public void doesNotAllowBlankText() {
        Opcio o = new Opcio(5, "Valid");
        assertThrows(IllegalArgumentException.class, () -> o.setText("   "));
    }

    @Test
    public void doesNotSupportsNegativeOrdre() {
        Opcio o = new Opcio(0, "Text");
        assertThrows(IllegalArgumentException.class, () -> o.setOrdre(-1));
    }
}
