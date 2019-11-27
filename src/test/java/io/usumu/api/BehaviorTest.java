package io.usumu.api;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "classpath:io/usumu/api/features",
    plugin = {
        "pretty",
        "html:target/cucumber-reports"
    }
)
public class BehaviorTest {

}