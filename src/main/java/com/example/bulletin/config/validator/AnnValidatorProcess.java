package com.example.bulletin.config.validator;

import com.example.bulletin.entity.Announce;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AnnValidatorProcess implements ConstraintValidator<AnnValidator, Announce> {
    @Override
    public boolean isValid(Announce announce, ConstraintValidatorContext context) {
        if (announce.getPublishDate() == null || announce.getDueDate() == null) return true;
        if (announce.getDueDate().isBefore(announce.getPublishDate())) {
            context.disableDefaultConstraintViolation();

            context.buildConstraintViolationWithTemplate("截止日期不能早於發佈日期")
                    .addPropertyNode("dueDate")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
