package com.example.clientstorage.components.client;

import com.example.clientstorage.domain.Client;
import com.example.clientstorage.domain.Contract;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private final ComboBox<Contract> contracts = new ComboBox<>("Contract");
    private final TextField company = new TextField("Company");
    private final EmailField email = new EmailField("Email");
    private final Button save = new Button("Save", VaadinIcon.CHECK.create());
    private final Button cancel = new Button("Cancel");
    private final HorizontalLayout actions = new HorizontalLayout(save, cancel);

    private final Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;


    public interface ChangeHandler {
        void onChange();
    }

    public ClientEdit(RepoClient repoClient) {
        this.repoClient = repoClient;


        binder.forField(name).bind(Client::getName, Client::setName);
        binder.forField(number).bind(Client::getNumber, Client::setNumber);
        binder.forField(site).bind(Client::getSite, Client::setSite);
        binder.forField(company).bind(Client::getCompany, Client::setCompany);
        contracts.setItems(Contract.values());
        binder.forField(contracts).bind(Client::getContract, Client::setContract);
        binder.forField(email).bind(Client::getEmail, Client::setEmail);

        add(name, number, site, company, contracts, email, actions);

        setSpacing(true);

        save.getElement().getThemeList().add("primary");
        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());
        cancel.addClickListener(e -> setVisible(false));

        setVisible(false);

    }


    private void save() {
        repoClient.save(client);
        changeHandler.onChange();
    }

    public void editClient(Client newClient) {
        if (newClient == null) {
            setVisible(false);
            return;
        }
        client = newClient;

        binder.setBean(this.client);
        setVisible(true);
        name.focus();
    }
}
