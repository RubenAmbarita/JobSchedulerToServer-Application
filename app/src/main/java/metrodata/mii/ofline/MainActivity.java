package metrodata.mii.ofline;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import metrodata.mii.ofline.Model.DataItem;
import metrodata.mii.ofline.Model.ResponseName;
import metrodata.mii.ofline.Network.ApiClient;
import metrodata.mii.ofline.Network.ApiInterface;
import metrodata.mii.ofline.adapter.AdapterList;
import metrodata.mii.ofline.db.DatabaseOpenHelper;
import metrodata.mii.ofline.receiver.NetworkStateChecker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static final int NAME_SYNCED_WITH_SERVER = 1;
    public static final String DATA_SAVED_BROADCAST = "metrodata.mii.ofline";

    @BindView(R.id.editTextName)
    EditText editTextName;
    @BindView(R.id.buttonSave)
    Button buttonSave;
    @BindView(R.id.listViewNames)
    ListView listViewNames;

    private DatabaseOpenHelper db;
    private List<DataItem> names;
    private List<DataItem> dataFromServer;

    //Broadcast receiver to know the sync status
    private BroadcastReceiver broadcastReceiver;

    //adapterobject for list view
    private AdapterList nameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        registerForContextMenu(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

        db = new DatabaseOpenHelper(this);
        dataFromServer = new ArrayList<>();

        loadData();
    }

    private void loadData() {
//        names.clear();
        Cursor cursor = db.getNames();
        if(cursor.moveToFirst()){
            do {
            DataItem dataItem = new DataItem(
            cursor.getString(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_NAME)),
            cursor.getInt(cursor.getColumnIndex(DatabaseOpenHelper.COLUMN_STATUS)));
                names.add(dataItem);

        } while (cursor.moveToNext());
        nameAdapter = new AdapterList(this, names );
        listViewNames.setAdapter(nameAdapter);
            getdataFromServer();
    }


    
}

    private void getdataFromServer() {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Saving Name . . .");
        progressDialog.show();

        ApiInterface service = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseName> call = service.getdata();
        call.enqueue(new Callback<ResponseName>() {
            @Override
            public void onResponse(Call<ResponseName> call, Response<ResponseName> response) {
                Log.d("", "data respon: "+new Gson().toJson(response).toString());
                //get data

                names.clear();
                progressDialog.dismiss();
                ResponseName getdataName = response.body();
                if (!getdataName.isError()){
                    List<DataItem> dataNama = getdataName.getData();
                    for (int i = 0 ; i < dataNama.size();i++){
                        DataItem newNamaFromServer = dataNama.get(i);
                        String namaFromServer = newNamaFromServer.getName();

                        //insert data
                        DataItem namaModel = new DataItem(namaFromServer,NAME_SYNCED_WITH_SERVER);
                        names.add(namaModel);


                    }
                    refreshList();
                }

            }

            @Override
            public void onFailure(Call<ResponseName> call, Throwable t) {
                Toast.makeText(MainActivity.this, " error get data"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshList() {
        nameAdapter.notifyDataSetChanged();
    }
}
