package tagliaferro.adriano.projetoposto.Data;

import java.io.Serializable;

/**
 * Created by Adriano on 25/01/2016.
 */
public class Veiculo implements Serializable{

    private int id;
    private String nome;
    private String cat;
    private String comb;

    public Veiculo(int id, String nome, String cat, String comb){
        this.id = id;
        this.nome = nome;
        this.cat = cat;
        this.comb = comb;
    }

    public Veiculo(String nome, String cat, String comb){

        this.nome = nome;
        this.cat = cat;
        this.comb = comb;
    }

    public Veiculo(){}


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getComb() {
        return comb;
    }

    public void setComb(String comb) {
        this.comb = comb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
