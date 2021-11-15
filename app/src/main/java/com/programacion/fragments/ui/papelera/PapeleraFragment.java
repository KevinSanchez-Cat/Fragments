package com.programacion.fragments.ui.papelera;

import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.ui.Mascotas;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PapeleraFragment extends Fragment {
    private ExpandableListView expandableListView; //ReciclerView
    private ExpandableListAdapter expandableListAdapter;
    private List<String> expandableListNombres;
    private HashMap<String, Mascotas> listaContactos;
    private int lastExpandedPosition = -1;
    //expandableListView

    View root;
    EditText ediBuscaNombre;
    ImageButton btnBusca;
    List<Mascotas> elements;
    ListAdapter listAdapter;


    private static AlertDialog dialog;
    private static Button btnPopCerrar;
    private PapeleraViewModel mViewModel;

    public static PapeleraFragment newInstance() {
        return new PapeleraFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_papelera, container, false);

        ediBuscaNombre = root.findViewById(R.id.editBuscConsultarID);
        btnBusca = root.findViewById(R.id.iBtnBuscarConsultarID);
        this.expandableListView = root.findViewById(R.id.expandableListView);

        expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int positio, long id) {

            }
        });
        btnBusca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = ediBuscaNombre.getText().toString();
                if (id.isEmpty()) {
                    listaContactos = getContactos();
                    expandableListNombres = new ArrayList<>(listaContactos.keySet());
                    expandableListAdapter = new CustomExpandableListAdapter(getContext(),
                            expandableListNombres, listaContactos, inflater, getActivity());

                    expandableListView.setAdapter(expandableListAdapter);
                    Toast.makeText(getContext(), "Ingrese algo en el campo Nombre", Toast.LENGTH_LONG).show();
                } else {
                    Sqlite sqlite = new Sqlite(getContext());
                    if (sqlite.abrirDB() != null) {
                        HashMap<String, Mascotas> listaC = new HashMap<>();
                        Cursor c = sqlite.getRegistrosNombreInactivos(id);

                        if (c.moveToFirst()) {

                            do {

                                Mascotas m = new Mascotas(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(4), c.getString(12), c.getString(13), c.getString(14));
                                listaC.put(c.getString(1), m);
                            } while (c.moveToNext());
                            expandableListNombres = new ArrayList<>(listaC.keySet());
                            expandableListAdapter = new CustomExpandableListAdapter(getContext(),
                                    expandableListNombres, listaC, inflater, getActivity());
                            expandableListView.setAdapter(expandableListAdapter);
                        } else {
                            listaContactos = getContactos();
                            expandableListNombres = new ArrayList<>(listaContactos.keySet());
                            expandableListAdapter = new CustomExpandableListAdapter(getContext(),
                                    expandableListNombres, listaContactos, inflater, getActivity());

                            expandableListView.setAdapter(expandableListAdapter);
                            Toast.makeText(getContext(), "No existen coincidencias", Toast.LENGTH_LONG).show();
                        }
                        sqlite.cerrarDB();
                    } else {
                        Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });


        this.listaContactos = getContactos();
        this.expandableListNombres = new ArrayList<>(listaContactos.keySet());
        this.expandableListAdapter = new CustomExpandableListAdapter(getContext(),
                expandableListNombres, listaContactos, inflater, getActivity());

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1 && groupPosition != lastExpandedPosition) {
                    expandableListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        /**
         expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
        @Override public void onGroupCollapse(int groupPosition) {
        Toast.makeText(getBaseContext(),"List Collapsed:" +
        expandableListNombres.get(groupPosition),Toast.LENGTH_SHORT).show();
        }
        });

         expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
        @Override public boolean onChildClick(ExpandableListView parent, View v,
        int groupPosition, int childPosition, long id) {

        Toast.makeText(getContext(),
        expandableListNombres.get(groupPosition) +
        " ---> " + listaContactos.get(expandableListNombres.get(groupPosition))
        , Toast.LENGTH_SHORT).show();
        return false;
        }
        });
         */
        return root;
    }

    private HashMap<String, Mascotas> getContactos() {
        HashMap<String, Mascotas> listaC = new HashMap<>();
        Sqlite sqlite = new Sqlite(getContext());

        if (sqlite.abrirDB() != null) {
            Cursor c = sqlite.getRegistroInActivos();

            if (c.moveToFirst()) {
                do {
                    Mascotas m = new Mascotas(Integer.parseInt(c.getString(0)), c.getString(1), c.getString(4), c.getString(12), c.getString(13), c.getString(14));
                    listaC.put(c.getString(1), m);
                } while (c.moveToNext());
            }
            sqlite.cerrarDB();
        } else {
            Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
        }
        return listaC;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PapeleraViewModel.class);
        // TODO: Use the ViewModel
    }

    public static void createNewAboutDialog(Context c, int id, LayoutInflater inflater) {

        Sqlite sqlite = new Sqlite(c);
        String nom_mas = "";
        String nom_pro_ = "";
        String nom_vet = "";
        String especie = "";
        String raza = "";
        String color = "";
        String sexo = "";
        String dir = "";
        String tel = "";
        String email = "";
        String parti = "";
        String nac = "";
        String imgA = "";

        if (sqlite.abrirDB() != null) {
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
                }
            } else {
                Toast.makeText(c, "Registro corrupto", Toast.LENGTH_LONG).show();
            }
            sqlite.cerrarDB();
        } else {
            Toast.makeText(c, "No se puede abrir base", Toast.LENGTH_LONG).show();
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
        final View aboutPop = inflater.inflate(R.layout.dialog_mascota, null);
        btnPopCerrar = (Button) aboutPop.findViewById(R.id.btnCerrarDialog);
        TextView txtID = aboutPop.findViewById(R.id.textDetID);
        TextView txtNomMas = aboutPop.findViewById(R.id.textDetNomMas);
        TextView txtNomProp = aboutPop.findViewById(R.id.textDetNomProp);
        TextView txtNomVet = aboutPop.findViewById(R.id.textDetVet);
        TextView txtDir = aboutPop.findViewById(R.id.textDetDirec);
        TextView txtTel = aboutPop.findViewById(R.id.textDetTel);
        TextView txtEmail = aboutPop.findViewById(R.id.textDetEmail);
        TextView txtEspecie = aboutPop.findViewById(R.id.textDetEsp);
        TextView txtNac = aboutPop.findViewById(R.id.textDetNac);
        TextView txtSexo = aboutPop.findViewById(R.id.textDetSex);
        TextView txtRaza = aboutPop.findViewById(R.id.textDetRaz);
        TextView txtColor = aboutPop.findViewById(R.id.textDetCol);
        TextView txtParticularidades = aboutPop.findViewById(R.id.textDetPart);
        ImageView imageView = aboutPop.findViewById(R.id.imgDetalleFoto);

        txtID.setText(String.valueOf(id));
        txtNomMas.setText(nom_mas);
        txtNomProp.setText(nom_pro_);
        txtNomVet.setText(nom_vet);
        txtDir.setText(dir);
        txtTel.setText(tel);
        txtEmail.setText(email);
        txtEspecie.setText(especie);
        txtNac.setText(nac);
        txtSexo.setText(sexo);
        txtRaza.setText(raza);
        txtColor.setText(color);
        txtParticularidades.setText(parti);
        cargarImagen2(imgA, imageView);
        dialogBuilder.setView(aboutPop);
        dialog = dialogBuilder.create();
        dialog.show();


        btnPopCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //cerrar
    }

    private static void cargarImagen2(String imgA, ImageView imageView) {

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
                imageView.setImageBitmap(bm);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
}