package edu.pwste.firebaseapp;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import edu.pwste.firebaseapp.Adapter.ListItemAdapter;
import edu.pwste.firebaseapp.Model.ToDo;

public class MainActivity extends AppCompatActivity {
    List<ToDo> toDoList = new ArrayList<>();
    FirebaseFirestore db;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText titleMD,descriptiom;
    public boolean isUpdate=false;
    public String idUpdate="";

    ListItemAdapter listItemAdapter;

    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db= FirebaseFirestore.getInstance();

        dialog = new SpotsDialog(this);
        titleMD = (MaterialEditText) findViewById(R.id.title);
        descriptiom= (MaterialEditText) findViewById(R.id.description);
        fab= (FloatingActionButton)findViewById(R.id.add);

        recyclerView= (RecyclerView)findViewById(R.id.listToDO);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
       loadData();
    }

    private void loadData() {
        if(!toDoList.isEmpty()){
            toDoList.clear();
        }
        dialog.show();
        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for(DocumentSnapshot doc:task.getResult()){
                            ToDo td= new ToDo(doc.getString("id"),
                                    doc.getString("title"),
                                    doc.getString("descritpion"));
                            toDoList.add(td);
                        }
                        listItemAdapter= new ListItemAdapter(MainActivity.this, toDoList);
                        recyclerView.setAdapter(listItemAdapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });


    }

    public void addOnClick(View view) {
        if(!isUpdate){
            setData(titleMD.getText().toString(),descriptiom.getText().toString());
        }
        else{
            uptadeData(titleMD.getText().toString(),descriptiom.getText().toString());
            isUpdate=!isUpdate;
        }
    }

    private void uptadeData(String title, String description) {
        db.collection("ToDoList").document(idUpdate)
                .update("title",title,"descritpion",description)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this,"Uptaded !",Toast.LENGTH_SHORT).show();
                    }
                });
        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    private void setData(String title, String description) {
        String id = UUID.randomUUID().toString();
        Map<String,Object> todo= new HashMap<>();
        todo.put("id",id);
        todo.put("title",title);
        todo.put("descritpion",description);
        db.collection("ToDoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle().equals("DELETE")){
            deleteOrder(item.getOrder());
        }
        return true;
    }

    private void deleteOrder(int order) {
        db.collection("ToDoList")
                .document(toDoList.get(order).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }
}
