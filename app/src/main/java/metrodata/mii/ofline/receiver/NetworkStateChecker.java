package metrodata.mii.ofline.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import metrodata.mii.ofline.MainActivity;
import metrodata.mii.ofline.Model.ResponseName;
import metrodata.mii.ofline.Network.ApiClient;
import metrodata.mii.ofline.Network.ApiInterface;
import metrodata.mii.ofline.db.DatabaseOpenHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkStateChecker extends BroadcastReceiver {
    private Context context;
    private DatabaseOpenHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        db = new DatabaseOpenHelper(context);

        //cek jaringan
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo acNetworkInfo = cm.getActiveNetworkInfo();
        //cek jaringan
        if (acNetworkInfo !=null){
            if (acNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI || acNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

                Cursor cursor = db.getUnsyncedNames();
                if (cursor.moveToFirst()){
                    do {
                        saveName(
                                cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_ID)),
                                cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME))
                        );
                    }
                    while (cursor.moveToNext());
                }
            }
        }
    }

    private void saveName(final int id, final String nama) {
        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseName> call = service.insertName(nama);
        call.enqueue(new Callback<ResponseName>() {
            @Override
            public void onResponse(Call<ResponseName> call, Response<ResponseName> response) {
                boolean error = response.body().isError();
                if (error == false) {
                    db.updateNameStatus(id, MainActivity.NAME_SYNCED_WITH_SERVER);

                    //sending the broadcast to refresh the list
                    context.sendBroadcast(new Intent(MainActivity.DATA_SAVED_BROADCAST));

                }
            }

            @Override
            public void onFailure(Call<ResponseName> call, Throwable t) {

            }
        });
    }
}
