package org.equinox.util;

import org.springframework.data.domain.Sort;

public class SortingDirectionUtils {
    public static Sort.Direction convertFromString(String directionAsString) {
        switch (directionAsString.toLowerCase()) {
            case "asc":
                return Sort.Direction.ASC;
            case "desc":
                return Sort.Direction.DESC;
            default:
                return Sort.Direction.ASC;
        }
    }
}
