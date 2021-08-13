package com.example.englishword;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.tts.TextToSpeech.ERROR;

public class MainActivity extends AppCompatActivity {

    FrameLayout frame;
    View view;
    private DrawerLayout mDrawerLayout;
    DBHelper dbHelper;
    SQLiteDatabase db = null;
    private TextToSpeech tts;

    void TTS_English() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    void TTS_Korean() {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != ERROR) {
                    tts.setLanguage(Locale.KOREAN);
                }
            }
        });
    }


    //==================================================메인페이지 관련 코드 및 변수==================================================//

    Cursor cursor;
    ArrayAdapter adapter;
    EditText main_page_et1;
    TextView main_page_tv1, main_page_tv2;
    Button main_page_bt1, main_page_bt2, main_page_bt3, main_page_bt4;
    Switch main_page_sw1;
    ListView listView;
    String main_page_changelanguage = "source=en&target=ko&text=";
    boolean check;

    void main_page_list() {
        cursor = null;
        cursor = db.rawQuery("SELECT * FROM tableName", null);
        startManagingCursor(cursor);
        adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1);
        while (cursor.moveToNext()) {
            adapter.add("단어 : " + cursor.getString(0) + "\n" + "뜻 : " + cursor.getString(1));
        }
        listView.setAdapter(adapter);
    }

    void main_page_help() {
        View dlgView = View.inflate(this, R.layout.activity_papagohelp, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        final TextView tv1 = (TextView) dlgView.findViewById(R.id.tv1);
        final TextView tv2 = (TextView) dlgView.findViewById(R.id.tv2);
        final TextView tv3 = (TextView) dlgView.findViewById(R.id.tv3);
        final TextView tv4 = (TextView) dlgView.findViewById(R.id.tv4);
        final TextView tv5 = (TextView) dlgView.findViewById(R.id.tv5);

        String change_help = getString(R.string.change_help);
        String listen_help = getString(R.string.listen_help);
        String add_help = getString(R.string.add_help);
        String offsw_help = getString(R.string.offsw_help);
        String onsw_help = getString(R.string.onsw_help);

        tv1.setText(change_help);
        tv2.setText(listen_help);
        tv3.setText(add_help);
        tv4.setText(offsw_help);
        tv5.setText(onsw_help);

        dlg.setTitle("도움말");
        dlg.setView(dlgView);
        dlg.setNegativeButton("확인", null);
        dlg.show();
    }

    void main_page() {
        view = null;
        frame = (FrameLayout) findViewById(R.id.frame);
        LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = in.inflate(R.layout.activity_main_page, null);
        frame.addView(view);
        TTS_Korean();
        main_page_et1 = view.findViewById(R.id.et1);
        main_page_bt1 = view.findViewById(R.id.bt1);
        main_page_bt2 = view.findViewById(R.id.bt2);
        main_page_bt3 = view.findViewById(R.id.bt3);
        main_page_bt4 = view.findViewById(R.id.bt4);
        main_page_tv1 = view.findViewById(R.id.tv1);
        main_page_tv2 = view.findViewById(R.id.tv2);
        main_page_sw1 = view.findViewById(R.id.sw1);
        listView = view.findViewById(R.id.listView);
        main_page_tv2.setText("영어 -> 한글");
        check = false;

        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();
        main_page_list();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();

        main_page_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (main_page_et1.getText().toString().equals("") || main_page_et1.getText().toString().equals(null)) {
                    Toast.makeText(getApplicationContext(), "검색할 단어를 입력해 주세요", Toast.LENGTH_SHORT).show();
                }
                MainActivity.Translate translate = new MainActivity.Translate();
                translate.execute();
            }
        });

        main_page_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main_page_tv1.getText().toString().equals("") || main_page_tv1.getText().toString().equals(null)) {
                    Toast.makeText(getApplicationContext(), "단어를 검색한 뒤 사용해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    tts.speak(main_page_tv1.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        main_page_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (main_page_tv1.getText().toString().equals("") || main_page_tv1.getText().toString().equals("null")) {
                    Toast.makeText(getApplicationContext(), "먼저 번역을 해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (check == false) {
                        String translation_before = main_page_et1.getText().toString();
                        String translation_after = main_page_tv1.getText().toString();
                        String english = translation_before;
                        String korean = translation_after;
                        db.execSQL("INSERT INTO tableName VALUES('" + english + "', '" + korean + "');");
                        Toast.makeText(getApplicationContext(), "단어 등록", Toast.LENGTH_SHORT).show();
                        main_page_et1.setText(null);
                        main_page_tv1.setText(null);
                        main_page_list();
                    } else {
                        String translation_before = main_page_et1.getText().toString();
                        String translation_after = main_page_tv1.getText().toString();
                        String english = translation_after;
                        String korean = translation_before;
                        db.execSQL("INSERT INTO tableName VALUES('" + english + "', '" + korean + "');");
                        Toast.makeText(getApplicationContext(), "단어 등록", Toast.LENGTH_SHORT).show();
                        main_page_et1.setText(null);
                        main_page_tv1.setText(null);
                        main_page_list();
                    }
                }
            }
        });

        main_page_bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_page_help();
            }
        });
        main_page_sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    check = true;
                    main_page_changelanguage = "source=ko&target=en&text=";
                    main_page_tv2.setText("한글 -> 영어");
                    TTS_English();
                } else {
                    check = false;
                    main_page_changelanguage = "source=en&target=ko&text=";
                    main_page_tv2.setText("영어 -> 한글");
                    TTS_Korean();
                }
            }
        });

    }

    // ==================================================메인페이지 끝==================================================//


    // ==================================================단어 편집 페이지 소스 및 변수==================================================//

    Button word_management_bt1, word_management_bt2, word_management_bt3;
    boolean word_check;
    Cursor cursor2;
    ListView listView2;
    ArrayAdapter adapter2;
    String[] check_word = new String[1];
    String[] check_word2 = new String[1];

    void word_management_list() {
        cursor2 = null;
        cursor2 = db.rawQuery("SELECT * FROM tableName", null);
        startManagingCursor(cursor2);
        final ArrayList<String> items = new ArrayList<String>();
        adapter2 = new ArrayAdapter(this,
                android.R.layout.simple_list_item_single_choice, items);
        while (cursor2.moveToNext()) {
            adapter2.add("단어 : " + cursor2.getString(0) + "\n" + "뜻 : " + cursor2.getString(1));
        }
        listView2.setAdapter(adapter2);
        listView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    void word_management() {
        TTS_English();
        view = null;
        word_check = false;
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();
        frame = (FrameLayout) findViewById(R.id.frame);
        LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = in.inflate(R.layout.activity_word_management, null);
        frame.addView(view);
        listView2 = view.findViewById(R.id.listView);
        word_management_bt1 = view.findViewById(R.id.bt1);
        word_management_bt2 = view.findViewById(R.id.bt2);
        word_management_bt3 = view.findViewById(R.id.bt3);
        check_word[0] = null;
        check_word2[0] = null;
        word_management_list();

        listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (word_check == false) {
                    check_word[0] = null;
                    check_word[0] = (String) adapterView.getAdapter().getItem(i);
                    word_check = true;
                } else if (word_check == true) {
                    check_word2[0] = null;
                    check_word2[0] = (String) adapterView.getAdapter().getItem(i);
                    if (check_word2[0] == check_word[0]) {
                        word_check = false;
                        word_management_list();
                    } else {
                        check_word[0] = null;
                        check_word[0] = (String) adapterView.getAdapter().getItem(i);
                        word_check = true;
                    }
                }
            }
        });


        word_management_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (word_check == true) {
                    String First_Change = check_word[0].replace("단어 : ", "");
                    String Second_change = First_Change.substring(First_Change.lastIndexOf("뜻 : ") + 4);
                    String korean = Second_change;
                    db.execSQL("DELETE FROM tableName WHERE korean = '" + korean + "';");
                    word_management_list();
                    header();
                    word_check = false;
                } else {
                    Toast.makeText(getApplicationContext(), "단어를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        word_management_bt2.setOnClickListener(new View.OnClickListener() {
            String english = null;

            @Override
            public void onClick(View v) {
                cursor2 = null;
                if (word_check == true) {
                    String First_Change = check_word[0].replace("단어 : ", "");
                    String Second_change = First_Change.substring(First_Change.lastIndexOf("뜻 : ") + 4);
                    String korean = Second_change;
                    cursor2 = db.rawQuery("SELECT * FROM tableName WHERE korean = ?", new String[]{korean});
                    while (cursor2.moveToNext()) {
                        english = cursor2.getString(0);
                    }
                    Toast.makeText(getApplicationContext(), "" + english, Toast.LENGTH_SHORT).show();
                    tts.speak("" + english, TextToSpeech.QUEUE_FLUSH, null);
                    word_management_list();
                    word_check = false;
                    english = null;
                } else {
                    Toast.makeText(getApplicationContext(), "단어를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        word_management_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                word_management_select();
            }
        });

    }

    void word_management_select() {
        View dlgView = View.inflate(this, R.layout.activity_select, null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        final EditText et1 = dlgView.findViewById(R.id.et1);
        final EditText et2 = dlgView.findViewById(R.id.et2);
        Button bt1 = dlgView.findViewById(R.id.bt1);

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String english = et1.getText().toString();
                String korean = et2.getText().toString();
                if (et1.getText().toString().equals("") || et2.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "단어와 뜻을 입력해 주세요", Toast.LENGTH_SHORT).show();
                } else {
                    db.execSQL("INSERT INTO tableName VALUES('" + english + "', '" + korean + "');");
                    Toast.makeText(getApplicationContext(), "단어 추가", Toast.LENGTH_SHORT).show();
                    et1.setText(null);
                    et2.setText(null);
                }
                word_management_list();
                header();
            }
        });
        dlg.setNegativeButton("확인", null);
        dlg.setView(dlgView);
        dlg.show();
    }

    // ==================================================단어 편집 페이지 끝==================================================//


    // ==================================================단어 테스트 페이지 소스 및 변수==================================================//

    boolean korean_test_check = false;
    boolean english_test_check = false;
    String english;
    String korean;
    String hint;
    boolean test_possible;
    int num_questions;
    int index;
    Cursor cursor_test;
    int test_int;
    int test_int2;
    String[] test_english;
    String[] test_korean;
    TextView test_tv1;
    EditText test_et1;
    Button test_bt1, test_bt2, test_bt3, test_bt4;
    LinearLayout choice, test1;

    void test_word() {
        english = null;
        korean = null;
        cursor_test = null;
        hint = null;
        test_int = 0;
        test_int2 = 0;
        cursor_test = db.rawQuery("SELECT * FROM tableName", null);
        startManagingCursor(cursor_test);
        test_int2 = cursor_test.getCount();
        if (test_int2 > 0) {
            test_possible = true;
            test_english = new String[test_int2];
            test_korean = new String[test_int2];
            while (cursor_test.moveToNext()) {
                english = cursor_test.getString(0);
                korean = cursor_test.getString(1);
                test_english[test_int] = english;
                test_korean[test_int] = korean;
                test_int++;
            }
        } else {
            test_possible = false;
            Toast.makeText(getApplicationContext(), "저장된 단어가 없습니다!", Toast.LENGTH_SHORT).show();
        }

    }

    void test_english_problem() {
        if (num_questions > 0) {
            index = (int) Math.floor(Math.random() * num_questions);
            test_tv1.setText(test_english[index] + "에 해당하는 뜻은?");
        } else {
            test_tv1.setText("문제가 다 떨어졌습니다.");
            Toast.makeText(this, "문제가 다 떨어졌습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void test_korean_problem() {
        if (num_questions > 0) {
            index = (int) Math.floor(Math.random() * num_questions);
            test_tv1.setText(test_korean[index] + "에 해당하는 영어 단어은?");
        } else {
            test_tv1.setText("문제가 다 떨어졌습니다.");
            Toast.makeText(this, "문제가 다 떨어졌습니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    void useword() {
        for (int i = index + 1; i < num_questions; i++) {
            test_english[i - 1] = test_english[i];
            test_korean[i - 1] = test_korean[i];
        }
        num_questions--;
    }

    void korean_test_buttoncheck() {
        test_word();
        if (test_possible == true) {
            korean_test_check = true;
            num_questions = test_english.length;
            test_english_problem();
        }
    }

    void english_test_buttoncheck() {
        test_word();
        if (test_possible == true) {
            english_test_check = true;
            num_questions = test_english.length;
            test_korean_problem();
        }
    }

    void test_page() {
        view = null;
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();
        frame = (FrameLayout) findViewById(R.id.frame);
        LayoutInflater in = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        view = in.inflate(R.layout.activity_word_test, null);
        frame.addView(view);

        korean_test_check = false;
        english_test_check = false;

        test_tv1 = view.findViewById(R.id.tv1);
        test_et1 = view.findViewById(R.id.et1);
        test_bt1 = view.findViewById(R.id.bt1);
        test_bt2 = view.findViewById(R.id.bt2);
        test_bt3 = view.findViewById(R.id.bt3);
        test_bt4 = view.findViewById(R.id.bt4);

        choice = view.findViewById(R.id.choice);
        test1 = view.findViewById(R.id.test1);

        choice.setVisibility(View.VISIBLE);
        test1.setVisibility(View.INVISIBLE);


        test_bt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1.setVisibility(View.VISIBLE);
                choice.setVisibility(View.INVISIBLE);
                korean_test_buttoncheck();
            }
        });

        test_bt4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test1.setVisibility(View.VISIBLE);
                choice.setVisibility(View.INVISIBLE);
                english_test_buttoncheck();
            }
        });

        test_bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test_possible == true) {
                    if (korean_test_check == true) {
                        if (test_et1.getText().toString().equals(test_korean[index])) {
                            Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_SHORT).show();
                            useword();
                            test_english_problem();
                            test_et1.setText(null);
                        } else {
                            Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
                            test_et1.setText(null);
                        }
                    } else if (english_test_check == true) {
                        if (test_et1.getText().toString().equals(test_english[index])) {
                            Toast.makeText(getApplicationContext(), "정답입니다!", Toast.LENGTH_SHORT).show();
                            useword();
                            test_korean_problem();
                            test_et1.setText(null);
                        } else {
                            Toast.makeText(getApplicationContext(), "오답입니다.", Toast.LENGTH_SHORT).show();
                            test_et1.setText(null);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "잘못된 경로 입니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "저장된 단어가 없습니다!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        test_bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test_possible == true) {
                    if (korean_test_check == true) {
                        hint = test_korean[index].substring(0, 1);
                    } else {
                        hint = test_english[index].substring(0, 1);
                    }
                    Toast.makeText(getApplicationContext(), "첫글자 : " + hint, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "저장된 단어가 없습니다!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // ==================================================단어 테스트 페이지 끝==================================================//


    // ==================================================그 외의 소스 및 변수==================================================//

    int word_number;
    View headers;
    Cursor cursor_header;
    TextView header_tv1;

    void header() {
        word_number = 0;
        dbHelper = new DBHelper(this, 4);
        db = dbHelper.getWritableDatabase();
        cursor_header = db.rawQuery("SELECT * FROM tableName", null);
        startManagingCursor(cursor_header);
        while (cursor_header.moveToNext()) {
            word_number++;
        }
        header_tv1.setText("등록된 단어의 개수 : " + word_number);
    }

    void reset() {
        cursor = null;
        cursor2 = null;
        english = null;
        korean = null;
        hint = null;
        view = null;
        check_word[0] = null;
        check_word2[0] = null;
    }

    String main_page_getresult;

    class Translate extends AsyncTask<String, Void, String> {

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override

        protected String doInBackground(String... strings) {

            String clientId = "Dh49Gc9axM3q2lOlCvoK";
            String clientSecret = "K3E8IDihOT";
            try {
                String text = URLEncoder.encode(main_page_et1.getText().toString(), "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                String postParams = main_page_changelanguage + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                System.out.println(response.toString());
                main_page_getresult = response.toString();

                main_page_getresult = main_page_getresult.split("\"")[27];
                main_page_tv1.setText(main_page_getresult);
            } catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.memu_button);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        headers = navigationView.inflateHeaderView(R.layout.activity_header);
        header_tv1 = headers.findViewById(R.id.header_tv1);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                reset();
                int id = menuItem.getItemId();
                if (id == R.id.account) {
                    header();
                    main_page();
                } else if (id == R.id.insert) {
                    header();
                    word_management();
                } else if (id == R.id.cart) {
                    header();
                    test_page();
                }

                return true;
            }

        });
        reset();
        header();
        main_page();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        header();
        switch (id) {
            case android.R.id.home:
                header();
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
                toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
                finish();
                toast.cancel();
            }
        }


    }

    private long backKeyPressedTime = 0;
    private Toast toast;

}