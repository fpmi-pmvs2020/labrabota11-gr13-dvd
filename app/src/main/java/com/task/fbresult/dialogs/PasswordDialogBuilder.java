package com.task.fbresult.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.task.fbresult.R;

public class PasswordDialogBuilder extends DialogBuilder {
    private EditText etPassword;
    private EditText etConfirmPassword;

    public PasswordDialogBuilder(Context context) {
        super(context);
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.password_dialog, null);
    }

    @Override
    void checkAndSetFields() {
        etPassword = mainWindow.findViewById(R.id.etNewPassword);
        etConfirmPassword = mainWindow.findViewById(R.id.etConfirmNewPassword);
    }

    @Override
    String[] getFieldValues() throws Exception {
        String newPass = etPassword.getText().toString();
        String confNewPass = etConfirmPassword.getText().toString();
        if (newPass.isEmpty() || confNewPass.isEmpty())
            throw new Exception(context.getString(R.string.empty_fields_ex));
        if (!newPass.equals(confNewPass))
            throw new Exception(context.getString(R.string.conf_pass_ex));
        if (newPass.length() < 6)
            throw new Exception(context.getString(R.string.pass_length_ex));
        return new String[]{newPass};
    }

    @Override
    void setData(String[] values) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(values[0]);
    }
}
