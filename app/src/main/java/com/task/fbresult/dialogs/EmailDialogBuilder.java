package com.task.fbresult.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.task.fbresult.R;

public class EmailDialogBuilder extends DialogBuilder{
    private EditText etEmail;

    public EmailDialogBuilder(Context context) {
        super(context);
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.email_dialog,null);
    }

    @Override
    void checkAndSetFields() {
        etEmail = mainWindow.findViewById(R.id.etEmail);
    }

    @Override
    String[] getFieldValues() throws Exception {
        String email = etEmail.getText().toString();
        if(email.isEmpty())
            throw new Exception(context.getString(R.string.empty_fields_ex));
        return new String[]{email};
    }

    @Override
    void setData(String[] values) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(values[0]);
    }
}
