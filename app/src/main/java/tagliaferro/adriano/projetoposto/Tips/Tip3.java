package tagliaferro.adriano.projetoposto.Tips;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import tagliaferro.adriano.projetoposto.R;

/**
 * Created by Adriano on 15/04/2016.
 */
public class Tip3 extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setTitle("Tips");
        dialog.setMessage(R.string.tip3_abas);
        dialog.setNeutralButton("ENTENDI", this);
        return dialog.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
