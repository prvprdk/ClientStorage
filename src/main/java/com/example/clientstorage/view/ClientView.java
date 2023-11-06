package com.example.clientstorage.view;

import com.example.clientstorage.components.Menu;
import com.example.clientstorage.components.client.ClientEdit;
import com.example.clientstorage.domain.Client;
import com.example.clientstorage.domain.Contract;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.util.StringUtils;

@Route(value = "clients")
public class ClientView extends VerticalLayout {
    private final TextField filter = new TextField("", "Filter by name");
    private final Button addNewClient = new Button("addNew");

    private final ClientEdit clientEdit;
    private final RepoClient repoClient;
    private final Grid<Client> grid = new Grid<>(Client.class, false);
    private final Binder<Client> binder = new Binder<>(Client.class);
    private final Editor<Client> editor = grid.getEditor();
    private final Menu menuComponent;


    public ClientView(ClientEdit clientEdit, RepoClient repoClient, Menu menuComponent) {
        this.clientEdit = clientEdit;
        this.repoClient = repoClient;
        this.menuComponent = menuComponent;


        Grid.Column<Client> name = grid.addColumn(Client::getName).setHeader("name");
        Grid.Column<Client> company = grid.addColumn(Client::getCompany).setHeader("company");
        Grid.Column<Client> number = grid.addColumn(Client::getNumber).setHeader("number");
        Grid.Column<Client> site = grid.addColumn(Client::getSite).setHeader("site");
        Grid.Column<Client> email = grid.addColumn(Client::getEmail).setHeader("email");
        Grid.Column<Client> contact = grid.addColumn(Client::getContract).setHeader("contact");
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

        editor.setBinder( binder);

        TextField nameField = new TextField();
        nameField.setWidthFull();
        binder.forField(nameField).bind(Client::getName, Client::setName);
        name.setEditorComponent(nameField);

        TextField companyField = new TextField();
       companyField.setWidthFull();
        binder.forField(companyField).bind(Client::getCompany, Client::setCompany);
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
        binder.forField(emailField).bind(Client::getEmail, Client::setEmail);
        email.setEditorComponent(emailField);

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

        actions.setPadding(false);
        editColumn.setEditorComponent(actions);


        getThemeList().clear();
        getThemeList().add("spacing-s");

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listClient(e.getValue()));

        addNewClient.addClickListener(e -> clientEdit.editClient(new Client()));


        add(  menuComponent,  filter, addNewClient,  grid, clientEdit);


        clientEdit.setChangeHandler(() -> {
            listClient(null);
            clientEdit.setVisible(false);
        });

        listClient(null);

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
