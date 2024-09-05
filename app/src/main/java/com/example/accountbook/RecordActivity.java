package com.example.accountbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecordActivity extends AppCompatActivity {
    TextView textViewDate;
    CheckBox incomeCheckbox, expenseCheckbox;
    EditText moneyEditText, detailEditText;
    Button saveButton;
    int recordIndex = -1; // 기록 인덱스 변수

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_title);
        setTitle("가 계 부");

        textViewDate = findViewById(R.id.tvDate);
        detailEditText = findViewById(R.id.etDetail);
        moneyEditText = findViewById(R.id.etMoney);
        incomeCheckbox = findViewById(R.id.cbIncome);
        expenseCheckbox = findViewById(R.id.cbExpense);
        saveButton = findViewById(R.id.btnSave);

        // 초기 체크박스 상태 설정 (둘 다 선택되지 않은 상태)
        incomeCheckbox.setChecked(false);
        expenseCheckbox.setChecked(false);

        // MainActivity로부터 전달받은 데이터 확인
        Intent intent = getIntent();
        if (intent != null) {
            String selectedDate = intent.getStringExtra("selectedDate");
            if (selectedDate != null) {
                textViewDate.setText(selectedDate);
            }

            // 기존 기록 데이터를 확인하고 표시
            String recordDetail = intent.getStringExtra("recordDetail");
            int recordMoney = intent.getIntExtra("recordMoney", 0);
            boolean isIncome = intent.getBooleanExtra("isIncome", false);
            recordIndex = intent.getIntExtra("recordIndex", -1);

            if (recordDetail != null) {
                detailEditText.setText(recordDetail);
            }
            if (recordMoney != 0) {
                moneyEditText.setText(String.valueOf(recordMoney));
            }
            if (isIncome) {
                incomeCheckbox.setChecked(true);
            } else if (recordDetail != null) { // 기록이 있다면 지출 체크박스를 선택
                expenseCheckbox.setChecked(true);
            }
        }

        // 체크박스 리스너 설정
        incomeCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (incomeCheckbox.isChecked()) {
                    expenseCheckbox.setChecked(false);
                }
            }
        });

        expenseCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expenseCheckbox.isChecked()) {
                    incomeCheckbox.setChecked(false);
                }
            }
        });

        // 저장 버튼 클릭 시
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력한 정보 가져오기
                String detail = detailEditText.getText().toString();
                String money = moneyEditText.getText().toString();
                boolean isIncome = incomeCheckbox.isChecked();
                boolean isExpense = expenseCheckbox.isChecked();

                if (detail.isEmpty() || money.isEmpty() || (!isIncome && !isExpense)) {
                    // 필수 항목이 비어있는 경우 토스트 메시지 표시
                    Toast.makeText(RecordActivity.this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    // 선택된 날짜 가져오기
                    String selectedDate = textViewDate.getText().toString();

                    // 데이터를 인텐트에 담아 반환
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("detail", detail);
                    resultIntent.putExtra("money", money);
                    resultIntent.putExtra("isIncome", isIncome);
                    resultIntent.putExtra("selectedDate", selectedDate);
                    if (recordIndex != -1) {
                        resultIntent.putExtra("recordIndex", recordIndex);
                    }
                    setResult(RESULT_OK, resultIntent);
                    Toast.makeText(RecordActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                    finish(); // 현재 Activity 종료
                }
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
}
