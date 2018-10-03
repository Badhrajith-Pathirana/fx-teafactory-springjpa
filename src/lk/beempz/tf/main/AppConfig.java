package lk.beempz.tf.main;

import lk.beempz.tf.business.custom.impl.BankBOImpl;
import lk.beempz.tf.dao.custom.impl.BankDAOImpl;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(JPAConfig.class)
@Configuration
@ComponentScan(basePackageClasses = {BankDAOImpl.class , BankBOImpl.class})
public class AppConfig {
}
