package com.example.allinoneapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPickerListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.allinoneapp.MainActivity.database;

public class ServiceDetailsForm extends AppCompatActivity {

    private ArrayList<ServiceDetails> serviceDetails;
    private LinearLayout myLayout;
    private Integer questionIndex;
    private DatePickerDialog datePickerDialog;
    private Button btnDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_details_form);

        String[] Questions = { "How big is your house?","Do you ned cleaning tools and materials",
                               "What type of add-ons would you need?","Special intructions (if any)"
                               ,"When do you need it?","Request Summary"};
        String[] Descriptions = {"","Cleaning tools: Mop, Broom, Rags, Cleaning Solution etc.",
                                 "","","",""};
        String[] ControlTypes = { "radiobutton","radiobutton", "numberpicker","remarks",
                                  "datepicker","summary"};

        List<String> Choices1 = new ArrayList<String>();
        Choices1.add("20 - 60 sqm");
        Choices1.add("60-100 sqm");
        Choices1.add("more than 100 sqm");

        List<String> Choices2 = new ArrayList<String>();
        Choices2.add("Yes (Additional 700php)");
        Choices2.add("No, i will provide them");

        List<String> Choices3 = new ArrayList<String>();
        Choices3.add("Decluttering");
        Choices3.add("Clothes Folding");
        Choices3.add("Ironing");
        Choices3.add("Refrigerator Cleaning & Defrosting");

        List<String> ChoicesAnswer = new ArrayList<String>();
        ChoicesAnswer.add("0");
        ChoicesAnswer.add("0");
        ChoicesAnswer.add("0");
        ChoicesAnswer.add("0");

        questionIndex = 0;

        serviceDetails = new ArrayList<>();
        for (int i =0;i<Questions.length; i++){

            if (i==0){
                ServiceDetails newModel = new ServiceDetails(Questions[i],Descriptions[i],
                                                           ControlTypes[i], Choices1, "",null);
                serviceDetails.add(newModel);
            }
            if (i==1){
                ServiceDetails newModel = new ServiceDetails(Questions[i],Descriptions[i],
                        ControlTypes[i], Choices2, "",null);
                serviceDetails.add(newModel);
            }
            if (i==2){
                ServiceDetails newModel = new ServiceDetails(Questions[i],Descriptions[i],
                        ControlTypes[i], Choices3, "",ChoicesAnswer);
                serviceDetails.add(newModel);
            }

            if (i>2){
                ServiceDetails newModel = new ServiceDetails(Questions[i],Descriptions[i],
                        ControlTypes[i], null, "",null);
                serviceDetails.add(newModel);
            }

            Log.i("Questions", Questions[i]);

        }

        myLayout = (LinearLayout) findViewById(R.id.servicedetails_layout);

        GenerateForm();


    }

    private  void GenerateForm(){

        myLayout.removeAllViews();
        TextView QuestionText = new TextView(ServiceDetailsForm.this);
        QuestionText.setText(serviceDetails.get(questionIndex).getQuestion());
        QuestionText.setTextSize(25);
        QuestionText.setTextColor(getResources().getColor(R.color.gradEnd));
        myLayout.addView(QuestionText);

        LinearLayout choicesLayout = new LinearLayout(ServiceDetailsForm.this);
        choicesLayout.setOrientation(LinearLayout.VERTICAL);
        choicesLayout.setVerticalGravity(Gravity.NO_GRAVITY);
        choicesLayout.setPadding(10, 100, 0,50);

        if (serviceDetails.get(questionIndex).getControlType() =="radiobutton"){

            RadioGroup radioGroup = new RadioGroup(ServiceDetailsForm.this);
              for (int i = 0;i<serviceDetails.get(questionIndex).Choices.size(); i++){
                RadioButton radioButton = new RadioButton(ServiceDetailsForm.this);
                radioButton.setText(serviceDetails.get(questionIndex).Choices.get(i).toString());
                radioGroup.addView(radioButton);
            }

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb= findViewById(checkedId);
                    String radioText=rb.getText().toString();
                    serviceDetails.get(questionIndex).setAnswer(radioText);
                }
            });

            choicesLayout.addView(radioGroup);
            myLayout.addView(choicesLayout);

        }

        if (serviceDetails.get(questionIndex).getControlType() == "numberpicker"){

              for (int i = 0;i<serviceDetails.get(questionIndex).Choices.size(); i++){

                LinearLayout layout = new LinearLayout(ServiceDetailsForm.this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);

                layout.setPadding(10, 50, 10, 50);

                TextView pickerText = new TextView(ServiceDetailsForm.this);
                pickerText.setText(serviceDetails.get(questionIndex).Choices.get(i).toString());

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0,0,50,0);

                pickerText.setLayoutParams(params);
                layout.addView(pickerText);

                ScrollableNumberPicker numberPicker = new ScrollableNumberPicker(ServiceDetailsForm.this);
                numberPicker.setBackground(getResources().getDrawable(R.drawable.number_picker_bg_color));
                numberPicker.setMaxValue(10);
                numberPicker.setMinValue(0);
                numberPicker.setStepSize(1);
                numberPicker.setScrollBarSize(20);
                numberPicker.setId(i);

                numberPicker.setListener(new ScrollableNumberPickerListener() {
                    @Override
                    public void onNumberPicked(int value) {
                        serviceDetails.get(questionIndex).setChoicesAnswer(String.valueOf(value), numberPicker.getId());
                    }
                });

                layout.addView(numberPicker);
                myLayout.addView(layout);

            }

        }

        if (serviceDetails.get(questionIndex).getControlType() == "remarks"){

            serviceDetails.get(questionIndex).setAnswer("none");

            EditText remarks = new EditText(ServiceDetailsForm.this);
            remarks.setSingleLine(false);
            remarks.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            remarks.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            remarks.setMaxLines(10);
            remarks.setGravity(Gravity.CENTER);
            remarks.setHint("Leave this blank if none");
            remarks.setVerticalScrollBarEnabled(true);
            remarks.setMovementMethod(ScrollingMovementMethod.getInstance());
            remarks.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            remarks.setBackground(getResources().getDrawable(R.drawable.number_picker_bg_color));
            remarks.setTextColor(getResources().getColor(R.color.black));

            remarks.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { }

                @Override
                public void afterTextChanged(Editable s) {
                    serviceDetails.get(questionIndex).setAnswer(s.toString());
                }
            });

            choicesLayout.addView(remarks);
            myLayout.addView(choicesLayout);

        }

        if (serviceDetails.get(questionIndex).getControlType() == "datepicker"){

            serviceDetails.get(questionIndex).setAnswer(getTodaysDate());

            LinearLayout layout = new LinearLayout(ServiceDetailsForm.this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
            layout.setPadding(5, 5, 5, 5);

            TextView pickerText = new TextView(ServiceDetailsForm.this);
            pickerText.setHint("Select preferred date: ");
            pickerText.setTextColor(getResources().getColor(R.color.black));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,50,0);

            pickerText.setLayoutParams(params);

            this.initDatePicker();

             btnDatePicker  = new Button(ServiceDetailsForm.this);
             btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_date_range_24, 0);
             btnDatePicker.setTextColor(getResources().getColor(R.color.gradEnd));
             btnDatePicker.setText(getTodaysDate());
             btnDatePicker.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_date_range_24, 0, 0, 0);
             btnDatePicker.setId(questionIndex);

             btnDatePicker.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     datePickerDialog.show();
                 }
             });

             layout.addView(pickerText);
             layout.addView(btnDatePicker);
             choicesLayout.addView(layout);
             myLayout.addView(choicesLayout);

        }

        if(serviceDetails.get(questionIndex).getControlType() == "summary"){

            for (int i =0;i<serviceDetails.size()-1; i++) {

                TextView SummaryText = new TextView(ServiceDetailsForm.this);
                SummaryText.setText(serviceDetails.get(i).getQuestion() + ": ");
                SummaryText.setTextSize(15);
                SummaryText.setTextColor(getResources().getColor(R.color.black));

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 50, 0, 0);

                SummaryText.setLayoutParams(params);
                choicesLayout.addView(SummaryText);

                if (serviceDetails.get(i).getControlType() == "numberpicker") {

//                    for (int j = 0;j<serviceDetails.get(i).Choices.length; j++) {
                      for (int j = 0;j<serviceDetails.get(i).Choices.size()-1; j++) {

                        LinearLayout layout = new LinearLayout(ServiceDetailsForm.this);
                        layout.setOrientation(LinearLayout.HORIZONTAL);
                        layout.setHorizontalGravity(Gravity.NO_GRAVITY);
                        layout.setPadding(0, 2, 0, 0);

//                        if (serviceDetails.get(i).ChoicesAnswer[j] != "0") {
                          if (serviceDetails.get(i).ChoicesAnswer.get(j).toString() != "0") {

                                params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.WRAP_CONTENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(0, 0, 0, 0);

                                TextView SummaryAnswer = new TextView(ServiceDetailsForm.this);
//                              SummaryAnswer.setText(serviceDetails.get(i).Choices[j].toString());
                                SummaryAnswer.setText(serviceDetails.get(i).Choices.get(j).toString());
                                SummaryAnswer.setTextSize(15);
                                SummaryAnswer.setTextColor(getResources().getColor(R.color.gradEnd));

                                SummaryAnswer.setLayoutParams(params);
                                layout.addView(SummaryAnswer);

                                TextView SummaryAnswer2 = new TextView(ServiceDetailsForm.this);
//                              SummaryAnswer2.setText(" : "+serviceDetails.get(i).ChoicesAnswer[j].toString());
                                SummaryAnswer2.setText(" : "+serviceDetails.get(i).ChoicesAnswer.get(j).toString());
                                SummaryAnswer2.setTextSize(15);
                                SummaryAnswer2.setTextColor(getResources().getColor(R.color.gradEnd));

                                SummaryAnswer2.setLayoutParams(params);
                                layout.addView(SummaryAnswer2);
                                choicesLayout.addView(layout);

                        }

                    }

                }
                else {

                    TextView SummaryAnswer = new TextView(ServiceDetailsForm.this);
                    SummaryAnswer.setText(serviceDetails.get(i).getAnswer());
                    SummaryAnswer.setTextSize(15);
                    SummaryAnswer.setTextColor(getResources().getColor(R.color.gradEnd));

                    params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0, 0, 0, 0);

                    SummaryAnswer.setLayoutParams(params);
                    choicesLayout.addView(SummaryAnswer);

                }

            }

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 100, 0, 0);

            TextView TotalText = new TextView(ServiceDetailsForm.this);
            TotalText.setText("Total Price: Php 500.00");
            TotalText.setTextSize(30);
            TotalText.setTextColor(getResources().getColor(R.color.gradEnd));
            TotalText.setLayoutParams(params);


            choicesLayout.addView(TotalText);

            myLayout.addView(choicesLayout);

        }


        LinearLayout btnlayout = new LinearLayout(ServiceDetailsForm.this);
        btnlayout.setOrientation(LinearLayout.HORIZONTAL);
        btnlayout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        btnlayout.setPadding(10, 100, 10, 0);

        if(questionIndex>0){

            Button btnBack = new Button(ServiceDetailsForm.this);
            btnBack.setText("Back");
            btnBack.setGravity(Gravity.CENTER);
            btnBack.setBackgroundColor(getResources().getColor(R.color.gradEnd));
            btnBack.setTextColor(getResources().getColor(R.color.white));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                                LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,0,100,0);

            btnBack.setLayoutParams(params);

            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionIndex--;
                    GenerateForm();
                }
            });

            btnlayout.addView(btnBack);
        }

        Button btnNext = new Button(ServiceDetailsForm.this);
        btnNext.setText("Submit Request");
        btnNext.setGravity(Gravity.CENTER);
        btnNext.setBackgroundColor(getResources().getColor(R.color.gradEnd));
        btnNext.setTextColor(getResources().getColor(R.color.white));

        if( questionIndex < (serviceDetails.size()-1) ){

            btnNext.setText("Next");
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionIndex++;
                    GenerateForm();
                }
            });
        }
        else {

            btnNext.setText("Submit booking");
            btnNext.setWidth(700);
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    for (int j = 0;j<serviceDetails.size()-1; j++) {

                        DatabaseReference myRef = database.getReference("Booking/"+MainActivity.UserLoginID+"/"+ String.valueOf(j));
                        ServiceDetails newSD = new ServiceDetails(serviceDetails.get(j).getQuestion(),
                                                                   serviceDetails.get(j).getDescription(),
                                                                   serviceDetails.get(j).getControlType(),
                                                                   serviceDetails.get(j).getChoices(),
                                                                   serviceDetails.get(j).getAnswer(),
                                                                   serviceDetails.get(j).getChoicesAnswer());
                        myRef.setValue(newSD);

                    }

                    Toast.makeText(ServiceDetailsForm.this, "your request has been submitted", Toast.LENGTH_LONG).show();
                    finish();
//                    Intent intent = new Intent(ServiceDetailsForm.this, MainActivity.class);
//                    startActivity(intent);
                }
            });

        }

        btnlayout.addView(btnNext);
        myLayout.addView(btnlayout);

    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year)
    {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                btnDatePicker.setText(date);
                serviceDetails.get(btnDatePicker.getId()).setAnswer(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_DEVICE_DEFAULT_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

}