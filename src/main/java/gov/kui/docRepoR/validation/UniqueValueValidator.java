package gov.kui.docRepoR.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Service
public class UniqueValueValidator implements ConstraintValidator<UniqueValue, String> {

    private ApplicationContext applicationContext;
    private CheckValueIsExists service;
    private String fieldName;

    /*
    public UniqueValueValidator(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }
     */

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext){
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(UniqueValue uniqueValue){
        Class<? extends CheckValueIsExists> clazz = uniqueValue.service();
        this.fieldName = uniqueValue.fieldName();
        this.service = this.applicationContext.getBean(clazz);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return !service.isExistsValueInField(value, this.fieldName);
    }
}
