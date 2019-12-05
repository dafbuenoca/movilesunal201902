package co.edu.unal.radionicawebservice;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.edu.unal.radionicawebservice.models.Municipio;


public class CustomMunicipioList extends BaseAdapter {

    private Activity context;
    private List<Municipio> municipios;

    public CustomMunicipioList(Activity context, List<Municipio> municipios) {
        //   super(context, R.layout.row_item, countries);
        this.context = context;
        this.municipios = municipios;

    }

    @Override
    public int getCount() {
        if(municipios.size() <=0)
            return 1;
        return municipios.size();
    }

    @Override
    public Object getItem(int posicion) {
        return posicion;
    }

    @Override
    public long getItemId(int posicion) {
        return posicion;
    }

    @Override
    public View getView(int posicion, View convertView, ViewGroup viewGroup) {

        View row = convertView;

        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;

        if(convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);
            vh.estacion = (TextView) row.findViewById(R.id.estacion);
            vh.poblacion = (TextView) row.findViewById(R.id.poblacion);
            vh.nombreMunicipio = (TextView) row.findViewById(R.id.nombreMunicipio);
            vh.departamento = (TextView) row.findViewById(R.id.departamento);
            // store the holder with the view.
            row.setTag(vh);
        }
        else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.departamento.setText("Departamento: " + municipios.get(posicion).getDepartemento());
        vh.nombreMunicipio.setText("municipio: " + municipios.get(posicion).getMunicipio());
        vh.poblacion.setText("poblacion:" + municipios.get(posicion).getPoblacion());
        vh.estacion.setText("estacion: " + municipios.get(posicion).getEstacion());

        return  row;
    }

    public static class ViewHolder
    {
        TextView estacion;
        TextView departamento;
        TextView nombreMunicipio;
        TextView poblacion;
    }


}
