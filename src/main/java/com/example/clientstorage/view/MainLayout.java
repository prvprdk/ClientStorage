package com.example.clientstorage.view;

import com.example.clientstorage.security.SecurityService;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("/")
@PageTitle("Space Company")
public class MainLayout extends AppLayout {
    private final SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        this.securityService = securityService;
        createHeader();


    }

    private void createHeader() {
        H1 title = new H1("Space Company");
        title.getStyle().set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)").set("margin", "0")
                .set("position", "absolute");

        String u = securityService.getAuthenticatedUser().getUsername();
        Button logout = new Button("Log out " + u, e -> securityService.logout());

        addToNavbar(title, getTabs(), logout);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(createTab("Client"), createTab("Access"));

        return tabs;
    }

    private Tab createTab(String viewName) {
        RouterLink link = new RouterLink();
        link.add(viewName);

        switch (viewName) {
            case "Client":
                link.setRoute(ClientView.class);
                link.setTabIndex(-1);
                return new Tab(link);
            case "Access":
                link.setRoute(SiteView.class);
                link.setTabIndex(-1);
                return new Tab(link);
        }

        return null;
    }
}