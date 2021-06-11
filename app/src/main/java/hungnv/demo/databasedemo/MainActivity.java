package hungnv.demo.databasedemo;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import hungnv.demo.databasedemo.daos.StudentAdapter;
import hungnv.demo.databasedemo.daos.StudentDao;
import hungnv.demo.databasedemo.databinding.ActivityMainBinding;
import hungnv.demo.databasedemo.dtos.StudentDto;

public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 6789;
    private ListView listViewStudent;
    private TextView txtTitle;
    private StudentAdapter adapter;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void clickToLoadFromRaw(MenuItem item) {
        listViewStudent = findViewById(R.id.listViewStudent);
        txtTitle = findViewById(R.id.txtTitle);
        try {
            Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();
            adapter = new StudentAdapter();
            txtTitle.setText("List Student from Raw");
            InputStream is = getResources().openRawResource(R.raw.data);
            StudentDao dao = new StudentDao();
            List<StudentDto> result = dao.loadFromRaw(is);
            adapter.setListStudents(result);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDto dto = (StudentDto) listViewStudent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intent.putExtra("action", "update");
                    intent.putExtra("dto", dto);
                    startActivity(intent);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void clickToLoadFromRawToInternal(MenuItem item) {
        try {
            StudentDao dao = new StudentDao();
            InputStream is = getResources().openRawResource(R.raw.data);
            List<StudentDto> list = dao.loadFromRaw(is);
            FileOutputStream fos = openFileOutput("hungnv.txt", MODE_PRIVATE);
            dao.saveToInternal(fos, list);
            Toast.makeText(this, "Save success", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToLoadFromInternal(MenuItem item) {
        try {
            listViewStudent = findViewById(R.id.listViewStudent);
            txtTitle = findViewById(R.id.txtTitle);
            adapter = new StudentAdapter();
            txtTitle.setText("List Student From Internal");
            FileInputStream fis = openFileInput("hungnv.txt");
            StudentDao dao = new StudentDao();
            List<StudentDto> listStudents = dao.loadFromInternal(fis);
            adapter.setListStudents(listStudents);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDto dto = (StudentDto) listViewStudent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intent.putExtra("action", "update");
                    intent.putExtra("dto", dto);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToCreate(View view) {
        Intent intent = new Intent(this, StudentDetailActivity.class);
        intent.putExtra("action", "create");
        startActivity(intent);
    }

    public void clickToSaveToExternal(MenuItem item) {
        try {
            FileInputStream fis = openFileInput("hungnv.txt");
            StudentDao dao = new StudentDao();
            List<StudentDto> listStudent = dao.loadFromInternal(fis);
            dao.saveToExternal(listStudent);
            Toast.makeText(this, "Save to SD Card Success", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToLoadFromExternal(MenuItem item) {
        try {
            listViewStudent = findViewById(R.id.listViewStudent);
            txtTitle = findViewById(R.id.txtTitle);
            adapter = new StudentAdapter();
            txtTitle.setText("List Student From External");
            StudentDao dao = new StudentDao();
            List<StudentDto> listStudent = dao.loadFromExternal();
            adapter.setListStudents(listStudent);
            listViewStudent.setAdapter(adapter);
            listViewStudent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    StudentDto dto = (StudentDto) listViewStudent.getItemAtPosition(position);
                    Intent intent = new Intent(MainActivity.this, StudentDetailActivity.class);
                    intent.putExtra("action", "update");
                    intent.putExtra("dto", dto);
                    startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clickToBackupToExternal(MenuItem item) {
        File sdCard = Environment.getExternalStorageDirectory();
        String realPath = sdCard.getAbsolutePath();
        String desDir = realPath + "/HungnvFiles";
        File directory = new File(desDir);
        if(!directory.exists()) {
            directory.mkdir();
        }
        String dataPath = "/data/data/" + this.getPackageName() + "/files";
        File dataDir = new File(dataPath);
        File[] listFile = dataDir.listFiles();
        if (listFile != null) {
            for (int i = 0; i < listFile.length; i++) {
                File f = listFile[i];
                MyUtils utils = new MyUtils();
                try {
                    utils.copyFile(f.getAbsolutePath(), desDir + "/" + f.getName());
                    Toast.makeText(this, "Backup to SDCard", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void clickToRestoreFromExternal(MenuItem item) {
        try {
            String dataPathDes = "/data/data/" + this.getPackageName() + "/files";
            File sdCard = Environment.getExternalStorageDirectory();
            String readPath = sdCard.getAbsolutePath();
            String srcDir = readPath + "/HungnvFiles";
            File dataSrcDir = new File(srcDir);
            File[] listFile = dataSrcDir.listFiles();
            if(listFile != null) {
                for (int i = 0; i < listFile.length; i++) {
                    File f = listFile[i];
                    MyUtils utils = new MyUtils();
                    utils.copyFile(f.getAbsolutePath(), dataPathDes + "/" + f.getName());
                    Toast.makeText(this, "Restore data success", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}