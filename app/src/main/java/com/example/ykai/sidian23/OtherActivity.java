package com.example.ykai.sidian23;

/**
 * Created by ykai on 2015/2/3.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class OtherActivity extends Activity {
    private TextView view ;
    private Button b2;
    private Button breL;
    private Button bexit;
    private TextView result_text=null;
    private static  Handler handler=new Handler();
    private static    String Oname;
    private static    String Opassword;
    private static    String Oresult;
    private static String Ouid;
    String uriAPI="";
    private static String Osignuptime;
    private static String Ogetuptime;
    private TextView tv2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.mylayout);
        SysApplication.getInstance().addActivity(this);
        tv2 = (TextView)this.findViewById(R.id.tv2);
        b2=(Button)this.findViewById(R.id.b2);
        bexit=(Button)this.findViewById(R.id.button_exit);
        breL=(Button)this.findViewById(R.id.button_relogin);
        result_text=(TextView)this.findViewById(R.id.result_text);
        Intent intent =this.getIntent();//得到激活她的意图    //创建一个Intent

       SharedPreferences sharedPreferences= getSharedPreferences("test",
                MainActivity.MODE_PRIVATE);
// 使用getString方法获得value，注意第2个参数是value的默认值
        Oname =sharedPreferences.getString("name", "");
        Opassword =sharedPreferences.getString("habit", "");
//使用toast信息提示框显示信息

        tv2.setText("欢迎："+ Oname);
//登录部分


  //如果登录失败

        b2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new MyThread()).start();

         //       finish();

            }
        });
        bexit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SysApplication.getInstance().exit();
            }
        });
        breL.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
             finish();
            }
        });


        //
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {

            SysApplication.getInstance().exit();

        }

        return super.onKeyDown(keyCode, event);
    }
    public class MyThread implements Runnable {

        @Override
        public void run() {
       // 下载xml
            HttpPost httpRequest;
            HttpResponse httpResponse;
            try{

             uriAPI=   "http://4dian.sinaapp.com/api/login/email/"+Oname+"/password/"+Opassword;

                httpRequest = new HttpPost(uriAPI);

            httpResponse = new DefaultHttpClient().execute(httpRequest);


            if (httpResponse.getStatusLine().getStatusCode() == 200) {

                Oresult = EntityUtils.toString(httpResponse
                        .getEntity());
              Oresult= "{'user':    "+ Oresult+ "   }";



                // mTextView1.setText(strResult);
            } else {
                // mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
            }
            } catch (Exception e) {
                e.printStackTrace();


            }


            try {
                JSONObject jsonObj = new JSONObject(Oresult).getJSONObject("user");

                Ouid = jsonObj.getString("uid");
                Osignuptime =jsonObj.getString("signuptime");
                Oresult=Oresult+Ouid;
            }
            catch(JSONException e) {

            }


            //    *****************************    签到

            uriAPI="http://4dian.sinaapp.com/api/get1up/"+Ouid;
            httpRequest = new HttpPost(uriAPI);
            try {
                httpResponse = new DefaultHttpClient().execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {

                    Oresult = EntityUtils.toString(httpResponse
                            .getEntity());
                    Oresult= "{'user':    "+ Oresult+ "   }";



                    // mTextView1.setText(strResult);
                } else {
                    // mTextView1.setText("Error Response: "+httpResponse.getStatusLine().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


            try {
                JSONObject jsonObj = new JSONObject(Oresult).getJSONObject("user");

                Ouid = jsonObj.getString("uid");
                Ogetuptime =jsonObj.getString("gp_time");

            }
            catch(JSONException e) {

            }

            handler.post(new Runnable() {
                @Override
                public void run() {



                    // 在Post中操作UI组件mTextView

    if( Oresult.indexOf("fail")  > -1 ||   Oresult.indexOf("error")  > -1 ) {


        SharedPreferences mySharedPreferences= getSharedPreferences("test",
                MainActivity.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
        SharedPreferences.Editor editor = mySharedPreferences.edit();
//用putString的方法保存数据
        editor.putString("status", "密码错误");

        //提交当前数据
        editor.commit();

    finish();
    }
    else
   {


       SharedPreferences mySharedPreferences= getSharedPreferences("test",
               MainActivity.MODE_PRIVATE);
//实例化SharedPreferences.Editor对象（第二步）
       SharedPreferences.Editor editor = mySharedPreferences.edit();
//用putString的方法保存数据
       editor.putString("status", "欢迎来到四点网");

       //提交当前数据
       editor.commit();
      result_text.setText("签到成功！距离梦想又进了一步！");
       AlertDialog.Builder builder  = new AlertDialog.Builder(OtherActivity.this);
       builder.setTitle("签到" ) ;
       builder.setMessage("您今天是"+Ogetuptime+"起床   击败了全社区不知道多少用户" ) ;
       builder.setPositiveButton("确认" ,  null );
       builder.show();
   }
                }
            });


        }
    }

}