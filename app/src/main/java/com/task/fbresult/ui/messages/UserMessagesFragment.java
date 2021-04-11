package com.task.fbresult.ui.messages;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.MessageActivity;
import com.task.fbresult.R;
import com.task.fbresult.model.MessageState;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.MessageAdapter;
import com.task.fbresult.ui.adapters.NodeListener;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.var;

import static android.os.Looper.getMainLooper;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserMessagesFragment extends Fragment implements NodeListener {
    private static final String MESSAGES_KEY = "messages";

    View root;

    Spinner spinner;
    RecyclerView messagesRecycler;

    List<MyMessage> messages;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_person_messages, container, false);
        thread.start();
        return root;

    }

    private final Handler messageHandler = new Handler(getMainLooper(), msg -> {
        if (msg.what == 1) {
            Bundle data = msg.getData();
            messages = data.getParcelableArrayList(MESSAGES_KEY);
        }
        configureRecycler();
        configureSpinner();

        showDutiesBelongsSpinnerPosition(spinner.getSelectedItemPosition());
        return true;
    });

    private void configureRecycler() {
        messagesRecycler = root.findViewById(R.id.recPersonMessages);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageAdapter messageAdapter = new MessageAdapter(getContext(), new ArrayList<>(), this);
        messagesRecycler.setAdapter(messageAdapter);
    }


    private void configureSpinner() {
        spinner = root.findViewById(R.id.spnMessageType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.message_types, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                showDutiesBelongsSpinnerPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //unreachable
            }
        });
    }

    private void showDutiesBelongsSpinnerPosition(int spnPosition) {
        MessageAdapter messageAdapter = (MessageAdapter) messagesRecycler.getAdapter();
        changeMessagesOnAdapterBelongsSpnPosition(messageAdapter, spnPosition);
        messageAdapter.notifyDataSetChanged();
    }

    private void changeMessagesOnAdapterBelongsSpnPosition(MessageAdapter messageAdapter, int spnPosition) {
        switch (spnPosition) {
            case 0:
                messageAdapter.setItems(getUserToOthersMessages());
                break;
            case 1:
                messageAdapter.setItems(getIncomingNotAnsweredMessages());
                break;
            case 2:
                messageAdapter.setItems(getUserToOthersNotAnsweredMessages());
        }
    }

    private List<MyMessage> getIncomingNotAnsweredMessages() {
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return messages.stream()
                .filter(message -> message.getRecipientIntervalData().getPersonId()
                        .equals(currentPerson.getFirebaseId()))
                .filter(message ->
                        message.getMessageState() == MessageState.SENT
                                || message.getMessageState() == MessageState.READ)
                .collect(Collectors.toList());
    }

    private List<MyMessage> getUserToOthersNotAnsweredMessages() {
        var messages = getUserToOthersMessages();
        return messages.stream()
                .filter(myMessage ->
                        myMessage.getMessageState() == MessageState.READ
                                || myMessage.getMessageState() == MessageState.SENT)
                .collect(Collectors.toList());
    }

    private List<MyMessage> getUserToOthersMessages() {
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return messages.stream()
                .filter(message -> message.getAuthorIntervalData().getPersonId()
                        .equals(currentPerson.getFirebaseId()))
                .collect(Collectors.toList());
    }


    Thread thread = new Thread(() -> {
        Message message = messageHandler.obtainMessage(1);
        Bundle data = new Bundle();
        var mess = loadMessages();
        data.putParcelableArrayList(MESSAGES_KEY, (ArrayList<? extends Parcelable>) mess);
        message.setData(data);
        messageHandler.sendMessage(message);

    });

    @Override
    public void onResume() {
        super.onResume();
        if(messagesRecycler!=null)
            messagesRecycler.getAdapter().notifyDataSetChanged();
    }

    private List<MyMessage> loadMessages() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        List<MyMessage> myMessages = DAORequester.getPersonToOtherMessages(currentUser);
        List<MyMessage> incomingMessages = DAORequester.getPersonIncomingMessages(currentUser);
        myMessages.addAll(incomingMessages);
        return myMessages;
    }

    @Override
    public void nodeClicked(int indexOf) {
        MessageAdapter adapter = (MessageAdapter) messagesRecycler.getAdapter();
        MyMessage selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
        if(selectedDuty.getMessageState()!=MessageState.ACCEPTED
                && selectedDuty.getMessageState() != MessageState.DECLINED) {
            loadMessageActivity(selectedDuty);
        }
    }

    private void loadMessageActivity(MyMessage message) {
        MessageActivity.getInstance(message, getContext());
    }

}