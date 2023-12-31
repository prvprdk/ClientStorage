package com.example.clientstorage.components.site;


import com.example.clientstorage.components.client.ClientEdit;
import com.example.clientstorage.domain.Access;
import com.example.clientstorage.domain.TypeAccesses;
import com.example.clientstorage.repo.RepoAccesses;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Setter;

@SpringComponent
@UIScope
public class AccessForm extends FormLayout {


    private final Binder<Access> binder = new Binder<>(Access.class, false);
    private Access access;
    private final RepoAccesses repoAccesses;
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

    public AccessForm(RepoAccesses repoAccesses) {
        this.repoAccesses = repoAccesses;

        TextField urlField = new TextField("url");
        TextField loginField = new TextField("login");
        TextField passwordField = new TextField("password");
        Button addButton = new Button("save", VaadinIcon.PLUS.create());

        ComboBox<TypeAccesses> typeAccessesComboBox = new ComboBox<>("type");

        typeAccessesComboBox.setItems(TypeAccesses.values());
        binder.forField(urlField).asRequired("url must not be empty").bind(Access::getUrl, Access::setUrl);
        binder.forField(loginField).asRequired("login must not be empty").bind(Access::getLogin, Access::setLogin);
        binder.forField(passwordField).asRequired("password must not be empty").bind(Access::getPassword, Access::setPassword);
        binder.forField(typeAccessesComboBox).asRequired("access must not be empty").bind(Access::getTypeAccesses, Access::setTypeAccesses);


        urlField.setSizeFull();
        loginField.setSizeFull();
        passwordField.setSizeFull();
        typeAccessesComboBox.setSizeFull();

        extracted();

        setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3),
                new FormLayout.ResponsiveStep("800px", 7));


        add(urlField, loginField, passwordField, typeAccessesComboBox, addButton);
        addButton.addClickListener(e -> addAccess());


    }

    private void extracted() {
        access = new Access();
        binder.setBean(access);

    }

    public void addAccess() {
        if (binder.isValid()) {
            accessAuthor.setAuthor(access);
            repoAccesses.save(access);
            changeHandler.onChange();
            extracted();
        }
    }
}
