package de.hsa.movietvratings.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import de.hsa.movietvratings.layout.MainLayout;
import jakarta.annotation.security.PermitAll;

@PageTitle("Startseite")
@Route(value = "", layout = MainLayout.class)
@PermitAll
public class SearchView extends HorizontalLayout {
    private final TextField search;

    public SearchView() {
        setSizeFull();

        Div div = new Div();
        div.setClassName("search-view");
        div.setSizeFull();

        FlexLayout flex = new FlexLayout();
        flex.setAlignContent(FlexLayout.ContentAlignment.CENTER);
        flex.setAlignItems(Alignment.CENTER);
        flex.setClassName("flex-layout");

        // H3-Element für den Text "Suche" innerhalb des FlexLayouts hinzufügen
        H3 searchLabel = new H3("Suche");
        searchLabel.setClassName("search-label");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        search = new TextField();
        search.setPlaceholder("Suche");
        search.setClassName("search");
        search.getElement().addEventListener("keyup", e -> {
            getUI().ifPresent(ui -> ui.navigate(SearchResultView.class, search.getValue()));
        }).addEventData("element.value").setFilter("event.key == 'Enter'");

        Button searchButton = new Button("Suchen", new Icon(VaadinIcon.SEARCH));
        searchButton.setClassName("search-button");
        searchButton.getElement().addEventListener("click", e -> {
                    getUI().ifPresent(ui -> ui.navigate(SearchResultView.class, search.getValue()));
        }).addEventData("element.value");
        horizontalLayout.add(search, searchButton);

        flex.add(searchLabel, horizontalLayout);

        div.add(flex);
        add(div);
    }
}
