package hungnv.demo.databasedemo.daos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import hungnv.demo.databasedemo.R;
import hungnv.demo.databasedemo.dtos.StudentDto;

public class StudentAdapter extends BaseAdapter {
    private List<StudentDto> listStudents;

    public void setListStudents(List<StudentDto> listStudents) {
        this.listStudents = listStudents;
    }

    @Override
    public int getCount() {
        return listStudents.size();
    }

    @Override
    public Object getItem(int position) {
        return listStudents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
        }
        TextView txtId = convertView.findViewById(R.id.txtId);
        TextView txtName = convertView.findViewById(R.id.txtName);
        TextView txtMark = convertView.findViewById(R.id.txtMark);
        StudentDto dto = listStudents.get(position);
        txtId.setText(dto.getId());
        txtName.setText(dto.getName());
        txtMark.setText(dto.getMark() + "");
        return convertView;
    }
}
