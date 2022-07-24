package com.idc.fashion.Firebase;

import android.net.Uri;

import androidx.annotation.NonNull;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idc.fashion.Model.CategoryItems;
import com.idc.fashion.Model.Item;
import com.idc.fashion.Model.User;
import com.idc.fashion.SecurityHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Database {
    public static final String USERS_PATH = "Users";
    private static final Map<LifecycleOwner, FirebaseObserver> firebaseObservers = new HashMap<>();

    public static class ItemsManager {


        public static void listenForAllItemsByCategory(LifecycleOwner lifecycleOwner,
                                                       String category,
                                                       OnDataFetched<ArrayList<Item>> itemsFetchedListener) {

            if (!firebaseObservers.containsKey(lifecycleOwner)) {
                DefaultLifecycleObserver newObserver = createFirebaseLifeCycleObserver(lifecycleOwner);
                firebaseObservers.put(lifecycleOwner, new FirebaseObserver(newObserver));
                lifecycleOwner.getLifecycle().addObserver(newObserver);
            }
            addListener(lifecycleOwner,
                    FirebaseFirestore
                            .getInstance()
                            .collection(CategoryItems.CATEGORIES)
                            .document("doc")
                            .collection(category)
                            .addSnapshotListener((value, error) -> {
                                ArrayList<Item> items = new ArrayList<>();
                                for (DocumentSnapshot itemSnap : value.getDocuments()) {
                                    items.add(itemSnap.toObject(Item.class));
                                }
                                itemsFetchedListener.onFetched(items);
                            }));
        }

        public static void listenForAllItems(
                LifecycleOwner lifecycleOwner,
                OnDataFetched<CategoryItems> itemsFetchedListener) {
            String[] categories = new String[]{
                    CategoryItems.SHIRTS,
                    CategoryItems.PANTS,
                    CategoryItems.DRESS,
                    CategoryItems.SKIRTS};
            int firstCategory = 0;
            listenForAllItems(lifecycleOwner,
                    itemsFetchedListener,
                    new CategoryItems(),
                    categories, firstCategory);
        }

        private static void listenForAllItems(LifecycleOwner lifecycleOwner,
                                              OnDataFetched<CategoryItems> listOnDataFetched,
                                              CategoryItems categoryItems,
                                              String[] categories,
                                              int category) {
            listenForAllItemsByCategory(lifecycleOwner, categories[category], data -> {
                // put current category data
                categoryItems.setAllByCategory(categories[category], data);
                // last category -> return full answer
                if (category == categories.length - 1) {
                    listOnDataFetched.onFetched(categoryItems);
                } else {  // move to next category
                    listenForAllItems(
                            lifecycleOwner,
                            listOnDataFetched,
                            categoryItems,
                            categories,
                            category + 1);
                }
            });
        }

        public static void saveItem(Item item, Uri uri,OnSuccessListener<String> onSuccessListener, OnFailureListener onFailureListener) {
            StorageReference newReference = FirebaseStorage.getInstance().getReference("FashionImages/" + item.getCategory() + "/" + item.getItemId());
            newReference.putFile(uri)
                    .addOnSuccessListener(taskSnapshot -> {
                        newReference.getDownloadUrl()
                                .addOnSuccessListener(imageUrl -> {
                                    item.setImageAddress(SecurityHelper.Encrypt(imageUrl.toString()));
                                    FirebaseFirestore
                                            .getInstance()
                                            .collection(CategoryItems.CATEGORIES)
                                            .document("doc")
                                            .collection(item.getCategory())
                                            .document(item.getItemId())
                                            .set(item)
                                            .addOnSuccessListener(unused -> onSuccessListener.onSuccess("Successfully added " + item.getName()))
                                            .addOnFailureListener(onFailureListener);
                                }).addOnFailureListener(onFailureListener);
                    }).addOnFailureListener(onFailureListener);
        }

        public static void removeItem(Item item,OnSuccessListener<String> onSuccessListener,OnFailureListener onFailureListener) {
            FirebaseFirestore
                    .getInstance()
                    .collection(CategoryItems.CATEGORIES)
                    .document("doc")
                    .collection(item.getCategory())
                    .document(item.getItemId())
                    .delete()
                    .addOnSuccessListener(unused -> onSuccessListener.onSuccess("Successfully removed item " + item.getName()))
                    .addOnFailureListener(onFailureListener);
        }
    }

    public static class UserManager {
        public static void saveUser(User user) {
            String userId = FirebaseAuth.getInstance().getUid();
            assert userId != null;
            FirebaseFirestore.getInstance()
                    .collection(USERS_PATH)
                    .document(userId)
                    .set(user);
        }
    }

    private static DefaultLifecycleObserver createFirebaseLifeCycleObserver(LifecycleOwner lifecycleOwner) {
        return new DefaultLifecycleObserver() {
            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onDestroy(owner);
                removeAllListeners(lifecycleOwner);
            }

            @Override
            public void onStop(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onStop(owner);
                removeAllListeners(lifecycleOwner);
            }
        };
    }

    private static void addListener(LifecycleOwner lifecycleOwner, ListenerRegistration listenerRegistration) {
        firebaseObservers.get(lifecycleOwner).addListener(listenerRegistration);
    }

    private static void removeAllListeners(LifecycleOwner lifecycleOwner) {
        if (firebaseObservers.containsKey(lifecycleOwner) && firebaseObservers.get(lifecycleOwner).hasListeners()) {
            firebaseObservers.get(lifecycleOwner).removeAllListeners();
            firebaseObservers.remove(lifecycleOwner);
        }
    }
}
