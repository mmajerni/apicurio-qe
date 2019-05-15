package apicurito.tests.configuration;

import lombok.Getter;

@Getter
public enum Component {

    UI("apicurito-ui"),

    SERVICE("apicurito-service");

    private final String name;

    Component(String name) {
        this.name = name;
    }
}
