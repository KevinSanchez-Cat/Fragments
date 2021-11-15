package com.programacion.fragments.ui.editar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.programacion.fragments.R;
import com.programacion.fragments.SaveImage;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.databinding.FragmentEditarBinding;
import com.programacion.fragments.ui.acerca.AcercaFragment;
import com.programacion.fragments.ui.consultar.ConsultarFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

public class EditarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    private com.google.android.material.textfield.TextInputEditText editBuscaID;
    private com.google.android.material.textfield.TextInputEditText editID;
    private com.google.android.material.textfield.TextInputEditText editNomMas;
    private com.google.android.material.textfield.TextInputEditText editNomProp;
    private com.google.android.material.textfield.TextInputEditText editDireccion;
    private com.google.android.material.textfield.TextInputEditText editTelefono;
    private com.google.android.material.textfield.TextInputEditText editEmail;
    private com.google.android.material.textfield.TextInputEditText editNacimiento;
    private com.google.android.material.textfield.TextInputEditText editColor;
    private com.google.android.material.textfield.TextInputEditText editParticulares;

    private ImageButton btnBuscaID;
    private ImageButton btnCalendar;
    private ImageView imgFoto;
    private Spinner spnEspecie;
    private Spinner spnRaza;
    private Spinner spnSexo;
    private Spinner spnVeterinario;
    private Button btnLimpiar;
    private Button btnEditar;
    private Button btnCancelar;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnTomaFoto;

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
    private boolean bndCal, bndMasc;

    //Atributos necesarios
    private DatePickerDialog datePicker;
    private Calendar cal;
    private static int anio, mes, dia;

    private Uri photoURI;
    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int REQUEST_PERMISSION_CAMERA = 100;
    public static final int REQUEST_PERMISSION_WRITE_STORANGE = 200;
    public static String img = "", sexo, veterinario, especie, raza;


    //FOTOGRAFIA
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;


    //Instancia a la base de datos
    public Sqlite sqlite;


    private EditarViewModel editarViewModel;
    private FragmentEditarBinding binding;
    View root;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        editarViewModel =
                new ViewModelProvider(this).get(EditarViewModel.class);

        binding = FragmentEditarBinding.inflate(inflater, container, false);
        root = inflater.inflate(R.layout.fragment_editar, container, false);
        initUI(root);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initUI(View root) {
        editBuscaID = root.findViewById(R.id.editBuscEditID);
        editID = root.findViewById(R.id.editEditID);
        editNomMas = root.findViewById(R.id.ediEditNomMas);
        editNomProp = root.findViewById(R.id.editEditNomPropie);
        editDireccion = root.findViewById(R.id.editEditDireccion);
        editTelefono = root.findViewById(R.id.editEditTel);
        editEmail = root.findViewById(R.id.editEditEmail);
        editNacimiento = root.findViewById(R.id.editEditNac);
        editColor = root.findViewById(R.id.editEditColor);
        editParticulares = root.findViewById(R.id.editEditSenas);
        btnBuscaID = root.findViewById(R.id.iBtnBuscarEditID);
        btnCalendar = root.findViewById(R.id.iBtnEditNac);
        imgFoto = root.findViewById(R.id.imgEditFoto);
        spnEspecie = root.findViewById(R.id.spinEditEspecie);
        spnRaza = root.findViewById(R.id.spinEditRaza);
        spnSexo = root.findViewById(R.id.spinEditSexo);
        spnVeterinario = root.findViewById(R.id.spinEdiVeterinario);
        btnLimpiar = root.findViewById(R.id.btnEditLimpiar);
        btnEditar = root.findViewById(R.id.btnEditGuardar);
        //  btnCancelar = root.findViewById(R.id.btnEditCancelar);
        btnTomaFoto = root.findViewById(R.id.fBtnEditFoto);


        spinnerComponent(root);
        btnCalendar.setOnClickListener(this);
        btnBuscaID.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
        //  btnCancelar.setOnClickListener(this);
        btnTomaFoto.setOnClickListener(this);

    }

    private void spinnerComponent(View root) {
        ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.especie, R.layout.spinner_item_model);
        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sx, R.layout.spinner_item_model);
        veterinarioAdapter = ArrayAdapter.createFromResource(getContext(), R.array.veterinarioARRAY, R.layout.spinner_item_model);
        razaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);

        spnEspecie = root.findViewById(R.id.spinEditEspecie);
        spnEspecie.setAdapter(especieAdapter);

        spnRaza = root.findViewById(R.id.spinEditRaza);
        spnRaza.setAdapter(razaAdapter);

        spnSexo = root.findViewById(R.id.spinEditSexo);
        spnSexo.setAdapter(sexoAdapter);

        spnVeterinario = root.findViewById(R.id.spinEdiVeterinario);
        spnVeterinario.setAdapter(veterinarioAdapter);

        spnVeterinario.setOnItemSelectedListener(this);
        spnEspecie.setOnItemSelectedListener(this);
        spnSexo.setOnItemSelectedListener(this);
        spnRaza.setOnItemSelectedListener(this);

    }

    public boolean validaVacios(EditText... o) {
        boolean b = true;
        for (EditText x : o) {
            if (x.getText().toString().isEmpty()) {
                b = false;
                x.requestFocus();
                x.setError("Campo obligatorio");
            }
        }
        return b;
    }

    private void limpiar() {
        limpiarEditx(editNacimiento, editTelefono, editColor, editEmail, editDireccion, editID, editNomMas, editNomProp, editParticulares);
        imgFoto.setImageResource(R.drawable.perro_1);
        sexo = "";
        veterinario = "";
        especie = "";
        raza = "";

        ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.especie, R.layout.spinner_item_model);
        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sx, R.layout.spinner_item_model);
        veterinarioAdapter = ArrayAdapter.createFromResource(getContext(), R.array.veterinarioARRAY, R.layout.spinner_item_model);
        razaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);

        spnEspecie.setAdapter(especieAdapter);
        spnRaza.setAdapter(razaAdapter);
        spnSexo.setAdapter(sexoAdapter);
        spnVeterinario.setAdapter(veterinarioAdapter);

    }

    public void mostrarDialogOpciones() {
        CharSequence[] opciones = {"Tomar foto", "Elegir de galeria", "Cancelar"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Elige una opción");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (opciones[i].equals("Tomar foto")) {
                    abrirCamera();
                    //   Toast.makeText(getContext(), "Cargar camara...", Toast.LENGTH_SHORT).show();
                } else if (opciones[i].equals("Elegir de galeria")) {

                    Intent intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/");
                    startActivityForResult(intent.createChooser(intent, "Seleccione"), COD_SELECCIONA);
                } else {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }


    private void abrirCamera() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(intent, COD_FOTO);
            }
        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_PERMISSION_CAMERA
            );
        }
    }

    private void cargarImagen(String image, ImageView iv) {
        try {
            // File filePhoto = new File(image);
            // Uri uriPhoto = FileProvider.getUriForFile(getContext(), "com.programacion.fragments", filePhoto);
            // iv.setImageURI(uriPhoto);
            //** imgFoto.setImageURI(Uri.parse(new File(image).toString()));
            imgFoto.setImageURI(Uri.fromFile(new File(image)));
        } catch (Exception e) {
            Toast.makeText(getContext(), "Fallo en cargar imagen " + image, Toast.LENGTH_LONG).show();
            Log.d("Carga de imagen", "Error al cargar imagen " + image + "\nMensaje: " + e.getMessage() + " \nCausa: " + e.getCause());
        }
    }

    private void limpiarEditx(EditText... o) {
        for (EditText x : o) {
            x.setText("");
        }
    }

    private static int obtenerPosicion(Spinner spinner, String item) {
        int position = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)) {
                position = i;
            }
        }
        return position;
    }

    @Override
    public void onDateSet(DatePicker viewDate, int year, int month, int dayOfMonth) {
        editNacimiento.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                if (data != null) {
                    Uri miPath = data.getData();
                    imgFoto.setImageURI(miPath);
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContext().getContentResolver().query(miPath, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String paths = cursor.getString(columnIndex);
                    cursor.close();
                    img = paths;
                    // bitmap = BitmapFactory.decodeFile(path);
                    //  imgProducto.setImageBitmap(bitmap);
                }
                break;

            case COD_FOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Bundle extras = data.getExtras();
                    //optiene la imagen
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //hacer un mapeo para la imagen
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    //Comprime la imagen
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    //Rota la imagen
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap imageBitmapNuevo = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

                    //Guarda
                    String tituliImge = "IMG_" + System.currentTimeMillis();
                    String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), imageBitmapNuevo, tituliImge, "Foto de la app de mascotas");
                    //muestra la imagen
                    //  photoURI = Uri.parse(path);
                    imgFoto.setImageBitmap(imageBitmapNuevo);
                    img = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures" + "/" + tituliImge + ".jpg";
                }
                break;

        }
    }

    public void guardarImagenVeriones(Bitmap imageBitmap) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            //Salvar la imagen

            OutputStream fos = null;
            File file = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                ContentResolver resolver = getActivity().getContentResolver();
                ContentValues values = new ContentValues();
                String filename = System.currentTimeMillis() + "image_examenple";
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp");
                values.put(MediaStore.Images.Media.IS_PENDING, 1);
                Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                Uri imageUri = resolver.insert(collection, values);
                try {
                    fos = resolver.openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                resolver.update(imageUri, values, null, null);
            } else {
                String imageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
                String fileName = System.currentTimeMillis() + ".jpg";
                file = new File(imageDir, fileName);

                try {
                    fos = new FileOutputStream(file);
                    System.out.println(file.getPath());
                    img = file.getPath();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            boolean saved = imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            if (saved) {


                Toast.makeText(getContext(), "Se ha salvado la captura", Toast.LENGTH_LONG).show();
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file != null) {
                MediaScannerConnection.scanFile(getContext(), new String[]{file.toString()}, null, null);
            }

        } else {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_PERMISSION_WRITE_STORANGE
            );

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

    String estatus = "";

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtnEditNac:
                cal = Calendar.getInstance();
                anio = cal.get(Calendar.YEAR);
                mes = cal.get(Calendar.MONTH);
                dia = cal.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(getContext(), this, anio, mes, dia);
                datePicker.show();
                break;
            case R.id.btnEditGuardar:
                String txtID = editID.getText().toString();
                if(txtID.isEmpty()){
                    mostarToast("No se hay datos que actualizar");
                }else{
                    if(validaVacios(editNomMas,editNomProp,editColor,editDireccion,editTelefono,editEmail,editParticulares,editNacimiento)){
                        String fechaV = editNacimiento.getText().toString();
                        if(validaFecha(fechaV)){
                            editNacimiento.requestFocus();
                            editNacimiento.setError("Fecha no valida");
                        }else{
                            int id = Integer.parseInt(editID.getText().toString());
                            String nombre_masc = editNomMas.getText().toString();
                            String nombre_propi = editNomProp.getText().toString();
                            String fecha_nacimiento = editNacimiento.getText().toString();
                            String color = editColor.getText().toString();
                            String direccion = editDireccion.getText().toString();
                            String telefono = editTelefono.getText().toString();
                            String email = editEmail.getText().toString();
                            String particularidades = String.valueOf(editParticulares.getText());
                            String nombre_veteri = spnVeterinario.getSelectedItem().toString();
                            String raz = spnRaza.getSelectedItem().toString();
                            String esp = spnEspecie.getSelectedItem().toString();
                            String sexoAnimal = spnSexo.getSelectedItem().toString();


                            sqlite = new Sqlite(getContext());
                            if (sqlite.abrirDB() != null) {
                                if (sqlite.updateRegistroMascota(id, nombre_masc, nombre_propi, nombre_veteri, raz, esp,
                                        color, sexoAnimal, direccion, telefono, email, particularidades, fecha_nacimiento, img, estatus).equals("Información de la mascota actualizada")) {
                                    //  Toast.makeText(getContext(), "Datos almacenados", Toast.LENGTH_LONG).show();
                                    mostarToast("Datos editados");
                                    limpiar();
                                    img="";

                                } else {
                                    //  Toast.makeText(getContext(), "Error de almacenamiento", Toast.LENGTH_LONG).show();
                                    mostarToast("Error al editar");
                                }
                                sqlite.cerrarDB();

                            } else {
                                //Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
                                mostarToast("No se puede abrir base");
                                sqlite.cerrarDB();
                            }
                        }

                    }else{
                        mostarToast("Hay campos vacios");
                    }

                }

                break;
            case R.id.btnEditLimpiar:
                limpiar();
                break;
            case R.id.fBtnEditFoto:
                mostrarDialogOpciones();
                break;
            case R.id.iBtnBuscarEditID:
                String idString = editBuscaID.getText().toString();
                if (idString.isEmpty()) {
                    // Toast.makeText(getContext(), "Llene el campo del ID", Toast.LENGTH_LONG).show();
                    mostarToast("Llene el campo del ID");
                    editID.setText("");
                    editNomMas.setText("");
                    editNomProp.setText("");
                    editColor.setText("");
                    editDireccion.setText("");
                    editTelefono.setText("");
                    editParticulares.setText("");
                    editEmail.setText("");
                    editNacimiento.setText("");
                    imgFoto.setImageResource(R.drawable.perro_1);
                    ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
                    especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.especie, R.layout.spinner_item_model);
                    sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sx, R.layout.spinner_item_model);
                    veterinarioAdapter = ArrayAdapter.createFromResource(getContext(), R.array.veterinarioARRAY, R.layout.spinner_item_model);
                    razaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);

                    spnEspecie.setAdapter(especieAdapter);
                    spnRaza.setAdapter(razaAdapter);
                    spnSexo.setAdapter(sexoAdapter);
                    spnVeterinario.setAdapter(veterinarioAdapter);
                } else {
                    sqlite = new Sqlite(getContext());

                    if (sqlite.abrirDB() != null) {
                        id = Integer.parseInt(editBuscaID.getText().toString());
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

                                System.out.println("0: " + cursor.getString(0) + "\n" +
                                        "1:" + cursor.getString(1) + "\n" +
                                        "2:" + cursor.getString(2) + "\n" + "3:" + cursor.getString(3) + "\n" +
                                        "4:" + cursor.getString(4) + "\n" + "5:" + cursor.getString(5) + "\n" +
                                        "6:" + cursor.getString(6) + "\n" + "7:" + cursor.getString(7) + "\n" +
                                        "8:" + cursor.getString(8) + "\n"
                                        + "9:" + cursor.getString(9) + "\n" +
                                        "10:" + cursor.getString(10) + "\n" + "11:" + cursor.getString(11) + "\n" +
                                        "12:" + cursor.getString(12) + "\n"
                                        + "13:" + cursor.getString(13));
                                editID.setText(String.valueOf(id));
                                editNomMas.setText(nom_mas);
                                editNomProp.setText(nom_pro_);
                                editColor.setText(color);
                                editDireccion.setText(dir);
                                editTelefono.setText(tel);
                                editEmail.setText(email);
                                editParticulares.setText(parti);
                                editNacimiento.setText(nac);

                                spnEspecie.setSelection(obtenerPosicion(spnEspecie, especie));
                                ArrayAdapter<CharSequence> especieAdapter;
                                switch (obtenerPosicion(spnEspecie, especie)) {
                                    case 1:
                                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc1, R.layout.spinner_item_model);
                                        break;
                                    case 2:
                                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc2, R.layout.spinner_item_model);
                                        break;
                                    default:
                                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);
                                        break;
                                }
                                spnRaza.setAdapter(especieAdapter);
                                spnRaza.setSelection(obtenerPosicion(spnRaza, raza));

                                spnSexo.setSelection(obtenerPosicion(spnSexo, sexo));
                                spnVeterinario.setSelection(obtenerPosicion(spnVeterinario, nom_vet));

                                cargarImagen2();
                                bndCal = true;
                                bndMasc = true;

                            }
                        } else {
                            //Toast.makeText(getContext(), "No se encontro el ID", Toast.LENGTH_LONG).show();
                            mostarToast("No se encontro el ID");
                            editID.setText("");
                            editNomMas.setText("");
                            editNomProp.setText("");
                            editColor.setText("");
                            editDireccion.setText("");
                            editTelefono.setText("");
                            editParticulares.setText("");
                            editEmail.setText("");
                            editNacimiento.setText("");
                            imgFoto.setImageResource(R.drawable.perro_1);
                            ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
                            especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.especie, R.layout.spinner_item_model);
                            sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sx, R.layout.spinner_item_model);
                            veterinarioAdapter = ArrayAdapter.createFromResource(getContext(), R.array.veterinarioARRAY, R.layout.spinner_item_model);
                            razaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);

                            spnEspecie.setAdapter(especieAdapter);
                            spnRaza.setAdapter(razaAdapter);
                            spnSexo.setAdapter(sexoAdapter);
                            spnVeterinario.setAdapter(veterinarioAdapter);
                        }
                        sqlite.cerrarDB();
                    } else {
                        //Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
                        mostarToast("No se puede abrir base");
                    }
                }


                break;

        }
    }

    private boolean validaFecha(String editNacimiento) {
        String edad = "";

        String naci = editNacimiento.toString();
        String dias[] = naci.toString().split("/");

        int dia = Integer.parseInt(dias[0]);
        int mes = Integer.parseInt(dias[1]);
        int anio = Integer.parseInt(dias[2]);

        Calendar c = Calendar.getInstance();
        int anioC = c.get(Calendar.YEAR);
        int mesC = c.get(Calendar.MONTH)+1;
        int diaC = c.get(Calendar.DAY_OF_MONTH);
        int anios = anioC - anio;
        int meses = mesC - mes;
        int semanas = (diaC - dia) / 7;
        if (anio == anioC) {
            if (mes == mesC) {
                if(dia<=diaC){
                    return false;
                }else{
                    return true;
                }
            } else if (mes < mesC) {
                return false;
            }else{
                return true;
            }
        } else if (anio < anioC) {
            return false;
        }else{
            return true;
        }

    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinEditSexo:
                if (position != 0) {
                    sexo = adapterView.getItemAtPosition(position).toString();
                } else {
                    sexo = "";
                }
                break;
            case R.id.spinEditEspecie:
                ArrayAdapter<CharSequence> especieAdapter;

                switch (position) {
                    case 1:
                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc1, R.layout.spinner_item_model);
                        break;
                    case 2:
                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc2, R.layout.spinner_item_model);
                        break;
                    default:
                        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);
                        break;
                }
                spnRaza.setAdapter(especieAdapter);
                if (position != 0) {
                    especie = adapterView.getItemAtPosition(position).toString();
                } else {
                    especie = "";
                }
                break;
            case R.id.spinEdiVeterinario:
                if (position != 0) {
                    veterinario = adapterView.getItemAtPosition(position).toString();
                } else {
                    veterinario = "";
                }
                break;
            case R.id.spinEditRaza:
                if (position != 0) {
                    raza = adapterView.getItemAtPosition(position).toString();
                } else {
                    raza = "";
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
            } catch (Exception e) {
                e.printStackTrace();
            }
            imgFoto.setImageBitmap(bm);
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
        } else {
            return;
        }
    }
}