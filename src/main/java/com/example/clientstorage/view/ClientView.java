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
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route(value = "clients", layout = MainLayout.class)
@PageTitle("Clients")

public class ClientView extends VerticalLayout {
    private final TextField filter = new TextField("", "Filter by name");
    private final Button addNewClient = new Button(new Icon(VaadinIcon.PLUS));
    private final ClientEdit clientEdit;
    private final RepoClient repoClient;
    private final Grid<Client> grid = new Grid<>(Client.class, false);
    private final Binder<Client> binder = new Binder<>(Client.class);
    private final Editor<Client> editor = grid.getEditor();


    public ClientView(ClientEdit clientEdit, RepoClient repoClient) {
        this.clientEdit = clientEdit;
        this.repoClient = repoClient;

        Grid.Column<Client> name = grid.addColumn(Client::getName).setHeader("name").setSortable(true);
        Grid.Column<Client> company = grid.addColumn(Client::getCompany).setHeader("company").setSortable(true);
        Grid.Column<Client> number = grid.addColumn(Client::getNumber).setHeader("number").setSortable(true);
        Grid.Column<Client> site = grid.addColumn(Client::getSite).setHeader("site");
        Grid.Column<Client> email = grid.addColumn(Client::getEmail).setHeader("email");
        Grid.Column<Client> contact = grid.addColumn(Client::getContract).setHeader("contact").setSortable(true);
        Grid.Column<Client> editColumn = grid.addComponentColumn(client -> {
            Button editButton = new Button("edit");
            editButton.addClickListener(e -> {
                        if (editor.isOpen())
                            editor.cancel();
                        grid.getEditor().editItem(client);
                    }
            );
            return editButton;
        });

        editor.setBinder(binder);

        TextField nameField = new TextField();
        setField(nameField, name);

        TextField companyField = new TextField();
        setField(companyField, company);

        TextField numberField = new TextField();
        setField(numberField, number);

        TextField siteField = new TextField();
        setField(siteField, site);

        EmailField emailField = new EmailField();
        setField(emailField, email);

        ComboBox<Contract> contractComboBox = new ComboBox<>();
        contractComboBox.setItems(Contract.values());
        contractComboBox.setAllowCustomValue(true);
        binder.forField(contractComboBox).bind(Client::getContract, Client::setContract);
        contact.setEditorComponent(contractComboBox);


        Button saveButton = new Button("Save", e -> {
            repoClient.save(editor.setBinder(binder).getItem());
            listClient(null);
        });

        Button cancelButton = new Button(VaadinIcon.CLOSE.create(), e -> editor.closeEditor());

        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);

        Button deleteButton = new Button("delete", e -> {
            repoClient.delete(editor.setBinder(binder).getItem());
            listClient(null);
        });

        deleteButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        HorizontalLayout actions = new HorizontalLayout(saveButton, cancelButton, deleteButton);

        editColumn.setEditorComponent(actions);

        setSizeFull();


        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listClient(e.getValue()));

        addNewClient.addClickListener(e -> {
                    if (clientEdit.isVisible()) {
                        clientEdit.setVisible(false);
                    } else {
                        clientEdit.editClient(new Client());
                    }
                }
        );


        add(filter, grid, addNewClient, clientEdit);


        clientEdit.setChangeHandler(() -> {
            listClient(null);
            clientEdit.setVisible(false);
        });

        listClient(null);

    }

    private void setField(TextField nameField, Grid.Column<Client> name) {
        nameField.setWidthFull();
        binder.forField(nameField).bind(Client::getName, Client::setName);
        name.setEditorComponent(nameField);
    }

    private void setField(EmailField nameField, Grid.Column<Client> name) {
        nameField.setWidthFull();
        binder.forField(nameField).bind(Client::getName, Client::setName);
        name.setEditorComponent(nameField);
    }

    private void save() {

    }

    public void listClient(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repoClient.findByName(filterText));
        } else {
            grid.setItems(repoClient.findAll());
        }
    }
}
