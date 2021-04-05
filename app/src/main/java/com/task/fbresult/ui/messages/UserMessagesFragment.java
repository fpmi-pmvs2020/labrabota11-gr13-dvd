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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.task.fbresult.DutyActivity;
import com.task.fbresult.R;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.Person;
import com.task.fbresult.ui.adapters.MessageAdapter;
import com.task.fbresult.ui.adapters.TimedDutyAdapter;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.var;

@RequiresApi(api = Build.VERSION_CODES.O)
public class UserMessagesFragment extends Fragment {
    private static final String MESSAGES_KEY = "messages";

    private UserMessagesViewModel userMessagesViewModel;
    View root;

    Spinner spinner;
    RecyclerView messagesRecycler;

    List<MyMessage> messages;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        userMessagesViewModel =
                new ViewModelProvider(this).get(UserMessagesViewModel.class);
        root = inflater.inflate(R.layout.fragment_person_duties, container, false);
        Handler handler = new Handler(this.getActivity().getMainLooper(), msg -> {
            if (msg.what == 1) {
                Bundle data = msg.getData();
                messages =  data.getParcelableArrayList(MESSAGES_KEY);
            }
            configureRecycler();
            configureSpinner();
            showDutiesBelongsSpinnerPosition(spinner.getSelectedItemPosition());
            return true;
        });

        new Thread(()-> {
            Message message = handler.obtainMessage(1);
            Bundle data = new Bundle();
            var duties = loadMessages();
            data.putParcelableArrayList(MESSAGES_KEY, (ArrayList<? extends Parcelable>) duties);
            message.setData(data);
            handler.sendMessage(message);

        }).start();

        return root;
    }

    private void configureRecycler(){
        messagesRecycler = root.findViewById(R.id.recPersonDuties);
        messagesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        MessageAdapter messageAdapter = new MessageAdapter(getContext(), new ArrayList<>(), indexOf -> {
            TimedDutyAdapter adapter = (TimedDutyAdapter) messagesRecycler.getAdapter();
            Duty selectedDuty = Objects.requireNonNull(adapter).items.get(indexOf);
            loadDutyActivity(selectedDuty);
        });
        messagesRecycler.setAdapter(messageAdapter);
    }

    private void configureSpinner(){
        spinner = root.findViewById(R.id.spnDutyType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.duty_interval_types, R.layout.spinner_item);
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

    private void showDutiesBelongsSpinnerPosition(int spnPosition){
        MessageAdapter messageAdapter = (MessageAdapter) messagesRecycler.getAdapter();
        changeMessagesOnAdapterBelongsSpnPosition(messageAdapter,spnPosition);
        messageAdapter.notifyDataSetChanged();
    }

    private void changeMessagesOnAdapterBelongsSpnPosition(MessageAdapter messageAdapter, int spnPosition){
        switch (spnPosition){
            case 0:
                messageAdapter.setItems(getIncomingMessages());
                break;
            case 1:
                messageAdapter.setItems(getUserToOthersMessages());
                break;
        }
    }

    private List<MyMessage> getUserToOthersMessages(){
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return messages.stream()
                .filter(message -> message.getAuthorId().equals(currentPerson.getFirebaseId()))
                .collect(Collectors.toList());
    }

    private List<MyMessage> getIncomingMessages(){
        var currentPerson = FBUtils.getCurrentUserAsPerson();
        return messages.stream()
                .filter(message->message.getRecipientId().equals(currentPerson.getFirebaseId()))
                .collect(Collectors.toList());
    }

    private void loadDutyActivity(Duty duty){
        DutyActivity.getInstance(duty,getContext());
    }

    private List<MyMessage> loadMessages() {
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        List<MyMessage>myMessages = DAORequester.getPersonToOtherMessages(currentUser);
        List<MyMessage>incomingMessages = DAORequester.getPersonIncomingMessages(currentUser);
        myMessages.addAll(incomingMessages);
        myMessages.sort((first,second)->first.getFrom().compareTo(second.getFrom()));
        return myMessages;
    }
}