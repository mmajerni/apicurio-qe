package apicurito.tests.steps.verification;

import org.junit.Rule;

import org.assertj.core.api.SoftAssertions;

class CollectorHelper {
    @Rule
    static SoftAssertions collector = new SoftAssertions();

    static SoftAssertions getCollector() {
        return collector;
    }

    static void initCollector() {
        collector = new SoftAssertions();
    }
}
