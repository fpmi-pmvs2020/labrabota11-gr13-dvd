package com.task.fbresult.ui.people_on_duty;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeInterval;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PeopleProviders {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item> getOrderedListOfPerson(Duty duty){
        List<PeopleOnDuty> list = DAORequester.getPeopleOnDuty(duty);
        List<com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item> items = list.stream().map(PeopleProviders::mapWith)
                .sorted(
                        Comparator
                                .comparingInt(e -> getMax(((com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item) e).state))
                                .thenComparing(e -> ((com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item) e).people.getFrom())
                ).collect(Collectors.toList());
        Set<com.task.fbresult.ui.people_on_duty.PeopleOnDutyState> set = new HashSet<>();
        set.add(com.task.fbresult.ui.people_on_duty.PeopleOnDutyState.TITLE);
        items.add(0, new com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item(new PeopleOnDuty(0,0,0, LocalDateTime.now(),LocalDateTime.now()),set));
        return items;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item mapWith(PeopleOnDuty people){
        com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item ans = new com.task.fbresult.ui.people_on_duty.PeopleAdapter.Item(people, new HashSet<>());
        Person currentUser = FBUtils.getCurrentUserAsPerson();
        if(currentUser.getId() == people.getPersonId()){
            ans.state.add(com.task.fbresult.ui.people_on_duty.PeopleOnDutyState.ME);
        }

        com.task.fbresult.ui.people_on_duty.PeopleOnDutyState state = getPeopleOnDutyState(people);
        ans.state.add(state);
        return ans;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PeopleOnDutyState getPeopleOnDutyState(PeopleOnDuty people){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTimeInterval interval = new LocalDateTimeInterval(people.getFrom(),people.getTo());
        int pointPosition = interval.getPointPosition(now);
        return getDutyState(pointPosition);
    }

    private static PeopleOnDutyState getDutyState(int position){
        switch (position){
            case -1:
                return PeopleOnDutyState.IN_FUTURE;
            case 0:
                return PeopleOnDutyState.IN_PROGRESS;
            case 1:
                return PeopleOnDutyState.ENDED;
            default:
                return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int getMax(Set<PeopleOnDutyState> state){
        return state.stream().mapToInt(value -> value.order).min().orElse(-1);
    }

}
