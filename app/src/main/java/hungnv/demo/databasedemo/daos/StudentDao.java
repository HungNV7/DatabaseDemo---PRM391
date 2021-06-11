package hungnv.demo.databasedemo.daos;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.RowSetWriter;

import hungnv.demo.databasedemo.dtos.StudentDto;

public class StudentDao {
    public StudentDao() {
    }

    public List<StudentDto> loadFromRaw(InputStream is) throws Exception {
        List<StudentDto> result = new ArrayList<>();
        StudentDto dto = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        String s = null;
        try {
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            while ((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                dto = new StudentDto(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if(isr != null) {
                isr.close();
            }
        }
        return result;
    }

    public void saveToInternal(FileOutputStream fos, List<StudentDto> list) throws IOException {
        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(fos);
            String result = "";
            for (StudentDto dto: list) {
                result += dto.toString() + "\n";
            }
            osw.write(result);
            osw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(osw != null) {
                osw.close();
            }
        }
    }

    public List<StudentDto> loadFromInternal(FileInputStream fis) throws Exception {
        List<StudentDto> result = new ArrayList<>();
        StudentDto dto = null;
        BufferedReader br = null;
        InputStreamReader isr = null;
        String s = null;
        try {
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);
            while ((s = br.readLine()) != null) {
                String[] tmp = s.split("-");
                dto = new StudentDto(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
                result.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if(isr != null) {
                isr.close();
            }
        }
        return result;
    }

    public boolean saveToExternal(List<StudentDto> listStudent) throws Exception {
        boolean check = false;
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            File sdCard = Environment.getExternalStorageDirectory();
            String realPath = sdCard.getAbsolutePath();
            File directory = new File(realPath + "/HungnvFiles");
            directory.mkdir();
            File f = new File(directory, "hungnv.txt");
            fos = new FileOutputStream(f);
            osw = new OutputStreamWriter(fos);
            String result = "";
            for (StudentDto dto: listStudent) {
                result += dto.toString() + "\n";
            }
            osw.write(result);
            osw.flush();
            check = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(osw != null) {
                osw.close();
            }
            if(fos != null) {
                fos.close();
            }
            return check;
        }
    }

    public List<StudentDto> loadFromExternal() throws Exception {
        List<StudentDto> result = new ArrayList<>();
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        File directory = new File(realPath + "/HungnvFiles");
        File file = new File(directory, "hungnv.txt");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        String s = "";
        while ((s = br.readLine()) != null) {
            String[] tmp = s.split("-");
            StudentDto dto = new StudentDto(tmp[0], tmp[1], Float.parseFloat(tmp[2]));
            result.add(dto);
        }
        return result;
    }
}
