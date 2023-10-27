package com.example.clientstorage.components;

import com.example.clientstorage.domain.Client;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.example.clientstorage.components.*;
import lombok.Setter;


@SpringComponent
@UIScope
public class ClientEdit extends VerticalLayout implements KeyNotifier {
    private final RepoClient repoClient;

    private Client client;

    TextField name = new TextField("Name");
    TextField number = new TextField("Number");
    TextField site = new TextField("Site");
    private Button save = new Button("Save", VaadinIcon.CHECK.create());
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    private HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public ClientEdit(RepoClient repoClient) {
        this.repoClient = repoClient;
        add(name, number, site, actions);
        binder.bindInstanceFields(this);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editClient(client));

        setVisible(false);

    }

    void save() {
        repoClient.save(client);
        changeHandler.onChange();
    }

    void delete() {
        repoClient.delete(client);
        changeHandler.onChange();
    }

    public void editClient(Client newClient){
        if (newClient == null){
            setVisible(false);
        }
        assert newClient != null;
        if (newClient.getId() != null){
            client = repoClient.findById(newClient.getId()).orElse(newClient);
        }else {
            client = newClient;
        }
        binder.setBean(client);
        setVisible(true);
        name.focus();
    }
}
