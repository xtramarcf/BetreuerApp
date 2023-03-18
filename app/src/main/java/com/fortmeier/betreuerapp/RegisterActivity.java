package com.fortmeier.betreuerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fortmeier.betreuerapp.model.User;
import com.fortmeier.betreuerapp.tutor.QuestTutorAddingActivity;
import com.fortmeier.betreuerapp.tutor.TutorActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText etName;
    private EditText etFirstName;
    private EditText etEmail;
    private EditText etPassword;

    private Spinner spinnerUserType;
    private MaterialCardView selectCard;
    private TextView tvSelectExpertises;
    private TextView tvExpertises;
    private Button btnRegister;
    private Button btnCancel;
    private HashMap<String, User> userData;
    private boolean[] selectedExpertises;
    ArrayList<Integer> expertisesList = new ArrayList<>();
    String[] expertisesArray = {"Physik", "Biologie", "Wirtschaft", "Geschichte", "Mathe", "Chemie", "Philosophie", "Spanisch", "Soziologie", "Psychologie"};

    private FirebaseAuth firebaseAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        etName = findViewById(R.id.name);
        etFirstName = findViewById(R.id.firstName);
        etEmail = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        selectCard = findViewById(R.id.selectCard);
        tvSelectExpertises = findViewById(R.id.tv_select_expertises);
        tvExpertises = findViewById(R.id.tv_expertises);
        btnRegister = findViewById(R.id.btn_register);
        btnCancel = findViewById(R.id.btn_cancel);
        selectedExpertises = new boolean[expertisesArray.length];

        firebaseAuth = FirebaseAuth.getInstance();
        userData = (HashMap<String, User>) getIntent().getSerializableExtra("map");

        selectCard.setOnClickListener(v -> {
            showExpertisesDialog();
        });

        // Spinner code
        spinnerUserType = (Spinner) findViewById(R.id.tutor_student);
        spinnerUserType.setOnItemSelectedListener(this);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.tutor_student, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUserType.setAdapter(adapter);

        btnRegister.setOnClickListener(view -> {
            if (validateRegisterData()) {
                registerUser();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.putExtra("map", userData);
                startActivity(intent);
                finish();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (spinnerUserType.getSelectedItem().toString().equals("Betreuer")) {
            selectCard.setVisibility(View.VISIBLE);
            tvExpertises.setVisibility(View.VISIBLE);
        } else {
            selectCard.setVisibility(View.GONE);
            tvExpertises.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void showExpertisesDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);

        builder.setTitle("Wähle Fachgebiete");
        builder.setCancelable(false);

        builder.setMultiChoiceItems(expertisesArray, selectedExpertises, (dialogInterface, which, isChecked) -> {
            if (isChecked) {
                expertisesList.add(which);
            } else {
                expertisesList.remove(which);
            }
        }).setPositiveButton("Ok", (dialogInterface, i) -> {
            //create string builder
            StringBuilder stringBuilder = new StringBuilder();
            for (int j = 0; j < expertisesList.size(); j++) {

                stringBuilder.append(expertisesArray[expertisesList.get(j)]);

                //check condition

                if (j != expertisesList.size() - 1) {
                    stringBuilder.append(", ");
                }

                tvSelectExpertises.setText(stringBuilder.toString());
            }
        }).setNegativeButton("Abbrechen", (dialogInterface, i) -> dialogInterface.dismiss()).setNeutralButton("Alles löschen", (dialogInterface, i) -> {
            //clearing all selected expertises on clear all click
            for (int j = 0; j < selectedExpertises.length; j++) {
                selectedExpertises[j] = false;

                expertisesList.clear();
                tvSelectExpertises.setText("");
            }
        });
        builder.show();
    }

    private boolean validateRegisterData() {
        String txt_password = etPassword.getText().toString();
        if (TextUtils.isEmpty(etName.getText().toString()) || TextUtils.isEmpty(etFirstName.getText().toString()) || TextUtils.isEmpty(etEmail.getText().toString()) || TextUtils.isEmpty(txt_password)) {
            Toast.makeText(this, "Bitte alle Felder ausfüllen!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (txt_password.length() < 6) {
            Toast.makeText(this, "Passwort zu kurz!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void loadUserDataInDB() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String txtName = etName.getText().toString();
        String txtFirstName = etFirstName.getText().toString();
        String txtEmail = etEmail.getText().toString();
        String txtUserType = spinnerUserType.getSelectedItem().toString();
        String txtExpertises = null;
        if (!tvSelectExpertises.getText().toString().equals("Wähle Fachgebiete") || !tvSelectExpertises.getText().toString().equals("")) {
            txtExpertises = tvSelectExpertises.getText().toString();
        }

        Map<String, Object> userData = new HashMap<>();
        userData.put("eMail", txtEmail);
        userData.put("name", txtName);
        userData.put("firstName", txtFirstName);
        userData.put("userType", txtUserType);
        userData.put("expertises", txtExpertises);

        db.collection("users").document(txtEmail).set(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void registerUser() {
        String txtEmail = etEmail.getText().toString();
        String txtPassword = etPassword.getText().toString();

        firebaseAuth.createUserWithEmailAndPassword(txtEmail, txtPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    loadUserDataInDB();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("map", userData);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(RegisterActivity.this, "Registrierung fehlgeschlagen!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}