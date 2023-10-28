package com.example.clientstorage.components;

import com.example.clientstorage.domain.Client;
import com.example.clientstorage.repo.RepoClient;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Getter;
import org.springframework.util.StringUtils;

@SpringComponent
@UIScope
public class ClientTable extends VerticalLayout {
    private final RepoClient repoClient;
    @Getter
    private final Grid<Client> grid = new Grid<>(Client.class, false);

    public ClientTable(RepoClient repoClient){
        this.repoClient = repoClient;

        grid.addColumn(Client::getId).setHeader("Id");
        grid.addColumn(Client::getName).setHeader("Name");
        grid.addColumn(Client::getCompany).setHeader("Company");
        grid.addColumn(Client::getNumber).setHeader("Number");
        grid.addColumn(Client::getSite).setHeader("Site");
        grid.addColumn(Client::getEmail).setHeader("Email");
        grid.addColumn(Client::getContract).setHeader("Contract");

        add(grid);
        listClient(null);
    }

   public void listClient(String filterText) {
        if (StringUtils.hasText(filterText)) {
            grid.setItems(repoClient.findByName(filterText));
        } else {
            grid.setItems(repoClient.findAll());
        }
    }

}
