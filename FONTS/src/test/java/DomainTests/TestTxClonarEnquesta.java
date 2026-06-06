package DomainTests;

import Domain.Controllers.IAdminCtrl;
import Domain.Factories.CtrlFactory;
import Domain.Model.*;
import Domain.Transactions.DomainController;
import Domain.Transactions.TxNovaRespostaEnquesta;
import org.junit.Before;
import org.junit.Test;

public class TestTxClonarEnquesta {

    IAdminCtrl iAdminCtrl;
    String nomAdmin = "test";
    int enquestaId;

    @Before
    public void setUp() {
        iAdminCtrl = CtrlFactory.getInstance().GetAdminCtrl();

        try{
            iAdminCtrl.GetAdmin(nomAdmin);
        } catch (Exception e) {
            Admin a = new Admin(nomAdmin,"password123");
            iAdminCtrl.AddAdmin(a);
            iAdminCtrl.updateAdmin(a);
        }

        Enquesta e = new Enquesta("A_CLONAR");

        Pregunta p = new Pregunta();
        p.setTitol("Quina edat tens?");
        p.setObligatoria(true);
        p.setTipusPregunta(PreguntaNumerica.getInstance());
        e.afegirPregunta(p);

        Pregunta p2 = new Pregunta();
        p2.setTitol("Quin és el teu color preferit?");
        p2.setObligatoria(false);
        p2.setTipusPregunta(PreguntaSeleccioUnicaNoOrdenada.getInstance());
        p2.afegirOpcio("Vermell");
        p2.afegirOpcio("Blau");
        p2.afegirOpcio("Verd");
        e.afegirPregunta(p2);

        CtrlFactory f = CtrlFactory.getInstance();
        f.GetEnquestaCtrl().AddEnquesta(e);
        enquestaId = e.getId();

        TxNovaRespostaEnquesta tx = new TxNovaRespostaEnquesta(e.getId(), new String[]{"25", "1"});
        tx.execute();
    }

    @Test
    public void testClonarEnquesta() {
        DomainController dc = new DomainController();

        dc.clonarEnquesta(enquestaId, nomAdmin);
    }
}
