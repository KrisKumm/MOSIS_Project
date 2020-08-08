package rs.elfak.mosis.kristijan.heavenguide.service;



import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Region;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Review;
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

    public void DeleteAttraction(String id){
        final DocumentReference documentReference = fStore.collection("attractions").document(id);

        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // moz dodas neki TOST OVDE
            }
        });
    }
    public void GetAttraction(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("attractions").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Attraction attraction = documentSnapshot.toObject(Attraction.class);
                getReviews(documentReference, new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        attraction.reviews = new ArrayList<Review>((ArrayList<Review> )object);
                        firebaseCallback.onCallback(attraction);
                    }
                });
            }
        });

    }
    public void AddAttraction(Attraction attraction, String managerId){

        DocumentReference documentReference;
        if(attraction.getUId() == null){
            documentReference = fStore.collection("attractions").document();
        }else{
            documentReference = fStore.collection("attractions").document(attraction.getUId());
        }
        documentReference.set(attraction).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        if(attraction.getUId() == null){
            fStore.collection("managers").document(managerId)
                    .update( "attractions" , FieldValue.arrayUnion( documentReference ))
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
    public void GetTourGroup(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("tour-groups").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firebaseCallback.onCallback(documentSnapshot.toObject(TourGroup.class));
            }
        });
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
    public void UpdateGuideLocation(String id, GeoPoint location){
        final DocumentReference documentReference = fStore.collection("tour-groups").document(id);
        documentReference
                .update("tourGuideLocation", location)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

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
    public void GetRegion(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("regions").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                firebaseCallback.onCallback(documentSnapshot.toObject(Region.class));
            }
        });
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
    public void GetTour(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("tours").document(id);


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Tour tour = documentSnapshot.toObject(Tour.class);
                getReviews(documentReference, new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        tour.reviews = new ArrayList<Review>((ArrayList<Review> )object);
                        firebaseCallback.onCallback(tour);
                    }
                });
            }
        });
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
    public void GetUser(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("users").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
               final User user = documentSnapshot.toObject(User.class);
               getNotifications(documentReference, new FirebaseCallback() {
                   @Override
                   public void onCallback(Object object) {
                       user.notifications = new ArrayList<Notification>((ArrayList<Notification>) object);
                       firebaseCallback.onCallback(user);
                   }
               });
            }
        });
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
    public void GetManager(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("managers").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final Manager manager = documentSnapshot.toObject(Manager.class);
                getNotifications(documentReference, new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        manager.notifications = new ArrayList<Notification>((ArrayList<Notification>) object);
                        firebaseCallback.onCallback(manager);
                    }
                });

            }
        });
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
    public void GetGuide(String id, final FirebaseCallback firebaseCallback){
        final DocumentReference documentReference = fStore.collection("guides").document(id);

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final TourGuide guide = documentSnapshot.toObject(TourGuide.class);
                getNotifications(documentReference, new FirebaseCallback() {
                    @Override
                    public void onCallback(Object object) {
                        guide.notifications = new ArrayList<Notification>((ArrayList<Notification>) object);
                        firebaseCallback.onCallback(guide);
                    }
                });
            }
        });

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

    public void AddReview(DocumentReference documentReference, Review review){

        documentReference.collection("review").document().set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //DODAJ TOST AKO OCES
            }
        });

    }
    public void getReviews(DocumentReference documentReference, final FirebaseCallback firebaseCallback){

        final ArrayList<Review> reviews = new ArrayList<Review>();
        documentReference.collection("reviews").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot doc: queryDocumentSnapshots) {
                    reviews.add(doc.toObject(Review.class));
                }
                firebaseCallback.onCallback(reviews);
            }
        });
    }

    public void AddNotification(DocumentReference documentReference, Notification notification){
        // treba da se doda kod za ostali tip notifikacija
        documentReference.collection("notifications").document().set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //DODAJ TOST AKO OCES
            }
        });
    }
    public void DeleteNotification(DocumentReference documentReference, String id){

        documentReference.collection("notifications").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // URADI NESTO AKO OCES
            }
        });
    }
    public void getNotifications(DocumentReference documentReference, final FirebaseCallback firebaseCallback){

        final ArrayList<Notification> notifications = new ArrayList<Notification>();
        documentReference.collection("notifications").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot doc: queryDocumentSnapshots) {
                    notifications.add(doc.toObject(Notification.class));
                }
                firebaseCallback.onCallback(notifications);
            }
        });
    }

}











