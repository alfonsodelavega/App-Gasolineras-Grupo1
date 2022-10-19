package es.unican.is.appgasolineras.activities.main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import es.unican.is.appgasolineras.common.prefs.IPrefs;
import es.unican.is.appgasolineras.model.Gasolinera;
import es.unican.is.appgasolineras.repository.IGasolinerasRepository;

public class MainPresenter implements IMainContract.Presenter {

    private static final String IDCOMUNIDAD = "idComunidad";
    private static final String TIPOGASOLINA = "tipoGasolina";
    private static final String MAXPRECIOSTRING = "maxPrecio";
    private static final String DIESELA = "dieselA";
    private static final String NORMAL95 = "normal95";
    private static final String NORMAL95E10 = "normal95E10";
    private static final String NORMAL95E5P = "normal95E5p";
    private static final String NORMAL98E5 = "normal98E5";
    private static final String NORMAL98E10 = "normal98E10";
    private static final String DIESELP = "dieselP";
    private static final String DIESELB = "dieselB";
    private static final String BIOETANOL = "bioEtanol";
    private static final String BIODIESEL = "bioDiesel";
    private static final String GLP = "glp";
    private static final String GASC = "gasC";
    private static final String GASL = "gasL";
    private static final String H2 = "h2";

    private final IMainContract.View view;
    private IGasolinerasRepository repository;
    private IPrefs prefs;
    double max = Double.MIN_VALUE;
    String maxPrecio;
    Boolean red;
    private List<Gasolinera> data;

    private List<Gasolinera> shownGasolineras;

    public MainPresenter(IMainContract.View view, IPrefs prefs, Boolean red) {
        this.view = view;
        this.prefs = prefs;
        this.red = red;
    }

    @Override
    public void init() {
        if (repository == null) {
            repository = view.getGasolineraRepository();
        }
        if (repository != null) {
            if (red) {
                doSyncInit();
            } else {
                //Persistir
            }
        }
    }

    /**
    private void doAsyncInit() {
        repository.requestGasolineras(new Callback<List<Gasolinera>>() {

            //@Override
            public void onSuccess(List<Gasolinera> data) {
                List<Gasolinera> dataAsync;
                if (prefs.getString(IDCOMUNIDAD).equals("")){
                    dataAsync = repository.todasGasolineras();
                }else {
                    dataAsync = repository.getGasolineras(prefs.getString(IDCOMUNIDAD));
                }
                maxPrecio = maximoEntreTodas(dataAsync);
                dataAsync = filtraTipo(dataAsync, prefs.getString(TIPOGASOLINA));
                dataAsync = filtraPrecio(dataAsync, prefs.getString(maxPrecioString));
                prefs.putString(maxPrecioString, maxPrecio);
                view.showGasolineras(dataAsync);
                shownGasolineras = dataAsync;
                view.showLoadCorrect(dataAsync.size());
            }

            //@Override
            public void onFailure() {
                shownGasolineras = null;
                view.showLoadErrorRed();
            }
        });
    }
    */

    private void doSyncInit() {
        List<Gasolinera> dataSync;
        if (prefs.getString(IDCOMUNIDAD).equals("")){
            dataSync = repository.todasGasolineras();
        }else {
            dataSync = repository.getGasolineras(prefs.getString(IDCOMUNIDAD));
        }

        if (dataSync != null) {
            dataSync = filtraTipo(dataSync, prefs.getString(TIPOGASOLINA));
            maxPrecio = maximoEntreTodas(dataSync);
            dataSync = filtraPrecio(dataSync, prefs.getString(MAXPRECIOSTRING));
            this.data = dataSync;
            prefs.putString(MAXPRECIOSTRING, maxPrecio);
            view.showGasolineras(dataSync);
            shownGasolineras = dataSync;
            view.showLoadCorrect(dataSync.size());
        } else {
            shownGasolineras = null;
            view.showLoadErrorServidor();
        }
    }

    @Override
    public void onGasolineraClicked(int index) {
        if (shownGasolineras != null && index < shownGasolineras.size()) {
            Gasolinera gasolinera = shownGasolineras.get(index);
            view.openGasolineraDetails(gasolinera);
        }
    }

    @Override
    public void onInfoClicked() {
        view.openInfoView();
    }

    @Override
    public void onRefreshClicked() {
        init();
    }

    @Override
    public void onHomeClicked() { view.openMenuPrincipal(); }

    @Override
    public void onPrecioClicked() {view.openFiltroPrecio();}

    @Override
    public void onResetFiltroPrecioClicked() {
        prefs.putString(MAXPRECIOSTRING, maxPrecio);
        doSyncInit();
    }

    @Override
    public List<Gasolinera> filtraTipo(List<Gasolinera> data, String tipoCombustible) {
        List<Gasolinera> listaDevolver;
        switch (tipoCombustible) {
            case DIESELA:
                listaDevolver = data.stream().filter(g -> !g.getDieselA().equals("")).collect(Collectors.toList());
                break;
            case NORMAL95:
                listaDevolver = data.stream().filter(g -> !g.getNormal95().equals("")).collect(Collectors.toList());
                break;
            case NORMAL95E10:
                listaDevolver = data.stream().filter(g -> !g.getGasolina95E10().equals("")).collect(Collectors.toList());
                break;
            case NORMAL95E5P:
                listaDevolver = data.stream().filter(g -> !g.getNormal95Prem().equals("")).collect(Collectors.toList());
                break;
            case NORMAL98E5:
                listaDevolver = data.stream().filter(g -> !g.getGasolina98E5().equals("")).collect(Collectors.toList());
                break;
            case NORMAL98E10:
                listaDevolver = data.stream().filter(g -> !g.getGasolina98E10().equals("")).collect(Collectors.toList());
                break;
            case DIESELB:
                listaDevolver = data.stream().filter(g -> !g.getDieselB().equals("")).collect(Collectors.toList());
                break;
            case DIESELP:
                listaDevolver = data.stream().filter(g -> !g.getDieselPrem().equals("")).collect(Collectors.toList());
                break;
            case BIOETANOL:
                listaDevolver = data.stream().filter(g -> !g.getBioetanol().equals("")).collect(Collectors.toList());
                break;
            case BIODIESEL:
                listaDevolver = data.stream().filter(g -> !g.getBiodiesel().equals("")).collect(Collectors.toList());
                break;
            case GLP:
                listaDevolver = data.stream().filter(g -> !g.getGasLicPet().equals("")).collect(Collectors.toList());
                break;
            case GASC:
                listaDevolver = data.stream().filter(g -> !g.getGasNatComp().equals("")).collect(Collectors.toList());
                break;
            case GASL:
                listaDevolver = data.stream().filter(g -> !g.getGasNatLic().equals("")).collect(Collectors.toList());
                break;
            case H2:
                listaDevolver = data.stream().filter(g -> !g.getHidrogeno().equals("")).collect(Collectors.toList());
                break;
            default:
                return data;
        }
        return listaDevolver;
    }

    @Override
    public List<Gasolinera> filtraPrecio(List<Gasolinera> data, String maxPrecio) {
        if (maxPrecio.equals("")) {
            return data;
        }
        String tipoCombustible = prefs.getString(TIPOGASOLINA);
        List<Gasolinera> listaDevolver;
        BigDecimal actual = new BigDecimal(maxPrecio).setScale(3, RoundingMode.UP);
        switch (tipoCombustible) {
            case DIESELA:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getDieselA().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case NORMAL95:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getNormal95().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case NORMAL95E10:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasolina95E10().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case NORMAL95E5P:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getNormal95Prem().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case NORMAL98E5:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasolina98E5().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case NORMAL98E10:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasolina98E10().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case DIESELB:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getDieselB().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case DIESELP:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getDieselPrem().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case BIOETANOL:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getBioetanol().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case BIODIESEL:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getBiodiesel().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case GLP:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasLicPet().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case GASC:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasNatComp().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case GASL:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getGasNatLic().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            case H2:
                listaDevolver = data.stream().filter(g ->
                        (new BigDecimal(g.getHidrogeno().replace(',', '.'))).compareTo(actual)<=0).collect(Collectors.toList());
                break;
            default:
                listaDevolver = data.stream().filter(g -> {
                    BigDecimal min;
                    Boolean result = false;
                    if (g.getNormal95().equals("") && g.getDieselA().equals("")) {
                        return false;
                    } else if (g.getDieselA().equals("")) {
                        min = new BigDecimal(g.getNormal95().replace(',', '.')).setScale(3, RoundingMode.UP);
                    } else if (g.getNormal95().equals("")) {
                        min = new BigDecimal(g.getDieselA().replace(',', '.')).setScale(3, RoundingMode.UP);
                    } else {
                        BigDecimal gas = new BigDecimal(g.getNormal95().replace(',', '.')).setScale(3, RoundingMode.UP);
                        BigDecimal diesel = new BigDecimal(g.getDieselA().replace(',', '.')).setScale(3, RoundingMode.UP);
                        if (gas.compareTo(diesel) >= 0) {
                            min = new BigDecimal(diesel.toString()).setScale(3, RoundingMode.UP);
                        } else {
                            min = new BigDecimal(gas.toString()).setScale(3, RoundingMode.UP);
                        }
                    }
                    if (min.compareTo(actual) <= 0) {
                        result = true;
                    }
                    return result;
                }).collect(Collectors.toList());
        }
        return listaDevolver;
    }

    @Override
    public String maximoEntreTodas(List<Gasolinera> data){
        if (data.isEmpty()){
            return "0.00";
        }
        String tipo = prefs.getString(TIPOGASOLINA);
        switch (tipo) {
            case DIESELA:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getDieselA().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getDieselA().replace(',', '.'));
                    }
                }
                break;
            case NORMAL95:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getNormal95().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getNormal95().replace(',', '.'));
                    }
                }
                break;
            case NORMAL95E10:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasolina95E10().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasolina95E10().replace(',', '.'));
                    }
                }
                break;
            case NORMAL95E5P:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getNormal95Prem().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getNormal95Prem().replace(',', '.'));
                    }
                }
                break;
            case NORMAL98E5:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasolina98E5().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasolina98E5().replace(',', '.'));
                    }
                }
                break;
            case NORMAL98E10:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasolina98E10().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasolina98E10().replace(',', '.'));
                    }
                }
                break;
            case DIESELB:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getDieselB().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getDieselB().replace(',', '.'));
                    }
                }
                break;
            case DIESELP:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getDieselPrem().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getDieselPrem().replace(',', '.'));
                    }
                }
                break;
            case BIOETANOL:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getBioetanol().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getBioetanol().replace(',', '.'));
                    }
                }
                break;
            case BIODIESEL:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getBiodiesel().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getBiodiesel().replace(',', '.'));
                    }
                }
                break;
            case GLP:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasLicPet().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasLicPet().replace(',', '.'));
                    }
                }
                break;
            case GASC:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasNatComp().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasNatComp().replace(',', '.'));
                    }
                }
                break;
            case GASL:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getGasNatLic().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getGasNatLic().replace(',', '.'));
                    }
                }
                break;
            case H2:
                for (Gasolinera g : data) {
                    if(Double.parseDouble(g.getHidrogeno().replace(',', '.')) > max) {
                        max = Double.parseDouble(g.getHidrogeno().replace(',', '.'));
                    }
                }
                break;
            default:
                for (Gasolinera g : data) {
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
        }
        return String.valueOf(max);
    }

    public String getMaximoEntreTodas() {
        return maximoEntreTodas(data);
    }
}
