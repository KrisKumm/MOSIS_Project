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
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    public void uploadPhoto(String to, String myId, String photoName, Bitmap photo, final Context context){
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("Uploading Image...");
        mProgressDialog.show();


        Uri uri = Uri.fromFile(persistImage(photo, photoName, context));
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
    public void downloadPhoto(String from, String myId, String photoName,final FirebaseCallback firebaseCallback){

        final StorageReference islandRef = mStorageRef.child("images/" + from + "/" + myId + "/" + photoName + ".jpeg");
        islandRef.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
            @Override
            public void onSuccess(StorageMetadata storageMetadata) {
                picSize = (int) storageMetadata.getSizeBytes();
                islandRef.getBytes(picSize).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, picSize);
                        firebaseCallback.onCallback(b);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        // Handle any errors
                        firebaseCallback.onCallback(null);
                    }
                });
            }
        });


    }
    public void downloadPhotos(String from, String myId, ArrayList<String> photoNames,final FirebaseCallback firebaseCallback){
        final ArrayList<Bitmap> photos = new ArrayList<Bitmap>();
        for (String photo : photoNames) {
            downloadPhoto(from, myId, photo, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    photos.add((Bitmap) object);
                    firebaseCallback.onCallback(photos);
                }
            });
        }
    }
    public void downloadCoverPhotos(String from, String photoName, ArrayList<String> ids,final FirebaseCallback firebaseCallback){
        for (String id : ids) {
            downloadPhoto(from, id, photoName, new FirebaseCallback() {
                @Override
                public void onCallback(Object object) {
                    firebaseCallback.onCallback((Bitmap) object);
                }
            });
        }
    }

    private void toastMessage(String message, Context context){
        Toast.makeText( context ,message,Toast.LENGTH_SHORT).show();
    }

    private File persistImage(Bitmap bitmap, String name, Context context) {
        File filesDir = context.getApplicationContext().getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
        }

        return imageFile;
    }
}

