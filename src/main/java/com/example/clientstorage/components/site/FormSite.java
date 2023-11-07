package com.example.clientstorage.components.site;

import com.example.clientstorage.components.client.ClientEdit;
import com.example.clientstorage.domain.Site;
import com.example.clientstorage.repo.RepoSite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

@SpringComponent
@UIScope
public class FormSite extends VerticalLayout {
    private final RepoSite repoSite;
    private Site site;

    private final Binder<Site> binder = new Binder<>(Site.class, false);
    private final TextField nameSite = new TextField("name site");
    @Setter
    private ClientEdit.ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }
    public FormSite(RepoSite repoSite ) {
        this.repoSite = repoSite;


        binder.forField(nameSite).bind(Site::getName, Site::setName);

        Button save = new Button("save");
        add(nameSite, save);

        setSpacing(true);
        save.addClickListener(e -> save());
        setVisible(false);

    }

    private void save() {
        repoSite.save(site);
        changeHandler.onChange();
    }

    public void openSiteForm(Site newSite) {

        if (newSite == null) {
            setVisible(false);
            return;
        }
        site = newSite;
        binder.setBean(site);

        setVisible(true);
        nameSite.focus();

    }
}
