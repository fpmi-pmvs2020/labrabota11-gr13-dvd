package com.task.fbresult.ui.profile;

import android.app.AlertDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.R;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.dialogs.DialogBuilder;
import com.task.fbresult.dialogs.DialogBuilderFactory;
import com.task.fbresult.dialogs.DialogType;
import com.task.fbresult.dialogs.FieldsDisplay;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeHelper;

public class ProfileFragment extends Fragment implements FieldsDisplay {

    private ProfileViewModel profileViewModel;
    private TextView tvBirthDate;
    private TextView tvEmail;
    private EditText etAddress;
    private EditText etName;
    private EditText etPhone;
    View root;

    private Button btnChangePass;
    private ImageButton btnEditData;
    private Button btnCommitChanges;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);
        root = inflater.inflate(R.layout.fragment_profile, container, false);
        tvEmail = root.findViewById(R.id.tvUserEmail);
        tvBirthDate = root.findViewById(R.id.tvBirthDate);

        etAddress = root.findViewById(R.id.etUserAddress);
        etName = root.findViewById(R.id.etUserName);
        etPhone = root.findViewById(R.id.etPhone);

        btnChangePass = root.findViewById(R.id.btnChangePassword);
        btnCommitChanges = root.findViewById(R.id.btnCommitChanges);
        btnEditData = root.findViewById(R.id.btnChangeUserData);

        btnEditData.setOnClickListener(v-> allowDataChanges());
        btnChangePass.setOnClickListener(v -> updatePassword());
        btnCommitChanges.setOnClickListener(v->commitChanges());


        Drawable drawable = (btnEditData.getDrawable());
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
        }

        update();

        return root;
    }

    private void allowDataChanges(){
        btnCommitChanges.setVisibility(View.VISIBLE);
        setFieldsEnabled(true);
    }

    private void setFieldsEnabled(boolean flag){
        etName.setEnabled(flag);
        etAddress.setEnabled(flag);
        etPhone.setEnabled(flag);
    }

    private void updatePassword(){
        DialogBuilderFactory builderFactory = new DialogBuilderFactory(getContext());
        DialogBuilder builder = builderFactory.getDialogBuilder(DialogType.PASSWORD_UPDATE);
        AlertDialog dialog = builder.build(null,
                null,this);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.light_blue_oval_shape);
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void commitChanges(){
        String fullFio = etName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();
        if(!(checkFio(fullFio) && checkAddress(address) && checkPhone(phone)))
            return;
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        currentUser.setAddress(address);
        currentUser.setFio(fullFio);
        currentUser.setTelephone(phone);
        new PersonDao().update(currentUser);
        setFieldsEnabled(false);
        btnCommitChanges.setVisibility(View.GONE);
    }

    private boolean checkFio(String fullFio){
        String[] fio = fullFio.split(" ");
        if(fio.length!=3) {
            showError(getString(R.string.fio_ex));
            return false;
        }
        for (String s : fio) {
            if (s.isEmpty()) {
                showError(getString(R.string.empty_fio_ex));
                return false;
            }
        }
        return true;
    }

    private boolean checkAddress(String address){
        if(address.isEmpty()) {
            showError(getString(R.string.addr_ex));
            return false;
        }
        return true;
    }

    private boolean checkPhone(String phone){
        if(!phone.matches("\\+375[\\d]{9}")){
            showError(getString(R.string.phone_ex));
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void update() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        etPhone.setText(currentUser.getTelephone());
        etName.setText(currentUser.getFio());
        etAddress.setText(currentUser.getAddress());
        tvEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        tvBirthDate.setText(LocalDateTimeHelper.getFormattedDate(currentUser.getBirthday()));
    }

    private void showError(String message){
        Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
    }
}