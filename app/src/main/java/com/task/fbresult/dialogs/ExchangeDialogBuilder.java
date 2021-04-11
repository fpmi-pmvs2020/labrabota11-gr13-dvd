package com.task.fbresult.dialogs;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.edmodo.rangebar.RangeBar;
import com.task.fbresult.R;
import com.task.fbresult.db.fbdao.FBMessageDao;
import com.task.fbresult.model.Duty;
import com.task.fbresult.model.DutyIntervalData;
import com.task.fbresult.model.MyMessage;
import com.task.fbresult.model.PeopleOnDuty;
import com.task.fbresult.model.Person;
import com.task.fbresult.util.DAORequester;
import com.task.fbresult.util.DutyUtils;
import com.task.fbresult.util.FBUtils;
import com.task.fbresult.util.LocalDateTimeInterval;
import com.task.fbresult.util.MessageSender;
import com.task.fbresult.util.MessageUtils;
import com.task.fbresult.util.SpinnerUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.var;

public class ExchangeDialogBuilder extends DialogBuilder {

    protected Duty currentDuty;
    private Spinner spMyDuty;
    private Spinner spGoalPerson;
    private List<PersonWithDuty> persons;
    private List<PeopleOnDuty> personDuties;
    protected List<PeopleOnDuty> goalPeopleOnDuties;

    protected SeekBarConfiguration myDutySeekBarConfiguration;
    protected SeekBarConfiguration otherDutySeekBarConfiguration;


    public ExchangeDialogBuilder(Context context, Duty currentDuty) {
        super(context);
        this.currentDuty = currentDuty;
    }

    @Override
    View findMainWindow() {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.exchange_dialog, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void checkAndSetFields() {
        spGoalPerson = mainWindow.findViewById(R.id.spExchangeGoalPerson);
        spMyDuty = mainWindow.findViewById(R.id.spMyDutyToChange);
        configureSeekBars();
        fillGoalPersonSpinner();
        fillMyDuties();
    }

    protected void setOtherDutyFieldsEnabled(boolean enabled) {
        spGoalPerson.setEnabled(enabled);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void configureSeekBars() {
        TextView tvMyDutyFrom = mainWindow.findViewById(R.id.tvMyDutyFrom);
        TextView tvMyDutyTo = mainWindow.findViewById(R.id.tvMyDutyTo);
        RangeBar myDutyRangeSeekBar = mainWindow.findViewById(R.id.sbMyDutyTime);
        myDutySeekBarConfiguration = new SeekBarConfiguration(myDutyRangeSeekBar, tvMyDutyFrom, tvMyDutyTo);
        TextView tvOtherDutyFrom = mainWindow.findViewById(R.id.tvOtherDutyFrom);
        TextView tvOtherDutyTo = mainWindow.findViewById(R.id.tvOtherDutyTo);
        RangeBar otherDutyRangeSeekBar = mainWindow.findViewById(R.id.sbOtherDutyTime);
        otherDutySeekBarConfiguration = new SeekBarConfiguration(otherDutyRangeSeekBar, tvOtherDutyFrom, tvOtherDutyTo);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void fillGoalPersonSpinner() {
        goalPeopleOnDuties = DAORequester.getPeopleOnDuty(currentDuty);
        persons = goalPeopleOnDuties.stream()
                .map(PersonWithDuty::new)
                .collect(Collectors.toList());

        List<String> personWithTime = persons.stream()
                .map(this::getNameAndDutyTime)
                .collect(Collectors.toList());

        spGoalPerson.setAdapter(SpinnerUtils.getStringAdapterOf(context, personWithTime));
        configureSpinner(spGoalPerson, goalPeopleOnDuties, otherDutySeekBarConfiguration);
    }

    protected void configureSpinner(Spinner spinner, List<PeopleOnDuty> peopleOnDuties, SeekBarConfiguration configuration) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                var selectedPeopleOnDuty = peopleOnDuties.get(position);
                configuration.updateWithDiapason(selectedPeopleOnDuty);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String getNameAndDutyTime(PersonWithDuty personWithDuty) {
        String surnameWithInitials = personWithDuty.person.getSurnameWithInitials();
        String from = personWithDuty.peopleOnDuty.getFrom().split("T")[1];
        String to = personWithDuty.peopleOnDuty.getTo().split("T")[1];
        return surnameWithInitials + " " + from + " - " + to;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void fillMyDuties() {
        Person me = FBUtils.getCurrentUserAsPerson();
        personDuties = DAORequester.getFuturePeopleOnDutiesOfPerson(me);
        List<String> dutyDayAndTime = personDuties.stream()
                .map(this::getDutyDayAndTime)
                .collect(Collectors.toList());

        spMyDuty.setAdapter(SpinnerUtils.getStringAdapterOf(context, dutyDayAndTime));
        configureSpinner(spMyDuty, personDuties, myDutySeekBarConfiguration);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected String getDutyDayAndTime(PeopleOnDuty duty) {
        String date = duty.fromAsLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE);
        String from = duty.getFrom().split("T")[1];
        String to = duty.getTo().split("T")[1];
        return date + " " + from + " - " + to;
    }

    @Override
    String[] getFieldValues() throws Exception {
        //ignored
        return new String[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    void setData(String[] values) {
        PersonWithDuty goalPersonAndDuty = persons.get((int) spGoalPerson.getSelectedItemId());
        PeopleOnDuty myDuty = personDuties.get((int) spMyDuty.getSelectedItemId());

        tryToSendMessageWith(myDuty, goalPersonAndDuty.peopleOnDuty);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void tryToSendMessageWith(PeopleOnDuty meOnDuty, PeopleOnDuty goalPeopleOnDuty) {
        try {
            var myDutyInterval = myDutySeekBarConfiguration.getSelectedInterval();
            var otherDutyInterval = otherDutySeekBarConfiguration.getSelectedInterval();
            MessageSender messageSender = new MessageSender(context, meOnDuty, goalPeopleOnDuty);
            messageSender.tryToSendMessageWith(myDutyInterval, otherDutyInterval);
        } catch (Exception e) {
            Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @RequiredArgsConstructor
    static class PersonWithDuty {
        public Person person;
        public PeopleOnDuty peopleOnDuty;

        public PersonWithDuty(PeopleOnDuty peopleOnDuty) {
            this.peopleOnDuty = peopleOnDuty;
            person = DAORequester.getPersonInPeopleOnDuty(peopleOnDuty);
        }
    }

    @RequiredArgsConstructor
    protected final static class SeekBarConfiguration {
        public RangeBar rangeSeekBar;
        private TextView tvFrom;
        private TextView tvTo;
        private LocalDateTimeInterval interval;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public SeekBarConfiguration(RangeBar rangeSeekBar, TextView tvFrom, TextView tvTo) {
            configureRangeSeekBar(rangeSeekBar);
            this.rangeSeekBar = rangeSeekBar;
            this.tvFrom = tvFrom;
            this.tvTo = tvTo;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public void updateWithDiapason(PeopleOnDuty peopleOnDuty) {
            interval = new LocalDateTimeInterval(
                    peopleOnDuty.fromAsLocalDateTime(),
                    peopleOnDuty.toAsLocalDateTime()
            );

            int hoursNumber = interval.getHoursBetween();
            rangeSeekBar.setTickCount(hoursNumber + 1);
            updateTvWithValues(0, hoursNumber);
        }


        @RequiresApi(api = Build.VERSION_CODES.O)
        private void configureRangeSeekBar(RangeBar rangeSeekBar) {
            rangeSeekBar.setOnRangeBarChangeListener((bar, minValue, maxValue) -> {
                if (minValue == maxValue) {
                    if (maxValue == interval.getHoursBetween())
                        rangeSeekBar.setThumbIndices(--minValue, maxValue);
                    else
                        rangeSeekBar.setThumbIndices(minValue, ++maxValue);
                }

                updateTvWithValues(minValue, maxValue);
            });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void updateTvWithValues(int minValue, int maxValue) {
            var fromHours = interval.getStart().plusHours(minValue).getHour();
            if (interval.getEnd().getMinute() != 0)
                maxValue--;
            var toHours = fromHours + (maxValue - minValue);
            tvFrom.setText(getFormattedTime(fromHours, interval.getStart().getMinute()));
            tvTo.setText(getFormattedTime(toHours, interval.getEnd().getMinute()));
        }

        private String getFormattedTime(int hours, int minutes) {
            String strMinutes = minutes < 10 ? "0" + minutes : String.valueOf(minutes);
            return hours + ":" + strMinutes;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public LocalDateTimeInterval getSelectedInterval() {
            int minValue = rangeSeekBar.getLeftIndex();
            int maxValue = rangeSeekBar.getRightIndex();
            var newStartLocalDateTime = interval.getStart().plusHours(minValue);
            var newEndLocalDateTime = interval.getEnd().minusHours(interval.getHoursBetween() - maxValue);
            if(newEndLocalDateTime.getMinute() == 0)
                newEndLocalDateTime = newEndLocalDateTime.minusMinutes(1);
            return new LocalDateTimeInterval(newStartLocalDateTime, newEndLocalDateTime);
        }
    }
}
