package de.hsa.movietvratings.components;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Tag("a")
public class TitleImageComponent extends Anchor {

    public TitleImageComponent(String imageUrl, String title, String href) {
        this(imageUrl, title, href, 20);
    }

    public TitleImageComponent(String imageUrl, String title, String href, int maxWidth) {
        Image image = new Image(imageUrl, "Image");
        image.setMaxWidth(maxWidth, Unit.REM);
        final H4 titleElement = new H4(title);
        titleElement.setMaxWidth(maxWidth, Unit.REM);
        getElement().appendChild(image.getElement(), titleElement.getElement());
        addClassName(LumoUtility.Margin.MEDIUM);
        setWidth(null);

        setHref(href);
    }
}
