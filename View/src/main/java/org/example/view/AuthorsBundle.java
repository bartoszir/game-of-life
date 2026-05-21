package org.example.view;

import java.util.ListResourceBundle;

public class AuthorsBundle extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"authors", "Ireneusz Bartoszek, Jakub Gajewski"}
        };
    }
}
