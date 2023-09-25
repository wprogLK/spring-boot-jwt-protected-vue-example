package ch.wproglk.example.web.utilities;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

@ComponentScan(basePackages = "ch.wproglk.aclu")
@ActiveProfiles("test")
public abstract class ControllerTest
{
}
