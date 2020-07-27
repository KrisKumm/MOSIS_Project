package rs.elfak.mosis.kristijan.heavenguide.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.ArrayList;

public class StorageService {

    private ProgressDialog mProgressDialog;
    private StorageReference mStorageRef;
    private int picSize;
    private static StorageService instance = null;
    private StorageService(){
        mStorageRef = FirebaseStorage.getInstance().getReference();
    }

    public static StorageService getInstance()
    {
        if (instance == null)
            instance = new StorageService();

        return instance;
    }
    public void uploadPhoto(String to, String myId, String photoName, File photo, final Context context){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.show();

        Uri uri = Uri.fromFile(photo);
        StorageReference storageReference = mStorageRef.child("images/" + to + "/" + myId + "/" + photoName + ".jpeg");
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Get a URL to the uploaded content
                Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                toastMessage("Upload Success", context);
                mProgressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                toastMessage("Upload Failed", context);
                mProgressDialog.dismiss();
            }
        })
        ;
    }
    public Bitmap downloadPhoto(String from, String myId, String photoName){

        final Bitmap[] image = new Bitmap[1];
        StorageReference islandRef = mStorageRef.child("images/" + from + "/" + myId + "/" + photoName + ".jpeg");
        islandRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                picSize = (int) storageMetadata.getSizeBytes();
            }
        });
        islandRef.getBytes(picSize).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap b = BitmapFactory.decodeByteArray(bytes,0,picSize);
                image[0] = b;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                // Handle any errors
            }
        });

        return image[0];
    }
    public ArrayList<Bitmap> downloadPhotos(String from, String myId, ArrayList<String> photoNames){
        ArrayList<Bitmap> photos = new ArrayList<Bitmap>();
        for (String photo : photoNames) {
            photos.add(downloadPhoto(from, myId, photo));
        }
        return photos;
    }
    private void toastMessage(String message, Context context){
        Toast.makeText( context ,message,Toast.LENGTH_SHORT).show();
    }
}

