package com.example.optipark;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.optipark.databinding.ActivityContactBinding;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import Util.AppData;

// Allows users to send messages to support via email.
public class ContactActivity extends DrawerBaseActivity implements View.OnClickListener {

    // Variables
    ActivityContactBinding activityContactBinding;
    EditText cName, cSubject, cMessage;
    String cn, cs, cm;
    Button cSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactBinding = ActivityContactBinding.inflate(getLayoutInflater());
        setTitle("Contact");
        setContentView(activityContactBinding.getRoot());
        init_ui();
    }

    // Initialize views
    private void init_ui() {
        cName = findViewById(R.id.cName);
        cSubject = findViewById(R.id.cSubject);
        cMessage = findViewById(R.id.cMessage);
        cSend = findViewById(R.id.cSend);
        cSend.setOnClickListener(this);
    }

    // Retrieves input from EditText fields, validates them, and sends the email.
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cSend) {
            cn = cName.getText().toString();
            cs = cSubject.getText().toString();
            cm = cMessage.getText().toString();
            // Check if any of the fields is empty
            if (cn.isEmpty() || cs.isEmpty() || cm.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }
            send_email(cn, cs, cm);
        } else {

        }
    }

    // Sends the email with the provided information.
    // Uses JavaMail API to send an email through SMTP.
    private void send_email(String cn, String cs, String cm) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", AppData.Gmail_Host);
        properties.put("mail.smtp,port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AppData.Sender_Email_Address, AppData.Sender_Email_Password);
            }
        });

        MimeMessage message = new MimeMessage(session);
        try {
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AppData.Receiver_Email_address));
            message.setSubject(cs);
            message.setText("From " + cn + "\n" + cm);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show();

        // Clear EditText fields
        cName.setText("");
        cSubject.setText("");
        cMessage.setText("");
    }
}