package rs.elfak.mosis.kristijan.heavenguide.service;



import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import rs.elfak.mosis.kristijan.heavenguide.data.model.Notification;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Attraction;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Manager;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Region;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Review;
import rs.elfak.mosis.kristijan.heavenguide.data.model.Star;
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
    public String AddAttraction(Attraction attraction, String managerId){

        DocumentReference documentReference;
        if(attraction.getUid() == null){
            documentReference = fStore.collection("attractions").document();
            attraction.setUid(documentReference.getId());
        }else {
            documentReference = fStore.collection("attractions").document(attraction.getUid());
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
        if(attraction.getUid() == null){
            fStore.collection("managers").document(managerId)
                    .update( "attractions" , FieldValue.arrayUnion( attraction ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
        return attraction.getUid();
    }
    public void GetAttractionsByName(String name, final  FirebaseCallback firebaseCallback){

        final ArrayList<Attraction> attractions = new ArrayList<Attraction>();
        final Query query = fStore.collection("attractions").whereEqualTo("name" , name);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                   attractions.add(document.toObject(Attraction.class));
                }
                firebaseCallback.onCallback(attractions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.onCallback(attractions);
            }
        });
    }
    public void GetAttractionsByLocation(GeoPoint topLeftPoint, GeoPoint bottomRightPoint, final FirebaseCallback firebaseCallback){
        final ArrayList<Attraction> attractions = new ArrayList<Attraction>();
        final Query query = fStore.collection("attractions")
                .whereLessThan("location" , topLeftPoint)
                .whereGreaterThan("location" , bottomRightPoint);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    attractions.add(document.toObject(Attraction.class));
                }
                firebaseCallback.onCallback(attractions);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.onCallback(attractions);
            }
        });
    }

    public void AddTourGroup(TourGroup tourGroup){

        DocumentReference documentReference;
        if(tourGroup.getUid() == null){
            documentReference = fStore.collection("tour-groups").document();
            tourGroup.setUid(documentReference.getId());
        }else{
            documentReference = fStore.collection("tour-groups").document(tourGroup.getUid());
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
    public ListenerRegistration OnTourGroupUpdate(String id, final FirebaseCallback firebaseCallback){
        DocumentReference docRef = fStore.collection("tour-groups").document(id);
        ListenerRegistration subscription = docRef.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException e) {
                firebaseCallback.onCallback(snapshot.toObject(TourGroup.class));
            }
        });
        return subscription;
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
        if(region.getUid() == null){
            documentReference = fStore.collection("regions").document();
            region.setUid(documentReference.getId());
        }else{
            documentReference = fStore.collection("regions").document(region.getUid());
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
    public void GetToursByName(String name, final FirebaseCallback firebaseCallback){

        final ArrayList<Tour> tours = new ArrayList<Tour>();
        final Query query = fStore.collection("tours").whereEqualTo("name" , name);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    tours.add(document.toObject(Tour.class));
                }
                firebaseCallback.onCallback(tours);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.onCallback(tours);
            }
        });
    }
    public void AddTour(Tour tour, String managerId){

        DocumentReference documentReference;
        if(tour.getUid() == null){
            documentReference = fStore.collection("tours").document();
            tour.setUid(documentReference.getId());
        }else{
            documentReference = fStore.collection("tours").document(tour.getUid());
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
        if(tour.getUid() == null){
            fStore.collection("managers").document(managerId)
                    .update( "tours" , FieldValue.arrayUnion( tour ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
    }

    public void AddUser(User user){
        DocumentReference documentReference = fStore.collection("users").document(user.getUid());

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
                       if(user != null)
                            user.notifications = new ArrayList<Notification>((ArrayList<Notification>) object);
                       firebaseCallback.onCallback(user);
                   }
               });
            }
        });
    }
    public void GetUsersByName(String name, final FirebaseCallback firebaseCallback){
        final ArrayList<User> users = new ArrayList<User>();
        final Query query = fStore.collection("users").whereEqualTo("name" , name);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                for (QueryDocumentSnapshot document : querySnapshot) {
                    users.add(document.toObject(User.class));
                }
                firebaseCallback.onCallback(users);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.onCallback(users);
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

    public DocumentReference GetUserReference(String id){
        return fStore.collection("users").document(id);
    }
    public DocumentReference GetTourGroupReference(String id){
        return fStore.collection("tour-groups").document(id);
    }

    public void AddManager(Manager manager){
        DocumentReference documentReference = fStore.collection("managers").document(manager.getUid());

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
                        if(manager != null)
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
        DocumentReference documentReference = fStore.collection("guides").document(guide.getUid());

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
                        if(guide != null)
                            guide.notifications = new ArrayList<Notification>((ArrayList<Notification>) object);
                        firebaseCallback.onCallback(guide);
                    }
                });
            }
        });

    }
    public void GetGuideByName(String name, final FirebaseCallback firebaseCallback){
        final Query query = fStore.collection("guides").whereEqualTo("name" , name);

        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                firebaseCallback.onCallback(querySnapshot.getDocuments().get(0).toObject(TourGuide.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                firebaseCallback.onCallback(null);
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

        DocumentReference df = documentReference.collection("review").document();
        review.setUid(documentReference.getId());
        df.set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        DocumentReference df = documentReference.collection("notifications").document();
        notification.setUid(df.getId());
        df.set(notification).addOnSuccessListener(new OnSuccessListener<Void>() {
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

    public void AddStar(DocumentReference documentReference, Star star){
        // treba da se doda kod za ostali tip notifikacija
        DocumentReference df = documentReference.collection("stars").document();
        star.setUid(df.getId());
        df.set(star).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //DODAJ TOST AKO OCES
            }
        });
    }
    public void DeleteStar(DocumentReference documentReference, String id){

        documentReference.collection("stars").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // URADI NESTO AKO OCES
            }
        });
    }
    public void getStars(DocumentReference documentReference, final FirebaseCallback firebaseCallback){

        final ArrayList<Star> stars = new ArrayList<Star>();
        documentReference.collection("stars").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                for (DocumentSnapshot doc: queryDocumentSnapshots) {
                    stars.add(doc.toObject(Star.class));
                }
                firebaseCallback.onCallback(stars);
            }
        });
    }
    public ListenerRegistration OnStarsUpdate(DocumentReference df, String id, final FirebaseCallback firebaseCallback){
        Query query = df.collection("stars");
        ListenerRegistration subscription = query.addSnapshotListener(MetadataChanges.EXCLUDE, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot snapshot, FirebaseFirestoreException e) {
                ArrayList<Star> stars = new ArrayList<Star>();
                for(DocumentSnapshot ds : snapshot){
                    stars.add(ds.toObject(Star.class));
                }
                firebaseCallback.onCallback(stars);
            }
        });
        return subscription;
    }

}











