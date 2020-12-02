package com.task.fbresult.dialogs;

import android.content.Context;

public class DialogBuilderFactory {
    private final Context context;

    public DialogBuilderFactory(Context context){
        this.context = context;
    }

    public DialogBuilder getDialogBuilder(DialogType type){
        switch (type){
            case PASSWORD_UPDATE: return new PasswordDialogBuilder(context);
            default: return null;
        }
    }
}
