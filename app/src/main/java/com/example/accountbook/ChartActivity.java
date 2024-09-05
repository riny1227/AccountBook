package com.example.accountbook;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChartActivity extends AppCompatActivity {

    TextView monthTextView, totalExpenseTextView;
    Calendar currentCalendar;
    HashMap<String, List<String>> monthlyExpenses;
    SimpleDateFormat monthFormat;
    LinearLayout expenseListLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_title);
        setTitle("가 계 부");

        monthTextView = findViewById(R.id.monthTextView);
        totalExpenseTextView = findViewById(R.id.totalExpenseTextView);
        ImageButton prevMonthButton = findViewById(R.id.prevMonthButton);
        ImageButton nextMonthButton = findViewById(R.id.nextMonthButton);
        expenseListLayout = findViewById(R.id.expenseList); // 수정: 지출 내역을 표시할 LinearLayout

        // MainActivity에서 전달된 월별 지출 데이터를 가져옵니다.
        monthlyExpenses = (HashMap<String, List<String>>) getIntent().getSerializableExtra("monthlyExpenses");

        // 현재 월을 가져옵니다.
        currentCalendar = Calendar.getInstance();
        monthFormat = new SimpleDateFormat("yyyy년 M월", Locale.getDefault());
        updateMonthView();

        // 이전 월 버튼 클릭 리스너 설정
        prevMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, -1);
                updateMonthView();
            }
        });

        // 다음 월 버튼 클릭 리스너 설정
        nextMonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.MONTH, 1);
                updateMonthView();
            }
        });

        // btn_back 클릭 리스너 설정
        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setContentDescription("뒤로 가기 버튼");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 Activity 종료
            }
        });
    }

    private void updateMonthView() {
        String currentMonth = monthFormat.format(currentCalendar.getTime());
        monthTextView.setText(currentMonth);

        int totalExpense = 0;
        if (monthlyExpenses.containsKey(currentMonth)) {
            totalExpense = calculateTotalExpense(monthlyExpenses.get(currentMonth));
        }
        totalExpenseTextView.setText("총 지출: " + totalExpense + "원");

        // 지출 내역을 표시합니다.
        expenseListLayout.removeAllViews(); // 기존 항목 제거
        if (monthlyExpenses.containsKey(currentMonth)) {
            List<String> expenses = monthlyExpenses.get(currentMonth);
            for (String expense : expenses) {
                TextView expenseTextView = new TextView(this);
                expenseTextView.setText(expense);
                expenseTextView.setTextSize(18); // 텍스트 크기 설정
                expenseListLayout.addView(expenseTextView);
            }
        } else {
            TextView noExpenseTextView = new TextView(this);
            noExpenseTextView.setText("이 달의 지출 내역이 없습니다.");
            expenseListLayout.addView(noExpenseTextView);
        }
    }

    private int calculateTotalExpense(List<String> expenses) {
        int totalExpense = 0;
        for (String expense : expenses) {
            String[] parts = expense.split(" - ");
            totalExpense += Integer.parseInt(parts[1].replace("원", "").trim());
        }
        return totalExpense;
    }
}
