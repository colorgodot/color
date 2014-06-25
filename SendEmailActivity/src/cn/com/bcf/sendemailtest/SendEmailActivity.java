package cn.com.bcf.sendemailtest;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendEmailActivity extends Activity {
	private Button send; 
	private EditText userid; 
	private EditText password; 
	private EditText from; 
	private EditText to; 
	private EditText subject; 
	private EditText body;
	LocationManager locManager;
	Handler handler;
	LocationThread locThread = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        send = (Button) findViewById(R.id.send); 
        userid = (EditText) findViewById(R.id.userid); 
        password = (EditText) findViewById(R.id.password); 
        from = (EditText) findViewById(R.id.from); 
        to = (EditText) findViewById(R.id.to); 
        subject = (EditText) findViewById(R.id.subject); 
        body = (EditText) findViewById(R.id.body);
        
        send.setText("Send Mail");
        userid.setText("godotcolor@163.com"); //��������û���
        password.setText("378499112");         //��������½����
        from.setText("godotcolor@163.com");//���͵�����
        to.setText("godotcolor@163.com"); //�����ĸ��ʼ�ȥ
        
        subject.setText("当前位置");
        body.setText("当前位置：");
        
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); 
        Location location = locManager.getLastKnownLocation(
                LocationManager.GPS_PROVIDER);
        
        updateView(location);
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER 
                , 10000l, 8.0f, new LocationListener()
            {
                @Override
                public void onLocationChanged(Location location)
                {
                    // 当GPS定位信息发生改变时，更新位置
                    updateView(location);
                }

                @Override
                public void onProviderDisabled(String provider)
                {
                    updateView(null);               
                }

                @Override
                public void onProviderEnabled(String provider)
                {
                    // 当GPS LocationProvider可用时，更新位置
                    updateView(locManager
                        .getLastKnownLocation(provider));               
                }

                @Override
                public void onStatusChanged(String provider, int status,
                    Bundle extras)
                {
                }
            }); 
        handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                // 如果消息来自于子线程
                if (msg.what == 0x123)
                {
                    Location location = locManager.getLastKnownLocation(
                            LocationManager.GPS_PROVIDER);
                    
                    updateView(location);
                    Log.d("SendMail", "handleMessage"); 
                } else if (msg.what == 0x124) {
                    locThread = null;
                    body.setText("Send is Stopped");
                    send.setText("Start Send");
                }
            }
        };
  
        
        send.setOnClickListener(new View.OnClickListener() 
        {
            
            @Override 
            public void onClick(View v) 
            {    
                // TODO Auto-generated method stub                    
                try 
                {   
                    if (null == locThread) {
                        locThread = new LocationThread(handler);
                    }
                    
                    if (locThread.getLoop() == true) {
                        locThread.setLoop(false);
                    } else {
                        locThread.setLoop(true);
                        locThread.start(); 
                        send.setText("Stop Send");
                    }

                 /*
               	 MailSenderInfo mailInfo = new MailSenderInfo();    
                 mailInfo.setMailServerHost("smtp.163.com");    
                 mailInfo.setMailServerPort("25");    
                 mailInfo.setValidate(true);    
                 mailInfo.setUserName(userid.getText().toString());  //��������ַ  
                 mailInfo.setPassword(password.getText().toString());//�����������    
                 mailInfo.setFromAddress(from.getText().toString());    
                 mailInfo.setToAddress(to.getText().toString());    
                 mailInfo.setSubject(subject.getText().toString());    
                 mailInfo.setContent(body.getText().toString());    
                 
                    //�������Ҫ4�����ʼ�   
                 SimpleMailSender sms = new SimpleMailSender();   
                     sms.sendTextMail(mailInfo);//���������ʽ    
                     //sms.sendHtmlMail(mailInfo);//����html��ʽ 
                     body.setText("smtp.163.com, 25");
                 */
                } 
                catch (Exception e) { 
                    Log.e("SendMail", e.getMessage(), e); 
                }
            } 
        }); 
    }
    
    
    public void updateView(Location newLocation) {
        //if ()
        {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息：\n");
            sb.append("经度：");
            if (newLocation != null)
                sb.append(newLocation.getLongitude());
            
            sb.append("\n纬度：");
            if (newLocation != null)
                sb.append(newLocation.getLatitude());
            sb.append("\n高度：");
            if (newLocation != null)
                sb.append(newLocation.getAltitude());
            sb.append("\n速度：");
            if (newLocation != null)
                sb.append(newLocation.getSpeed());
            sb.append("\n方向：");
            if (newLocation != null)
                sb.append(newLocation.getBearing());            
            
            
            // TODO Auto-generated method stub                    
            try 
            { 
             MailSenderInfo mailInfo = new MailSenderInfo();    
             mailInfo.setMailServerHost("smtp.163.com");    
             mailInfo.setMailServerPort("25");    
             mailInfo.setValidate(true);    
             mailInfo.setUserName(userid.getText().toString());  //��������ַ  
             mailInfo.setPassword(password.getText().toString());//�����������    
             mailInfo.setFromAddress(from.getText().toString());    
             mailInfo.setToAddress(to.getText().toString());    
             mailInfo.setSubject(subject.getText().toString());    
             mailInfo.setContent(sb.toString());
             
             
                //�������Ҫ4�����ʼ�   
             SimpleMailSender sms = new SimpleMailSender();
                 sms.sendTextMail(mailInfo);//���������ʽ                     
                 body.setText(sb.toString());
            } 
            catch (Exception e) { 
                Log.e("SendMail", e.getMessage(), e); 
            }
        
        }
    }
}