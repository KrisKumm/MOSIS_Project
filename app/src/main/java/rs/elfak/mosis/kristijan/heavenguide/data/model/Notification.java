package rs.elfak.mosis.kristijan.heavenguide.data.model;

import com.google.firebase.firestore.DocumentReference;

public class Notification {

    private String uid;
    private String message;
    private String from;
    private DocumentReference sender;
    private int type;


    public Notification(){}

    public Notification(String uId, String message,String from, DocumentReference sender, int type){
        this.uid = uId;
        this.message = message;
        this.from = from;
        this.sender = sender;
        this.type = type;
    }

    public String getUid(){return this.uid;}

    public void setUid(String uid){this.uid = uid;}

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
