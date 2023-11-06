package com.example.clientstorage.components;

import com.example.clientstorage.view.ClientView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@UIScope
@SpringComponent
public class Menu extends AppLayout {

    public Menu() {
        DrawerToggle toggle = new DrawerToggle();

        H1 title = new H1("DashSSSSSboard");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");

        Tabs tabs = getTabs();

        addToDrawer(tabs);
        addToNavbar(toggle, title);

        setPrimarySection(Section.DRAWER);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.CART, "Clients"),
                createTab(VaadinIcon.USER_HEART, "Access"));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        if (viewName.equals("Clients")) {
            link.setRoute(ClientView.class);
        }
        // Demo has no routes
        // link.setRoute(viewClass.java);
        link.setTabIndex(-1);

        return new Tab(link);
    }


}
