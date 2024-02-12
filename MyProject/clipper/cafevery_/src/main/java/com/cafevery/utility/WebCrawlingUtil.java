package com.cafevery.utility;

import com.cafevery.dto.type.EDay;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.cafevery.dto.type.EDay.parseSchedule;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebCrawlingUtil {

    public Map<EDay, String> getTimeData(String url) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        WebDriver driver = new ChromeDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            driver.get(url);
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".time_operation")));
            } catch (TimeoutException e) {
                log.info("Time operation element not found, skipping: {}", e.getMessage());
                return null;
            }

            List<WebElement> elementsWithLogEvent = driver.findElements(By.cssSelector("[data-logevent='main_info,more_timeinfo']"));

            if (elementsWithLogEvent.isEmpty()) {
                log.info("No more elements");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".txt_operation")));
                List<WebElement> operationElements = driver.findElements(By.cssSelector(".txt_operation"));
                for (WebElement operationElement : operationElements) {
                    log.info("Operation: {}", operationElement.getText());
                    log.info("result: {}", parseSchedule(operationElement.getText()));
                    return parseSchedule(operationElement.getText());
                }
            } else {
                log.info("More elements");
                WebElement moreButton = elementsWithLogEvent.get(0);
                wait.until(ExpectedConditions.elementToBeClickable(moreButton)).click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".list_operation")));
                List<WebElement> listOperationElements = driver.findElements(By.cssSelector(".list_operation"));

                WebElement webElement = listOperationElements.get(1);
                log.info("List Operation: {}", webElement.getText());
                log.info("List Operation: {}", parseSchedule(webElement.getText()));
                return parseSchedule(webElement.getText());
            }

        } catch (TimeoutException e) {
            log.info("TimeoutException: {}", e.getMessage());
            return null;
        } catch (NoSuchElementException e) {
            log.info("NoSuchElementException: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Exception occurred", e);
            return null;
        } finally {
            driver.quit();
        }
        return null;
    }
}
