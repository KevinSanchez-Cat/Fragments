package com.programacion.fragments.ui.busqueda;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sqlite;

import java.io.File;

public class BusquedaFragment extends Fragment implements View.OnClickListener {


    private com.google.android.material.textfield.TextInputEditText editBuscaID;
    private ImageButton iBtnBuscarID;
    private TextView textID;
    private TextView textNomMas;
    private TextView textNomProp;
    private TextView textDireccion;
    private TextView textTelefono;
    private TextView textEmail;
    private TextView textEspecie;
    private TextView textNac;
    private TextView textSexo;
    private TextView textRaza;
    private TextView textColor;
    private TextView textPart;
    private TextView textVeterinario;
    private TextView textEstatus;
    private ImageView imgFoto;
    private ImageView imgEstatus;

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
    private String estatus;

    public String sexo, especie, raza;

    //Instancia a la base de datos
    public Sqlite sqlite;
    View root;
    private BusquedaViewModel mViewModel;

    public static BusquedaFragment newInstance() {
        return new BusquedaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_busqueda, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        editBuscaID = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editSearchID);
        iBtnBuscarID = (ImageButton) root.findViewById(R.id.iBtnSearchID);
        textID = (TextView) root.findViewById(R.id.textSearchID);
        textNomMas = (TextView) root.findViewById(R.id.textSearchNomMas);
        textNomProp = (TextView) root.findViewById(R.id.textSearchNomProp);
        textDireccion = (TextView) root.findViewById(R.id.textSearchDirec);
        textTelefono = (TextView) root.findViewById(R.id.textSearchTel);
        textEmail = (TextView) root.findViewById(R.id.textSearchEmail);
        textEspecie = (TextView) root.findViewById(R.id.textSearchEsp);
        textNac = (TextView) root.findViewById(R.id.textSearchNac);
        textSexo = (TextView) root.findViewById(R.id.textSearchSex);
        textRaza = (TextView) root.findViewById(R.id.textSearchRaz);
        textColor = (TextView) root.findViewById(R.id.textSearchCol);
        textPart = (TextView) root.findViewById(R.id.textSearchPart);
        textEstatus = (TextView) root.findViewById(R.id.textSearchEstado);
        imgEstatus =  root.findViewById(R.id.imgSearchEstado);
        textVeterinario = (TextView) root.findViewById(R.id.textSearchVet);
        imgFoto = root.findViewById(R.id.imgSearchFoto);
        iBtnBuscarID.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(BusquedaViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtnSearchID:
                String ids = editBuscaID.getText().toString();
                if (ids.isEmpty()) {
                    mostarToast("Llena el campo del nombre");
                    textID.setText("id");
                    textNomMas.setText("nombre de la mascota");
                    textNomProp.setText("nombre del propietario");
                    textColor.setText("color");
                    textEspecie.setText("especie");
                    textRaza.setText("raza");
                    textSexo.setText("sexo");
                    textVeterinario.setText("nombre del veterinario");
                    textDireccion.setText("dirección");
                    textTelefono.setText("telefono");
                    textPart.setText("Particularidades");
                    textEmail.setText("e-mail");
                    textNac.setText("fecha de nacimiento");
                    imgFoto.setImageResource(R.drawable.perro_1);
                    imgEstatus.setImageResource(R.drawable.check);
                    textEstatus.setText("Estatus");
                } else {
                    sqlite = new Sqlite(getContext());

                    if (sqlite.abrirDB() != null) {

                        id = Integer.parseInt(ids);
                        if (sqlite.getValor(id).getCount() == 1) {
                            Cursor cursor = sqlite.getValor(id);
                            if (cursor.moveToFirst()) {

                                nom_mas = cursor.getString(1);
                                nom_pro_ = cursor.getString(2);
                                nom_vet = cursor.getString(3);
                                especie = cursor.getString(4);
                                raza = cursor.getString(5);
                                color = cursor.getString(6);
                                sexo = cursor.getString(7);
                                dir = cursor.getString(8);
                                tel = cursor.getString(9);
                                email = cursor.getString(10);
                                parti = cursor.getString(11);
                                nac = cursor.getString(12);
                                imgA = cursor.getString(13);
                                estatus = cursor.getString(14);

                                textID.setText(String.valueOf(id));
                                textNomMas.setText(nom_mas);
                                textNomProp.setText(nom_pro_);
                                textColor.setText(color);
                                textEspecie.setText(especie);
                                textRaza.setText(raza);
                                textSexo.setText(sexo);
                                textVeterinario.setText(nom_vet);
                                textDireccion.setText(dir);
                                textTelefono.setText(tel);
                                textPart.setText(parti);
                                textEmail.setText(email);
                                textNac.setText(nac);
                                textEstatus.setText(estatus.toLowerCase());
                                if(estatus.equals("ACTIVO")){
                                    imgEstatus.setImageResource(R.drawable.check_azul);
                                }else{
                                    imgEstatus.setImageResource(R.drawable.trash);
                                }
                                cargarImagen2();
                            }
                        } else {
                            mostarToast("No se encontro el ID");
                            textID.setText("id");
                            textNomMas.setText("nombre de la mascota");
                            textNomProp.setText("nombre del propietario");
                            textColor.setText("color");
                            textEspecie.setText("especie");
                            textRaza.setText("raza");
                            textSexo.setText("sexo");
                            textVeterinario.setText("nombre del veterinario");
                            textDireccion.setText("dirección");
                            textTelefono.setText("telefono");
                            textPart.setText("Particularidades");
                            textEmail.setText("e-mail");
                            textNac.setText("fecha de nacimiento");
                            imgFoto.setImageResource(R.drawable.perro_1);
                            imgEstatus.setImageResource(R.drawable.check);
                            textEstatus.setText("Estatus");
                        }
                        sqlite.cerrarDB();
                    } else {
                        mostarToast("No se puede abrir base");
                    }
                }
                break;
        }
    }

    private void cargarImagen2() {
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
        String imageLocation = imgA;
        File imageFile = new File(imageLocation);
        if (imageFile.exists()) {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            try {
                bm = BitmapFactory.decodeFile(imageLocation, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgFoto.setImageBitmap(bm);
        } else {
            imgFoto.setImageResource(R.drawable.perro_1);
        }
    }


    public void mostarToast(String txt) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) root.findViewById(R.id.layout_base));
        TextView textView = layout.findViewById(R.id.txtToast);
        textView.setText(txt);
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }
}