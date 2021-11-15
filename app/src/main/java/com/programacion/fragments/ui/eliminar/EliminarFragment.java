package com.programacion.fragments.ui.eliminar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.provider.SyncStateContract;
import android.text.Editable;
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
import java.util.Calendar;

public class EliminarFragment extends Fragment implements View.OnClickListener {

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
    private Button btnEliminar;
    private Button btnCancelar;
    private ImageView imgFoto;

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

    public static final int REQUEST_PERMISSION_WRITE_STORANGE = 200;
    public static final int WRITE_EXTERNAL_STORAGE = 100;
    private EliminarViewModel mViewModel;

    public static EliminarFragment newInstance() {
        return new EliminarFragment();
    }

    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_eliminar, container, false);
        init(root);
        return root;
    }

    private void init(View root) {
        editBuscaID = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editBuscaElimID);
        iBtnBuscarID = (ImageButton) root.findViewById(R.id.iBtnBuscarElimID);
        textID = (TextView) root.findViewById(R.id.textElimID);
        textNomMas = (TextView) root.findViewById(R.id.textElimNomMas);
        textNomProp = (TextView) root.findViewById(R.id.textElimNomProp);
        textDireccion = (TextView) root.findViewById(R.id.textElimDirec);
        textTelefono = (TextView) root.findViewById(R.id.textElimTel);
        textEmail = (TextView) root.findViewById(R.id.textElimEmail);
        textEspecie = (TextView) root.findViewById(R.id.textElimEsp);
        textNac = (TextView) root.findViewById(R.id.textElimNac);
        textSexo = (TextView) root.findViewById(R.id.textElimSex);
        textRaza = (TextView) root.findViewById(R.id.textElimRaz);
        textColor = (TextView) root.findViewById(R.id.textElimCol);
        textPart = (TextView) root.findViewById(R.id.textElimPart);
        textVeterinario = (TextView) root.findViewById(R.id.textElimVet);
        btnEliminar = (Button) root.findViewById(R.id.btnElimEliminar);
        //  btnCancelar = (Button) root.findViewById(R.id.btnElimCancelar);

        imgFoto = root.findViewById(R.id.imgElimFoto);
        iBtnBuscarID.setOnClickListener(this);
        btnEliminar.setOnClickListener(this);
        //  btnCancelar.setOnClickListener(this);
    }

    private void cargarImagen(String image, ImageView iv) {
        try {
            File filePhoto = new File(image);
            Uri uriPhoto = FileProvider.getUriForFile(getContext(), "com.programacion.fragments", filePhoto);
            iv.setImageURI(uriPhoto);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Fallo en cargar imagen " + img, Toast.LENGTH_LONG).show();
            Log.d("Carga de imagen", "Error al cargar imagen " + image + "\nMensaje: " + e.getMessage() + " \nCausa: " + e.getCause());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EliminarViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtnBuscarElimID:
                String ids = editBuscaID.getText().toString();
                if (ids.isEmpty()) {
                    //Toast.makeText(getContext(), , Toast.LENGTH_LONG).show();
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

                           /* File imgFile = new File(imgA);
                            if (imgFile.exists()) {

                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                imgFoto.setImageBitmap(myBitmap);
                            }*/
                                cargarImagen2();
                            }
                        } else {
                            // Toast.makeText(getContext(), "No se encontro el ID", Toast.LENGTH_LONG).show();
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
                        }


                        sqlite.cerrarDB();
                    } else {
                        //Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
                        mostarToast("No se puede abrir base");
                    }
                }

                break;

            case R.id.btnElimEliminar:

                sqlite = new Sqlite(getContext());
                ids = editBuscaID.getText().toString();
                String textoID = "";
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
                            textoID = "ID: " + ids + "\n" +
                                    "NOMBRE DE LA MASCOTA: " + nom_mas + "\n" +
                                    "NOMBRE DEL DUEÑO: " + nom_pro_ + "\n" +
                                    "NOMBRE DEL VETERINARIO: " + nom_vet + "\n" +
                                    "ESPECIE: " + especie + "\n" +
                                    "RAZA: " + raza + "\n" +
                                    "SEXO: " + sexo + "\n" +
                                    "COLOR: " + color + "\n" +
                                    "DIRECCION: " + dir + "\n" +
                                    "TELEFONO: " + tel + "\n" +
                                    "EMAIL: " + email + "\n" +
                                    "FECHA DEL NACIMIENTO: " + nac + "\n";

                        }
                    }
                    sqlite.cerrarDB();
                }

                AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                //View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_eliminar,null);
                View dialogView = inflater.inflate(R.layout.dialog_eliminar, null);

                dialogo.setView(dialogView);
                //dialogo.setCancelable(false);
                AlertDialog dialog_elimAlertDialog = dialogo.create();

                dialog_elimAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView txtInfo = dialogView.findViewById(R.id.textElemento);
                txtInfo.setText(textoID);

                dialogView.findViewById(R.id.btnDiagSi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqlite = new Sqlite(getContext());
                        if (sqlite.abrirDB() != null) {

                            if (sqlite.eliminar(id) > 0) {
                                mostarToast("Registro eliminado");
                            } else {
                                mostarToast("No se pudo eliminar");
                            }
                            sqlite.cerrarDB();
                        }

                        dialog_elimAlertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btnDiagSiPap).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqlite = new Sqlite(getContext());
                        if (sqlite.abrirDB() != null) {
                            String estatus="";
                            if (sqlite.getValor(id).getCount() == 1) {
                                Cursor cursor = sqlite.getValor(id);
                                if (cursor.moveToFirst()) {
                                   estatus = cursor.getString(14);
                                }
                            }
                            if(estatus.equals("ACTIVO")){
                                sqlite.eliminarPapelera(String.valueOf(id));
                                mostarToast("Registro enviado a la papelera");
                            }else{
                                mostarToast("El registro ya sé encuentra en la papelera");
                            }
                            sqlite.cerrarDB();
                        }

                        dialog_elimAlertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btnDiagNo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mostarToast("Acción cancelada");
                        dialog_elimAlertDialog.dismiss();
                    }
                });

  /*              dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    //Toast.makeText(getContext(),"Registro eliminado",Toast.LENGTH_LONG).show();
                    mostarToast("Registro eliminado");
                    }
                });
                dialogo.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       // Toast.makeText(getContext(),"Registro aún activo",Toast.LENGTH_LONG).show();
                        mostarToast("Acción cancelada");
                    }
                });
*/
                dialog_elimAlertDialog.show();


                break;
        }
    }

    private void cargarImagen2() {
        writeStoragePermissionGranted();

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

        String imageLocation = imgA;
        File imageFile = new File(imageLocation);

        if (imageFile.exists()) {
            options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            try {
                bm = BitmapFactory.decodeFile(imageLocation, options);
                imgFoto.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            imgFoto.setImageResource(R.drawable.perro_1);
        }


        //}
        /*
        Opcion 2 para cargar fotos
        final Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
if (cursor.moveToFirst()) {
    final ImageView imageView = (ImageView) findViewById(R.id.pictureView);


    if (Build.VERSION.SDK_INT >= 29) {
        // You can replace '0' by 'cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)'
        // Note that now, you read the column '_ID' and not the column 'DATA'
        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getInt(0));

        // now that you have the media URI, you can decode it to a bitmap
        try (ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(mediaUri, "r")) {
            if (pfd != null) {
                bitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
            }
        } catch (IOException ex) {

        }
    } else {
        // Repeat the code you already are using
    }
}
         */


    }

    public void writeStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION_WRITE_STORANGE
                );

            }
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