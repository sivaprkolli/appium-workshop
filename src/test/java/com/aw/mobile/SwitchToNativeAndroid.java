package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;

public class SwitchToNativeAndroid {

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
        automator2Options.setApp(System.getProperty("user.dir") + "/src/main/resources/NoBroker.apk");
        automator2Options.setAppPackage("com.nobroker.app");
        automator2Options.setAppWaitActivity("com.nobroker.app.activities.NBLauncherActivity");
        //automator2Options.setCapability("chromedriverExecutableDir", "/Users/shiva/Documents/Appium2Updates/chromedriver139");
        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723"), automator2Options);

        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

       WebDriverWait wait = new WebDriverWait(androidDriver, Duration.ofSeconds(30));
        Thread.sleep(5000);

        wait
                .until(ExpectedConditions
                        .elementToBeClickable(AppiumBy
                                .xpath("//*[ends-with(@resource-id,'yesPhoneState')]")))
                .click();

        //*[@text='Allow']
        wait
                .until(ExpectedConditions
                        .elementToBeClickable(AppiumBy
                                .androidUIAutomator("new UiSelector().text(\"While using the app\")")))
                .click();
        wait
                .until(ExpectedConditions
                        .elementToBeClickable(AppiumBy
                                .androidUIAutomator("new UiSelector().text(\"Allow\")")))
                .click();

        wait
                .until(ExpectedConditions
                        .elementToBeClickable(AppiumBy
                                .androidUIAutomator("new UiSelector().text(\"Allow\")")))
                .click();

    /*    for (int i = 0; i < 5; i++) {
            try {
                androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Need Assistance?\")")).isDisplayed();
                androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Need Assistance?\")")).click();
                break;
            } catch (NoSuchElementException nse) {
                swipeVertical();
            }
        }*/

        swipeVertical();
        swipeVertical();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Need Assistance?\")")).click();
        wait.until(ExpectedConditions.elementToBeClickable(AppiumBy.accessibilityId("dial"))).click();

        androidDriver.findElement(AppiumBy.accessibilityId("End call")).click();
        Thread.sleep(5000);
        androidDriver.activateApp("com.nobroker.app");

        Assert.assertTrue(androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Need Assistance?\")")).isDisplayed());



    }


    public void swipeVertical(){
        Dimension dimension = androidDriver.manage().window().getSize();
        System.out.println(dimension);

        int x = dimension.getWidth() /2;
        int startY = (int) (dimension.getHeight() * 0.9);
        int endY = (int) (dimension.getHeight() * 0.3);

        PointerInput pointerInput = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(pointerInput, 1);

        sequence.addAction(pointerInput.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, startY));
        sequence.addAction(pointerInput.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        sequence.addAction(pointerInput.createPointerMove(Duration.ofSeconds(1), PointerInput.Origin.viewport(), x, endY));
        sequence.addAction(pointerInput.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        androidDriver.perform(Collections.singletonList(sequence));

    }
}
