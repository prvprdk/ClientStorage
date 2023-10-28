package com.example.clientstorage.view;

import com.example.clientstorage.components.ClientEdit;
import com.example.clientstorage.components.ClientTable;
import com.example.clientstorage.domain.Client;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "clients")
public class MainView extends VerticalLayout {
    private final TextField filter = new TextField("", "Filter by name");
    private final Button addNewClient = new Button("addNew");
    private final HorizontalLayout toolBar = new HorizontalLayout(filter, addNewClient);
    private final ClientEdit clientEdit;
    private final ClientTable clientTable;


    public MainView(ClientEdit clientEdit, ClientTable clientTable) {
        this.clientEdit = clientEdit;
        this.clientTable = clientTable;

        add(toolBar, clientTable, clientEdit);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> clientTable.listClient(e.getValue()));

        clientTable.getGrid().asSingleSelect().addValueChangeListener(e ->
                clientEdit.editClient(e.getValue()));

        addNewClient.addClickListener(e -> clientEdit.editClient(new Client()));

        clientEdit.setChangeHandler(() -> {
            clientEdit.setVisible(false);
            clientTable.listClient(filter.getValue());
        });
    }
}
