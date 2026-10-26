package com.aw.mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.net.URL;
import java.time.Duration;

public class AndroidTest {
    AndroidDriver androidDriver;

    @BeforeTest
    public void setup() throws Exception {
        // Load device configuration from JSON.
        // Select device via: mvn test -Ddevice=Samsung  (defaults to first entry)
        String targetDevice = System.getProperty("device");

        ObjectMapper mapper = new ObjectMapper();
        InputStream configStream = getClass().getClassLoader()
                .getResourceAsStream("android_config.json");
        JsonNode root = mapper.readTree(configStream);
        JsonNode config = findDevice(root, targetDevice);

        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setPlatformName(config.get("platformName").asText());
        uiAutomator2Options.setPlatformVersion(config.get("platformVersion").asText());
        uiAutomator2Options.setDeviceName(config.get("deviceName").asText());
        uiAutomator2Options.setAutomationName(config.get("automationName").asText());
        uiAutomator2Options.setApp(config.get("app").asText());
        uiAutomator2Options.setCapability("df:build", config.get("dfBuild").asText());

        uiAutomator2Options.setCapability("df:liveVideo", true);
        uiAutomator2Options.setCapability("df:recordVideo", true);
        uiAutomator2Options.setCapability("df:recordScreenshots", true);
        uiAutomator2Options.setCapability("df:recordLogs", true);
        uiAutomator2Options.setCapability("df:recordPerformance", true);
        uiAutomator2Options.setCapability("df:recordNetwork", true);
        uiAutomator2Options.setCapability("df:recordSystemLogs", true);
        uiAutomator2Options.setCapability("df:recordDeviceLogs", true);
        uiAutomator2Options.setCapability("df:recordCrashLogs", true);
        uiAutomator2Options.setCapability("df:recordAppiumLogs", true);
        uiAutomator2Options.setCapability("df:release_on_session_end", true);
        uiAutomator2Options.setCapability("df:accesskey", "admin_FZTyLhwCG7iTvH");
        uiAutomator2Options.setCapability("df:token", "43062429-b480-4fa6-a9e2-505d3480ed64");

        androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), uiAutomator2Options);
        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    @AfterTest
    public void killServer(){
        androidDriver.quit();
    }

    /**
     * Finds a device entry from the "devices" array in the JSON config.
     * Matches by "name" field (case-insensitive). Falls back to the first entry
     * if targetDevice is null or no match is found.
     */
    private JsonNode findDevice(JsonNode root, String targetDevice) {
        JsonNode devices = root.get("devices");
        if (targetDevice != null) {
            for (JsonNode device : devices) {
                if (device.get("name").asText().equalsIgnoreCase(targetDevice)) {
                    System.out.println("[Config] Using Android device: " + device.get("name").asText());
                    return device;
                }
            }
            System.out.println("[Config] Device '" + targetDevice + "' not found, falling back to first entry.");
        }
        System.out.println("[Config] Using Android device: " + devices.get(0).get("name").asText());
        return devices.get(0);
    }

    @Test
    public void login() throws InterruptedException {

        Thread.sleep(5000);

        androidDriver.findElement(AppiumBy.accessibilityId("Menu")).click();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Sign In\")")).click();

        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"UserName\")")).click();
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"demouser\")")).click();
        Thread.sleep(3000);
        androidDriver.findElement(AppiumBy.androidUIAutomator("new UiSelector().text(\"Sign In\")")).click();
        Thread.sleep(5000);
        
    }
}
