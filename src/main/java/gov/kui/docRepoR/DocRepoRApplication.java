package gov.kui.docRepoR;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DocRepoRApplication {
	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DocRepoRApplication.class, args);

		for(String beanName: context.getBeanDefinitionNames()){
			System.out.println("beanName: "+beanName);
		}
	}
}
