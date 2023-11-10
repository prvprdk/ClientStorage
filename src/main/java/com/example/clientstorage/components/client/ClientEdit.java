package com.example.clientstorage.components.client;

import com.example.clientstorage.domain.Client;
import com.example.clientstorage.domain.Contract;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyNotifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;


@SpringComponent
@UIScope
public class ClientEdit extends FormLayout implements KeyNotifier {
    private final RepoClient repoClient;
    private Client client;
    private final TextField name = new TextField("Name");
    private final TextField number = new TextField("Number");
    private final TextField site = new TextField("Site");
    private final ComboBox<Contract> contracts = new ComboBox<>("Contract");
    private final TextField company = new TextField("Company");
    private final EmailField email = new EmailField("Email");
    private final Button save = new Button("save", VaadinIcon.HAND.create());


    private final Binder<Client> binder = new Binder<>(Client.class);
    @Setter
    private ChangeHandler changeHandler;


    public interface ChangeHandler {
        void onChange();
    }

    public ClientEdit(RepoClient repoClient) {
        this.repoClient = repoClient;

        setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3),
                new FormLayout.ResponsiveStep("800px", 7));

        add(name, number, site, company, contracts, email, save);


        binder.forField(name)
                .asRequired("name must not be empty")
                .bind(Client::getName, Client::setName);

        binder.forField(number)
                .asRequired("number must not be empty")
                .bind(Client::getNumber, Client::setNumber);

        binder.forField(site)
                .asRequired("site must not be empty")
                .bind(Client::getSite, Client::setSite);

        binder.forField(company)
                .asRequired("company must not be empty")
                .bind(Client::getCompany, Client::setCompany);

        contracts.setItems(Contract.values());
        binder.forField(contracts)
                .asRequired("contracts must not be empty")

                .bind(Client::getContract, Client::setContract);
        binder.forField(email)
                .withValidator(new EmailValidator("This doesn't look like a valid email address"))
                .bind(Client::getEmail, Client::setEmail);

        save.getElement().getThemeList().add("badge success");


        addKeyPressListener(Key.ENTER, e -> save());

        save.addClickListener(e -> save());

        setVisible(false);

    }


    private void save() {
        if (binder.isValid()) {
            repoClient.save(client);
            changeHandler.onChange();
        }
    }

    public void addClientForm(Client newClient) {
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
