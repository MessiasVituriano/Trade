package com.example.messias.trade;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference, livros;
    private Livro livro;
    private ArrayList<Livro> arrayLivros = new ArrayList<Livro>();


    private FloatingActionButton btnCadastrar;
    private EditText txtNome, txtDescricao, txtEstado;
    private ListView listaDeLivros;
    private ImageButton btnImagem;
    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();


        txtNome = (EditText) findViewById(R.id.editNome);
        txtDescricao = (EditText) findViewById(R.id.editDescricao);
        txtEstado = (EditText) findViewById(R.id.editEstado);
        btnCadastrar = (FloatingActionButton) findViewById(R.id.btnCadastrar);
        btnImagem = (ImageButton) findViewById(R.id.imagem);
        listaDeLivros = (ListView) findViewById(R.id.lista);

        ArrayAdapter<Livro> adapter = new ArrayAdapter<Livro>(this,
                android.R.layout.simple_list_item_1, arrayLivros);

        listaDeLivros.setAdapter(adapter);

        livros = reference.child("livros").child(user.getUid());
        livros.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //Getting the data from snapshot
                    Livro person = postSnapshot.getValue(Livro.class);

                    //Adding it to a string
                    //arrayLivros.add(person);

                    String string = "Nome: "+person.getNome()+"\nDescricao: "+person.getDescricao()+"\n\n";

                    Toast.makeText(SaveActivity.this, string, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnImagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nome, descricao, estado;

                nome = txtNome.getText().toString();
                descricao = txtDescricao.getText().toString();
                estado = txtEstado.getText().toString();

                if (nome.equals(null) || nome.equals("") || estado.equals(null) || estado.equals("")){
                    Toast.makeText(SaveActivity.this, "Preencha os campos", Toast.LENGTH_SHORT).show();
                }else{

                    DatabaseReference livros = reference.child("livros").child(user.getUid()).push();

                    livro = new Livro(nome, descricao, estado);

                    livros.child("estado").setValue(livro.getEstado());
                    livros.child("descricao").setValue(livro.getDescricao());
                    livros.child("nome").setValue(livro.getNome());
                    finish();
                }
            }
        });




}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK){
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();


            Bitmap bmp = null;
            try {
                bmp = getBitmapFromUri(selectedImage);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            btnImagem.setImageBitmap(bmp);
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }
}
