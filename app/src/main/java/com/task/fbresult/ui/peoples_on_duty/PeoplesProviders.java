package com.task.fbresult.ui.peoples_on_duty;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.task.fbresult.db.dao.DutyDao;
import com.task.fbresult.db.dao.PeopleOnDutyDao;
import com.task.fbresult.db.dao.PersonDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PeoplesProviders {

    public static List<PeopleOnDuty> getPersonOnDutyList(Duty duty){
        return new PeopleOnDutyDao().get(String.format(PeopleOnDutyDao.GET_PEOPLE_ON_DUTY_WITH_DUTY_ID, duty.getId()));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<PeopleAdapter.Item> getOrderedListOfPerson(Duty duty){
        List<PeopleOnDuty> list = getPersonOnDutyList(duty);
        return list.stream().map(PeoplesProviders::mapWith)
                .sorted(
                        Comparator
                                .comparingInt(e-> getMax(((PeopleAdapter.Item)e).state))
                                .thenComparing(e -> ((PeopleAdapter.Item)e).people.getFrom())
                ).collect(Collectors.toList());

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static PeopleAdapter.Item mapWith(PeopleOnDuty people){
        LocalDateTime now = LocalDateTime.now();
        PeopleAdapter.Item ans = new PeopleAdapter.Item(people, new HashSet<>());
        if(getUserAsPerson().equals(getPerson(people))){
            ans.state.add(PeopleOnDutyState.ME);
        }

        if(now.isBefore(people.getFrom())){
            ans.state.add(PeopleOnDutyState.IN_FUTURE);
            return ans;
        }

        if(now.isBefore(people.getTo())){
            ans.state.add(PeopleOnDutyState.IN_PROGRESS);
            return ans;
        }

        if(now.isAfter(people.getTo())){
            ans.state.add(PeopleOnDutyState.ENDED);
            return ans;
        }

        throw new RuntimeException("Strange behavior");
    }

    private static Person getUserAsPerson(){
        String login = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        String userQuery = String.format(PersonDao.GET_USER_WITH_LOGIN_QUERY,login);
        //return new PersonDao().get(String.format(PersonDao.GET_USER_WITH_ID, 128)).get(0);
        return new PersonDao().get(userQuery).get(0);
    }

    private static Person getPerson(PeopleOnDuty people){
        return new PersonDao().get(String.format(PersonDao.GET_USER_WITH_ID, people.getPersonId())).get(0);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private static int getMax(Set<PeopleOnDutyState> state){
        return state.stream().mapToInt(value -> value.order).min().orElse(-1);
    }

}
