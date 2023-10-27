package com.example.clientstorage.view;

import com.example.clientstorage.components.ClientEdit;
import com.example.clientstorage.domain.Client;

import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

@Route(value = "clients")
public class MainView extends VerticalLayout {
    private final RepoClient repoClient;
    private final TextField filter = new TextField("", "type to filter");
    private final Button addNewClient = new Button("addNew");
    private HorizontalLayout toolBar = new HorizontalLayout(filter, addNewClient);
    private final ClientEdit clientEdit;
    private final Grid<Client> grid = new Grid<>(Client.class);

    @Autowired
    public MainView(RepoClient repoClient, ClientEdit clientEdit) {
        this.repoClient = repoClient;
        this.clientEdit = clientEdit;
        add(toolBar, grid, clientEdit);

        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> listClint(e.getValue()));

        grid.asSingleSelect().addValueChangeListener(e -> {
            clientEdit.editClient(e.getValue());
        });

        addNewClient.addClickListener(e -> clientEdit.editClient(new Client()));

        clientEdit.setChangeHandler( () -> {
            clientEdit.setVisible(false);
            listClint(filter.getValue());
        });

        listClint("");
    }


    void listClint(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repoClient.findByName(filterText));
        } else {
            grid.setItems(repoClient.findAll());
        }
    }
}
