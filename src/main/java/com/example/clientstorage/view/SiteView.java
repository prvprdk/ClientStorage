package com.example.clientstorage.view;

import com.example.clientstorage.components.site.AccessTable;
import com.example.clientstorage.components.site.FormSite;
import com.example.clientstorage.domain.Site;
import com.example.clientstorage.repo.RepoSite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;


@Route("/sites")
public class SiteView extends VerticalLayout {

    private final RepoSite repoSite;
    private final AccessTable accessTable;

    private final FormSite formSite;

    private final Button openingForm = new Button(new Icon(VaadinIcon.CLOSE_SMALL));

    private final Grid<Site> grid = new Grid<>(Site.class, false);


    public SiteView(RepoSite repoSite, AccessTable accessTable, FormSite formSite) {

        this.repoSite = repoSite;
        this.accessTable = accessTable;
        this.formSite = formSite;

        grid.addColumn(Site::getName).setHeader("name site");

        openingForm.addClickListener(e -> {
            if (formSite.isVisible()) {
                formSite.setVisible(false);

            } else {
                formSite.openSiteForm(new Site());
            }
        });

        grid.asSingleSelect().addValueChangeListener(e -> {
            listSite();
            accessTable.listAccess(e.getValue());

        });

        add(openingForm, formSite, grid, accessTable);


        formSite.setChangeHandler(() -> {
            listSite();
            formSite.setVisible(false);
        });

        listSite();
    }

    private void listSite() {
        grid.setItems(repoSite.findAll());
    }

}
