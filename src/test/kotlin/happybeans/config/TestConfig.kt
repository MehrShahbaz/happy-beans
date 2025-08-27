package happybeans.config

import happybeans.HappyBeansApplication
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType

@TestConfiguration
@ComponentScan(
    basePackageClasses = [HappyBeansApplication::class],
    excludeFilters = [
        ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = [DataInitializer::class],
        ),
    ],
)
class TestConfig
