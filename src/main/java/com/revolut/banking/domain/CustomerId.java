package com.revolut.banking.domain;

import org.dizitart.no2.NitriteId;
import org.dizitart.no2.objects.Id;

import java.lang.annotation.Annotation;

/**
 * Created by 340675 on 11/08/2017.
 */
public class CustomerId implements Id{
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
