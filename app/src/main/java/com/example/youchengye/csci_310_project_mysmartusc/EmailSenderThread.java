package com.example.youchengye.csci_310_project_mysmartusc;

import java.io.IOException;

public class EmailSenderThread extends Thread {

    public void run(){
        EmailTools.sendEmail();
    }

    public void start(){
        super.start();
    }


}
