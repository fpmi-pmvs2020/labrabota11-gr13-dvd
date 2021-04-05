package com.task.fbresult.db.fbdao;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.task.fbresult.model.FBModel;
import com.task.fbresult.util.FBUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import lombok.var;

import static com.task.fbresult.db.DBHelper.PER_ON_DUTY_DUTY_ID_COLUMN;


public abstract class FBDao<T extends FBModel> {
    private static final String LOG_TAG = "db_log";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    public T getWithId(String firebaseId) {
        var constraints = new HashMap<String, ConstraintPair>();
        constraints.put(getIdColumn(), new ConstraintPair(firebaseId, ConstraintType.EQUALS));
        return get(constraints).get(0);
    }

    public List<T> get(Map<String, ConstraintPair> constraints) {
        ArrayList<T> result = new ArrayList<>();
        var query = reference.child(getTableName()).orderByValue();
        for (var entry : constraints.entrySet()) {
            query = FBUtils.buildQueryWithConstraints(query.getRef(), entry.getKey(), entry.getValue());
        }
        var task = query.get();
        while (!task.isComplete()) ;
        DataSnapshot mainSnapshot = task.getResult();
        for (DataSnapshot snapshot : mainSnapshot.getChildren()) {
            result.add(snapshot.getValue(getModelClass()));
        }
        return result;
    }

    public List<T> getAll() {
        AtomicBoolean flag = new AtomicBoolean(false);
        ArrayList<T> result = new ArrayList<>();
        reference.child(getTableName()).get()
                .addOnSuccessListener(dataSnapshot -> {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        result.add(snapshot.getValue(getModelClass()));
                    }
                    flag.set(true);
                });
        while (!flag.get()) ;
        return result;
    }

    public void clean() {
        AtomicBoolean isCompleted = new AtomicBoolean(false);
        DatabaseReference tableReference = reference.child(getTableName());
        tableReference.removeValue()
                .addOnSuccessListener(
                        aVoid -> {
                            Log.d(LOG_TAG, "--- Cleaning in " + getTableName() + " performed ");
                            isCompleted.set(true);
                        }
                );
        while (!isCompleted.get()) ;
    }

    public void save(T t) {
        DatabaseReference tableReference = reference.child(getTableName());
        DatabaseReference objReference = tableReference.push();
        t.setFirebaseId(objReference.getKey());
        objReference.setValue(t);
        Log.d(LOG_TAG, "--- Insert in " + getTableName() + ": key: " + t.getFirebaseId());
    }

    public void update(T t) {
        DatabaseReference objReference = reference.child(getTableName()).child(t.getFirebaseId());
        objReference.setValue(t);
        Log.d(LOG_TAG, getTableName() + "--- update in " + t);
    }

    public void delete(T t) {
        DatabaseReference objReference = reference.child(getTableName()).child(t.getFirebaseId());
        objReference.removeValue();
        Log.d(LOG_TAG, getTableName() + "--- delete in :" + t);
    }

    abstract String getTableName();

    abstract Class<T> getModelClass();

    abstract String getIdColumn();
}
