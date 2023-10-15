package xyz.epicebic.ebiclib.config.adapter;

import xyz.epicebic.ebiclib.Formatting;
import xyz.epicebic.ebiclib.util.StringUtils;

public class StringTypeAdapter implements StringAdapter<String> {

    @Override
    public String toString(String value) {
        return value;
    }

    @Override
    public String fromString(String value) {
        return Formatting.translate(value);
    }
}
