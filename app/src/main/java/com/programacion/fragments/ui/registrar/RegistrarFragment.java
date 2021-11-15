package com.programacion.fragments.ui.registrar;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sql;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.databinding.FragmentRegistrarBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegistrarFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener,
        DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {

    //Atributos del Layout
  //  private com.google.android.material.textfield.TextInputEditText editID;
    private com.google.android.material.textfield.TextInputEditText editNomMas;
    private com.google.android.material.textfield.TextInputEditText editNomProp;
    private com.google.android.material.textfield.TextInputEditText editDireccion;
    private com.google.android.material.textfield.TextInputEditText editTelefono;
    private com.google.android.material.textfield.TextInputEditText editEmail;
    private com.google.android.material.textfield.TextInputEditText editNacimiento;
    private com.google.android.material.textfield.TextInputEditText editColor;
    private com.google.android.material.textfield.TextInputEditText editParticulares;
    private ImageButton btnCalendar;
    private ImageView imgFoto;
    private Spinner spnEspecie;
    private Spinner spnRaza;
    private Spinner spnSexo;
    private Spinner spnVeterinario;
    private Button btnLimpiar;
    private Button btnGuardar;
    private Button btnCancelar;
    private com.google.android.material.floatingactionbutton.FloatingActionButton btnTomaFoto;
    //Atributos necesarios
    private DatePickerDialog datePicker;
    private Calendar cal;
    private static int anio, mes, dia;

    private Uri photoURI;
    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int REQUEST_PERMISSION_CAMERA = 100;
    public static final int REQUEST_PERMISSION_WRITE_STORANGE = 200;
    public static String img = "", sexo, veterinario, especie, raza, naci, parti;


    //FOTOGRAFIA
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;


    //Instancia a la base de datos
    public Sqlite sqlite;


    private RegistrarViewModel registrarViewModel;
    private FragmentRegistrarBinding binding;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        registrarViewModel =
                new ViewModelProvider(this).get(RegistrarViewModel.class);

        binding = FragmentRegistrarBinding.inflate(inflater, container, false);
        root = inflater.inflate(R.layout.fragment_registrar, container, false);
        initUI(root);
        return root;
    }


    public void initUI(View root) {
       // editID = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegisID);
        editNomMas = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegNomMas);
        editNomProp = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegNomPropie);
        editDireccion = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegDireccion);
        editTelefono = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegTel);
        editEmail = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegEmail);
        editNacimiento = root.findViewById(R.id.editRegisNac);
        editColor = (com.google.android.material.textfield.TextInputEditText) root.findViewById(R.id.editRegColor);
        editParticulares = root.findViewById(R.id.editRegSenas);
        btnCalendar = (ImageButton) root.findViewById(R.id.iBtnRegisNac);
        imgFoto = (ImageView) root.findViewById(R.id.imgRegisFoto);
        spnEspecie = (Spinner) root.findViewById(R.id.spinRegEspecie);
        spnSexo = (Spinner) root.findViewById(R.id.spinRegSexo);
        spnVeterinario = (Spinner) root.findViewById(R.id.spinRegVeterinario);
        spnRaza = (Spinner) root.findViewById(R.id.spinRegRaza);
        btnLimpiar = (Button) root.findViewById(R.id.btnRegisLimpiar);
        btnGuardar = (Button) root.findViewById(R.id.btnRegisGuardar);
        // btnCancelar = (Button) root.findViewById(R.id.btnRegisCancelar);
        btnTomaFoto = (com.google.android.material.floatingactionbutton.FloatingActionButton) root.findViewById(R.id.fBtnRegisFoto);

        spinnerComponent(root);
        btnCalendar.setOnClickListener(this);
        btnGuardar.setOnClickListener(this);
        btnLimpiar.setOnClickListener(this);
//        btnCancelar.setOnClickListener(this);
        btnTomaFoto.setOnClickListener(this);
    }

    private void spinnerComponent(View root) {
        ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
        especieAdapter = ArrayAdapter.createFromResource(getContext(), R.array.especie, R.layout.spinner_item_model);
        sexoAdapter = ArrayAdapter.createFromResource(getContext(), R.array.sx, R.layout.spinner_item_model);
        veterinarioAdapter = ArrayAdapter.createFromResource(getContext(), R.array.veterinarioARRAY, R.layout.spinner_item_model);
        razaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.opc0, R.layout.spinner_item_model);

        spnEspecie = root.findViewById(R.id.spinRegEspecie);
        spnEspecie.setAdapter(especieAdapter);

        spnRaza = root.findViewById(R.id.spinRegRaza);
        spnRaza.setAdapter(razaAdapter);

        spnSexo = root.findViewById(R.id.spinRegSexo);
        spnSexo.setAdapter(sexoAdapter);

        spnVeterinario = root.findViewById(R.id.spinRegVeterinario);
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
        limpiarEditx(editNacimiento, editTelefono, editColor, editEmail, editDireccion, editNomMas, editNomProp, editParticulares);
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
                    //  Toast.makeText(getContext(), "Cargar camara...", Toast.LENGTH_SHORT).show();
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

    private void limpiarEditx(EditText... o) {
        for (EditText x : o) {
            x.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onDateSet(DatePicker viewDate, int year, int month, int dayOfMonth) {
        editNacimiento.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
        naci = editNacimiento.getText().toString();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case COD_SELECCIONA:
                if(data!=null){
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
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    Bitmap imageBitmapNuevo = Bitmap.createBitmap(imageBitmap, 0, 0, imageBitmap.getWidth(), imageBitmap.getHeight(), matrix, true);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iBtnRegisNac:
                cal = Calendar.getInstance();
                anio = cal.get(Calendar.YEAR);
                mes = cal.get(Calendar.MONTH);
                dia = cal.get(Calendar.DAY_OF_MONTH);
                datePicker = new DatePickerDialog(getContext(), this, anio, mes, dia);
                datePicker.show();
                break;
            case R.id.btnRegisGuardar:
                if (validaVacios(editParticulares, editNomProp, editNomMas, editEmail, editDireccion, editColor, editTelefono,
                        editNacimiento)) {
                    String fechaV = naci;
                    if(validaFecha(fechaV)){
                        editNacimiento.requestFocus();
                        editNacimiento.setError("Fecha no valida");
                    }else{
                        // int id = Integer.parseInt(editID.getText().toString());
                        String nombre_masc = editNomMas.getText().toString();
                        String nombre_propi = editNomProp.getText().toString();
                        String fecha_nacimiento = naci;
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
                            if (sqlite.addRegistroMascota(nombre_masc, nombre_propi, nombre_veteri, raz, esp, color, sexoAnimal, direccion, telefono, email, particularidades, fecha_nacimiento, img,"ACTIVO")) {

                                //  Toast.makeText(getContext(), "Datos almacenados", Toast.LENGTH_LONG).show();
                                mostarToast("Datos almacenados");
                                limpiar();
                                img="";

                            } else {
                                //  Toast.makeText(getContext(), "Error de almacenamiento", Toast.LENGTH_LONG).show();
                                mostarToast("Error de almacenamiento");
                            }
                            sqlite.cerrarDB();

                        } else {
                            //Toast.makeText(getContext(), "No se puede abrir base", Toast.LENGTH_LONG).show();
                            mostarToast("No se puede abrir base");
                        }
                    }



                } else {
                    // Toast.makeText(getContext(), "Requiere llenar los campos", Toast.LENGTH_LONG).show();
                    mostarToast("Requiere llenar los campos");
                }
                break;
            case R.id.btnRegisLimpiar:
                limpiar();
                break;
            case R.id.fBtnRegisFoto:
                mostrarDialogOpciones();
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
                if(dia>diaC){
                    return true;
                }else{
                    return false;
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        switch (adapterView.getId()) {
            case R.id.spinRegSexo:
                if (position != 0) {
                    sexo = adapterView.getItemAtPosition(position).toString();
                } else {
                    sexo = "";
                }
                break;
            case R.id.spinRegEspecie:
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
            case R.id.spinRegVeterinario:
                veterinario = adapterView.getItemAtPosition(position).toString();
                break;
            case R.id.spinRegRaza:
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