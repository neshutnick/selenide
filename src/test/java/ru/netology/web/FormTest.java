package ru.netology.web;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FormTest {
   
    @BeforeEach
    void setUp() {
        open("http://localhost:7777");
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
        $("[data-test-id=notification] .notification__content")
            .shouldBe(visible, Duration.ofSeconds(15))
            .shouldHave(exactText("Встреча успешно забронирована на " + date));
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
        $("[data-test-id=city].input_invalid .input__sub")
            .shouldBe(visible)
            .shouldHave(exactText("Поле обязательно для заполнения"));
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
        $("[data-test-id=name].input_invalid .input__sub")
            .shouldBe(visible)
            .shouldHave(exactText("Поле обязательно для заполнения"));
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
        $("[data-test-id=phone].input_invalid .input__sub")
            .shouldBe(visible)
            .shouldHave(exactText("Поле обязательно для заполнения"));
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
        $("[data-test-id=name].input_invalid .input__sub")
            .shouldBe(visible)
            .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
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
        $("[data-test-id=phone].input_invalid .input__sub")
            .shouldBe(visible)
            .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test // город не из списка административных центров
    void shouldShowErrorIfCityIsUnavailable() {

        $("[data-test-id=city] input").setValue("Вашингтон");
        String date = DataGenerator.generateDate(3);
        var dateInput = $("[data-test-id=date] input");
        dateInput.doubleClick().sendKeys(date);
        $("[data-test-id=name] input").setValue("Иванов-Петров Иван");
        $("[data-test-id=phone] input").setValue("+79270000000");
        $("[data-test-id=agreement]").click();
        $("button.button").click();
        $("[data-test-id=city] .input__sub")
             .shouldBe(visible)
             .shouldHave(text("Доставка в выбранный город недоступна"));
    }
}