package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SwitchToBrowserAndroid {
    AndroidDriver androidDriver;

    /**
     * appium server --use-plugins=element-wait --allow-insecure chromedriver_autodownload
     * @throws MalformedURLException
     * @throws InterruptedException
     */
    @Test
    public void switchToChrome() throws MalformedURLException, InterruptedException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setAutomationName("UiAutomator2");
        uiAutomator2Options.setPlatformVersion("16.0");
        uiAutomator2Options.setPlatformName("Android");
        uiAutomator2Options.setDeviceName("SamSung");
        uiAutomator2Options.setUdid("emulator-5554");
        //uiAutomator2Options.setCapability("appium:chromedriver_autodownload", true);
        //uiAutomator2Options.setCapability("autoGrantPermissions", true);
        desiredCapabilities.setCapability("appPackage", "com.google.android.apps.messaging");
        uiAutomator2Options.merge(desiredCapabilities);
        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723"), uiAutomator2Options);
        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
        Thread.sleep(5000);

        androidDriver.activateApp("com.google.android.apps.messaging");
        androidDriver.findElement(AppiumBy.accessibilityId("Start chat")).click();

        try {
            androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"ContactSearchField\")")).sendKeys("9885952948");
        } catch (Exception e) {

            androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceIdMatches(\".*recipient_text_view\")")).sendKeys("9885952948");

        }
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"GlideMonogram\")")).click();


        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().textContains(\"Text message\")")).sendKeys("https://appium.io/docs/en/latest/");
        androidDriver.findElement(AppiumBy.accessibilityId("Send SMS")).click();
        Thread.sleep(10000);
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"https://appium.io/docs/en/latest/\").instance(0)")).click();
        Thread.sleep(10000);

        Set<String> views = androidDriver.getContextHandles();
        List<String> androidViews = new ArrayList<>(views);

        System.out.println(androidViews);
        androidDriver.context(androidViews.get(1));

        System.out.println(androidDriver.getCurrentUrl());

//        String test = androidDriver.findElement(AppiumBy.cssSelector("href=\"https://squidfunk.github.io/mkdocs-material/\"")).getText();
//        System.out.println(test);

        androidDriver.activateApp("com.google.android.apps.messaging");
        androidDriver.context(androidViews.get(0));
        Thread.sleep(3000);
        Assert.assertTrue(androidDriver.findElement(AppiumBy.accessibilityId("Back")).isDisplayed());

    }
}
