package es.unican.is.appgasolineras.activities.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.unican.is.appgasolineras.R;
import es.unican.is.appgasolineras.activities.filtrar.FiltrarPrecioView;
import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.common.prefs.Prefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.GasolinerasRepository;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;
import es.unican.is.appgasolineras.activities.detail.GasolineraDetailView;
import es.unican.is.appgasolineras.activities.info.InfoView;

public class MainView extends AppCompatActivity implements IMainContract.View {
    IPrefs prefs;
    private IMainContract.Presenter presenter;
    Double max = Double.MIN_VALUE;


    /*
    Activity lifecycle methods
     */

    /**
     * This method is automatically called when the activity is created
     * It fills the activity with the widgets (buttons, lists, etc.)
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Lista gasolineras");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        prefs = new Prefs(getApplicationContext(), "MY_APP");
        presenter = new MainPresenter(this);
        presenter.init();
        this.init();
    }

    /**
     * Create a menu in this activity (three dot menu on the top left)
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * This is the listener to the three-dot menu on the top left
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuInfo:
                presenter.onInfoClicked();
                return true;
            case R.id.menuRefresh:
                presenter.onRefreshClicked();
                return true;
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    IMainContract.View methods
     */

    @Override
    public void init() {
        // init UI listeners
        ListView lvGasolineras = findViewById(R.id.lvGasolineras);
        lvGasolineras.setOnItemClickListener((parent, view, position, id) -> {
            presenter.onGasolineraClicked(position);
        });
        Button precio = findViewById(R.id.btnFiltroPrecio);
        precio.setOnClickListener(view ->  {
                presenter.onPrecioClicked();
        });
    }

    @Override
    public IGasolinerasRepository getGasolineraRepository() {
        return new GasolinerasRepository(this);
    }

    @Override
    public void showGasolineras(List<Gasolinera> gasolineras) {
        for (Gasolinera g : gasolineras) {
            if (g.getNormal95() == null || g.getNormal95().equals("")
                    || g.getDieselA() == null || g.getDieselA().equals("")) {
                break;
            }
            Double maximo = Double.max(Double.parseDouble(g.getNormal95().replace(',','.')),
                    Double.parseDouble(g.getDieselA().replace(',', '.')));
            if (max < maximo) {
                max = maximo;
            }
        }
        gasolineras = presenter.filtra
                (gasolineras, prefs.getString("tipoGasolina"),
                        prefs.getInt("IDCCAA"), prefs.getString("maxPrecio"));
        prefs.delete("maxPrecio");
        GasolinerasArrayAdapter adapter = new GasolinerasArrayAdapter(this, gasolineras);
        ListView list = findViewById(R.id.lvGasolineras);
        list.setAdapter(adapter);
    }

    @Override
    public void showLoadCorrect(int gasolinerasCount) {
        String text = getResources().getString(R.string.loadCorrect);
        Toast.makeText(this, String.format(text, gasolinerasCount), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadError() {
        String text = getResources().getString(R.string.loadError);
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openGasolineraDetails(Gasolinera gasolinera) {
        Intent intent = new Intent(this, GasolineraDetailView.class);
        intent.putExtra(GasolineraDetailView.INTENT_GASOLINERA, gasolinera);
        startActivity(intent);
    }

    @Override
    public void openInfoView() {
        Intent intent = new Intent(this, InfoView.class);
        startActivity(intent);
    }

    @Override
    public void openFiltroPrecio() {
        Intent intent = new Intent(this, FiltrarPrecioView.class);
        intent.putExtra("max", String.valueOf(max));
        startActivity(intent);
    }
}
