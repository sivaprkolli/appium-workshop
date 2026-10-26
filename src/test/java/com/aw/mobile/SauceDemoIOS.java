package com.aw.mobile;

import java.time.Duration;

import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;

public class SauceDemoIOS extends IOSBaseTest{

    @Test 
    public void sauceDemoIOSTest() throws InterruptedException{
        Thread.sleep(5000);
        iosDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("value == 'Username'")).click();
        iosDriver.findElement(AppiumBy.iOSNsPredicateString("name == 'test-Username'")).sendKeys("standard_user");
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

    
}
