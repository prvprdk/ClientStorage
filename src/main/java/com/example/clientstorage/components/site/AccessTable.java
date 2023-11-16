package com.example.clientstorage.components.site;


import com.example.clientstorage.domain.Access;
import com.example.clientstorage.domain.Site;
import com.example.clientstorage.repo.RepoAccesses;
import com.example.clientstorage.repo.RepoSite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.lumo.LumoUtility;


@SpringComponent
@UIScope
public class AccessTable extends VerticalLayout {

    private final RepoSite repoSite;
    private final RepoAccesses repoAccesses;
    private final AccessForm accessForm;
    private Grid<Access> grid;
    H2 nameSite = new H2();

    private Site site;


    public AccessTable(RepoSite repoSite, RepoAccesses repoAccesses, AccessForm accessForm) {

        this.repoSite = repoSite;
        this.repoAccesses = repoAccesses;
        this.accessForm = accessForm;

        setupGrid();

        add(accessForm, grid);

        this.accessForm.setAccessAuthor(access -> access.setSite(site));
        this.accessForm.setChangeHandler(() -> updateListAccess(site.getId()));

        setVisible(false);
    }


    private void setupGrid() {

        grid = new Grid<>(Access.class, false);

        grid.addClassNames(LumoUtility.BorderRadius.LARGE, LumoUtility.BoxShadow.SMALL);

        add(nameSite);

        grid.addColumn(Access::getTypeAccesses).setHeader("type");
        grid.addColumn(Access::getUrl).setHeader("url");
        grid.addColumn(Access::getLogin).setHeader("login");
        grid.addColumn(Access::getPassword).setHeader("password");
        grid.addComponentColumn(access -> {
            Button deleteButton = new Button();
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            deleteButton.addClickListener(e -> delete(access));
            deleteButton.setIcon(new Icon(VaadinIcon.TRASH));
            return deleteButton;
        });

    }

    private void delete(Access access) {

        if (access == null) {
            return;
        }
        repoAccesses.delete(access);
        updateListAccess(site.getId());
    }

    private void updateListAccess(Long id) {
        grid.setItems(repoSite.findById(id).get().getAccessesSet());
    }

    public void listAccess(Site from) {

        if (from == null) {
            setVisible(false);
            return;
        }
        site = from;
        nameSite.setText(site.getName());
        grid.setItems(site.getAccessesSet());
        setVisible(true);
    }
}
