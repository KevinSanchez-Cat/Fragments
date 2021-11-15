package com.programacion.fragments.ui.busqueda;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.programacion.fragments.R;
import com.programacion.fragments.ui.Mascotas;
import com.programacion.fragments.ui.consultar.ConsultarFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> listTitulo;
    private HashMap<String, Mascotas> expandableListDetalles;
    private Activity activity;

    LayoutInflater layoutInflater;
    public CustomExpandableListAdapter(Context context,
                                       List<String> listTitulo,
                                       HashMap<String, Mascotas> expandableListDetalles, LayoutInflater inflater, Activity activity) {
        this.context = context;
        this.layoutInflater=inflater;
        this.listTitulo = listTitulo;
        this.expandableListDetalles = expandableListDetalles;
        this.activity=activity;

    }


    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Mascotas contacto = (Mascotas) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.item_exp, null);

        }

        CircleImageView circleImageView = convertView.findViewById(R.id.circleIMG);

        //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), contacto.getImagen());
       // circleImageView.setImageBitmap(bitmap);

        LinearLayout layoutEditar = convertView.findViewById(R.id.ilistEdit);
        LinearLayout layoutEliminar = convertView.findViewById(R.id.ilistDel);
        LinearLayout layouInfo= convertView.findViewById(R.id.ilistInfo);

        layoutEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layoutEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        layouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarFragment.createNewAboutDialog(v.getContext(), contacto.getId(),layoutInflater);

            }
        });



        Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        convertView.startAnimation(animation);

        return convertView;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        String nombre = (String) getGroup(groupPosition);
        Mascotas contacto = (Mascotas) getChild(groupPosition,0);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        TextView txtNombre = convertView.findViewById(R.id.textItemNom);
        TextView txtEspecie = convertView.findViewById(R.id.textItemEspecie);
        TextView txtedad = convertView.findViewById(R.id.textItemEdad);

        String edad="";
        String naci=contacto.getEdad().toString();
        String dias[]=naci.toString().split("/");

        int dia=Integer.parseInt(dias[0]);
        int mes=Integer.parseInt(dias[1]);
        int anio=Integer.parseInt(dias[2]);

        Calendar c = Calendar.getInstance();
        int anioC = c.get(Calendar.YEAR);
        int mesC = c.get(Calendar.MONTH);
        int diaC = c.get(Calendar.DAY_OF_MONTH);

        int anios=anioC-anio;
        int meses=mesC-mes;
        int semanas=(diaC-dia)/7;
        if((anios)>=1){
            if(anios==1){
                edad=anios+" año";
            }else{
                edad=anios+" años";
            }

        }else{
            if(meses<1){
                edad=semanas+ " semanas";
            }else if(meses==1){
                edad=meses+" mes";
            }else{
                edad=meses+" meses";
            }
        }


        txtedad.setText(edad);
        txtNombre.setText(String.valueOf(contacto.getNombreMascota()).toString().toUpperCase());
        txtEspecie.setText(String.valueOf(contacto.getEspecie()));

        //Animation animation=AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);





      //  Animation animationA = AnimationUtils.loadAnimation(context,(isExpanded)?R.anim.slide:R.anim.fade_transition);
       // convertView.startAnimation(animationA);
        return convertView;
    }
    private int lastposition=-1;

    @Override
    public int getGroupCount() {
        return this.listTitulo.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listTitulo.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.expandableListDetalles.get(this.listTitulo.get(groupPosition));
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
