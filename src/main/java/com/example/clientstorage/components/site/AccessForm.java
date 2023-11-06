package com.example.clientstorage.components.site;


import com.example.clientstorage.components.client.ClientEdit;
import com.example.clientstorage.domain.Access;
import com.example.clientstorage.domain.TypeAccesses;
import com.example.clientstorage.repo.RepoAccesses;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

@SpringComponent
@UIScope
public class AccessForm extends VerticalLayout {


    private final TextField urlField = new TextField("url");
    private final TextField loginField = new TextField("login");
    private final TextField passwordField = new TextField("password");
    private final Binder<Access> binder = new Binder<>(Access.class, false);
    private final Button addButton = new Button("add access", VaadinIcon.PLUS.create());

    private final ComboBox<TypeAccesses> typeAccessesComboBox = new ComboBox<>("type");

    private Access access;
    private  final RepoAccesses repoAccesses;
    @Setter
    private AccessAuthor accessAuthor;
    @Setter
    private ClientEdit.ChangeHandler changeHandler;


    public interface ChangeHandler {
        void onChange();
    }


    public interface AccessAuthor {
        void setAuthor(Access access);

    }

    public AccessForm(RepoAccesses repoAccesses){
        this.repoAccesses = repoAccesses;


        typeAccessesComboBox.setItems(TypeAccesses.values());
        binder.forField(urlField).bind(Access::getUrl, Access::setUrl);
        binder.forField(loginField).bind(Access::getLogin, Access::setLogin);
        binder.forField(passwordField).bind(Access::getPassword, Access::setPassword);
        binder.forField(typeAccessesComboBox).bind(Access::getTypeAccesses, Access::setTypeAccesses);


        HorizontalLayout horizontalLayout = new HorizontalLayout(urlField, loginField, passwordField, typeAccessesComboBox);

        urlField.setSizeFull();
        loginField.setSizeFull();
        passwordField.setSizeFull();
        typeAccessesComboBox.setSizeFull();

        extracted();

        add(horizontalLayout);
        addButton.addClickListener(e -> addAccess());
        add(horizontalLayout, addButton);


    }

    private void extracted() {
        access = new Access();
        binder.setBean(access);

    }

    public void addAccess() {
        accessAuthor.setAuthor(access);
        repoAccesses.save(access);
        changeHandler.onChange();
        extracted();
    }

}
