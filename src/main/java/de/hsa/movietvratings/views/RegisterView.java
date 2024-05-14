package de.hsa.movietvratings.views;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import de.hsa.movietvratings.services.UserService;
import jakarta.annotation.security.PermitAll;
import org.springframework.dao.DataIntegrityViolationException;

@PermitAll
@AnonymousAllowed
@Route("register")
public class RegisterView extends Div {
    private final UserService users;

    public RegisterView(UserService users) {
        setSizeFull();
        this.users = users;

        Div formContainer = new Div();
        formContainer.addClassName("register-container");
        formContainer.setSizeFull();

        FormLayout registerForm = new FormLayout();
        Div formContent = new Div();
        formContent.setClassName("register-form-content");
        registerForm.setClassName("register-view");
        H3 title = new H3("Registrierung");
        formContent.add(title);

        TextField usernameField = new TextField("Benutzername");
        usernameField.setRequiredIndicatorVisible(true);
        usernameField.setErrorMessage("Der Benutzername muss aus mindestens drei Zeichen bestehen");
        usernameField.setMinLength(3);

        PasswordField passwordField = new PasswordField("Passwort");
        passwordField.setErrorMessage("Das Passwort muss aus mindestens fünf Zeichen bestehen");
        passwordField.setRequiredIndicatorVisible(true);
        passwordField.setMinLength(5);

        PasswordField confirmPassword = new PasswordField("Passwort bestätigen");
        confirmPassword.setMinLength(5);
        confirmPassword.setRequiredIndicatorVisible(true);

        Button submitButton = new Button("Registrieren");
        submitButton.addClickShortcut(Key.ENTER);
        submitButton.addClickListener(event -> {
            String username = usernameField.getValue();
            String password = passwordField.getValue();
            String confirm = confirmPassword.getValue();

            if (passwordField.isInvalid() || usernameField.isInvalid() || confirmPassword.isInvalid()) {
                Notification notification = Notification.show("Bitte überprüfen Sie Ihre Eingaben.");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

            } else if (!password.equals(confirm)) {
                Notification notification = Notification
                        .show("Passwort stimmt nicht überein. Bitte überprüfen Sie Ihre Eingaben.");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } else if (!passwordField.isEmpty() && !usernameField.isEmpty() && !confirmPassword.isEmpty()) {
                try {
                    this.users.newUser(username, password);
                    Notification notification = Notification.show("Registrierung erfolgreich!");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    submitButton.getUI().ifPresent(ui -> ui.navigate("login"));
                } catch (DataIntegrityViolationException e) {
                    Notification notification = Notification
                            .show("Benutzername existiert bereits. Bitte verwenden Sie einen anderen Nutzernamen.");
                    notification.setPosition(Notification.Position.BOTTOM_CENTER);
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                }

            } else {
                Notification notification = Notification
                        .show("Die Registrierung ist schiefgelaufen, bitte versuchen Sie es erneut.");
                notification.setPosition(Notification.Position.BOTTOM_CENTER);
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        formContent.add(usernameField, passwordField, confirmPassword, submitButton);

        registerForm.add(formContent);
        formContainer.add(registerForm);
        add(formContainer);
    }
}
