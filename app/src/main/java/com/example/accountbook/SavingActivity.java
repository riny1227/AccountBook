package com.example.accountbook;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SavingActivity extends AppCompatActivity {
    TextView tvAsset, tvAssetName, tvAccount, tvAccountName;
    Button btnEdit;
    EditText dlgEdtAssetName, dlgEdtAsset, dlgEdtAccountName, dlgEdtAccount;
    View dialogView; // dialog.xml을 인플레이트할 뷰 변수 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saving);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_title);
        setTitle("가 계 부");

        tvAsset = (TextView) findViewById(R.id.tvAsset);
        tvAssetName = (TextView)findViewById(R.id.tvAssetName);
        tvAccount = (TextView) findViewById(R.id.tvAccount);
        tvAccountName = (TextView) findViewById(R.id.tvAccountName);
        btnEdit = (Button) findViewById(R.id.btnEdit);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // layout > dialog.xml 파일을 인플레이트하여 dialogView에 대입
                dialogView = (View) View.inflate(SavingActivity.this, R.layout.dialog, null);

                // AlertDialog.Builder 생성
                AlertDialog.Builder dlg = new AlertDialog.Builder(SavingActivity.this);
                dlg.setTitle("나의 자산");  // 제목
                dlg.setIcon(R.drawable.ic_saving_black); // 아이콘
                dlg.setView(dialogView);    // 인플레이트한 dialogView을 대화상자로 사용

                //setPositiveButton(문자열, 리스너) 메서드 만들기
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dlgEdtAssetName = (EditText) dialogView.findViewById(R.id.dlgEdt1);
                        dlgEdtAsset = (EditText) dialogView.findViewById(R.id.dlgEdt2);
                        dlgEdtAccountName = (EditText) dialogView.findViewById(R.id.dlgEdt3);
                        dlgEdtAccount = (EditText) dialogView.findViewById(R.id.dlgEdt4);

                        tvAssetName.setText(dlgEdtAssetName.getText().toString());
                        tvAsset.setText(dlgEdtAsset.getText().toString());
                        tvAccountName.setText(dlgEdtAccountName.getText().toString());
                        tvAccount.setText(dlgEdtAccount.getText().toString());
                    }
                });

                // 취소 버튼
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dlg.show();
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
