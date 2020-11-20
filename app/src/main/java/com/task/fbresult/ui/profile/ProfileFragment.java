package com.task.fbresult.ui.profile;

import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.task.fbresult.R;
import com.task.fbresult.dialogs.DialogBuilder;
import com.task.fbresult.dialogs.DialogBuilderFactory;
import com.task.fbresult.dialogs.DialogType;
import com.task.fbresult.dialogs.FieldsDisplay;

public class ProfileFragment extends Fragment implements FieldsDisplay {

    private ProfileViewModel profileViewModel;
    private TextView tvName;
    private TextView tvEmail;
    private EditText etPass;

    private Button btnChangeEmail;
    private Button btnChangePass;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        tvName = root.findViewById(R.id.tvUserName);
        tvEmail = root.findViewById(R.id.tvUserEmail);

        btnChangeEmail = root.findViewById(R.id.btnChangeEmail);
        btnChangePass = root.findViewById(R.id.btnChangePassword);

        btnChangeEmail.setOnClickListener(view->updateEmail());
        btnChangePass.setOnClickListener(v -> updatePassword());

        return root;
    }

    private void updateEmail(){
        DialogBuilderFactory builderFactory = new DialogBuilderFactory(getContext());
        DialogBuilder builder = builderFactory.getDialogBuilder(DialogType.EMAIL_UPDATE);
        builder.build(getString(R.string.email_dialog_title),
                getString(R.string.email_dialog_msg),this);
    }

    private void updatePassword(){
        DialogBuilderFactory builderFactory = new DialogBuilderFactory(getContext());
        DialogBuilder builder = builderFactory.getDialogBuilder(DialogType.PASSWORD_UPDATE);
        builder.build(getString(R.string.pass_dialog_title),
                getString(R.string.pass_dialog_msg),this);
    }

    @Override
    public void update() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        tvName.setText(user.getDisplayName());
        tvEmail.setText(user.getEmail());
    }
}