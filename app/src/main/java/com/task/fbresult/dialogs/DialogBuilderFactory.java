package com.task.fbresult.dialogs;

import android.content.Context;

public class DialogBuilderFactory {
    private final Context context;

    public DialogBuilderFactory(Context context) {
        this.context = context;
    }

    public DialogBuilder getDialogBuilder(DialogType type) {
        switch (type) {
            case PASSWORD_UPDATE:
                return new PasswordDialogBuilder(context);
            case MAKE_EXCHANGE:
                return new ExchangeDialogBuilder(context);
            case EXCHANGE_ON_CURRENT:
                return new ExchangeOnCurrentDialogBuilder(context);
            default:
                return null;
        }
    }
}
