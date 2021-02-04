package com.catata.myfirebaseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

public class Actividad2 extends AppCompatActivity {

    private static final String TAG = Actividad2.class.getName();

    TextView tvDatosUser;

    RecyclerView lista;
    MyAdapter myAdapter;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad2);

        initView();


    }

    private void initView() {
        tvDatosUser = (TextView) findViewById(R.id.tvDatosUser);
        lista = (RecyclerView) findViewById(R.id.lista);
        lista.setLayoutManager(new GridLayoutManager(this, 2));
        myAdapter = new MyAdapter(new ArrayList<Libro>());
        lista.setAdapter(myAdapter);


        Intent i = getIntent();

        if (i.hasExtra(MainActivity.EXTRA_USER_NAME) && i.hasExtra(MainActivity.EXTRA_USER_EMAIL)) {
            tvDatosUser.setText(i.getStringExtra(MainActivity.EXTRA_USER_EMAIL) + "(" + i.getStringExtra(MainActivity.EXTRA_USER_NAME) + ")");
        } else {
            tvDatosUser.setText("Anonymous");
        }

        obtenerDatos();
    }

    private void obtenerDatos() {
        database = FirebaseDatabase.getInstance();
        DatabaseReference librosRef = database.getReference("libros");

        librosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Libro libro = dataSnapshot.getValue(Libro.class);
                //myAdapter.addItem(libro);
                //Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        /*Para colecciones de objetos*/
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                Libro libro = dataSnapshot.getValue(Libro.class);
                myAdapter.addItem(libro);




            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                Libro libro = (Libro) dataSnapshot.getValue(Libro.class);
                myAdapter.updateItem(libro);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                  Libro libro = dataSnapshot.getValue(Libro.class);
                  myAdapter.delItem(libro);

                // ...
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());


                Libro movedLibro = dataSnapshot.getValue(Libro.class);

                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postLibros:onCancelled", databaseError.toException());
                Toast.makeText(Actividad2.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }


        };

        librosRef.addChildEventListener(childEventListener);
    }



    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        NavUtils.navigateUpFromSameTask(this);
    }

    public void addBook(View view) {
        DatabaseReference libros = database.getReference("libros");
        Libro libro = new Libro();
        libro.setAuthor("J.R.R. Tolkien");
        libro.setDescription("Su historia se desarrolla en la Tercera Edad del Sol de la Tierra Media, un lugar ficticio poblado por hombres y otras razas antropomorfas como los hobbits, los elfos o los enanos, así como por muchas otras criaturas reales y fantásticas. La novela narra el viaje del protagonista principal, Frodo Bolsón, hobbit de la Comarca, para destruir el Anillo Único y la consiguiente guerra que provocará el enemigo para recuperarlo, ya que es la principal fuente de poder de su creador, el Señor oscuro, Sauron.");
        libro.setId(9);
        libro.setTitle("El Señor de los Anillos");
        libro.setPublication_date("03/02/2021");
        libro.setUrl_image("https://i.blogs.es/0ffc50/sauron/1366_2000.jpeg");
        libros.push().setValue(libro)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Escritura ok
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Escritura KO
                    }
                });

    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        List<Libro> libros;

        public MyAdapter(List<Libro> libros) {
            this.libros = libros;
        }

        public void addItem(Libro l){
            libros.add(l);
            notifyItemInserted(libros.size()-1);

            notifyDataSetChanged();
        }

        public void delItem(Libro libro){
            Libro book = null;
            for (Libro l:libros) {
                if(l.getId()== libro.getId()){
                    book = l;
                }
            }

            libros.remove(book);

            notifyDataSetChanged();
        }

        public void updateItem(Libro libro) {
            for (Libro l:libros) {
                l.update(libro);
            }

            notifyDataSetChanged();
        }



        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(Actividad2.this).inflate(R.layout.item_view, parent,false);
            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Picasso.get().load(libros.get(position).getUrl_image()).into(holder.imagen);

            holder.titulo.setText(libros.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return libros.size();
        }



        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imagen;
            TextView titulo;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imagen = (ImageView) itemView.findViewById(R.id.imagen);
                titulo = (TextView) itemView.findViewById(R.id.titulo);
            }
        }
    }
}