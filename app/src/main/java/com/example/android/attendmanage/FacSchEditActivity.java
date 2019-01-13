package com.example.android.attendmanage;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.android.attendmanage.adapter.SpinnerArrayAdapter;
import com.example.android.attendmanage.pojos.FacSchedule;
import com.example.android.attendmanage.utilities.ExtraUtils;
import com.example.android.attendmanage.volley.VolleyTask;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ca.antonious.materialdaypicker.MaterialDayPicker;
import ca.antonious.materialdaypicker.SingleSelectionMode;

public class FacSchEditActivity extends AppCompatActivity {

    private int collId;
    private int lectId = -1;

    /**
     *  Day Picker
     */
    @BindView(R.id.fse_day_pick)
    MaterialDayPicker materialDayPicker;
    String day = null;
    /**
     * facUserId
     */
    @BindView(R.id.fse_fac_spin)
    Spinner facSpinner;
    private String facUserId = null;

    /**
     * semester
     */
    @BindView(R.id.fse_semester_spin)
    Spinner semesterSpinner;
    private String semester = null;


    /**
     * branch
     */
    @BindView(R.id.fse_branch_spin)
    Spinner branchSpinner;
    private String branch = null;
    private SpinnerArrayAdapter branchAdapter;

    /**
     * section
     */
    @BindView(R.id.fse_section_spin)
    Spinner sectionSpinner;
    private String section = null;
    private SpinnerArrayAdapter sectionAdapter;

    /**
     * subject
     */
    @BindView(R.id.fse_subject_spin)
    Spinner subjectSpinner;
    private String subject = null;
    private SpinnerArrayAdapter subjectAdapter;

    /**
     * lecture No.
     */
    @BindView(R.id.fse_lect_no_tv)
    TextView lectNoTv;
    @BindView(R.id.fse_plus_lect_button)
    Button plusButton;
    @BindView(R.id.fse_minus_lect_button)
    Button minusButton;
    int lectNo=1;

    /**
     * lecture time
     */
    @BindView(R.id.fse_lect_start_input)
    TextInputLayout lectStartIn;

    @BindView(R.id.fse_lect_end_input)
    TextInputLayout lectEndIn;


    @OnClick(R.id.fse_done_fab)
    void saveFacSchedule() {

        String lectEnd = Objects.requireNonNull(lectEndIn.getEditText()).getText().toString().trim();
        String lectStart = Objects.requireNonNull(lectStartIn.getEditText()).getText().toString().trim();

        FacSchedule facSch = new FacSchedule(Integer.parseInt(semester), branch, section, lectNo,
                subject, lectStart, lectEnd, facUserId, day);
        Gson gson = new Gson();
        String facSchJson = gson.toJson(facSch);

        VolleyTask.saveFacSch(this, collId, lectId, facSchJson);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fac_sch_edit);
        ButterKnife.bind(this);

        collId = SharedPrefManager.getInstance(this).getCollId();

        int mode = getIntent().getIntExtra(ExtraUtils.EXTRA_EDIT_MODE, ExtraUtils.MODE_NEW);

        materialDayPicker.setSelectionMode(SingleSelectionMode.create());
        materialDayPicker.setBackgroundColor(Color.WHITE);
        materialDayPicker.setDayPressedListener((weekday, isSelected) -> {
                if (isSelected)
                    day = weekday.toString();
                else
                    day = null;
        });

        if (mode == ExtraUtils.MODE_UPDATE) {

            FacSchedule facSch = getIntent().getParcelableExtra(ExtraUtils.EXTRA_FAC_SCH_OBJ);
            lectId = facSch.getLectId();
            day = facSch.getDay();
            facUserId = facSch.getFacUserId();
            semester = String.valueOf(facSch.getSem());
            branch = facSch.getBName();
            section = facSch.getSection();
            subject = facSch.getSubName();
            lectNo = facSch.getLectNo();

            materialDayPicker.setSelectedDays(MaterialDayPicker.Weekday.valueOf(day));
            Objects.requireNonNull(lectStartIn.getEditText())
                    .setText(facSch.getLectStartTime());
            Objects.requireNonNull(lectEndIn.getEditText())
                    .setText(facSch.getLectEndTime());
        }

        VolleyTask.getFacUserIds(this, collId, jObj -> {
            try {
                JSONArray facIdJsonArr = jObj.getJSONArray("fac_user_ids");
                List<String> facIdList = new ArrayList<>();
                facIdList.add("Faculty");

                for (int i = 0; i < facIdJsonArr.length(); i++) {
                    facIdList.add(facIdJsonArr.getString(i));
                }

                String[] facIdArr = facIdList.toArray(new String[0]);
                SpinnerArrayAdapter facIdAdapter = new SpinnerArrayAdapter(FacSchEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        facIdArr);
                facSpinner.setAdapter(facIdAdapter);

                if (facUserId != null) {
                    for (int i = 0; i < facIdArr.length; i++) {
                        if (facIdArr[i].equals(facUserId)) {
                            facSpinner.setSelection(i);
                            break;
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        setupSemesterSpinner();
        VolleyTask.setupBranchSpinner(this, collId, jObj -> {
            try {
                JSONArray brJsonArr = jObj.getJSONArray("branch_names");
                List<String> brList = new ArrayList<>();
                brList.add("Branch");
                for(int i=0; i<brJsonArr.length(); i++) {
                    brList.add(brJsonArr.getString(i));
                }
                String[] brArr = brList.toArray(new String[0]);
                branchAdapter = new SpinnerArrayAdapter(FacSchEditActivity.this,
                        android.R.layout.simple_spinner_dropdown_item,
                        brArr);
                branchSpinner.setAdapter(branchAdapter);

                if (branch != null) {
                    for (int i = 0; i < brArr.length; i++) {
                        if (brArr[i].equals(branch)) {
                            branchSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
        ExtraUtils.emptySectionSpinner(this, sectionSpinner);
        ExtraUtils.emptySubjectSpinner(this, subjectSpinner);
        setupLectureChooser();
        setupTimePicker(lectStartIn);
        setupTimePicker(lectEndIn);

        facSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    facUserId = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    semester = (String) parent.getItemAtPosition(pos);
                    refreshSectionsSpinner();
                    refreshSubjectSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        branchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0) {
                    branch = (String) parent.getItemAtPosition(pos);
                    refreshSectionsSpinner();
                    refreshSubjectSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sectionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    section = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long l) {
                if (pos != 0)
                    subject = (String) parent.getItemAtPosition(pos);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setupTimePicker(TextInputLayout inputLayout) {
        Objects.requireNonNull(inputLayout.getEditText()).setOnClickListener(view -> {
            Calendar mcurrentTime = Calendar.getInstance();
            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
            int minute = mcurrentTime.get(Calendar.MINUTE);

            TimePickerDialog mTimePicker = new TimePickerDialog(FacSchEditActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                    String timeString = (selectedMinute < 10) ?  "%d:0%d:00" :  "%d:%d:00";

                    if (selectedHour<10) timeString = "0"+ timeString;

                    Objects.requireNonNull(inputLayout.getEditText())
                            .setText(String.format(Locale.US, timeString, selectedHour,
                                    selectedMinute));
                }
            }, hour, minute, false);
            mTimePicker.setTitle("Select Time");
            mTimePicker.show();
        });
    }

    private void setupSemesterSpinner() {
        String[] semArr = getResources().getStringArray(R.array.semester_array);
        SpinnerArrayAdapter semesterAdapter = new SpinnerArrayAdapter(FacSchEditActivity.this,
                android.R.layout.simple_spinner_dropdown_item,
                semArr);
        semesterSpinner.setAdapter(semesterAdapter);

        if (semester != null) {
            for (int i = 0; i < semArr.length; i++) {
                if (semArr[i].equals(semester)) {
                    semesterSpinner.setSelection(i);
                    break;
                }
            }
        }
    }

    /**
     * setup lecture chooser
     */
    private void setupLectureChooser() {
        lectNoTv.setText(String.valueOf(lectNo));
        minusButton.setEnabled(false);

        plusButton.setOnClickListener(v -> {

            if (lectNo < 8) {
                lectNoTv.setText(String.valueOf(++lectNo));
                plusButton.setEnabled(true);
            } else {
                plusButton.setEnabled(false);
            }
            minusButton.setEnabled(true);
        });

        minusButton.setOnClickListener(v -> {
            if (lectNo > 1) {
                lectNoTv.setText(String.valueOf(--lectNo));
                minusButton.setEnabled(true);
            } else {
                minusButton.setEnabled(false);
            }
            plusButton.setEnabled(true);
        });
    }

    private void refreshSectionsSpinner() {
        if (semester != null && branch != null) {
            final List<String> secList = new ArrayList<>();
            secList.add("Section");
            VolleyTask.getSections(FacSchEditActivity.this, branch,
                    semester, collId, jObj -> {

                        try {
                            JSONArray jsonArray = jObj.getJSONArray("sections");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                secList.add(jsonArray.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String[] secArr = secList.toArray(new String[0]);
                        SpinnerArrayAdapter sectionAdapter =
                                new SpinnerArrayAdapter(FacSchEditActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        secArr);
                        sectionSpinner.setAdapter(sectionAdapter);

                        if (section != null) {
                            for (int i = 0; i < secArr.length; i++) {
                                if (secArr[i].equals(section)) {
                                    sectionSpinner.setSelection(i);
                                    break;
                                }
                            }
                        }

                    });
        } else
            ExtraUtils.emptySectionSpinner(this, sectionSpinner);
    }

    private void refreshSubjectSpinner() {
        if (semester != null && branch != null) {
            final List<String> subList = new ArrayList<>();
            subList.add("Subject");
            VolleyTask.getSubjectNames(FacSchEditActivity.this, branch,
                    semester, collId, jObj -> {

                        try {
                            JSONArray jsonArray = jObj.getJSONArray("subject_names");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                subList.add(jsonArray.getString(i));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String[] subArr = subList.toArray(new String[0]);
                        SpinnerArrayAdapter subjectAdapter =
                                new SpinnerArrayAdapter(FacSchEditActivity.this,
                                        android.R.layout.simple_spinner_dropdown_item,
                                        subArr);
                        subjectSpinner.setAdapter(subjectAdapter);

                        if (subject != null) {
                            for (int i = 0; i < subArr.length; i++) {
                                if (subArr[i].equals(subject)) {
                                    subjectSpinner.setSelection(i);
                                    break;
                                }
                            }
                        }

                    });
        } else
            ExtraUtils.emptySubjectSpinner(this, subjectSpinner);
    }

}
