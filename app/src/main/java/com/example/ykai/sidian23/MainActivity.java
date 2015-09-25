package com.example.ykai.sidian23;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
    private Button buttonSubmit=null;
    private TextView textviewUsername=null;
 private EditText MeditUsername=null;
    private EditText MeditPassword=null;
    private TextView textviewLogin=null;
    private static String  Mpassword;
    private static String  Musername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SysApplication.getInstance().addActivity(this);
        buttonSubmit=(Button)this.findViewById(R.id.button_submit);
        textviewUsername=(TextView)this.findViewById(R.id.textview_username);
        textviewLogin=(TextView)this.findViewById(R.id.login_status);
MeditPassword=(EditText)this.findViewById(R.id.edittext_password);
        MeditUsername=(EditText)this.findViewById(R.id.edittext_name);
        //同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                MainActivity.MODE_PRIVATE);
// 使用getString方法获得value，注意第2个参数是value的默认值
       Musername =sharedPreferences.getString("name", "");
        Mpassword =sharedPreferences.getString("habit", "");
//使用toast信息提示框显示信息

if(Mpassword!=""&&Musername!="")
 {        // 如果不为空
    Intent intent = new Intent();
    intent.setClass(MainActivity.this, OtherActivity.class);
  /*  Bundle bundle = new Bundle();
    bundle.putString("O_username", Musername);
    bundle.putString("O_password", Mpassword);
    intent.putExtras(bundle);
    */
    startActivityForResult(intent, 100);
}
        else
        {
//finish();
        }


        buttonSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

               Musername= MeditUsername.getText().toString();
                Mpassword=MeditPassword.getText().toString();


                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OtherActivity.class);
             /*   Bundle bundle =new Bundle();
                bundle.putString("O_username", Musername);
                bundle.putString("O_password", Mpassword);
                intent.putExtras(bundle);
               */ startActivityForResult(intent, 100); //requestcode=100

                //*******************************************     写入：
//实例化SharedPreferences对象（第一步）
                SharedPreferences mySharedPreferences= getSharedPreferences("test",
                        MainActivity.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
                SharedPreferences.Editor editor = mySharedPreferences.edit();
//用putString的方法保存数据
                editor.putString("name", Musername);
                editor.putString("habit", Mpassword);


//提交当前数据
                editor.commit();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==200){
            Bundle bundle = data.getExtras();
            String response = bundle.getString("response");

        }
        SharedPreferences sharedPreferences= getSharedPreferences("test",
                MainActivity.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Musername =sharedPreferences.getString("name", "");
        Mpassword =sharedPreferences.getString("habit", "");
        MeditUsername.setText(Musername);
        MeditPassword.setText(Mpassword);
        textviewLogin.setText(sharedPreferences.getString("status",""));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
