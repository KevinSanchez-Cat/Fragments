package com.programacion.fragments.ui.papelera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.ui.Mascotas;
import com.programacion.fragments.ui.consultar.ConsultarFragment;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

    private String img;
    private int id;
    private String nom_mas;
    private String nom_pro_;
    private String nom_vet;
    private String dir;
    private String tel;
    private String email;
    private String nac;
    private String color;
    private String parti;
    private String imgA;

    private Uri photoURI;
    public String sexo, veterinario, especie, raza;

    //Instancia a la base de datos
    public Sqlite sqlite;

    private Context context;
    private List<String> listTitulo;
    private HashMap<String, Mascotas> expandableListDetalles;
    private Activity activity;

    LayoutInflater layoutInflater;

    public CustomExpandableListAdapter(Context context,
                                       List<String> listTitulo,
                                       HashMap<String, Mascotas> expandableListDetalles, LayoutInflater inflater, Activity activity) {
        this.context = context;
        this.layoutInflater = inflater;
        this.listTitulo = listTitulo;
        this.expandableListDetalles = expandableListDetalles;
        this.activity = activity;

    }


    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final Mascotas contacto = (Mascotas) getChild(groupPosition, childPosition);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.item_exp_papelera, null);

        }

        //  CircleImageView circleImageView = convertView.findViewById(R.id.circleIMG);

        // Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), contacto.getImagen());
        // circleImageView.setImageBitmap(bitmap);

        LinearLayout layoutRestaurar = convertView.findViewById(R.id.ilistPapRest);
        LinearLayout layoutEliminar = convertView.findViewById(R.id.ilistPapEliDef);
        LinearLayout layouInfo = convertView.findViewById(R.id.ilistPapInfo);

        layoutRestaurar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqlite = new Sqlite(context);
                String ids = "";
                ids = String.valueOf(contacto.getId());
                String textoID = "";

                android.app.AlertDialog.Builder dialogo = new android.app.AlertDialog.Builder(context);
                //View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_eliminar,null);
                View dialogView = layoutInflater.inflate(R.layout.dialog_restaurar, null);

                dialogo.setView(dialogView);
                //dialogo.setCancelable(false);
                AlertDialog dialog_elimAlertDialog = dialogo.create();

                dialog_elimAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.btnSiRes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqlite = new Sqlite(context);
                        if (sqlite.abrirDB() != null) {
                            sqlite.restaurarPapelera(String.valueOf(contacto.getId()));
                            ;
                            Toast.makeText(context, "Registro restaurado", Toast.LENGTH_SHORT).show();

                            sqlite.cerrarDB();
                        }

                        dialog_elimAlertDialog.dismiss();

                    }
                });

                dialogView.findViewById(R.id.btnNoRes).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show();
                        dialog_elimAlertDialog.dismiss();
                    }
                });
                dialog_elimAlertDialog.show();
            }
        });

        layoutEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sqlite = new Sqlite(context);
                String ids = "";
                ids = String.valueOf(contacto.getId());
                String textoID = "";

                android.app.AlertDialog.Builder dialogo = new android.app.AlertDialog.Builder(context);
                //View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_eliminar,null);
                View dialogView = layoutInflater.inflate(R.layout.dialog_eliminar_def, null);

                dialogo.setView(dialogView);
                //dialogo.setCancelable(false);
                AlertDialog dialog_elimAlertDialog = dialogo.create();

                dialog_elimAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.btnSiDef).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqlite = new Sqlite(context);
                        if (sqlite.abrirDB() != null) {

                            if (sqlite.eliminar(contacto.getId()) > 0) {
                                Toast.makeText(context, "Registro eliminado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(context, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                            }

                            sqlite.cerrarDB();
                        }

                        dialog_elimAlertDialog.dismiss();
                    }
                });

                dialogView.findViewById(R.id.btnNoDef).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show();
                        dialog_elimAlertDialog.dismiss();
                    }
                });
                dialog_elimAlertDialog.show();
            }
        });

        layouInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsultarFragment.createNewAboutDialog(v.getContext(), contacto.getId(), layoutInflater);

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
        Mascotas contacto = (Mascotas) getChild(groupPosition, 0);

        if (convertView == null) {

            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = layoutInflater.inflate(R.layout.list_group, null);

        }

        TextView txtNombre = convertView.findViewById(R.id.textItemNom);
        TextView txtEspecie = convertView.findViewById(R.id.textItemEspecie);
        TextView txtedad = convertView.findViewById(R.id.textItemEdad);
        CircleImageView circleImageView = convertView.findViewById(R.id.circleIMG);

        String edad = "";
        String naci = contacto.getEdad().toString();
        String dias[] = naci.toString().split("/");

        int dia = Integer.parseInt(dias[0]);
        int mes = Integer.parseInt(dias[1]);
        int anio = Integer.parseInt(dias[2]);

        Calendar c = Calendar.getInstance();
        int anioC = c.get(Calendar.YEAR);
        int mesC = c.get(Calendar.MONTH);
        int diaC = c.get(Calendar.DAY_OF_MONTH);

        int anios = anioC - anio;
        int meses = mesC - mes;
        int semanas = (diaC - dia) / 7;
        if ((anios) >= 1) {
            if (anios == 1) {
                edad = anios + " año";
            } else {
                edad = anios + " años";
            }

        } else {
            if (meses < 1) {
                edad = semanas + " semanas";
            } else if (meses == 1) {
                edad = meses + " mes";
            } else {
                edad = meses + " meses";
            }
        }

        BitmapFactory.Options options = null;
        Bitmap bm = null;

        String[] projection = new String[]{
                MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                MediaStore.Images.ImageColumns.DATE_TAKEN,
                MediaStore.Images.ImageColumns.MIME_TYPE,
                MediaStore.Images.ImageColumns.DISPLAY_NAME,
        };

        //final Cursor cursor = getActivity().getContentResolver()
        //        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
        //                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        // if (cursor.moveToFirst()) {

        String imageLocation = contacto.getImagen();
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            try {
                bm = BitmapFactory.decodeFile(imageLocation, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            circleImageView.setImageBitmap(bm);
        } else {
            circleImageView.setImageResource(R.drawable.perro_1);
        }

        txtedad.setText(edad);
        txtNombre.setText(String.valueOf(contacto.getNombreMascota()).toString().toUpperCase());
        txtEspecie.setText(String.valueOf(contacto.getEspecie()));

        //Animation animation=AnimationUtils.loadAnimation(activity, android.R.anim.slide_in_left);


        //  Animation animationA = AnimationUtils.loadAnimation(context,(isExpanded)?R.anim.slide:R.anim.fade_transition);
        // convertView.startAnimation(animationA);
        return convertView;
    }

    private int lastposition = -1;

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
