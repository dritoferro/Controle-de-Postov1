package tagliaferro.adriano.projetoposto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import tagliaferro.adriano.projetoposto.Data.PosDAO;
import tagliaferro.adriano.projetoposto.Data.VeicDAO;
import tagliaferro.adriano.projetoposto.Data.Veiculo;

public class MainActivity extends Activity {

    PosDAO posDAO;
    VeicDAO veicDAO;
    static final int REQ_COD = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        posDAO = PosDAO.getInstance(this);
        veicDAO = VeicDAO.getInstance(this);

    }

    public void pressionado(View view) {
        /*Intent i = new Intent(this, Abast.class);
        startActivity(i);*/
        if (posDAO.listPos().size() > 0) {
            List<Veiculo> v;
            v = veicDAO.listVeicNome();
            if (v.size() != 0) {
                Intent intent = new Intent(this, Choose_Veiculo.class);
                startActivityForResult(intent, REQ_COD);

            } else {
                Intent intent = new Intent(this, IncAltVeiculo.class);
                startActivityForResult(intent, REQ_COD);
            }

        } else {
            Intent intent = new Intent(this, Inc_Alt_Posto.class);
            startActivityForResult(intent, REQ_COD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null){
            String vec = data.getStringExtra("nome");
            Intent i = new Intent(this, Abast.class);
            i.putExtra("nome", vec);
            startActivity(i);
        }
    }
}
