package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout recordContainer;
    List<Record> records;
    private static final int REQUEST_CODE_RECORD = 1;
    private static final int REQUEST_CODE_SAVING = 2;
    private static final int REQUEST_CODE_DRAW = 3;
    private static final int REQUEST_CODE_CHART = 4;
    String selectedDate;
    TextView totalIncomeTextView, totalExpenseTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_title);
        setTitle("가 계 부");

        recordContainer = findViewById(R.id.recordContainer);
        totalIncomeTextView = findViewById(R.id.totalIncomeTextView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        ImageButton btnChart = findViewById(R.id.btn_chart);
        ImageButton btnSaving = findViewById(R.id.btn_saving);
        ImageButton btnDraw = findViewById(R.id.btn_draw);

        // 예시 데이터 초기화
        initializeRecords();

        CalendarView calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "/" + (month + 1) + "/" + dayOfMonth;
                showRecordsForDate(selectedDate);
            }
        });

        // btn_chart 클릭 리스너 설정
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent에 월별 지출 데이터를 추가하여 전달
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("monthlyExpenses", calculateMonthlyExpenses());
                startActivityForResult(intent, REQUEST_CODE_CHART);
            }
        });

        // btn_saving 클릭 리스너 설정
        btnSaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    Intent intent = new Intent(MainActivity.this, SavingActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivityForResult(intent, REQUEST_CODE_SAVING);
                }
            }
        });

        // btn_draw 클릭 리스너 설정
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDate != null) {
                    Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivityForResult(intent, REQUEST_CODE_RECORD);
                }
            }
        });
    }

    private void initializeRecords() {
        // 예시 데이터 초기화
        records = new ArrayList<>();
    }

    private void showRecordsForDate(String date) {
        // 선택된 날짜에 대한 기록을 표시합니다.
        // 기존에 표시된 기록들을 제거합니다.
        recordContainer.removeAllViews();

        // 선택된 날짜에 대한 기록이 없으면 메시지를 표시합니다.
        int totalIncome = 0;
        int totalExpense = 0;

        for (Record record : records) {
            if (record.getDate().equals(date)) {
                TextView recordTextView = new TextView(this);
                recordTextView.setText(record.toString());
                recordContainer.addView(recordTextView);

                if (record.isIncome()) {
                    totalIncome += record.getMoney();
                } else {
                    totalExpense += record.getMoney();
                }
            }
        }

        totalIncomeTextView.setText("총 수입: " + totalIncome + "원");
        totalExpenseTextView.setText("총 지출: " + totalExpense + "원");
    }

    private HashMap<String, List<String>> calculateMonthlyExpenses() {
        HashMap<String, List<String>> monthlyExpenses = new HashMap<>();
        for (Record record : records) {
            String[] dateParts = record.getDate().split("/");
            String yearMonth = dateParts[0] + "년 " + dateParts[1] + "월";
            String day = dateParts[2] + "일";

            if (!record.isIncome()) {
                String expenseDetail = dateParts[1] + "월 " + dateParts[2] + "일 - " + record.getMoney() + "원 - " + record.getDetail();
                if (monthlyExpenses.containsKey(yearMonth)) {
                    monthlyExpenses.get(yearMonth).add(expenseDetail);
                } else {
                    List<String> expensesList = new ArrayList<>();
                    expensesList.add(expenseDetail);
                    monthlyExpenses.put(yearMonth, expensesList);
                }
            }
        }
        return monthlyExpenses;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == REQUEST_CODE_RECORD || requestCode == REQUEST_CODE_SAVING || requestCode == REQUEST_CODE_DRAW)
                && resultCode == RESULT_OK && data != null) {
            String detail = data.getStringExtra("detail");
            int money = Integer.parseInt(data.getStringExtra("money"));
            boolean isIncome = data.getBooleanExtra("isIncome", false);
            String date = data.getStringExtra("selectedDate");

            Record record = new Record(detail, money, isIncome, date);

            records.add(record);

            // 기록이 추가된 날짜에 대한 기록을 다시 표시
            showRecordsForDate(date);
        }
    }

    // Record 클래스를 정의합니다.
    public static class Record implements java.io.Serializable {
        private String detail;
        private int money;
        private boolean isIncome;
        private String date;

        public Record(String detail, int money, boolean isIncome, String date) {
            this.detail = detail;
            this.money = money;
            this.isIncome = isIncome;
            this.date = date;
        }

        public int getMoney() {
            return money;
        }

        public boolean isIncome() {
            return isIncome;
        }

        public String getDate() {
            return date;
        }

        public String getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            String recordType = isIncome ? "수입" : "지출";
            return recordType + ": " + money + "원 - " + detail;
        }
    }
}
