package com.example.go4lunch.infrastructure.repository;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;
import androidx.lifecycle.LiveData;

import com.example.go4lunch.model.WorkmateModel;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkmateRepository extends LiveData<ArrayList<WorkmateModel>> {

    private static final String USER_COLLECTION_NAME = "users";
    private static final String RESTAURANT_COLLECTION_NAME = "restaurant_liked_by_workmate";

    public static final String PLACE_ID_LIKED = "placeIdLiked";

    private static volatile WorkmateRepository instance;
    private final Executor executor = Executors.newSingleThreadExecutor();


    private CollectionReference getWorkmateCollection() {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
    }

    private CollectionReference getRestaurantLikedByWorkmateCollection() {
        return FirebaseFirestore.getInstance().collection(RESTAURANT_COLLECTION_NAME);
    }

    public static WorkmateRepository getInstance() {
        WorkmateRepository result = instance;
        if (result != null)
            return instance;
        synchronized (WorkmateRepository.class) {
            if (instance == null) {
                instance = new WorkmateRepository();
            }
            return instance;
        }
    }

    public Task<Void> signOut(Context context) {
        return FirebaseRepository.getAuthUi().signOut(context);
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = FirebaseRepository.getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    public static WorkmateModel createWorkmate() {
        FirebaseUser user = FirebaseRepository.getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
            String userName = (user.getDisplayName());
            String uid = user.getUid();

            return new WorkmateModel(uid, userName, urlPicture);
        } else
            return null;
    }

    public Task<DocumentSnapshot> saveWorkmate() {
        WorkmateModel workmateModel = createWorkmate();
        String uid = this.getCurrentUserUID();
        if (uid != null && workmateModel != null) {
            return getWorkmateCollection().document(uid)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> this.getWorkmateCollection().document(workmateModel.getUserUid()).set(workmateModel));

        } else
            return null;
    }

    public void deleteWorkmateWhoLikedRestaurant(String userUID) {
        getRestaurantLikedByWorkmateCollection()
                .document(userUID)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("delete_user", "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w("delete_user", "Error deleting document", e));
    }

    public void listenWorkmateWhoLikePlace(String placeId) {
        getRestaurantLikedByWorkmateCollection()
                .whereEqualTo(PLACE_ID_LIKED, placeId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value != null) {
                        //final List<DocumentSnapshot> documents = value.();
                        ArrayList<WorkmateModel> workmateModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                            WorkmateModel workmateModel = queryDocumentSnapshot.toObject(WorkmateModel.class);
                            workmateModels.add(workmateModel);
                        }
                        setValue(workmateModels);
                    } else {

                        Log.d("TAG", "Current data: null");
                    }
                });
    }

    /**
     * Use in workmateFragment for listen new workmate
     */

    public void listenWorkmate() {
        getWorkmateCollection()
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        Log.w("TAG", "Listen failed.", error);
                        return;
                    }
                    if (value != null) {
                        //final List<DocumentSnapshot> documents = value.();
                        ArrayList<WorkmateModel> workmateModels = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : value) {
                            WorkmateModel workmateModel = queryDocumentSnapshot.toObject(WorkmateModel.class);
                            workmateModels.add(workmateModel);
                        }
                        setValue(workmateModels);

                    } else {

                        Log.d("TAG", "Current data: null");
                    }
                });
    }

    /**
     * Persist workmate when the user like a restaurant
     */
    public void saveWorkmateWhoLikedRestaurant(String placeId) {
        WorkmateModel workmateModel = createWorkmate();
        if (workmateModel != null) {
            workmateModel.setPlaceIdLiked(placeId);
            getRestaurantLikedByWorkmateCollection()
                    .document(workmateModel.getUserUid())
                    .set(workmateModel)
                    .addOnSuccessListener(event -> Log.d("add workmate", "ok"))
                    .addOnFailureListener(event -> Log.w("add workmate", event.getMessage()));
        }
    }


    public void deleteWorkmateLikedRestaurantCollection() {
        int batchSize = 100;
        executor.execute(() -> {
            try {
                Query query = getRestaurantLikedByWorkmateCollection()
                        .orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = getRestaurantLikedByWorkmateCollection().orderBy(FieldPath.documentId())
                            .startAfter(last.getId())
                            .limit(batchSize);
                    deleted = deleteQueryBatch(query);
                }
            } catch (Exception e) {
                Log.w("deleteCollectionDebug", e.getMessage());
            }
        });
    }

    @WorkerThread
    private List<DocumentSnapshot> deleteQueryBatch(final Query query) throws Exception {
        QuerySnapshot querySnapshot = Tasks.await(query.get());

        WriteBatch batch = query.getFirestore().batch();
        for (QueryDocumentSnapshot snapshot : querySnapshot) {
            batch.delete(snapshot.getReference());
            Log.i("deleteCollectionDebug", "in the second for");
        }
        Tasks.await(batch.commit());
        Log.i("deleteCollectionDebug", "after the await");

        return querySnapshot.getDocuments();
    }
}
