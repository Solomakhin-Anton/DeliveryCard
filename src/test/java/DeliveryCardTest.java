import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;

public class DeliveryCardTest {

    LocalDate today = LocalDate.now();
    LocalDate newDate = today.plusDays(3);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @BeforeEach
    void Setup() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSubmitRequest() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSubmitRequestWithDropDownList() {
        open("http://localhost:9999");
        $("[placeholder='Город']").setValue("Санк");
        $$(".menu-item").first().click();
        $("[placeholder='Дата встречи']").click();
        $(".calendar__day_state_current").click();
        $("[name=name]").setValue("Имя Фамилия");
        $("[name=phone]").setValue("+79815463321");
        $(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSubmitWithIncorrectCity() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Даллас");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Доставка в выбранный город недоступна"));
    }


    @Test
    void shouldSubmitRequestWithoutDate() {
        $("[placeholder='Город']").setValue("Казань");
        $("[name=name]").setValue("Имя Фамилия");
        $("[name=phone]").setValue("+79815463321");
        $(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        $(withText("Успешно")).waitUntil(visible, 15000);
    }

    @Test
    void shouldSubmitWithIncorrectDate() {
        LocalDate newDate = today.plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldSubmitWithoutName() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmitWithIncorrectName() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Name Surname");
        form.$("[name=phone]").setValue("+79815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldSubmitWithoutNumber() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldSubmitWithIncorrectNumber() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("89815463321");
        form.$(".checkbox__box").click();
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_theme_alfa-on-white.input_invalid .input__sub")
                .shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldSubmitWithoutCheckbox() {
        SelenideElement form = $("form[class='form form_size_m form_theme_alfa-on-white']");
        form.$("[placeholder='Город']").setValue("Казань");
        form.$("[placeholder='Дата встречи']").doubleClick().sendKeys(formatter.format(newDate));
        form.$("[name=name]").setValue("Имя Фамилия");
        form.$("[name=phone]").setValue("+79815463321");
        $$(".button__content").find(exactText("Забронировать")).click();
        form.$(".input_invalid")
                .shouldHave(exactText("Я соглашаюсь с условиями обработки и использования моих персональных данных"));
    }
}