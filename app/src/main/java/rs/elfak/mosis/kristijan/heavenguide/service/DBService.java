package rs.elfak.mosis.kristijan.heavenguide.service;

import android.app.Notification;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Atraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Region;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Tour;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGroup;
import rs.elfak.mosis.kristijan.heavenguide.data.model.TourGuide;
import rs.elfak.mosis.kristijan.heavenguide.data.model.User;

public class DBService
{
    private static DBService instance = null;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private DBService()
    {
    }

    public static DBService getInstance()
    {
        if (instance == null)
            instance = new DBService();

        return instance;
    }

    public void DeleteAtraction(String id){
        final DocumentReference documentReference = fStore.collection("atractions").document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // moz dodas neki TOST OVDE
            }
        });
    }
    public Atraction GetAtraction(String id){
        final DocumentReference documentReference = fStore.collection("atractions").document(id);

        final Atraction[] atraction = new Atraction[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                atraction[0] = documentSnapshot.toObject(Atraction.class);
            }
        });
        return atraction[0];
    }
    public void AddAtraction(Atraction atraction, String managerId){

        DocumentReference documentReference;
        if(atraction.getUId() == null){
            documentReference = fStore.collection("atractions").document();
        }else{
            documentReference = fStore.collection("atractions").document(atraction.getUId());
        }
        documentReference.set(atraction).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        if(atraction.getUId() == null){
            fStore.collection("managers").document(managerId)
                    .update( "atractions" , FieldValue.arrayUnion( documentReference ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
    }

    public void AddTourGroup(TourGroup tourGroup){

        DocumentReference documentReference;
        if(tourGroup.getUId() == null){
            documentReference = fStore.collection("tour-groups").document();
        }else{
            documentReference = fStore.collection("tour-groups").document(tourGroup.getUId());
        }
        documentReference.set(tourGroup).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Log.w(TAG, "Error adding document", e);
            }
        });
    }
    public TourGroup GetTourGroup(String id){
        final DocumentReference documentReference = fStore.collection("tour-groups").document(id);

        final TourGroup[] tourGroup = new TourGroup[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                tourGroup[0] = documentSnapshot.toObject(TourGroup.class);
            }
        });
        return tourGroup[0];
    }
    public void DeleteTourGroup(String id){
        final DocumentReference documentReference = fStore.collection("tour-groups").document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // moz dodas neki TOST OVDE
            }
        });
    }

    public void AddRegion(Region region){

        DocumentReference documentReference;
        if(region.getUId() == null){
            documentReference = fStore.collection("regions").document();
        }else{
            documentReference = fStore.collection("regions").document(region.getUId());
        }
        documentReference.set(region).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
    }
    public Region GetRegion(String id){
        final DocumentReference documentReference = fStore.collection("regions").document(id);

        final Region[] region = new Region[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                region[0] = documentSnapshot.toObject(Region.class);
            }
        });
        return region[0];
    }
    public void DeleteRegion(String id){
        final DocumentReference documentReference = fStore.collection("regions").document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // moz dodas neki TOST OVDE
            }
        });
    }

    public void DeleteTour(String id){
        final DocumentReference documentReference = fStore.collection("tours").document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // moz dodas neki TOST OVDE
            }
        });
    }
    public DocumentSnapshot GetTour(String id){
        final DocumentReference documentReference = fStore.collection("tours").document(id);

        final DocumentSnapshot[] doc = new DocumentSnapshot[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                doc[0] = documentSnapshot;
            }
        });
        return doc[0];
    }
    public void AddTour(Tour tour){

        DocumentReference documentReference;
        if(tour.getUId() == null){
            documentReference = fStore.collection("tours").document();
        }else{
            documentReference = fStore.collection("tours").document(tour.getUId());
        }
        documentReference.set(tour).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Log.d(TAG, "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Log.w(TAG, "Error adding document", e);
                    }
                });
        if(tour.getUId() == null){
            fStore.collection("managers").document(tour.getUId())
                    .update( "tours" , FieldValue.arrayUnion( documentReference ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
    }

    public void AddUser(User user){
        DocumentReference documentReference = fStore.collection("users").document(user.getUId());

        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText( LoginActivity.this, "uspeo si",
                //Toast.LENGTH_SHORT).show();
            }
        });
    }
    public User GetUser(String id){
        final DocumentReference documentReference = fStore.collection("users").document(id);

        final User[] user = new User[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user[0] = documentSnapshot.toObject(User.class);
            }
        });
        return user[0];
    }
    public void DeleteUser(String id){
        final DocumentReference documentReference = fStore.collection("users").document(id);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                //dodaj neki TOST nzm
            }
        });
    }

    public void AddManager(Manager manager){
        DocumentReference documentReference = fStore.collection("managers").document(manager.getUId());

        documentReference.set(manager).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText( LoginActivity.this, "uspeo si",
                //Toast.LENGTH_SHORT).show();
            }
        });
    }
    public Manager GetManager(String id){
        final DocumentReference documentReference = fStore.collection("managers").document(id);

        final Manager[] manager = new Manager[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                manager[0] = documentSnapshot.toObject(Manager.class);
            }
        });
        return manager[0];
    }
    public void DeleteManager(String id){
        final DocumentReference documentReference = fStore.collection("managers").document(id);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                //dodaj neki TOST nzm
            }
        });
    }

    public void AddGuide(TourGuide guide){
        DocumentReference documentReference = fStore.collection("guides").document(guide.getUId());

        documentReference.set(guide).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText( LoginActivity.this, "uspeo si",
                //Toast.LENGTH_SHORT).show();
            }
        });
    }
    public TourGuide GetGuide(String id){
        final DocumentReference documentReference = fStore.collection("guides").document(id);

        final TourGuide[] guide = new TourGuide[1];
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                guide[0] = documentSnapshot.toObject(TourGuide.class);
            }
        });
        return guide[0];
    }
    public void DeleteGuide(String id){
        final DocumentReference documentReference = fStore.collection("guides").document(id);
        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void avoid) {
                //dodaj neki TOST nzm
            }
        });
    }

    public void AddReview(DocumentReference documentReference, int rating, String comment){

        Map<String, Object> review = new HashMap<String , Object>();
        review.put("rating", rating);
        if(comment != null){
            review.put("comment", comment);
        }
        documentReference.collection("review").document().set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //DODAJ TOST AKO OCES
            }
        });

    }

    public void AddNotification(DocumentReference documentReference, Notification notification){
        // treba da se doda kod za ostali tip notifikacija
        documentReference.collection("notification").document().set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //DODAJ TOST AKO OCES
            }
        });
    }
    public void DeleteNotification(DocumentReference documentReference, String id){

        documentReference.collection("notification").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // URADI NESTO AKO OCES
            }
        });
    }

}











