package de.hsa.movietvratings.layout;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import de.hsa.movietvratings.security.SecurityService;
import de.hsa.movietvratings.views.TopMoviesView;
import de.hsa.movietvratings.views.SearchView;
import de.hsa.movietvratings.views.TopTVView;
import de.hsa.movietvratings.views.WatchlistView;
import de.hsa.movietvratings.views.RatingView;
import de.hsa.movietvratings.views.CommentView;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private final SecurityService securityService;

    private H2 viewTitle;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;

        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Movie TV Ratings");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.MEDIUM);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Startseite", SearchView.class, LineAwesomeIcon.GLOBE_SOLID.create()));
        nav.addItem(new SideNavItem("Top Filme", TopMoviesView.class, LineAwesomeIcon.ARROW_CIRCLE_UP_SOLID.create()));
        nav.addItem(new SideNavItem("Top Serien", TopTVView.class, LineAwesomeIcon.ARROW_CIRCLE_UP_SOLID.create()));
        nav.addItem(new SideNavItem("Merkliste", WatchlistView.class, LineAwesomeIcon.BOOKMARK_SOLID.create()));
        nav.addItem(new SideNavItem("Bewertungen", RatingView.class, LineAwesomeIcon.STAR_SOLID.create()));
        nav.addItem(new SideNavItem("Kommentare", CommentView.class, LineAwesomeIcon.COMMENTS_SOLID.create()));

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        Div userDiv = new Div();
        userDiv.getElement().getStyle().set("margin", "5px");
        userDiv.getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "space-between").set("padding", "8px").set("margin", "10px");
        Button userIcon = new Button();
        userIcon.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        userIcon.setIcon(LineAwesomeIcon.USER_CIRCLE.create());
        userIcon.getStyle().set("padding", "1.6px");
        Span span = new Span(securityService.getAuthenticatedUser().getUsername());
        span.getStyle().set("margin-left", "8px").set("margin-right", "8px");
        Button logoutIcon = new Button();
        logoutIcon.setIcon(LineAwesomeIcon.SIGN_OUT_ALT_SOLID.create());
        logoutIcon.getStyle().set("padding", "1.6px");
        logoutIcon.getTooltip().setText("Abmelden");
        logoutIcon.addClickListener(e -> showLogoutDialog());
        logoutIcon.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        Span userSpan = new Span(userIcon, span);
        userDiv.add(userSpan, logoutIcon);
        layout.getElement().appendChild(userDiv.getElement());


        return layout;
    }

    private void showLogoutDialog() {
        Dialog dialog = new Dialog();
        dialog.setWidth(450, Unit.PIXELS);
        dialog.setHeaderTitle("Abmelden");
        dialog.add("Wollen Sie sich wirklich abmelden?");
        Button confirmButton = new Button("Ja", event -> {
            securityService.logout();
            dialog.close();
        });
        Button cancelButton = new Button("Nein", event -> dialog.close());
        dialog.getFooter().add(confirmButton, cancelButton);
        dialog.open();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}