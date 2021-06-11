package hungnv.demo.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.zip.DataFormatException;

import hungnv.demo.databasedemo.daos.StudentDao;
import hungnv.demo.databasedemo.dtos.StudentDto;

public class StudentDetailActivity extends AppCompatActivity {
    private EditText edtId, edtName, edtMark;
    private String action;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_detail);
        edtId = findViewById(R.id.edtID);
        edtName = findViewById(R.id.edtName);
        edtMark = findViewById(R.id.edtMark);
        Intent intent = this.getIntent();
        action = intent.getStringExtra("action");
        if(action.equals("update")) {
            StudentDto dto = (StudentDto) intent.getSerializableExtra("dto");
            edtId.setText(dto.getId());
            edtName.setText(dto.getName());
            edtMark.setText(dto.getMark() + "");
        }

    }

    public void clickToSave(View view) {
        String id = edtId.getText().toString();
        String name = edtName.getText().toString();
        float mark = Float.parseFloat(edtMark.getText().toString());
        try {
            StudentDao dao = new StudentDao();
            FileInputStream fis = openFileInput("hungnv.txt");
            List<StudentDto> listStudent = dao.loadFromInternal(fis);
            FileOutputStream fos = openFileOutput("hungnv.txt", MODE_PRIVATE);
            StudentDto dto = new StudentDto(id, name, mark);
            if(action.equals("create")) {
                listStudent.add(dto);
            } else if(action.equals("update")) {
                for (StudentDto studentDto: listStudent) {
                    if(studentDto.getId().equals(dto.getId())) {
                        studentDto.setName(dto.getName());
                        studentDto.setMark(dto.getMark());
                    }
                }
            }

            dao.saveToInternal(fos, listStudent);
            Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}