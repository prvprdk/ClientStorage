package com.example.clientstorage.view;

import com.example.clientstorage.components.client.ClientEdit;
import com.example.clientstorage.domain.Client;
import com.example.clientstorage.domain.Contract;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import org.springframework.util.StringUtils;

@PermitAll

@Route(value = "/", layout = MainLayout.class)
@PageTitle("Clients")

public class ClientView extends VerticalLayout {
    private final TextField filter = new TextField("", "Filter by name");
    private final Button addNewClient = new Button(new Icon(VaadinIcon.PLUS));
    private final ClientEdit clientEdit;
    private final RepoClient repoClient;
    private final Grid<Client> grid = new Grid<>(Client.class, false);
    private final Binder<Client> binder = new Binder<>(Client.class);
    private final Editor<Client> editor = grid.getEditor();
    private final Grid.Column<Client> name = grid.addColumn(Client::getName).setHeader("name").setSortable(true);
    private final Grid.Column<Client> company = grid.addColumn(Client::getCompany).setHeader("company").setSortable(true);
    private final Grid.Column<Client> number = grid.addColumn(Client::getNumber).setHeader("number").setSortable(true);
    private final Grid.Column<Client> site = grid.addColumn(Client::getSite).setHeader("site");
    private final Grid.Column<Client> email = grid.addColumn(Client::getEmail).setHeader("email");
    private final Grid.Column<Client> contact = grid.addColumn(Client::getContract).setHeader("contact").setSortable(true);
    private final Grid.Column<Client> editColumn = grid.addComponentColumn(client -> {
        Button editButton = new Button("edit");
        editButton.addClickListener(e -> {
            if (editor.isOpen())
                editor.cancel();
            grid.getEditor().editItem(client);
        });
        return editButton;
    });


    public ClientView(ClientEdit clientEdit, RepoClient repoClient) {
        this.clientEdit = clientEdit;
        this.repoClient = repoClient;

        editor.setBinder(binder);
        setEditComp();


        grid.addClassNames(LumoUtility.BorderRadius.LARGE, LumoUtility.BoxShadow.SMALL);


        Button saveButton = new Button(new Icon(VaadinIcon.BOLT), e -> save(editor.setBinder(binder).getItem()));
        saveButton
                .addThemeVariants(ButtonVariant.LUMO_SUCCESS,
                        ButtonVariant.LUMO_ICON);

        Button deleteButton = new Button(new Icon(VaadinIcon.TRASH), e -> delete(editor.setBinder(binder).getItem()));
        deleteButton
                .addThemeVariants(ButtonVariant.LUMO_ICON,
                        ButtonVariant.LUMO_ERROR,
                        ButtonVariant.LUMO_TERTIARY);

        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.closeEditor());
        cancelButton
                .addThemeVariants(ButtonVariant.LUMO_ICON,
                        ButtonVariant.LUMO_CONTRAST,
                        ButtonVariant.LUMO_ERROR);


        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);

        editColumn.setEditorComponent(actions);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listClient(e.getValue()));

        addNewClient.addClickListener(e -> {
                    if (clientEdit.isVisible()) {
                        clientEdit.setVisible(false);
                    } else {
                        clientEdit.addClientForm(new Client());
                    }
                }
        );


        add(filter, addNewClient, clientEdit, grid);

        clientEdit.setChangeHandler(() -> {
            listClient(null);
            clientEdit.setVisible(false);
        });

        listClient(null);

    }

    private void save(Client client) {
        repoClient.save(client);
        listClient(null);
    }

    private void delete(Client client) {
        repoClient.delete(client);
        listClient(null);
    }

    public void listClient(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repoClient.findByName(filterText));
        } else {
            grid.setItems(repoClient.findAll());
        }
    }

    private void setEditComp() {
        TextField nameField = new TextField();
        nameField.setWidthFull();
        binder.forField(nameField)

                .bind(Client::getName, Client::setName);
        name.setEditorComponent(nameField);

        TextField companyField = new TextField();
        companyField.setWidthFull();
        binder.forField(companyField)
                .asRequired("Oops")
                .bind(Client::getCompany, Client::setCompany);
        company.setEditorComponent(companyField);

        TextField numberField = new TextField();
        numberField.setWidthFull();
        binder.forField(numberField).bind(Client::getNumber, Client::setNumber);
        number.setEditorComponent(numberField);

        TextField siteField = new TextField();
        siteField.setWidthFull();
        binder.forField(siteField).bind(Client::getSite, Client::setSite);
        site.setEditorComponent(siteField);

        EmailField emailField = new EmailField();
        emailField.setWidthFull();
        binder.forField(emailField)
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .withValidator(
                        email -> email.endsWith("@acme.com"),
                        "Only acme.com email addresses are allowed")
                .bind(Client::getEmail, Client::setEmail);
        email.setEditorComponent(emailField);

        ComboBox<Contract> contractComboBox = new ComboBox<>();
        contractComboBox.setItems(Contract.values());
        contractComboBox.setAllowCustomValue(true);
        binder.forField(contractComboBox).bind(Client::getContract, Client::setContract);
        contact.setEditorComponent(contractComboBox);
    }
}
