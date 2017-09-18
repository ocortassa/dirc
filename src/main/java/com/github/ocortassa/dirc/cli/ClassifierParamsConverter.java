package com.github.ocortassa.dirc.cli;

import com.beust.jcommander.IStringConverter;

/**
 * Created by [OC] on 18/08/2014.
 */
public class ClassifierParamsConverter implements IStringConverter<Boolean> {

    @Override
    public Boolean convert(String value) {
        return Boolean.valueOf(value);
    }
}
