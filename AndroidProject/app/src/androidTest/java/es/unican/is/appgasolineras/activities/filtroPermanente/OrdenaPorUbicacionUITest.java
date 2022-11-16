package es.unican.is.appgasolineras.activities.filtroPermanente;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.is.appgasolineras.utils.Matchers.hasElements;
import static es.unican.is.appgasolineras.utils.Matchers.sizeElements;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.main.MainView;
import es.unican.is.appgasolineras.activities.menuPrincipal.MenuPrincipalView;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;

public class OrdenaPorUbicacionUITest {

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL2();
        MainView.setPruebas(true);
        FiltroPermanenteView.setPruebas(true);
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        MainView.setPruebas(false);
        FiltroPermanenteView.setPruebas(false);
    }

    @Rule
    public ActivityScenarioRule<MenuPrincipalView> activityRule =
            new ActivityScenarioRule(MenuPrincipalView.class);

    @Test
    public void guardarFiltrosTest() {
        //PRUEBA: Exito con todos los filtros
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).perform(scrollTo(), click());
        // Selecciono en el spinner del combustible: Gasolina 95 E5
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).check(matches(withSpinnerText("Gasolina 95 E5")));
        onView(withId(R.id.spinner_CCAA)).perform(scrollTo(), click());
        // Selecciono en el spinner de la comunidad: Todas
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));
        //Selecciono Si en el checkbox
        onView(withId(R.id.checkBoxSi)).perform(scrollTo(), click());
        onView(withId(R.id.checkBoxSi)).check(matches(isChecked()));
        // Guardo filtros
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());
        //Veo Lista
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));

        //Dos primeras gasolineras de la lista
        DataInteraction g;
        g = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        g.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA N-623 KM. 136")));

        DataInteraction g2;
        g2 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        g2.onChildView(withId(R.id.tvAddress)).check(matches(withText("AVENIDA LUIS DE LA CONCHA, 63")));

        //PRUEBA. No se activa el ordenar por localizacion
        Espresso.pressBack();
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).perform(scrollTo(), click());
        // Selecciono en el spinner del combustible: Gasolina 95 E5
        onData(anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).check(matches(withSpinnerText("Gasolina 95 E5")));
        onView(withId(R.id.spinner_CCAA)).perform(scrollTo(), click());
        // Selecciono en el spinner de la comunidad: Todas
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));
        //Selecciono No en el checkbox
        onView(withId(R.id.checkBoxNo)).perform(scrollTo(), click());
        onView(withId(R.id.checkBoxNo)).check(matches(isChecked()));
        // Guardo filtros
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());
        //Veo Lista
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));

        //Dos primeras gasolineras de la lista
        DataInteraction g3;
        g3 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        g3.onChildView(withId(R.id.tvAddress)).check(matches(withText("CARRETERA 6316 KM. 10,5")));

        DataInteraction g4;
        g4 = onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        g4.onChildView(withId(R.id.tvAddress)).check(matches(withText("CR N-629 79,7")));

        //PRUEBA. Filtros demasiado restrictivos
        Espresso.pressBack();
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).perform(scrollTo(), click());
        // Selecciono en el spinner del combustible: Hidrogeno
        onData(anything()).atPosition(14).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).check(matches(withSpinnerText("Hidrógeno")));
        onView(withId(R.id.spinner_CCAA)).perform(scrollTo(), click());
        // Selecciono en el spinner de la comunidad: Todas
        onData(anything()).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));
        //Selecciono Si en el checkbox
        onView(withId(R.id.checkBoxSi)).perform(scrollTo(), click());
        onView(withId(R.id.checkBoxSi)).check(matches(isChecked()));
        // Guardo filtros
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());
        //Veo Lista
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(sizeElements(0)));

        //PRUEBA. Comprobar si persisten los filtros
        Espresso.pressBack();
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.spinner_combustible)).check(matches(withSpinnerText("Hidrógeno")));
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));
        onView(withId(R.id.checkBoxSi)).check(matches(isChecked()));
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());


        //PRUEBA:Reseteo
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.btnResetearPermanentes)).perform(scrollTo(), click());
        // Compruebo que los filtros se han reseteado
        onView(withId(R.id.spinner_combustible)).check(matches(withSpinnerText("Todos")));
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));
        onView(withId(R.id.checkBoxNo)).check(matches(isChecked()));

        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
    }
}
