package jp.saka1029.cspint.depend;

import java.util.Objects;

public class BaseVariable extends Variable {

    private final Domain domain;
    public Domain domain() { return domain; }

    BaseVariable(String name, Domain domain) {
        super(name);
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(domain, "domain");
        this.domain = domain;
    }

}
