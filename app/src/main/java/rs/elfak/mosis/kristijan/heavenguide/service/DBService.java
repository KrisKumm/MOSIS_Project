package rs.elfak.mosis.kristijan.heavenguide.service;

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
    public void AddAtraction(String atractionId,Atraction atraction, String managerId){

        DocumentReference documentReference;
        if(atractionId == null){
            documentReference = fStore.collection("atractions").document();
        }else{
            documentReference = fStore.collection("atractions").document(atractionId);
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
        if(atractionId == null){
            fStore.collection("managers").document(managerId)
                    .update( "atractions" , FieldValue.arrayUnion( documentReference ))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                        }
                    });
        }
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
        DocumentReference documentReference = fStore.collection("users").document(user.getUid());

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

class Atraction{

    private String uId;
    private String name;
    private String description;
    private ArrayList pictures;
    private GeoPoint location;
    private DocumentReference myRegion;


    Atraction(){}

    Atraction(String uId, String name, String description, ArrayList pictures, GeoPoint location, DocumentReference myRegion){

        this.uId = uId;
        this.name = name;
        this.description = description;
        this.pictures = pictures;
        this.location = location;
        this.myRegion = myRegion;
    }

    public String getUId(){
        return uId;
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public ArrayList getPictures(){
        return pictures;
    }

    public GeoPoint getLocation(){
        return location;
    }

    public DocumentReference getMyRegion(){
        return myRegion;
    }
}

class Tour{

    private String uId;
    private String managerId;
    private String guideId;
    private String name;
    private String description;
    private String portrait;
    private GeoPoint location;
    private Timestamp startsAt;
    private Timestamp endsAt;
    private DocumentReference myRegion;
    private ArrayList<DocumentReference> atractions;
    private ArrayList<String> pendingTourists;


    Tour(){}

    Tour(String uId, String managerId, String guideId, String name, String description, String portrait, GeoPoint location,
         Timestamp startsAt, Timestamp endsAt, DocumentReference myRegion, ArrayList<DocumentReference> atractions,
         ArrayList<String> pendingTourists){
        this.uId = uId;
        this.managerId = managerId;
        this.guideId = guideId;
        this.name = name;
        this.description = description;
        this.portrait = portrait;
        this.location = location;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.myRegion = myRegion;
        this.atractions = atractions;
        this.pendingTourists = pendingTourists;
    }

    public String getUId(){
        return this.uId;
    }
    public String getManagerId(){
        return this.managerId;
    }
    public String getGuideId(){
        return this.guideId;
    }
    public String getName(){
        return this.name;
    }
    public String getDescription(){
        return this.description;
    }
    public String getPortrait(){
        return this.portrait;
    }
    public GeoPoint getLocation(){
        return this.location;
    }
    public Timestamp getStartsAt(){
        return this.startsAt;
    }
    public Timestamp getEndsAt(){
        return this.endsAt;
    }
    public DocumentReference getMyRegion(){
        return this.myRegion;
    }
    public ArrayList<DocumentReference> getAtractions(){
        return this.atractions;
    }
    public ArrayList<String> getPendingTourists(){
        return this.pendingTourists;
    }
}

class Manager{

    private String uId;
    private String name;
    private String portrait;
    private ArrayList<DocumentReference> atractions;
    private ArrayList<DocumentReference> tours;

    Manager(){}

    Manager(String uId, String name, String portrait, ArrayList<DocumentReference> atractions, ArrayList<DocumentReference> tours){
        this.uId = uId;
        this.name = name;
        this.portrait = portrait;
        this.atractions = atractions;
        this.tours = tours;
    }

    public String getUId(){
        return this.uId;
    }
    public String getName(){
        return this.name;
    }
    public String getPortrait(){
        return this.portrait;
    }
    public ArrayList<DocumentReference> getAtractions(){
        return this.atractions;
    }
    public ArrayList<DocumentReference> getTours(){
        return this.tours;
    }
}

class Notification {

    private String uId;
    private String message;
    private String from;
    private DocumentReference sender;
    private int type;


    public Notification(){}

    public Notification(String uId, String message,String from, DocumentReference sender, int type){
        this.uId = uId;
        this.message = message;
        this.from = from;
        this.sender = sender;
        this.type = type;
    }

    public String getUId(){
        return this.uId;
    }

    public String getMessage(){
        return this.message;
    }

    public String getFrom() {
        return from;
    }

    public DocumentReference getSender(){
        return this.sender;
    }

    public int getType() {
        return type;
    }
}

class User {

    private String name;
    private String uId;
    private String portrait;

    public User(){

    }

    public User(String name, String uId, String portrait){
        this.name = name;
        this.uId = uId;
        this.portrait = portrait;
    }

    public String getName(){
        return name;
    }

    public String getUid(){
        return uId;
    }

    public String getPortrait(){
        return portrait;
    }
}

