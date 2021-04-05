package com.task.fbresult.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBPersonDao;
import com.task.fbresult.dialogs.DialogBuilder;
import com.task.fbresult.dialogs.DialogBuilderFactory;
import com.task.fbresult.dialogs.DialogType;
import com.task.fbresult.dialogs.FieldsDisplay;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.ImgUtils;
import com.task.fbresult.util.LocalDateTimeHelper;

import java.io.IOException;
import java.time.LocalDate;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements FieldsDisplay {

    private ProfileViewModel profileViewModel;
    private TextView tvBirthDate;
    private TextView tvEmail;
    private EditText etAddress;
    private EditText etName;
    private EditText etPhone;
    private ImageView avatar;
    View root;

    private Person currentUser;

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

        avatar = root.findViewById(R.id.imgAvatar);
        avatar.setOnClickListener(v -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");

            startActivityForResult(photoPickerIntent, 1);
        });


        btnChangePass = root.findViewById(R.id.btnChangePassword);
        btnCommitChanges = root.findViewById(R.id.btnCommitChanges);
        btnEditData = root.findViewById(R.id.btnChangeUserData);

        btnEditData.setOnClickListener(v -> allowDataChanges());
        btnChangePass.setOnClickListener(v -> updatePassword());
        btnCommitChanges.setOnClickListener(v -> commitChanges());


        Drawable drawable = (btnEditData.getDrawable());
        if (drawable != null) {
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.dark_blue), PorterDuff.Mode.SRC_ATOP);
        }

        update();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            Uri chosenImageUri = data.getData();
            Bitmap mBitmap;
            try {
                mBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), chosenImageUri);
                avatar.setImageBitmap(mBitmap);
                currentUser.setAvatar(ImgUtils.getBitmapAsByteArray(mBitmap));
                updateCurrentUser();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void allowDataChanges() {
        btnCommitChanges.setVisibility(View.VISIBLE);
        setFieldsEnabled(true);
    }

    private void setFieldsEnabled(boolean flag) {
        etName.setEnabled(flag);
        etAddress.setEnabled(flag);
        etPhone.setEnabled(flag);
    }

    private void updatePassword() {
        DialogBuilderFactory builderFactory = new DialogBuilderFactory(getContext());
        DialogBuilder builder = builderFactory.getDialogBuilder(DialogType.PASSWORD_UPDATE);
        AlertDialog dialog = builder.build(null,
                null, this);
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.light_blue_oval_shape);
        dialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void commitChanges() {
        String fullFio = etName.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhone.getText().toString();
        if (!(checkFio(fullFio) && checkAddress(address) && checkPhone(phone)))
            return;

        currentUser.setAddress(address);
        currentUser.setFio(fullFio);
        currentUser.setTelephone(phone);
        updateCurrentUser();
        setFieldsEnabled(false);
        btnCommitChanges.setVisibility(View.GONE);
    }

    private boolean checkFio(String fullFio) {
        String[] fio = fullFio.split(" ");
        if (fio.length != 3) {
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

    private boolean checkAddress(String address) {
        if (address.isEmpty()) {
            showError(getString(R.string.addr_ex));
            return false;
        }
        return true;
    }

    private boolean checkPhone(String phone) {
        if (!phone.matches("\\+375[\\d]{9}")) {
            showError(getString(R.string.phone_ex));
            return false;
        }
        return true;
    }

    private void updateCurrentUser(){
        Completable.fromAction(this::updateUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        //Do your stuff here
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
    }

    private void updateUser(){
        new FBPersonDao().update(currentUser);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void update() {
        Completable.fromAction(() -> currentUser = FBUtils.getCurrentUserAsPerson())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                etPhone.setText(currentUser.getTelephone());
                etName.setText(currentUser.getFio());
                etAddress.setText(currentUser.getAddress());
                tvEmail.setText(currentUser.getLogin());
                tvBirthDate.setText(LocalDateTimeHelper.getFormattedDate(LocalDate.parse(currentUser.getBirthday())));
                byte[] avatarBytes = currentUser.getAvatar();
                if(avatarBytes!=null) {
                    Bitmap imgAvatar = ImgUtils.getByteArrayAsBitmap(avatarBytes);
                    avatar.setImageBitmap(imgAvatar);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {

            }
        });

    }

    private void showError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}