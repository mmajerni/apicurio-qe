package apicurito.tests.configuration;

import lombok.Getter;

@Getter
public enum Component {

    UI("apicurito-ui"),
    SERVICE("apicurito-service-ui"),
    OPERATOR("apicurito-operator");

    private final String name;

    Component(String name) {
        this.name = name;
    }
}
