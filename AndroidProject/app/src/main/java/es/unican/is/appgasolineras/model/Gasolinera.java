package es.unican.is.appgasolineras.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * Class that represents a Gas Station, with the attributes defined in the following REST API
 * https://sedeaplicaciones.minetur.gob.es/ServiciosRESTCarburantes/PreciosCarburantes/help
 *
 * The API can be easily tested with https://reqbin.com/
 *
 * The actual gas stations retrieved from the REST API have more properties than the ones currently
 * defined in this class.
 */
@Entity(tableName = "gasolineras")
public class Gasolinera implements Parcelable {

    @SerializedName("IDEESS") @NonNull @PrimaryKey  private String id;

    @SerializedName("Rótulo")                               private String rotulo;
    @SerializedName("C.P.")                                 private String cp;
    @SerializedName("Dirección")                            private String direccion;
    @SerializedName("Municipio")                            private String municipio;
    @SerializedName("Horario")                              private String horario;
    @SerializedName("Precio Biodiesel")                     private String biodiesel;
    @SerializedName("Precio Bioetanol")                     private String bioetanol;
    @SerializedName("Precio Gas Natural Comprimido")        private String gasNatComp;
    @SerializedName("Precio Gas Natural Licuado")           private String gasNatLic;
    @SerializedName("Precio Gases licuados del petróleo")   private String gasLicPet;
    @SerializedName("Precio Gasoleo A")                     private String dieselA;
    @SerializedName("Precio Gasoleo B")                     private String dieselB;
    @SerializedName("Precio Gasoleo Premium")               private String dieselPrem;
    @SerializedName("Precio Gasolina 95 E10")               private String gasolina95E10;
    @SerializedName("Precio Gasolina 95 E5")                private String normal95;
    @SerializedName("Precio Gasolina 95 E5 Premium")        private String normal95Prem;
    @SerializedName("Precio Gasolina 98 E10")               private String gasolina98E10;
    @SerializedName("Precio Gasolina 98 E5")                private String gasolina98E5;
    @SerializedName("Precio Hidrogeno")                     private String hidrogeno;

    @SerializedName("IDCCAA")                               private String IDCCAA;

    public Gasolinera() {

    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getDieselA() {
        return dieselA;
    }

    public void setDieselA(String dieselA) {
        this.dieselA = dieselA;
    }

    public String getNormal95() {
        return normal95;
    }

    public void setNormal95(String normal95) {
        this.normal95 = normal95;
    }

    public String getHorario() { return horario;}

    public void setHorario(String horario) { this.horario = horario; }

    public String getIDCCAA(){return this.IDCCAA;}

    public void setIDCCAA(String IDCCAA) { this.IDCCAA = IDCCAA;}

    public String getHidrogeno() {
        return hidrogeno;
    }

    public void setHidrogeno(String hidrogeno) {
        this.hidrogeno = hidrogeno;
    }

    public String getGasolina98E5() {
        return gasolina98E5;
    }

    public void setGasolina98E5(String gasolina98E5) {
        this.gasolina98E5 = gasolina98E5;
    }

    public String getGasolina98E10() {
        return gasolina98E10;
    }

    public void setGasolina98E10(String gasolina98E10) {
        this.gasolina98E10 = gasolina98E10;
    }

    public String getNormal95Prem() {
        return normal95Prem;
    }

    public void setNormal95Prem(String normal95Prem) {
        this.normal95Prem = normal95Prem;
    }

    public String getGasolina95E10() {
        return gasolina95E10;
    }

    public void setGasolina95E10(String gasolina95E10) {
        this.gasolina95E10 = gasolina95E10;
    }

    public String getDieselPrem() {
        return dieselPrem;
    }

    public void setDieselPrem(String dieselPrem) {
        this.dieselPrem = dieselPrem;
    }

    public String getDieselB() {
        return dieselB;
    }

    public void setDieselB(String dieselB) {
        this.dieselB = dieselB;
    }

    public String getGasLicPet() {
        return gasLicPet;
    }

    public void setGasLicPet(String gasLicPet) {
        this.gasLicPet = gasLicPet;
    }

    public String getGasNatLic() {
        return gasNatLic;
    }

    public void setGasNatLic(String gasNatLic) {
        this.gasNatLic = gasNatLic;
    }

    public String getGasNatComp() {
        return gasNatComp;
    }

    public void setGasNatComp(String gasNatComp) {
        this.gasNatComp = gasNatComp;
    }

    public String getBioetanol() {
        return bioetanol;
    }

    public void setBioetanol(String bioetanol) {
        this.bioetanol = bioetanol;
    }

    public String getBiodiesel() {
        return biodiesel;
    }

    public void setBiodiesel(String biodiesel) {
        this.biodiesel = biodiesel;
    }


    /*
     * Methods for Parcelable interface. Needed to send this object in an Intent.
     *
     * IMPORTANT: if more properties are added, these methods must be modified accordingly,
     * otherwise those properties will not be correctly saved in an Intent.
     */

    protected Gasolinera(Parcel in) {
        id = in.readString();
        rotulo = in.readString();
        cp = in.readString();
        direccion = in.readString();
        municipio = in.readString();
        horario = in.readString();
        dieselA = in.readString();
        normal95 = in.readString();
        hidrogeno = in.readString();
        gasolina98E5 = in.readString();
        gasolina98E10 = in.readString();
        normal95Prem = in.readString();
        gasolina95E10 = in.readString();
        dieselPrem = in.readString();
        dieselB = in.readString();
        gasLicPet = in.readString();
        gasNatLic = in.readString();
        gasNatComp = in.readString();
        bioetanol = in.readString();
        biodiesel = in.readString();
    }

    public static final Creator<Gasolinera> CREATOR = new Creator<Gasolinera>() {
        @Override
        public Gasolinera createFromParcel(Parcel in) {
            return new Gasolinera(in);
        }

        @Override
        public Gasolinera[] newArray(int size) {
            return new Gasolinera[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(rotulo);
        dest.writeString(cp);
        dest.writeString(direccion);
        dest.writeString(municipio);
        dest.writeString(horario);
        dest.writeString(dieselA);
        dest.writeString(normal95);
        dest.writeString(hidrogeno);
        dest.writeString(gasolina98E5);
        dest.writeString(gasolina98E10);
        dest.writeString(normal95Prem);
        dest.writeString(gasolina95E10);
        dest.writeString(dieselPrem);
        dest.writeString(dieselB);
        dest.writeString(gasLicPet);
        dest.writeString(gasNatLic);
        dest.writeString(gasNatComp);
        dest.writeString(bioetanol);
        dest.writeString(biodiesel);
    }
}
