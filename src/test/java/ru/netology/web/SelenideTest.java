package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

class SelenideTest {

    private String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    void shouldTest() {
        open("http://localhost:9999");
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Владивосток");
        String planningDate = generateDate(4, "dd.MM.yyyy");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        form.$("[data-test-id=date] input").setValue(planningDate);
        form.$("[data-test-id=name] input").setValue("Иванов Роман");
        form.$("[data-test-id=phone] input").setValue("+79210000101");
        form.$("[data-test-id=agreement]").click();
        form.$("button.button").click();
        $(".notification__content")
                .shouldBe(Condition.visible, Duration.ofSeconds(15))
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + planningDate));
        form.$("button.button").click();
        $("[data-test-id=replan-notification] .notification__title")
                .shouldHave(Condition.exactText("Необходимо подтверждение"))
                .shouldHave(visible);
        String secondPlanningDate = generateDate(7, "dd.MM.yyyy");
        form.$("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        form.$("[data-test-id=date] input").setValue(secondPlanningDate);
        $("[data-test-id=replan-notification] button").click();
        $(".notification__content")
                .shouldHave(Condition.exactText("Встреча успешно запланирована на " + secondPlanningDate))
                .shouldHave(visible);
        form.$("button.button").click();

    }
}
