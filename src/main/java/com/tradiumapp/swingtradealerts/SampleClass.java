package com.tradiumapp.swingtradealerts;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class SampleClass {
    @Autowired
    private Rollbar rollbar;

    public void doSomething(){
        rollbar.debug("22 message");
    }
}
