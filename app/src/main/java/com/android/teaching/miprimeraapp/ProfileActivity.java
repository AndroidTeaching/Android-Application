package com.android.teaching.miprimeraapp;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    // Views
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText ageEditText;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        ageEditText = findViewById(R.id.age_edit_text);
        ageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // MOSTRAR DatePickerDialog
                    new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            // Escribir la fecha en el edit text
                            int anoActual = Calendar.getInstance().get(Calendar.YEAR);
                            int edad = anoActual - year;
                            ageEditText.setText(String.valueOf(edad));
                        }
                    }, 1980, 1, 1).show();
                }
            }
        });
        radioButtonMale = findViewById(R.id.radio_button_male);
        radioButtonFemale = findViewById(R.id.radio_button_female);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Cargar imagen de perfil
        if (isExternalStorageReadable()) {
            File profileImage = new File(getExternalFilesDir(null), "profile.png");
            if (profileImage.exists()) {
                ImageView profileImageView = findViewById(R.id.image_profile);
                profileImageView.setImageURI(Uri.fromFile(profileImage));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


        SharedPreferences myPreferences = getSharedPreferences(
                getString(R.string.user_preferences),
                Context.MODE_PRIVATE
        );
        String usernameValue = myPreferences.getString("username_key", "");
        AppDatabase myDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my-database-name")
                .allowMainThreadQueries()
                .build();
        User myUser = myDatabase.userDao().findUserByUsername(usernameValue);
        if (myUser != null) {
            usernameEditText.setText(myUser.getUsername());
            emailEditText.setText(myUser.getEmail());
            passwordEditText.setText(myUser.getPassword());
            ageEditText.setText(myUser.getAge());
            if (myUser.getGender().equals("H")) {
                radioButtonMale.setChecked(true);
            } else if (myUser.getGender().equals("M")) {
                radioButtonFemale.setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // Guardar el perfil
                saveInternal();
                break;
            case R.id.action_delete:
                // Borrar el perfil
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveInternal() {
        // Edit Texts
        Log.d("ProfileActivity", "Username: " + usernameEditText.getText());
        Log.d("ProfileActivity", "Email: " + emailEditText.getText());
        Log.d("ProfileActivity", "Password: " + passwordEditText.getText());
        Log.d("ProfileActivity", "Age: " + ageEditText.getText());


        // Radio Buttons
        if (radioButtonMale.isChecked()) {
            // El usuario ha seleccionado "H"
            Log.d("ProfileActivity", "Gender: male");
        } else if (radioButtonFemale.isChecked()) {
            // El usuario ha seleccionado "M"
            Log.d("ProfileActivity", "Gender: female");
        }

        // Guardar usuario en base de datos
        AppDatabase myDatabase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "my-database-name")
                .allowMainThreadQueries()
                .build();
        try {

            User myNewUser = new User();
            myNewUser.setUsername(usernameEditText.getText().toString());
            myNewUser.setPassword(passwordEditText.getText().toString());
            myNewUser.setEmail(emailEditText.getText().toString());
            myNewUser.setAge(ageEditText.getText().toString());
            if (radioButtonMale.isChecked()) {
                myNewUser.setGender("H");
            } else if (radioButtonFemale.isChecked()) {
                myNewUser.setGender("M");
            }
            myDatabase.userDao().insert(myNewUser);
        } catch (SQLiteConstraintException ex) {
            Toast.makeText(this, "Username is already registered",
                    Toast.LENGTH_LONG).show();
        }
    }

    public void guardarDatos(View view) {
        saveInternal();
    }

    /**
     * Método que se ejecutará cuando el usuario pulse "Delete"
     *
     * @param view -
     */
    public void onDelete(View view) {
        // Mostrar un dialogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "SÍ"
                Toast.makeText(ProfileActivity.this, "SI QUIERO!",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "NO"
                Toast.makeText(ProfileActivity.this, "NO QUIERO!",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "CANCELAR"
                Toast.makeText(ProfileActivity.this, "Candelando...",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.create().show();
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();

        File myFile = createImageFile();
        if (myFile.exists()) {
            ImageView imageView = findViewById(R.id.image_profile);
            imageView.setImageBitmap(BitmapFactory.decodeFile(myFile.getAbsolutePath()));
        }
    }

    private File createImageFile() {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(storageDir, "profile.jpg");
    }

    public void camaraIntent(View view) {
        // El usuario ha pulsado la imagen de perfil
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File outputFile = createImageFile();
            outputFile.delete();
            Uri fileUri = FileProvider.getUriForFile(this,
                    "com.android.teaching.miprimeraapp",
                    outputFile);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // Si alguna aplicación de mi dispositivo puede hacer fotos
            startActivityForResult(takePictureIntent, 100);
        }
    }
}














