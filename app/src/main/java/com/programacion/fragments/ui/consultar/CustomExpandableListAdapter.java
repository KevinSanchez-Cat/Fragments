package com.programacion.fragments.ui.consultar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.ui.Mascotas;
import com.programacion.fragments.ui.editar.EditarFragment;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter{

    View root;
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
    Fragment fragment;

    private Uri photoURI;
    public String sexo, veterinario, especie, raza;

    //Instancia a la base de datos
    public Sqlite sqlite;

    private Context context;
    private List<String> listTitulo;
    private HashMap<String, Mascotas> expandableListDetalles;
    private Activity activity;

    public final Calendar c= Calendar.getInstance();

    final int mesC=c.get(Calendar.MONTH);
    final int diaC=c.get(Calendar.DAY_OF_MONTH);
    final int anioC=c.get(Calendar.YEAR);

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

            convertView = layoutInflater.inflate(R.layout.item_exp, null);

        }

        CircleImageView circleImageView = convertView.findViewById(R.id.circleIMG);

        //Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), contacto.getImagen());
        // circleImageView.setImageBitmap(bitmap);

        LinearLayout layoutEditar = convertView.findViewById(R.id.ilistEdit);
        LinearLayout layoutEliminar = convertView.findViewById(R.id.ilistDel);
        LinearLayout layouInfo = convertView.findViewById(R.id.ilistInfo);

        layoutEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 editar(contacto);

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
                View dialogView = layoutInflater.inflate(R.layout.dialog_eliminar_papelera, null);

                dialogo.setView(dialogView);
                //dialogo.setCancelable(false);
                AlertDialog dialog_elimAlertDialog = dialogo.create();

                dialog_elimAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogView.findViewById(R.id.btnSiOpc).setOnClickListener(new View.OnClickListener() {
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
                dialogView.findViewById(R.id.btnSiPapOpc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sqlite = new Sqlite(context);
                        if (sqlite.abrirDB() != null) {

                            sqlite.eliminarPapelera(String.valueOf(contacto.getId()));
                            Toast.makeText(context, "Registro enviado a la papelera", Toast.LENGTH_SHORT).show();
                            sqlite.cerrarDB();
                        }

                        dialog_elimAlertDialog.dismiss();
                    }
                });
                dialogView.findViewById(R.id.btnNoOpc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show();
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

    private com.google.android.material.textfield.TextInputEditText editID;
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
    private AutoCompleteTextView spnRaza;
    private Spinner spnSexo;
    private Spinner spnVeterinario;
    private Button btnLimpiar;
    private Button btnEditar;
    private Button btnCancelar;


    private static final String CERO="0";
    private static final String BARRA="/";

    private boolean bndCal, bndMasc;

    //Atributos necesarios
    private DatePickerDialog datePicker;
    private Calendar cal;
    private static int anio, mes, dia;

    public static final int REQUEST_TAKE_PHOTO = 101;
    public static final int REQUEST_PERMISSION_CAMERA = 100;
    public static final int REQUEST_PERMISSION_WRITE_STORANGE = 200;


    //FOTOGRAFIA
    private static final int COD_SELECCIONA = 10;
    private static final int COD_FOTO = 20;


    //Instancia a la base de datos
    public void editar(Mascotas contacto){
        sqlite = new Sqlite(context);
        String ids = "";
        ids = String.valueOf(contacto.getId());
        String textoID = "";

        android.app.AlertDialog.Builder dialogo = new android.app.AlertDialog.Builder(context);
        //View dialogView=LayoutInflater.from(getContext()).inflate(R.layout.dialog_eliminar,null);
        View dialogView = layoutInflater.inflate(R.layout.fragment_actualizar, null);


        dialogo.setView(dialogView);

        //dialogo.setCancelable(false);
        AlertDialog dialog_elimAlertDialog = dialogo.create();
        editID = dialogView.findViewById(R.id.editActualizaID);
        editNomMas = dialogView.findViewById(R.id.ediActualizaNomMas);
        editNomProp = dialogView.findViewById(R.id.editActualizaNomPropie);
        editDireccion = dialogView.findViewById(R.id.editActualizaDireccion);
        editTelefono = dialogView.findViewById(R.id.editActualizaTel);
        editEmail = dialogView.findViewById(R.id.editActualizaEmail);
        editNacimiento = dialogView.findViewById(R.id.editActualizaNac);
        editColor =dialogView.findViewById(R.id.editActualizaColor);
        editParticulares = dialogView.findViewById(R.id.editActualizaSenas);
        btnCalendar = dialogView.findViewById(R.id.iBtnActualizaNac);
        imgFoto = dialogView.findViewById(R.id.imgActualizaFoto);
        spnEspecie =dialogView.findViewById(R.id.spinActualizaEspecie);
        spnRaza =dialogView.findViewById(R.id.spinActualizaRaza);
        spnSexo = dialogView.findViewById(R.id.spinActualizaSexo);
        spnVeterinario = dialogView.findViewById(R.id.spinActualizaVeterinario);
        btnLimpiar = dialogView.findViewById(R.id.btnActualizaLimpiar);
        btnEditar = dialogView.findViewById(R.id.btnActualizaGuardar);
         btnCancelar = dialogView.findViewById(R.id.btnActualizaCancelar);

        sqlite = new Sqlite(context);
        spinnerComponent(dialogView);
        if (sqlite.abrirDB() != null) {
            id = contacto.getId();
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
                    img = cursor.getString(13);

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
                            especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc1, R.layout.spinner_item_model);
                            break;
                        case 2:
                            especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc2, R.layout.spinner_item_model);
                            break;
                        default:
                            especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc0, R.layout.spinner_item_model);
                            break;
                    }
                    spnRaza.setAdapter(especieAdapter);

                    spnSexo.setSelection(obtenerPosicion(spnSexo, sexo));
                    spnVeterinario.setSelection(obtenerPosicion(spnVeterinario, nom_vet));

                    cargarImagen2();
                    String position = "";
                    for (int i = 0; i < especieAdapter.getCount(); i++) {
                        String s = String.valueOf(especieAdapter.getItem(i));
                        System.out.println(s);
                        if (s.equalsIgnoreCase(cursor.getString(5))) {
                            position = s;
                        }
                    }
                    spnRaza.setText(position);
                }
            }
        }

        btnCalendar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DatePickerDialog recogeFecha=new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofMonth) {
                        final int mesActual=month+1;
                        String diaf=(dayofMonth<10) ? CERO+String.valueOf(dayofMonth): String.valueOf(dayofMonth);
                        String mesf=(mesActual<10) ? CERO+String.valueOf(mesActual): String.valueOf(mesActual);
                        editNacimiento.setText(""+diaf+BARRA+mesf+BARRA+ year);
                    }
                },anioC,mesC, diaC);
                recogeFecha.show();
            }
        });



        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nombre_masc = editNomMas.getText().toString();
                String nombre_propi = editNomProp.getText().toString();
                String fecha_nacimiento = editNacimiento.getText().toString();
                String color = editColor.getText().toString();
                String direccion = editDireccion.getText().toString();
                String telefono = editTelefono.getText().toString();
                String email = editEmail.getText().toString();
                String particularidades = String.valueOf(editParticulares.getText());
                String nombre_veteri = spnVeterinario.getSelectedItem().toString();
                String raz = spnRaza.getText().toString();
                String esp = spnEspecie.getSelectedItem().toString();
                String sexoAnimal = spnSexo.getSelectedItem().toString();

                sqlite = new Sqlite(context);

                if (sqlite.abrirDB() != null) {
                    if (sqlite.updateRegistroMascota(contacto.getId(), nombre_masc, nombre_propi, nombre_veteri, raz, esp,
                            color, sexoAnimal, direccion, telefono, email, particularidades, fecha_nacimiento, img,"ACTIVO").equals("Información de la mascota actualizada")) {
                          Toast.makeText(context, "Datos almacenados", Toast.LENGTH_LONG).show();
                       //mostarToast("Datos editados");
                        limpiar();

                    } else {
                          Toast.makeText(context, "Error de almacenamiento", Toast.LENGTH_LONG).show();
                       // mostarToast("Error al editar");
                    }
                    sqlite.cerrarDB();

                } else {
                    Toast.makeText(context, "No se puede abrir base", Toast.LENGTH_LONG).show();
                    //mostarToast("No se puede abrir base");
                }

                //if (sqlite.updateRegistroMascota(id, nombre_masc, nombre_propi, nombre_veteri, raza, especie, color, sexo, direccion, telefono, email, particularidades, fecha_nacimiento, img)) {
                //    Toast.makeText(getContext(), "Datos almacenados", Toast.LENGTH_LONG).show();
                //    limpiar();
                //} else {
                //    Toast.makeText(getContext(), "Error de almacenamiento", Toast.LENGTH_LONG).show();
                //}
                sqlite.cerrarDB();
                dialog_elimAlertDialog.dismiss();
            }
        });
        btnLimpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                limpiar();
            }
        });

        //658
        // dialog_elimAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   TextView txtInfo = dialogView.findViewById(R.id.editBuscEditID);
        // txtInfo.setText(textoID);


        dialogView.findViewById(R.id.btnActualizaCancelar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_elimAlertDialog.dismiss();
            }
        });

        dialog_elimAlertDialog.show();
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

        //final Cursor cursor = getActivity().getContentResolver()
        //        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null,
        //                null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        // if (cursor.moveToFirst()) {

        String imageLocation = img;
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
    private void spinnerComponent(View root) {
        ArrayAdapter<CharSequence> especieAdapter, sexoAdapter, veterinarioAdapter, razaAdapter;
        especieAdapter = ArrayAdapter.createFromResource(context, R.array.especie, R.layout.spinner_item_model);
        sexoAdapter = ArrayAdapter.createFromResource(context, R.array.sx, R.layout.spinner_item_model);
        veterinarioAdapter = ArrayAdapter.createFromResource(context, R.array.veterinarioARRAY, R.layout.spinner_item_model);
        razaAdapter = ArrayAdapter.createFromResource(context, R.array.opc0, R.layout.spinner_item_model);

        spnEspecie = root.findViewById(R.id.spinActualizaEspecie);
        spnEspecie.setAdapter(especieAdapter);

        spnRaza = root.findViewById(R.id.spinActualizaRaza);
        spnRaza.setAdapter(razaAdapter);

        spnSexo = root.findViewById(R.id.spinActualizaSexo);
        spnSexo.setAdapter(sexoAdapter);

        spnVeterinario = root.findViewById(R.id.spinActualizaVeterinario);
        spnVeterinario.setAdapter(veterinarioAdapter);

        spnVeterinario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    veterinario = adapterView.getItemAtPosition(i).toString();
                } else {
                    veterinario = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnEspecie.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ArrayAdapter<CharSequence> especieAdapter;

                switch (i) {
                    case 1:
                        especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc1, R.layout.spinner_item_model);
                        break;
                    case 2:
                        especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc2, R.layout.spinner_item_model);
                        break;
                    default:
                        especieAdapter = ArrayAdapter.createFromResource(context, R.array.opc0, R.layout.spinner_item_model);
                        break;
                }
                spnRaza.setAdapter(especieAdapter);
                if (i != 0) {
                    especie = adapterView.getItemAtPosition(i).toString();
                } else {
                    especie = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnSexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    sexo = adapterView.getItemAtPosition(i).toString();
                } else {
                    sexo = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spnRaza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    raza = adapterView.getItemAtPosition(i).toString();
                } else {
                    raza = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void mCaptura(View v){
        Intent pictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(pictureIntent.resolveActivity(activity.getPackageManager())!= null){
            //registerForActivityResult(pictureIntent,REQUEST_IMAGE_CAPTURA);

        }

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
        especieAdapter = ArrayAdapter.createFromResource(context, R.array.especie, R.layout.spinner_item_model);
        sexoAdapter = ArrayAdapter.createFromResource(context, R.array.sx, R.layout.spinner_item_model);
        veterinarioAdapter = ArrayAdapter.createFromResource(context, R.array.veterinarioARRAY, R.layout.spinner_item_model);
        razaAdapter = ArrayAdapter.createFromResource(context, R.array.opc0, R.layout.spinner_item_model);

        spnEspecie.setAdapter(especieAdapter);
        spnRaza.setAdapter(razaAdapter);
        spnSexo.setAdapter(sexoAdapter);
        spnVeterinario.setAdapter(veterinarioAdapter);

    }




    private void cargarImagen(String image, ImageView iv) {
        try {
            // File filePhoto = new File(image);
            // Uri uriPhoto = FileProvider.getUriForFile(getContext(), "com.programacion.fragments", filePhoto);
            // iv.setImageURI(uriPhoto);
            //** imgFoto.setImageURI(Uri.parse(new File(image).toString()));
            imgFoto.setImageURI(Uri.fromFile(new File(image)));
        } catch (Exception e) {
            Toast.makeText(context, "Fallo en cargar imagen " + image, Toast.LENGTH_LONG).show();
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
