package de.hsa.movietvratings.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route("login")
@PermitAll
public class LoginView extends Div implements BeforeEnterObserver {
    private final LoginForm login;

    public LoginView() {
        setSizeFull();

        FlexLayout container = new FlexLayout();
        container.setFlexDirection(FlexLayout.FlexDirection.COLUMN);
        container.setSizeFull();

        container.addClassName("login-container");

        this.login = new LoginForm();
        this.login.setI18n(createGermanI18n());

        this.login.getElement().getThemeList().add("dark");
        this.login.setForgotPasswordButtonVisible(true);
        this.login.setAction("login");
        this.login.addForgotPasswordListener(event -> getUI().ifPresent(ui -> ui.navigate(RegisterView.class)));

        container.add(this.login);

        add(container);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            this.login.setError(true);
        }
    }

    private LoginI18n createGermanI18n() {
        LoginI18n i18n = LoginI18n.createDefault();

        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Anmeldung");
        i18nForm.setUsername("Benutzername");
        i18nForm.setPassword("Passwort");
        i18nForm.setSubmit("Anmelden");
        i18nForm.setForgotPassword("Account erstellen");
        i18n.setForm(i18nForm);

        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Benutzername/Passwort ungültig");
        i18nErrorMessage.setMessage("Bitte überprüfen Sie Ihre Eingaben und versuchen Sie es erneut.");
        i18n.setErrorMessage(i18nErrorMessage);
        return i18n;
    }
}
