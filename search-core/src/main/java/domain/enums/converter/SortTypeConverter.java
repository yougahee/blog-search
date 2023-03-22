package domain.enums.converter;

import domain.enums.SortType;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Locale;

public class SortTypeConverter implements Converter<String, SortType> {

    @Override
    public SortType convert(String s) {
        return StringUtils.isNotEmpty(s) ? SortType.valueOf(s.toLowerCase(Locale.ROOT)) : null;
    }
}
