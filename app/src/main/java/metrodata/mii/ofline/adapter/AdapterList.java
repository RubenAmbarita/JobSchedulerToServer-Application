package metrodata.mii.ofline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.jar.Attributes;

import metrodata.mii.ofline.Model.DataItem;
import metrodata.mii.ofline.R;

public class AdapterList extends BaseAdapter {

    private List<DataItem> names;
    private Context context;

    public AdapterList(Context context,List<DataItem> names) {

        this.names = names;
        this.context = context;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listViewItem = inflater.inflate(R.layout.itemnames,null,true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        ImageView imageViewStatus = (ImageView) listViewItem.findViewById(R.id.imageViewStatus);

        //getting current name
        DataItem dataItem = names.get(position);

        textViewName.setText(dataItem.getName());

        if(dataItem.getStatus()==0){
            imageViewStatus.setBackgroundResource(R.drawable.jam);
        }else{
            imageViewStatus.setBackgroundResource(R.drawable.success);
        }
        return listViewItem;
    }
}
