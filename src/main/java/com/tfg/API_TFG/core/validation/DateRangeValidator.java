package com.tfg.API_TFG.core.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Object> {

    private String startField;
    private String endField;

    @Override
    public void initialize(ValidDateRange constraintAnnotation) {
        this.startField = constraintAnnotation.start();
        this.endField = constraintAnnotation.end();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        Object startObj = new BeanWrapperImpl(value).getPropertyValue(startField);
        Object endObj = new BeanWrapperImpl(value).getPropertyValue(endField);

        if (startObj == null || endObj == null) return true;

        if (!(startObj instanceof Comparable) || !(endObj instanceof Comparable)) return true;
        if (!startObj.getClass().isAssignableFrom(endObj.getClass())) return true;

        Comparable start = (Comparable) startObj;
        Comparable end = (Comparable) endObj;

        boolean ok = end.compareTo(start) >= 0;
        if (!ok) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode(endField)
                    .addConstraintViolation();
        }
        return ok;
    }
}