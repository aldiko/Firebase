package com.example.firebasechatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView messageListView;
    private MessageAdapter adapter;
    private ProgressBar progressBar;
    private ImageButton sendImageButton;
    private Button sendMessageButton;
    private EditText messageEditText;

    private String username;

    FirebaseDatabase database;
    DatabaseReference messagesDatabaseReference;
    ChildEventListener messagesChildEventListener;

    private FirebaseAuth mAuth;
    DatabaseReference userDatabaseReference;
    //private List<String> userInfo;
    private Intent i;


//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            currentUser.reload();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progessBar);
        sendImageButton = findViewById(R.id.sendPhotoButton);
        sendMessageButton = findViewById(R.id.sendMessageButton);
        messageEditText = findViewById(R.id.messageEditText);


        

        i = getIntent();
        String uid = i.getStringExtra("userId");
        userDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users");
        userDatabaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userClass = snapshot.getValue(User.class);
                username = userClass.fullName;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ArrayList<String> logAndPass = new ArrayList<>();
//        logAndPass = i.getExtras().getStringArrayList("userId");
//        String email = logAndPass.get(0);
//        String password = logAndPass.get(1);
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//
//                            FirebaseUser user = mAuth.getCurrentUser();
//                            //updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                        }
//                    }
//                });




        //getUserInfo();//метод в котором читаем пользователя и достаем данные

        //realtime database
        database = FirebaseDatabase.getInstance();
        messagesDatabaseReference = database.getReference().child("messages");
        //




        List<MessageModel> messageModels = new ArrayList<>();

        messageListView=findViewById(R.id.messageListView);
        adapter= new MessageAdapter(this,R.layout.message_item,messageModels);
        messageListView.setAdapter(adapter);


        progressBar.setVisibility(View.INVISIBLE);





        // добавляем для того чтобы реализовать у edittextview аттрибут правильно (т.е чтобы кнопка была активна только при наличии текста)
        messageEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //когда текст внутри view поменялся
                if(s.toString().trim().length()>0){
                    sendMessageButton.setEnabled(true);
                }
                else {
                    sendMessageButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        //ограничение символов в одном сообщении
        messageEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)});



        //giving listeners for btns
        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageModel message= new MessageModel(messageEditText.getText().toString(),username,null);
                messagesDatabaseReference.push().setValue(message);

                messageEditText.setText("");
            }
        });

        sendImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        //листенер потомков для отображения сообщений в приложении
        messagesChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //когда добавляется потомок
                MessageModel message = snapshot.getValue(MessageModel.class);// в атрибуте узказываем в каком классе можно расспознать

                adapter.add(message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //когда изменяется потомок
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //когда произошла ошибка в базе данных
            }
        };

        messagesDatabaseReference.addChildEventListener(messagesChildEventListener);

    }


    public void getUserInfo(){
        userDatabaseReference=FirebaseDatabase.getInstance().getReference().child("Users");


        ArrayList<String> logAndPass = new ArrayList<>();
        logAndPass = i.getExtras().getStringArrayList("userId");

        if (logAndPass.get(0)!=null && logAndPass.get(1)!=null) {
            mAuth.signInWithEmailAndPassword(logAndPass.get(0), logAndPass.get(1)).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if (user != null) {
                        userDatabaseReference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //GenericTypeIndicator<List<String>> gt = new GenericTypeIndicator<List<String>>() {};
                                //userInfo = snapshot.getValue(gt);
                                User userClass = snapshot.getValue(User.class);
                                username = userClass.fullName;
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            });
        }
    }
}