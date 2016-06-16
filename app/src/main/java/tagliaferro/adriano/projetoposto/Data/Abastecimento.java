package tagliaferro.adriano.projetoposto.Data;

import java.io.Serializable;
import java.util.Currency;
import java.util.Date;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Abastecimento implements Serializable{

    private int id;
    private String data;
    private Double val;
    private String posto;
    private Double km;
    private String veiculo;
    private Double precoL;

    public Abastecimento(){}

    public Abastecimento( String data, Double val, String posto, Double km, String veiculo){

        this.data = data;
        this.val = val;
        this.posto = posto;
        this.km = km;
        this.veiculo = veiculo;
    }

    public Abastecimento(int id, String data, Double val, String posto, Double km, String veiculo, Double precoL){

        this.id = id;
        this.data = data;
        this.val = val;
        this.posto = posto;
        this.km = km;
        this.veiculo = veiculo;
        this.precoL = precoL;
    }

    public Abastecimento(String data, Double val, String posto, Double km, String vec, Double vl) {
        this.data = data;
        this.val = val;
        this.posto = posto;
        this.km = km;
        this.veiculo = vec;
        this.precoL = vl;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public String getPosto() {
        return posto;
    }

    public void setPosto(String posto) {
        this.posto = posto;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(String veiculo) {
        this.veiculo = veiculo;
    }

    public Double getPrecoL() {
        return precoL;
    }

    public void setPrecoL(Double precoL) {
        this.precoL = precoL;
    }
}
