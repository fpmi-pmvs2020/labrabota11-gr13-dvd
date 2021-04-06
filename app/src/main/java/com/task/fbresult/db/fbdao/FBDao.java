package com.task.fbresult.db.fbdao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
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
        return get(getIdColumn(), new ConstraintPair(firebaseId, ConstraintType.EQUALS)).get(0);
    }

    public List<T> get(String parameterName, ConstraintPair constraintPair) {
        ArrayList<T> result = new ArrayList<>();
        var ref = reference.child(getTableName());
        var query = FBUtils.buildQueryWithConstraints(ref, parameterName, constraintPair);
        var task = query.get();
        while (!task.isComplete()) ;
        DataSnapshot mainSnapshot = task.getResult();
        for (DataSnapshot snapshot : mainSnapshot.getChildren()) {
            result.add(snapshot.getValue(getModelClass()));
        }
        return result;
    }

    public List<T> getAll() {
        ArrayList<T> result = new ArrayList<>();
        var task = reference.child(getTableName()).get();
        while (!task.isComplete()) ;
        var dataSnapshot = task.getResult();
        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
            result.add(snapshot.getValue(getModelClass()));
        }
        return result;
    }

    public void clean() {
        DatabaseReference tableReference = reference.child(getTableName());
        tableReference.removeValue().addOnCompleteListener(
                task1 -> Log.d(LOG_TAG, "--- Cleaning in " + getTableName() + " performed ")
        );
    }

    public void save(T t) {
        DatabaseReference tableReference = reference.child(getTableName());
        DatabaseReference objReference = tableReference.push();
        t.setFirebaseId(objReference.getKey());
        objReference.setValue(t)
                .addOnCompleteListener(
                        task -> Log.d(LOG_TAG, "--- Insert in " + getTableName() + ": key: " + t.getFirebaseId())
                );

    }

    public void update(T t) {
        DatabaseReference objReference = reference.child(getTableName()).child(t.getFirebaseId());
        objReference.setValue(t).addOnCompleteListener(task -> Log.d(LOG_TAG, getTableName() + "--- update in " + t));

    }

    public void delete(T t) {
        DatabaseReference objReference = reference.child(getTableName()).child(t.getFirebaseId());
        objReference.removeValue().addOnCompleteListener(task -> Log.d(LOG_TAG, getTableName() + "--- delete in :" + t));
    }

    abstract String getTableName();

    abstract Class<T> getModelClass();

    abstract String getIdColumn();
}
