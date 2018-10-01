package apicurito.tests.configuration;

import lombok.Getter;

@Getter
public enum Component {

    GENERATOR("fuse-apicurito-generator"),
    UI("apicurito-ui");

    private final String name;

    Component(String name) {
        this.name = name;
    }
}
