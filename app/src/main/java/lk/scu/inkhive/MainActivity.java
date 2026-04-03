package lk.scu.inkhive;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import lk.scu.inkhive.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
      // EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //SA24610703, SA24610771, SA24610519
        //youtube tutorial
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if(id==R.id.home){
                replaceFragment(new HomeFragment());
            }else if (id== R.id.details){
                replaceFragment(new DetailsFragment());
            }else if (id == R.id.favourites){
                replaceFragment(new FavouritesFragment());
            }else if (id == R.id.profile){
                replaceFragment(new ProfileFragment());
            }
            return true;
        });// switch didnt work so we have changed it to if-else

        fetchDataFromFirebase();
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_Layout,fragment);
        fragmentTransaction.commit();
    }

    // navigation bar youtube tutorial link: https://youtu.be/jOFLmKMOcK0?si=nA1M_kvShshCofPb
    // i have made 4 fragments for 4 individual pages

    //Mr Dilhan(student assistant)
   private void fetchDataFromFirebase() {
       FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference colRef = db.collection("temp");
          colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                    for(QueryDocumentSnapshot doc : task.getResult())
                        Log.d("[MAIN]", doc.getId() + " => " + doc.getData());
                else
                    Log.d("[MAIN]", "Error getting documents: ", task.getException());
            }
       });
    }
}