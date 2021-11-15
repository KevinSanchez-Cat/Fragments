package com.programacion.fragments.ui.consultar;


import android.Manifest;
import android.animation.Animator;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.programacion.fragments.R;
import com.programacion.fragments.bd.Sqlite;
import com.programacion.fragments.ui.Mascotas;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<Mascotas> mData;
    private LayoutInflater mInflater;
    private Context context;

    private static AlertDialog dialog;
    private static Button btnPopCerrar;

    View v;

    public ListAdapter(List<Mascotas> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        v = mInflater.inflate(com.programacion.fragments.R.layout.element_lista, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
        holder.cv.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // if (holder.name.getText().equals("video1")) {
                   // VideoActivity.vidWeb.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.video1));
                //}
            }
        });
    }
/*
    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
      //  animateCircularRevel(holder.itemView);
    }
*/
    public void animateCircularRevel(View view){
        int centerX=0;
        int centerY=0;
        int startRadius=0;
        int endRadius=Math.max(view.getWidth(),view.getHeight());
        Animator animator= ViewAnimationUtils.createCircularReveal(view,centerX,centerY,startRadius,endRadius);
        view.setVisibility(View.VISIBLE);
        animator.start();
    }


    public void setItems(List<Mascotas> items) {
        mData = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemNombre, itemEdad, itemEspecie;
        ImageView itemImagen;
       // ImageButton iBtnVer;
        CardView cv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNombre = itemView.findViewById(R.id.textItemNom);
            itemEdad = itemView.findViewById(R.id.textItemEdad);
            itemEspecie = itemView.findViewById(R.id.textItemEspecie);
         //   iBtnVer = itemView.findViewById(R.id.ibtnVer);
            itemImagen=itemView.findViewById(R.id.circleIMG);
            cv=itemView.findViewById(R.id.cv);
        }

        void bindData(final Mascotas item) {
            itemNombre.setText(item.getNombreMascota().toUpperCase());

            String edad="";
            String naci=item.getEdad();
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

            itemEspecie.setText(item.getEspecie());
            itemEdad.setText(edad);
            //itemEspecie.setText(item.getEspecie());
         /*   iBtnVer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        createNewAboutDialog(view.getContext(), item.getId());
                    } catch (Exception e) {

                    }

                }
            });*/
        }


        public void createNewAboutDialog(Context c, int id) {

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
            } else {
                Toast.makeText(c, "No se puede abrir base", Toast.LENGTH_LONG).show();
            }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(c);
            final View aboutPop = mInflater.inflate(R.layout.dialog_mascota, null);
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
        private void cargarImagen2(String imgA, ImageView imageView) {

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
}

