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
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class WorkmateRepository extends LiveData<ArrayList<WorkmateModel>> {

    private static final String USER_COLLECTION_NAME = "users";
    private static final String RESTAURANT_COLLECTION_NAME = "restaurant_liked";

    private static volatile WorkmateRepository instance;
    private final Executor executor = Executors.newSingleThreadExecutor();

    private CollectionReference getUserCollection() {
        return FirebaseFirestore.getInstance().collection(USER_COLLECTION_NAME);
    }

    private CollectionReference getRestaurantCollection() {
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

    @Nullable
    public FirebaseUser getCurrentUser() {
        return FirebaseRepository.getAuth().getCurrentUser();
    }

    public Task<Void> signOut(Context context) {
        return FirebaseRepository.getAuthUi().signOut(context);
    }

    public Task<Void> deleteUser(Context context) {
        return FirebaseRepository.getAuthUi().delete(context);
    }

    @Nullable
    public String getCurrentUserUID() {
        FirebaseUser user = getCurrentUser();
        return (user != null) ? user.getUid() : null;
    }

    private WorkmateModel createUser() {
        FirebaseUser user = getCurrentUser();
        if (user != null) {
            String urlPicture = (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : null);
            String userName = (user.getDisplayName());
            String uid = user.getUid();

            return new WorkmateModel(uid, userName, urlPicture);
        } else
            return null;
    }

    public Task<DocumentSnapshot> persistUser() {
        WorkmateModel workmateModel = createUser();
        return Objects.requireNonNull(getUserData()).addOnSuccessListener(documentSnapshot -> this.getUserCollection().document(workmateModel.getUserUid()).set(workmateModel));
    }

    private Task<DocumentSnapshot> getUserData() {
        String uid = this.getCurrentUserUID();
        if (uid != null) {
            return this.getUserCollection().document(uid).get();
        } else {
            return null;
        }
    }

    public void deleteWorkmateWhoLikedRestaurant(String userUID) {
        getRestaurantCollection()
                .document(userUID)
                .delete()
                .addOnSuccessListener(aVoid -> Log.d("delete_user", "DocumentSnapshot successfully deleted!"))
                .addOnFailureListener(e -> Log.w("delete_user", "Error deleting document", e));
    }


    public void addSnapShotListener(String placeId) {
        getRestaurantCollection()
                .whereEqualTo("placeIdLiked", placeId)
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

    public void addWorkmateSnapShotListener() {
        getUserCollection()
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
     *
     * @param placeId
     */
    public void persistWorkmateLikedRestaurant(String placeId) {
        WorkmateModel workmateModel = createUser();
        if (workmateModel != null) {
            workmateModel.setPlaceIdLiked(placeId);
            getRestaurantCollection()
                    .document(workmateModel.getUserUid())
                    .set(workmateModel)
                    .addOnSuccessListener(event -> Log.d("add workmate", "DocumentSnapshot written with ID: " + "ok"))
                    .addOnFailureListener(event -> Log.w("add workmate", event.getMessage()));
        }
    }

    public void deleteCollection(/*List<String> placeIdList*/) {
        int batchSize = 100;
        executor.execute(() -> {
            try {
                Query query = getRestaurantCollection()
                        .orderBy(FieldPath.documentId()).limit(batchSize);

                // Get a list of deleted documents
                List<DocumentSnapshot> deleted = deleteQueryBatch(query);

                // While the deleted documents in the last batch indicate that there
                // may still be more documents in the collection, page down to the
                // next batch and delete again
                while (deleted.size() >= batchSize) {
                    DocumentSnapshot last = deleted.get(deleted.size() - 1);
                    query = getRestaurantCollection().orderBy(FieldPath.documentId())
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

    public void getListRestaurant() {
        CollectionReference collection = FirebaseFirestore.getInstance().collection("restaurant_liked");
        collection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("getListRestaurant", document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d("getListRestaurant", "Error getting documents: ", task.getException());
                    }
                });
    }


}
