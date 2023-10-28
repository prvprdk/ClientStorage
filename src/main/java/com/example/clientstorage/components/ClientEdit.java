package com.example.clientstorage.components;

import com.example.clientstorage.domain.Client;
import com.example.clientstorage.domain.Contract;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;


@SpringComponent
@UIScope
public class ClientEdit extends VerticalLayout implements KeyNotifier {
    private final RepoClient repoClient;
    private Client client;

    private final TextField name = new TextField("Name");
    private final TextField number = new TextField("Number");
    private final TextField site = new TextField("Site");

    private final RadioButtonGroup<Contract> contracts = new RadioButtonGroup<>();

    TextField company = new TextField("Company");

    EmailField email = new EmailField("Email");

    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final Button delete = new Button("Delete");
    private final HorizontalLayout actions = new HorizontalLayout(save, cancel, delete);
    private final HorizontalLayout field = new HorizontalLayout(name, number, site, company, contracts, email);
    private final Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;

    public interface ChangeHandler {
        void onChange();
    }

    public ClientEdit(RepoClient repoClient) {
        this.repoClient = repoClient;

        contracts.setItems(Contract.values());

        add(field, actions);
        binder.bindInstanceFields(this);
        binder.forField(contracts).bind(Client::getContract, Client::setContract);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        delete.getElement().getThemeList().add("error");

        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        delete.addClickListener(e -> delete());
        cancel.addClickListener(e -> editClient(client));

        setVisible(false);

    }

    private void save() {
        repoClient.save(client);
        changeHandler.onChange();
    }

    private void delete() {
        repoClient.delete(client);
        changeHandler.onChange();
    }

    public void editClient(Client newClient) {
        if (newClient == null) {
            setVisible(false);
            return;

        }

        if (newClient.getId() != null) {
           client = repoClient.findById(newClient.getId()).orElse(newClient);
        } else {
            client = newClient;
        }
        binder.setBean(this.client);
        setVisible(true);
        name.focus();

    }
}
