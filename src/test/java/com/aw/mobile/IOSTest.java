package com.aw.mobile;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.PortUnreachableException;
import java.net.URL;
import java.time.Duration;
import java.util.Collections;

public class IOSTest {
    IOSDriver iosDriver;

    @Test
    public void loginIOSTest() throws MalformedURLException, InterruptedException {
        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        xcuiTestOptions.setPlatformName("iOS");
        xcuiTestOptions.setPlatformVersion("26.0");
        xcuiTestOptions.setDeviceName("iPhone 16e");
        xcuiTestOptions.setAutomationName("xcuitest");
        xcuiTestOptions.setApp("/Users/shiva/Documents/AppiumWorkshop/src/main/resources/IOS_SauceDemo.app");

        iosDriver = new IOSDriver(new URL("http://127.0.0.1:4724"), xcuiTestOptions);

        Thread.sleep(5000);
        iosDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("value == 'Username'")).click();
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("value == 'Username'")).sendKeys("standard_user");
        //iosDriver.findElement(AppiumBy.iOSNsPredicateString("type == 'XCUIElementTypeSecureTextField' AND value == 'Password'")).sendKeys("");
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
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("name == 'test-Delete'")).click();

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
