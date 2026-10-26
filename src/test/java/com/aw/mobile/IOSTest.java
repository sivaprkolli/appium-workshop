package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;

import java.net.MalformedURLException;

import java.net.URL;
import java.time.Duration;
import java.util.Collections;

public class IOSTest extends IOSBaseTest {
    IOSDriver iosDriver;

    @Test
    public void loginIOSTest() throws MalformedURLException, InterruptedException {
        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        xcuiTestOptions.setPlatformName("iOS");
        xcuiTestOptions.setPlatformVersion("27.0");
        xcuiTestOptions.setDeviceName("iPhone 18 Pro Max");
        xcuiTestOptions.setAutomationName("xcuitest");

        //00008150-001E70962601401C 
        //xcuiTestOptions.setUdid("C258E876-3649-47DC-8301-9802BED026BC");
        xcuiTestOptions.setUdid("478730CB-B101-4E35-A790-7B657BC0D0D5");
        //xcuiTestOptions.setApp("/Users/shiva/Documents/AppiumWorkshop/src/main/resources/IOS_SauceDemo.app");

        xcuiTestOptions.setApp("file-1792648510542.zip");
        //xcuiTestOptions.setAppPackage("com.swaglabsmobileapp");
        //xcuiTestOptions.setAppWaitActivity("com.swaglabsmobileapp.MainActivity");
        xcuiTestOptions.setCapability("df:build", "IOS Test Latest");
        //xcuiTestOptions.setCapability("df:name", "Login - Valid User");
       // xcuiTestOptions.setCapability("df:title", "Login - Valid User");
         xcuiTestOptions.setCapability("df:liveVideo", true);
        xcuiTestOptions.setCapability("df:recordVideo", true);
        xcuiTestOptions.setCapability("df:recordScreenshots", true);
        xcuiTestOptions.setCapability("df:recordLogs", true);
        xcuiTestOptions.setCapability("df:recordPerformance", true);    
        xcuiTestOptions.setCapability("df:recordNetwork", true);
        xcuiTestOptions.setCapability("df:recordSystemLogs", true);
        xcuiTestOptions.setCapability("df:recordDeviceLogs", true);
        xcuiTestOptions.setCapability("df:recordCrashLogs", true);
        xcuiTestOptions.setCapability("df:recordAppiumLogs", true);
        xcuiTestOptions.setCapability("df:release_on_session_end", true);
        // xcuiTestOptions.setCapability("df:accesskey", "admin_FZTyLhwCG7iTvH");
        // xcuiTestOptions.setCapability("df:token", "43062429-b480-4fa6-a9e2-505d3480ed64");
        //xcuiTestOptions.setCapability("setSessionName", "Session loginIOSTest");

        iosDriver = new IOSDriver(new URL("http://172.16.3.178/:4723/wd/hub"), xcuiTestOptions);

        Thread.sleep(5000);
        iosDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("value == 'Username'")).click();
        iosDriver.findElement(AppiumBy.accessibilityId("test-Username")).sendKeys("standard_user");
        //iosDriver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeSecureTextField' AND value == 'Password'")).sendKeys("");
        iosDriver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeSecureTextField[$value == \"Password\"$]")).clear();
        iosDriver.findElement(AppiumBy.iOSClassChain("**/XCUIElementTypeSecureTextField[$value == \"Password\"$]")).sendKeys("secret_sauce");
        iosDriver.findElement(AppiumBy.name("test-LOGIN")).click();


        Thread.sleep(5000);
        swipeVertical();
        Thread.sleep(5000);

        iosDriver.findElement(AppiumBy.xpath("(//XCUIElementTypeOther[@name=\"test-ADD TO CART\"])[1]")).click();
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("name == 'test-Cart'")).click();
        Thread.sleep(3000);
        swipeHorizontallyOnElement();
        Thread.sleep(3000);
        swipeHorizontal();
        //iosDriver.findElement(AppiumBy.iOSNsPredicateString("name == 'test-Delete'")).click();

    }

    @AfterTest 
    public void killSession(){
        iosDriver.quit();
    }



    public void swipeHorizontallyOnElement(){
        Dimension dimension = iosDriver.findElement(AppiumBy.iOSNsPredicateString("name == 'test-REMOVE'")).getSize();
        System.out.println(dimension);

        int startX = (int) (dimension.getWidth() * 0.8);
        int endX = (int) (dimension.getWidth() * 0.4);

        int y = dimension.getHeight()/2;

        PointerInput pointerInput = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(pointerInput, 1);

        sequence.addAction(pointerInput.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
        sequence.addAction(pointerInput.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        sequence.addAction(pointerInput.createPointerMove(Duration.ofSeconds(2), PointerInput.Origin.viewport(), endX, y));
        sequence.addAction(pointerInput.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        iosDriver.perform(Collections.singletonList(sequence));

    }


    public void swipeHorizontal(){
        Dimension dimension = iosDriver.manage().window().getSize();
        System.out.println(dimension);

        int startX = (int) (dimension.getWidth() * 0.8);
        int endX = (int) (dimension.getWidth() * 0.4);

        int y = dimension.getHeight()/3;

        PointerInput pointerInput = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence sequence = new Sequence(pointerInput, 1);

        sequence.addAction(pointerInput.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, y));
        sequence.addAction(pointerInput.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));

        sequence.addAction(pointerInput.createPointerMove(Duration.ofSeconds(2), PointerInput.Origin.viewport(), endX, y));
        sequence.addAction(pointerInput.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

        iosDriver.perform(Collections.singletonList(sequence));
    }

    public void swipeVertical(){
        Dimension dimension = iosDriver.manage().window().getSize();
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

        iosDriver.perform(Collections.singletonList(sequence));

    }
}
