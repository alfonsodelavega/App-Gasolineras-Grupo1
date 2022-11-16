package es.unican.is.appgasolineras.activities.listaFavoritas;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

import static es.unican.is.appgasolineras.utils.Matchers.hasElements;
import static es.unican.is.appgasolineras.utils.Matchers.sizeElements;

import android.Manifest;
import android.view.View;

import androidx.room.Room;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.rule.GrantPermissionRule;

import org.hamcrest.CoreMatchers;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.menuPrincipal.MenuPrincipalView;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.db.GasolineraDao;
import es.unican.is.appgasolineras.repository.db.GasolineraDatabase;
import es.unican.is.appgasolineras.repository.rest.GasolinerasServiceConstants;
import es.unican.is.appgasolineras.utils.ScreenshotTestRule;

public class ListaFavoritasUITest {

    private static View decorView;
    private static GasolineraDao gas;
    private GasolineraDatabase db;

    @BeforeClass
    public static void setUp() {
        GasolinerasServiceConstants.setStaticURL();
    }

    @AfterClass
    public static void clean() {
        GasolinerasServiceConstants.setMinecoURL();
        gas.deleteAll();
    }

    @Before
    public void inicializa() {
        db = Room.databaseBuilder(getApplicationContext(),
                GasolineraDatabase.class, "database-name").allowMainThreadQueries().build();
        gas = db.gasolineraDao();
        gas.deleteAll();
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    // IMPORTANTE: No tiene rule, se incluye en el rule de abajo
    public ActivityScenarioRule<MenuPrincipalView> activityRule =
            new ActivityScenarioRule(MenuPrincipalView.class);

    // Aquí se combinan el ActivityScenarioRule y el ScreenshotTestRule,
    // de forma que la captura de pantalla se haga antes de que se cierre la actividad
    @Rule
    public final TestRule activityAndScreenshotRule = RuleChain
            .outerRule(activityRule)
            .around(new ScreenshotTestRule());

    @Rule
    public GrantPermissionRule permissionRule =
            GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);


    @Test
    public void anhadirGasolineraAListaFavoritasTest() {

        /*
          Seleccionamos Cantabria como comunidad ya que el fichero estatico solo
          dispone de gasolineras de esta comunidad autonoma
         */
        // Anhadimos a los filtros Cantabria
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.btnResetearPermanentes)).perform(scrollTo(), click());
        // Selecciono en el spinner de la comunidad: Cantabria
        onView(withId(R.id.spinner_CCAA)).perform(scrollTo(), click());
        onData(CoreMatchers.anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Cantabria")));
        // Guardo los filtros
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());

        /*
          Caso de prueba PI.1: Exito, Anhadir gasolinera (se prueba anhadiendo
              la primera gasolinera mostrada y la ultima
         */

        /*
          Se anhade la primera y se comprueba
         */

        // Se comprueba que se muestran elementos en la lista y el toast mostrado
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
        onView(withText("Se han cargado 156 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        //Direccion de la gasolinera en primera posicion de la lista
        String direccion1 = "CARRETERA 6316 KM. 10,5";
        DataInteraction gasEnLista1;
        gasEnLista1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);

        // Comprobamos que concuerda con la direccion
        gasEnLista1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));

        // Accedemos a su vista detallada
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(scrollTo(), click());

        // Anhadimos a favoritos y comprobamos el toast
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());
        onView(withText("Se ha añadido la gasolinera correctamente")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        // Volvemos al menu principal
        Espresso.pressBack();
        Espresso.pressBack();

        // Accedemos a la lista de favoritos y comprobamos que se muestra 1 gasolinera
        onView(withId(R.id.btnAccederFavoritos)).perform(scrollTo(), click());
        onView(withText("Se han cargado 1 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        onView(withId(R.id.lvGasolineras2)).check(matches(sizeElements(1)));

        // Comparamos la direccion de esta con la que se encontraba en la lista de gasolineras
        // para comprobar que se trata de la misma
        DataInteraction gasEnListaFavoritas;
        gasEnListaFavoritas = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0);
        gasEnListaFavoritas.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));

        // Volvemos al menu principal
        Espresso.pressBack();


        /*
          Se anhade la ultima y se comprueba
         */

        // Se comprueba que se muestran elementos en la lista y el toast mostrado
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
        onView(withText("Se han cargado 156 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        //Direccion de la gasolinera en ultima posicion de la lista
        String direccion2 = "BARRIO SAN PANTALEÓN, S/N";
        DataInteraction gasEnLista2;
        gasEnLista2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(155);

        // Comprobamos que concuerda con la direccion
        gasEnLista2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));

        // Accedemos a su vista detallada
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(155).perform(scrollTo(), click());

        // Anhadimos a favoritos y comprobamos el toast
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());
        onView(withText("Se ha añadido la gasolinera correctamente")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        // Volvemos al menu principal
        Espresso.pressBack();
        Espresso.pressBack();

        // Accedemos a la lista de favoritos y comprobamos que se muestran 2 gasolineras
        onView(withId(R.id.btnAccederFavoritos)).perform(scrollTo(), click());
        onView(withText("Se han cargado 2 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        onView(withId(R.id.lvGasolineras2)).check(matches(sizeElements(2)));

        // Comparamos la direccion de ambas gasolineras que se encuentran en la lista de favoritas
        // para comprobar que se trata de las mismas que hemos seleccionado en la
        // lista de gasolineras
        // En la lista de favoritos, la ultima que se anhade se muestra la primera
        DataInteraction gasEnListaFavoritas1;
        gasEnListaFavoritas1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0);
        gasEnListaFavoritas1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));
        DataInteraction gasEnListaFavoritas2;
        gasEnListaFavoritas2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(1);
        gasEnListaFavoritas2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));

        // Volvemos al menu principal
        Espresso.pressBack();

        /*
          Caso de prueba PI.2: Anhadir gasolinera ya anhadida anteriormente
               Se prueba con la segunda gasolinera de la lista.
               Se prueba tanto que se ha anhadido a la lista como que no se
               ha anhadido dos veces
         */

        // Se comprueba que se muestran elementos en la lista y el toast mostrado
        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));
        onView(withText("Se han cargado 156 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        //Direccion de la gasolinera en segunda posicion de la lista
        String direccion3 = "CR N-629 79,7";
        DataInteraction gasEnLista3;
        gasEnLista3 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);

        // Comprobamos que concuerda con la direccion
        gasEnLista3.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion3)));

        // Accedemos a su vista detallada
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).perform(scrollTo(), click());

        // Anhadimos a favoritos y comprobamos el toast
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());
        onView(withText("Se ha añadido la gasolinera correctamente")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        // Pulsamos de nuevo en el boton de anhadir a favoritos y comprobamos el toast
        // que muestra que esta gasolinera ya esta en favoritos
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());
        onView(withText("La gasolinera ya se encuentra añadida a la lista")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));

        // Volvemos al menu principal
        Espresso.pressBack();
        Espresso.pressBack();

        // Accedemos a la lista de favoritos y comprobamos que se muestran 3 gasolineras
        // (Las 2 del caso de prueba anterior y la anhadida en este)
        onView(withId(R.id.btnAccederFavoritos)).perform(scrollTo(), click());
        onView(withText("Se han cargado 3 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        onView(withId(R.id.lvGasolineras2)).check(matches(sizeElements(3)));

        // Comparamos la direccion de las 3 gasolineras que se encuentran en la lista de favoritas
        // para comprobar que se trata de las mismas que hemos seleccionado en la
        // lista de gasolineras
        // En la lista de favoritos, la ultima que se anhade se muestra la primera
        gasEnListaFavoritas1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0);
        gasEnListaFavoritas1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion3)));
        gasEnListaFavoritas2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(1);
        gasEnListaFavoritas2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));
        DataInteraction gasEnListaFavoritas3;
        gasEnListaFavoritas3 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(2);
        gasEnListaFavoritas3.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));

        // Volvemos al menu principal
        Espresso.pressBack();


        /*
          Caso de prueba PI.3 : Persisten las gasolineras en favoritas si se cierra la aplicacion
          Como ya se ha comprobado en los casos de prueba anteriores el hecho de anhadir la
          gasolinera a la lista de favoritos, esta prueba va a consistir en cerrar la aplicacion
          por completo, volver a iniciarla y comprobar que las gasolineras aun estan en la lista
          de favoritas
          El test se encuentra comentado debido a problemas de integracion con gitHub Actions,
          pero se puede probar localmente
         */

        /*
        // Cerramos la aplicacion
        pressBackUnconditionally();
        activityRule.getScenario().close();
        // Volvemos a iniciar la aplicacion
        ActivityScenario.launch(MenuPrincipalView.class);
        // Accdemos a la lista de favoritas
        onView(withId(R.id.btnAccederFavoritos)).perform(scrollTo(), click());
        // Comprobamos que se muestran 3 gasolineras (las anhadidas en los casos
        // de prueba anteriores
        onView(withText("Se han cargado 3 gasolineras")).inRoot(RootMatchers.withDecorView(not(decorView))).check(matches(isDisplayed()));
        onView(withId(R.id.lvGasolineras2)).check(matches(sizeElements(3)));
        // Comparamos la direccion de las 3 gasolineras que se encuentran en la lista de favoritas
        // para comprobar que se trata de las mismas que hemos seleccionado en la
        // lista de gasolineras
        // En la lista de favoritos, la ultima que se anhade se muestra la primera
        gasEnListaFavoritas1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0);
        gasEnListaFavoritas1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion3)));
        gasEnListaFavoritas2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(1);
        gasEnListaFavoritas2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));
        gasEnListaFavoritas3 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(2);
        gasEnListaFavoritas3.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));
        // Volvemos al menu principal
        Espresso.pressBack();
        */

        // Reseteo los filtros
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        onView(withId(R.id.btnResetearPermanentes)).perform(scrollTo(), click());
        // Compruebo que los filtros se han reseteado
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Todas")));

    }


    @Test
    public void consultarGasolineraFavoritaTest(){
        // Anhadimos a los filtros Cantabria
        onView(withId(R.id.btnAccederFiltrosPermanentes)).perform(scrollTo(), click());
        // Selecciono en el spinner de la comunidad: Cantabria
        onView(withId(R.id.spinner_CCAA)).perform(scrollTo(), click());
        onData(CoreMatchers.anything()).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.spinner_CCAA)).check(matches(withSpinnerText("Cantabria")));
        onView(withId(R.id.btnGuardarPermanentes)).perform(scrollTo(), click());

        onView(withId(R.id.btnAccederLista)).perform(scrollTo(), click());
        onView(withId(R.id.lvGasolineras)).check(matches(hasElements()));

        String direccion1 = "CARRETERA 6316 KM. 10,5";
        DataInteraction gas1;
        gas1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0);
        gas1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());
        Espresso.pressBack();
        String direccion2 = "CR N-629 79,7";
        DataInteraction gas2;
        gas2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1);
        gas2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras)).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.btnAnhadirGasolineraFavoritas)).perform(scrollTo(), click());

        Espresso.pressBack();
        Espresso.pressBack();
        List<Gasolinera> gasolineras = db.gasolineraDao().getAll();
        db.gasolineraDao().deleteAll();
        //Pongo un precio absurdo a las gasolineras para luego comprobar si se actualizan
        for (Gasolinera g : gasolineras) {
            g.setDieselA("30");
            g.setNormal95("30");
            db.gasolineraDao().insertAll(g);
        }

        onView(withId(R.id.btnAccederFavoritos)).perform(click());
        onView(withId(R.id.lvGasolineras2)).check(matches(sizeElements(2)));
        DataInteraction fav1;
        String precioGas = "1,859";
        String precioDies = "1,999";
        fav1 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(1);
        fav1.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion1)));
        fav1.onChildView(withId(R.id.tv95)).check(matches(withText(precioGas)));
        fav1.onChildView(withId(R.id.tvDieselA)).check(matches(withText(precioDies)));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(1).perform(scrollTo(), click());
        onView(withId(R.id.tvGasolina)).check(matches(withText("Gasolina95: 1,85 €")));
        onView(withId(R.id.tvDiesel)).check(matches(withText("DiéselA: 1,99 €")));
        double diesel = Double.parseDouble(precioDies.replace(",", "."));
        double gasolina = Double.parseDouble(precioGas.replace(",", "."));
        onView(withId(R.id.tvMedia)).check(matches(withText("Media: " + String.valueOf((diesel + 2 * gasolina) / 3).replace(".", ",").substring(0, 4) + " €")));
        Espresso.pressBack();

        DataInteraction fav2 = onData(CoreMatchers.anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0);
        precioDies = "2,009";
        precioGas = "1,819";
        fav2.onChildView(withId(R.id.tvAddress)).check(matches(withText(direccion2)));
        fav2.onChildView(withId(R.id.tv95)).check(matches(withText(precioGas)));
        fav2.onChildView(withId(R.id.tvDieselA)).check(matches(withText(precioDies)));
        onData(anything()).inAdapterView(withId(R.id.lvGasolineras2)).atPosition(0).perform(scrollTo(), click());
        onView(withId(R.id.tvGasolina)).check(matches(withText("Gasolina95: 1,81 €")));
        onView(withId(R.id.tvDiesel)).check(matches(withText("DiéselA: 2,00 €")));
        diesel = Double.parseDouble(precioDies.replace(",", "."));
        gasolina = Double.parseDouble(precioGas.replace(",", "."));
        onView(withId(R.id.tvMedia)).check(matches(withText("Media: " + String.valueOf((diesel + 2 * gasolina) / 3).replace(".", ",").substring(0, 4) + " €")));

    }

}
