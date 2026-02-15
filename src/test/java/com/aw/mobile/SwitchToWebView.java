package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SwitchToWebView {

    AndroidDriver androidDriver;
    @Test
    public void switchToWebViewAndroid() throws MalformedURLException, InterruptedException {
        UiAutomator2Options automator2Options = new UiAutomator2Options();
        automator2Options.setPlatformName("Android");
        automator2Options.setPlatformVersion("16.0");
        automator2Options.setAutomationName("UiAutomator2");
        automator2Options.setDeviceName("Samsung");
        //automator2Options.setCapability("appium:ignoreHiddenApiPolicyError", true);
        //automator2Options.setCapability("chromedriver_autodownload", true);
        automator2Options.setApp(System.getProperty("user.dir")+"/src/main/resources/GeneralStore.apk");
        //automator2Options.setCapability("chromedriverExecutableDir", "/Users/shiva/Documents/Appium2Updates/chromedriver139");
        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723"), automator2Options);

        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Enter name here\")")).sendKeys("Test");
        androidDriver.findElement(AppiumBy.className("android.widget.Button")).click();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"ADD TO CART\")")).click();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*appbar_btn_cart\").instance(0)")).click();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*btnProceed\")")).click();

        Thread.sleep(8000);
        Set<String> views = androidDriver.getContextHandles();
        System.out.println(views);
        List<String> androidViews = new ArrayList<>(views);
        System.out.println("androidViews.get(1) :: " + androidViews.get(1) );
        androidDriver.context(androidViews.get(1));
        androidDriver.findElement(AppiumBy.cssSelector("textarea[aria-label=\"Google Search\"]")).sendKeys("Happy Shiva rathri");
        Thread.sleep(3000);

        androidDriver.navigate().back();
        Thread.sleep(1000);
        androidDriver.pressKey(new KeyEvent(AndroidKey.BACK));
        Thread.sleep(3000);
        androidDriver.context(androidViews.get(0));
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Enter name here\")")).sendKeys("Test");
    }
}
