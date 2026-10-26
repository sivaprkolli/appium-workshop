package com.aw.mobile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;

/**
 * Face ID review flow for the Apex dispatch app.
 *
 * IMPORTANT — this app's "Face ID" is CAMERA-BASED face recognition, NOT the Android
 * system BiometricPrompt/fingerprint sensor. Confirmed from a runtime screenshot: after
 * "Begin check" the app opens a live camera capture screen and, when no matching face is
 * presented, shows an "Unable to login" dialog with "Use Employee ID" / "Try again".
 *
 * Consequences for automation:
 *   - "mobile: fingerprint" has NO effect on this flow (it targets BiometricPrompt, which
 *     this screen does not use). It is kept here only for completeness of the biometric step.
 *   - For the login to actually succeed, the emulator's virtual (front) camera must present a
 *     face that matches the one enrolled in the app. A default emulator camera feed will not
 *     match, so the login fails with "Unable to login".
 *
 * This test therefore verifies the ACTUAL outcome: it fails if the "Unable to login" error
 * state is shown, and only passes when a genuine post-login screen is reached.
 */
public class BiometricHandle {

    private static final int FINGERPRINT_ID = 1;
    private static final String APPIUM_URL = "http://127.0.0.1:4723";
    private static final String APP_PATH =
            "/Users/shivak/Documents/appium-workshop/src/main/resources/ilAndroid.apk";

    private AndroidDriver androidDriver;

    @Test
    public void biometricCheck() throws MalformedURLException, InterruptedException, IOException {
        UiAutomator2Options uiAutomator2Options = new UiAutomator2Options();
        uiAutomator2Options.setPlatformName("Android");
        uiAutomator2Options.setPlatformVersion("17.0");
        uiAutomator2Options.setDeviceName("Samsung");
        uiAutomator2Options.setAutomationName("UiAutomator2");
        uiAutomator2Options.setApp(APP_PATH);

        androidDriver = new AndroidDriver(new URL(APPIUM_URL), uiAutomator2Options);
        androidDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        // 1. Enroll a fingerprint so the "Face ID" (BiometricPrompt) has something to match against.
        //    On the emulator this only works when a secure lock screen credential is configured.
        Map<String, Object> enrollArgs = new HashMap<>();
        enrollArgs.put("fingerprintId", FINGERPRINT_ID);
        androidDriver.executeScript("mobile: fingerprint", enrollArgs);

        // 2. Kick off the app's Face ID authentication flow.
        androidDriver.findElement(AppiumBy.accessibilityId("Employee Face ID")).click();
        androidDriver.findElement(
                AppiumBy.androidUIAutomator("new UiSelector().text(\"While using the app\")")).click();
        androidDriver.findElement(AppiumBy.accessibilityId("Begin check")).click();

        // 3. Wait for the BiometricPrompt to appear, then satisfy it with the enrolled fingerprint.
        Thread.sleep(3000);
        Map<String, Object> matchArgs = new HashMap<>();
        matchArgs.put("fingerprintId", FINGERPRINT_ID);
        androidDriver.executeScript("mobile: fingerprint", matchArgs);

        // 4. The face scan runs asynchronously ("Hold still" scanning phase). Poll until the flow
        //    reaches a TERMINAL state - either the "Unable to login" failure dialog, or a genuine
        //    post-login screen - instead of checking at an arbitrary fixed delay.
        Outcome outcome = waitForFaceIdOutcome(Duration.ofSeconds(25));

        // 5. Always capture a screenshot of the final state as evidence (pass OR fail).
        Path screenshotPath = captureScreenshot("faceid_login_" + outcome.name().toLowerCase());
        System.out.println("Face ID outcome: " + outcome + ". Screenshot saved to: " + screenshotPath);

        // 6. Assert the real result.
        Assert.assertEquals(outcome, Outcome.SUCCESS,
                "Face ID login did not succeed (outcome=" + outcome + "). "
                        + "This app uses camera-based face recognition; on the emulator the virtual "
                        + "camera does not present a face matching the enrolled one, so login fails "
                        + "with 'Unable to login'. See screenshot: " + screenshotPath);
    }

    private enum Outcome { SUCCESS, FAILED, TIMEOUT }

    /**
     * Polls the UI until the Face ID flow settles into a terminal state or the timeout elapses.
     * FAILED  -> the "Unable to login" / "Try again" dialog is visible.
     * SUCCESS -> the failure dialog is gone AND the app has left the scan screen
     *            (no "Hold still" / "Begin check"), i.e. a post-login screen is shown.
     */
    private Outcome waitForFaceIdOutcome(Duration timeout) throws InterruptedException {
        long deadline = System.currentTimeMillis() + timeout.toMillis();
        while (System.currentTimeMillis() < deadline) {
            // FAILURE takes priority and must be checked first: the app briefly shows neither the
            // scan UI nor the dialog while transitioning, so we must NOT infer success from that gap.
            // NOTE: this is a React Native app - labels are exposed via content-desc (accessibility
            // id), NOT the "text" attribute. So we match on description, not text.
            boolean failureVisible =
                    !androidDriver.findElements(
                            AppiumBy.accessibilityId("Unable to login")).isEmpty()
                    || !androidDriver.findElements(
                            AppiumBy.accessibilityId("Try again")).isEmpty();
            if (failureVisible) {
                return Outcome.FAILED;
            }

            // SUCCESS requires a POSITIVE post-login signal (the app's home/dashboard), never merely
            // the absence of the scan screen.
            boolean loggedIn =
                    !androidDriver.findElements(AppiumBy.androidUIAutomator(
                            "new UiSelector().descriptionContains(\"Dashboard\")")).isEmpty()
                    || !androidDriver.findElements(AppiumBy.androidUIAutomator(
                            "new UiSelector().descriptionContains(\"Log out\")")).isEmpty();
            if (loggedIn) {
                return Outcome.SUCCESS;
            }

            Thread.sleep(1000);
        }
        return Outcome.TIMEOUT;
    }

    /**
     * Saves a PNG screenshot of the current screen under target/screenshots and returns its path.
     */
    private Path captureScreenshot(String name) throws IOException {
        File src = ((TakesScreenshot) androidDriver).getScreenshotAs(OutputType.FILE);
        Path dir = Paths.get("target", "screenshots");
        Files.createDirectories(dir);
        Path dest = dir.resolve(name + "_" + System.currentTimeMillis() + ".png");
        Files.copy(src.toPath(), dest);
        return dest.toAbsolutePath();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (androidDriver != null) {
            androidDriver.quit();
        }
    }
}
