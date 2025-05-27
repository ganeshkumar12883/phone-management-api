package au.com.belong.phone.management.config;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class AppInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("app", Map.of(
                "name", "Phone Management API",
                "description", "Spring Boot API to manage Phone Number(s) Activation",
                "version", "1.0.0"
        ));
        builder.withDetail("developer", Map.of(
                "name", "Ganesh Kumar Vellaichamy",
                "email", "ganeshkumar12883@gmail.com"
        ));
    }
}
