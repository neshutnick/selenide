package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormTest {
    @BeforeAll
    static void setUpAll() {
        Configuration.headless = true;
        Configuration.browser = "chrome";
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:7777");
    }

    @AfterEach
    void tearDown() {
    }


    @Test // Все поля заполненые корректно, чекбокс отмечен
    void shouldSubmitRequestSuccessfully() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=notification]").should(appear, java.time.Duration.ofSeconds(15));
        $("[data-test-id=notification] .notification__title").shouldHave(text("Успешно!"));
    }

    @Test // Поле город пустое
    void shouldShowErrorIfCityIsEmpty() {
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        String errorText = $("[data-test-id=city].input_invalid .input__sub").getText();
        assertEquals("Поле обязательно для заполнения", errorText.trim());
    }

    @Test // Поле имя пустое
    void shouldShowErrorIfNameIsEmpty() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        String errorText = $("[data-test-id=name].input_invalid .input__sub").getText();
        assertEquals("Поле обязательно для заполнения", errorText.trim());
    }

    @Test // поле номер пустое
    void shouldShowErrorIfPhoneIsEmpty() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        String errorText = $("[data-test-id=phone].input_invalid .input__sub").getText();
        assertEquals("Поле обязательно для заполнения", errorText.trim());
    }

    @Test // чекбокс не отмечен
    void shouldShowErrorIfAgreementNotChecked() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("button.button").click();
        $("[data-test-id=agreement]").shouldHave(cssClass("input_invalid"));
    }

    @Test // имя на латицине
    void shouldShowErrorIfNameIsLatin() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Vasiliy");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        String errorText = $("[data-test-id=name].input_invalid .input__sub").getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.", errorText.trim());
    }

    @Test // телефон заполнен некорректно
    void shouldShowErrorIfPhoneIsInvalid() {

        $("[data-test-id=city] input").setValue("Москва");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("7634234234532");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        String errorText = $("[data-test-id=phone].input_invalid .input__sub").getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.", errorText.trim());
    }

    @Test // город не из списка административных центров
    void shouldShowErrorIfCityIsUnavailable() {
        $("[data-test-id=city] input").setValue("Вашингтон");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Кондраторов");
        $("[data-test-id=phone] input").setValue("+79047749014");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=city] .input__sub").shouldHave(text("Доставка в выбранный город недоступна"));
    }
}