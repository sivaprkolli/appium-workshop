package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;

public class AndroidTest {
    AndroidDriver androidDriver;

    @Test
    public void login() throws MalformedURLException, InterruptedException {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setPlatformName("Android");
        uiAutomator2Options.setPlatformVersion("16.0");
        uiAutomator2Options.setDeviceName("Samsung");
        uiAutomator2Options.setAutomationName("UiAutomator2");
        uiAutomator2Options.setApp("/Users/shiva/Documents/AppiumWorkshop/src/main/resources/Android_SauceDemo.apk");
        uiAutomator2Options.setAppPackage("com.swaglabsmobileapp");
        uiAutomator2Options.setAppWaitActivity("com.swaglabsmobileapp.MainActivity");

        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);


        Thread.sleep(5000);
        androidDriver.findElement(AppiumBy.accessibilityId("test-Username")).sendKeys("standard_user");
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Password\")")).sendKeys("secret_sauce");
        androidDriver.findElement(AppiumBy.xpath("//*[@content-desc=\"test-LOGIN\"]")).click();
        Thread.sleep(5000);

      //  androidDriver.quit();
    }
}
