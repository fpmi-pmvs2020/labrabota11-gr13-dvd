package com.task.fbresult.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.task.fbresult.R;

public abstract class DialogBuilder {
    protected Context context;

    protected View mainWindow;

    public DialogBuilder(Context context){
        this.context = context;
    }

    abstract View findMainWindow();
    abstract void checkAndSetFields();
    abstract String[] getFieldValues() throws Exception;
    abstract void setData(String[]values);

    public AlertDialog build(String title, String message,
                             FieldsDisplay fieldsDisplay){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        mainWindow = findMainWindow();
        dialogBuilder.setView(mainWindow);
        dialogBuilder.setNegativeButton(
                R.string.btn_neg_text, (dialog, which) -> dialog.dismiss());

        checkAndSetFields();

        dialogBuilder.setPositiveButton(R.string.btn_pos_text,(dialog, which) -> {
            String[]values;
            try{
                values = getFieldValues();
            } catch (Exception e) {
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                return;
            }
            setData(values);
            fieldsDisplay.update();
        });

        return dialogBuilder.create();
    }
}
