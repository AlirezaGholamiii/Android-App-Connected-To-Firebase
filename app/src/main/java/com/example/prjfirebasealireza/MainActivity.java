package com.example.prjfirebasealireza;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.prjfirebasealireza.model.Person;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        ValueEventListener, ChildEventListener {


    EditText edId, edName, edAge;
    Button btnAdd, btnUpdate, btnRemove, btnFind, btnFindAll;

    DatabaseReference personDatabase, personChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }


    private void initialize()
    {
        edId = findViewById(R.id.edId);
        edName = findViewById(R.id.edName);
        edAge = findViewById(R.id.edAge);
        btnAdd = findViewById(R.id.btnAdd);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnRemove = findViewById(R.id.btnDelete);
        btnFind = findViewById(R.id.btnFind);
        btnFindAll = findViewById(R.id.btnFindAll);
        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnRemove.setOnClickListener(this);
        btnFindAll.setOnClickListener(this);
        btnFind.setOnClickListener(this);

        personDatabase = FirebaseDatabase.getInstance().getReference("person");

    }




    @Override
    public void onClick(View view)
    {
        int id =view.getId();
        switch (id)
        {
            case R.id.btnAdd:
                addUpdateDocument("has been added successfuly");
                break;

            case R.id.btnUpdate:
                addUpdateDocument("has been Updated successfuly");
                break;

            case R.id.btnDelete:
                deleteDocument();
                break;
            case R.id.btnFind:
                findDocument();
                break;
            case R.id.btnFindAll:
                findAllDocuments();
                break;
        }
    }



    private void deleteDocument() {
        String id  = edId.getText().toString();
        personChild = personDatabase.child(id);
        personChild.removeValue();
        Toast.makeText(this, "The document has been" +
                " Removed successefully ", Toast.LENGTH_LONG).show();

        edId.setText(null);
        edId.requestFocus();
    }

    private void addUpdateDocument(String message) {
        
        try {
            int id = Integer.valueOf(edId.getText().toString());
            String name = edName.getText().toString();
            int age = Integer.valueOf(edAge.getText().toString());
            Person onePerson = new Person(id,name,age);
            personChild = personDatabase.child(String.valueOf(id));
            personChild.setValue(onePerson);

            Toast.makeText(this, "The document with id " + id
                    + "\n " + message, Toast.LENGTH_LONG).show();

            cleaWidgets();

        }catch (Exception e){
            Toast.makeText(this,e.getMessage(), Toast.LENGTH_LONG).show();
        }
}

    private void cleaWidgets() {

        edId.setText(null);
        edName.setText(null);
        edAge.setText(null);
        edId.requestFocus();
    }

    private void findDocument() {

        String id = edId.getText().toString();
        personChild = personDatabase.child(id);
        personChild.addValueEventListener(this);
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        String id = edId.getText().toString();

        if(snapshot.exists()){
            String name = snapshot.child("name").getValue().toString();
            String age = snapshot.child("age").getValue().toString();
            edName.setText(name);
            edAge.setText(age);

        }
        else {
            Toast.makeText(this, "The document with " +
                    " the id " + id + " doesnt exist!", Toast.LENGTH_LONG).show();
            edId.setText(null);
            edId.requestFocus();
        }

    }


    private void findAllDocuments() {

        personDatabase.addChildEventListener(this);
    }


    @Override
    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


        //Display all documents
        //method 1
        //Person person = snapshot.getValue(Person.class);
        //Log.d("FIREBASE", person.toString());

        //method 2 using map
        //Map<String, String > map = (Map)snapshot.getValue();
        //String name = map.get("name").toString();
        //Log.d("FIREBASE", "Name is : " + name);


        Map<String , Long> map1 = (Map)snapshot.getValue();
        long id = Long.valueOf(map1.get("id").toString());
        Log.d("FIREBASE", "ID is : " + id);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
}

