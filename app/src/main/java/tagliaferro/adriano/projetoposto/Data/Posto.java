package tagliaferro.adriano.projetoposto.Data;

import java.io.Serializable;
import java.util.Currency;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Posto implements Serializable {

    private int id;
    private String nome;
    private String comb1;
    private String comb2;
    private String comb3;
    private Double val1;
    private Double val2;
    private Double val3;

    public Posto(){}

    public Posto(int id, String nome, String comb1, String comb2, String comb3, Double val1, Double val2, Double val3) {

        this.id = id;
        this.nome = nome;
        this.comb1 = comb1;
        this.comb2 = comb2;
        this.comb3 = comb3;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public Posto(String nome, String comb1, String comb2, String comb3, Double val1, Double val2, Double val3) {

        this.nome = nome;
        this.comb1 = comb1;
        this.comb2 = comb2;
        this.comb3 = comb3;
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getComb1() {
        return comb1;
    }

    public void setComb1(String comb1) {
        this.comb1 = comb1;
    }

    public String getComb2() {
        return comb2;
    }

    public void setComb2(String comb2) {
        this.comb2 = comb2;
    }

    public String getComb3() {
        return comb3;
    }

    public void setComb3(String comb3) {
        this.comb3 = comb3;
    }

    public Double getVal1() {
        return val1;
    }

    public void setVal1(Double val1) {
        this.val1 = val1;
    }

    public Double getVal2() {
        return val2;
    }

    public void setVal2(Double val2) {
        this.val2 = val2;
    }

    public Double getVal3() {
        return val3;
    }

    public void setVal3(Double val3) {
        this.val3 = val3;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
