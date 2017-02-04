package alepacheco.com.rw;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    String[] permissions = new String[]{
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    Context ctx;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(alepacheco.com.rw.R.layout.activity_main);
        ctx = this;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Alert for permisions
            new AlertDialog.Builder(this)
                    .setTitle(alepacheco.com.rw.R.string.alert_title)
                    .setMessage(alepacheco.com.rw.R.string.alert_message)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            boolean permit = checkPermissions();
                            if (permit) {
                                new RunService().execute(ctx);
                            }
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .show();
        } else {
            new RunService().execute(ctx);
        }




    }

    private class RunService extends AsyncTask<Context, Void, Void> {
        protected Void doInBackground(Context... parms) {
            SystemClock.sleep(1000);
            Intent srv = new Intent(parms[0], MyService.class);
            startService(srv);
            return null;
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return checkPermissions();
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                new RunService().execute(this);
            }
        }
    }
}



