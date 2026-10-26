package com.aw.mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import java.net.URL;
import java.time.Duration;

public class IOSBaseTest {
    
     public IOSDriver iosDriver;


     /*
     # Android
mvn test -Dtest=AndroidTest -Ddevice=Samsung
mvn test -Dtest=AndroidTest -Ddevice=Pixe

# iOS (BaseTest / RealDevice)
mvn test -Dtest=RealDevice -Ddevice=iPhone17
mvn test -Dtest=SauceDemoIOS -Ddevice=iPhone18ProMax

     
     */

    @BeforeTest 
    public void setup() throws Exception {
        // Load device configuration from JSON.
        // Select device via: mvn test -Ddevice=iPhone17  (defaults to first entry)
        String targetDevice = System.getProperty("device");
        ObjectMapper mapper = new ObjectMapper();
        InputStream configStream = getClass().getClassLoader()
                .getResourceAsStream("ios_config.json");
        JsonNode root = mapper.readTree(configStream);
        JsonNode config = findDevice(root, targetDevice);

        XCUITestOptions xcuiTestOptions = new XCUITestOptions();
        xcuiTestOptions.setPlatformName(config.get("platformName").asText());
        xcuiTestOptions.setPlatformVersion(config.get("platformVersion").asText());
        xcuiTestOptions.setDeviceName(config.get("deviceName").asText());
        xcuiTestOptions.setAutomationName(config.get("automationName").asText());
        xcuiTestOptions.setUdid(config.get("udid").asText());
      //  xcuiTestOptions.setBundleId(config.get("bundleId").asText());
        xcuiTestOptions.setApp(config.get("app").asText());
        xcuiTestOptions.setCapability("df:build", config.get("dfBuild").asText());

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

        iosDriver = new IOSDriver(new URL("http://172.16.3.178:4723/wd/hub"), xcuiTestOptions);

    }

    @BeforeMethod
    public void setTestName(Method method) {
        String testName = getClass().getSimpleName()
                + "." + method.getName();

        ((JavascriptExecutor) iosDriver).executeScript(
            "devicefarm: setSessionName",
            new Object[] {
                Map.of("name", testName)
            }
        );
    }

    @AfterTest 
    public void killSession(){
        iosDriver.quit();
    }

    private JsonNode findDevice(JsonNode root, String targetDevice) {
        JsonNode devices = root.get("devices");
        if (targetDevice != null) {
            for (JsonNode device : devices) {
                if (device.get("name").asText().equalsIgnoreCase(targetDevice)) {
                    System.out.println("[Config] Using iOS device: " + device.get("name").asText());
                    return device;
                }
            }
            System.out.println("[Config] Device '" + targetDevice + "' not found, falling back to first entry.");
        }
        System.out.println("[Config] Using iOS device: " + devices.get(0).get("name").asText());
        return devices.get(0);
    }

    @AfterMethod(alwaysRun = true)
public void updateDeviceFarmStatus(ITestResult result) {
    String status;

    if (result.getStatus() == ITestResult.SUCCESS) {
        status = "passed";
    } else if (result.getStatus() == ITestResult.FAILURE) {
        status = "failed";
    } else {
        status = "skipped";
    }

    try {
        ((JavascriptExecutor) iosDriver).executeScript(
            "devicefarm: setSessionStatus",
            new Object[]{Map.of("status", status)}
        );
    } catch (Exception e) {
        System.err.println("Failed to update Device Farm session status: "
                + e.getMessage());
    }
}

    public void updateSessionStatus(AppiumDriver driver, String status) {
    ((JavascriptExecutor) driver).executeScript(
        "devicefarm: setSessionStatus",
        new Object[]{
            java.util.Map.of("status", status)
        }
    );
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
