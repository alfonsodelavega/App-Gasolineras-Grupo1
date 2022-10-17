package es.unican.is.appgasolineras.activities.filtrosPermanentes;

import java.util.ArrayList;
import java.util.List;

import es.unican.is.appgasolineras.model.IDCCAAs;

public class FiltroPermanenteMapper {
    private List<IDCCAAs> CCAAs;
    private List<String> combustibles;
    public FiltroPermanenteMapper() {
        CCAAs = new ArrayList<>();
        CCAAs.add(IDCCAAs.TODAS);
        CCAAs.add(IDCCAAs.CANTABRIA);
        CCAAs.add(IDCCAAs.ANDALUCIA);
        CCAAs.add(IDCCAAs.ARAGON);
        CCAAs.add(IDCCAAs.ASTURIAS);
        CCAAs.add(IDCCAAs.BALEARES);
        CCAAs.add(IDCCAAs.CANARIAS);
        CCAAs.add(IDCCAAs.CASTILLA_LA_MANCHA);
        CCAAs.add(IDCCAAs.CASTILLA_Y_LEON);
        CCAAs.add(IDCCAAs.CATALUNYA);
        CCAAs.add(IDCCAAs.COMUNIDAD_VALENCIANA);
        CCAAs.add(IDCCAAs.EXTREMADURA);
        CCAAs.add(IDCCAAs.GALICIA);
        CCAAs.add(IDCCAAs.MADRID);
        CCAAs.add(IDCCAAs.MURCIA);
        CCAAs.add(IDCCAAs.NAVARRA);
        CCAAs.add(IDCCAAs.PAIS_VASCO);
        CCAAs.add(IDCCAAs.RIOJA);
        CCAAs.add(IDCCAAs.CEUTA);
        CCAAs.add(IDCCAAs.MELILLA);

        combustibles = new ArrayList<>();
        combustibles.add("");
        combustibles.add("normal95");
        combustibles.add("dieselA");



    }

    public String getCCAA (int index){
        return CCAAs.get(index).getId();
    }

    public String getCombustible(int index){
        return combustibles.get(index);
    }

    public int getCCAAIndex (String ccaa) {
        IDCCAAs comunidad = IDCCAAs.getEnumByString(ccaa);
        return CCAAs.indexOf(comunidad);
    }
    public int getCombustibleIndex (String tipoCombustible) {
        return combustibles.indexOf(tipoCombustible);
    }
}
