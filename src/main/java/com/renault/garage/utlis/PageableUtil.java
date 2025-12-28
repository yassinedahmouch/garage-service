package com.renault.garage.utlis;

import java.util.Map;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtil {

    private PageableUtil() {}

    public static Pageable buildPageable(int page, int size, Map<String, String> sort) {
        Sort finalSort = Sort.unsorted();

        if (sort != null && !sort.isEmpty()) {
            for (Map.Entry<String, String> entry : sort.entrySet()) {
                String field = entry.getKey();
                String dir = entry.getValue() != null ? entry.getValue().toLowerCase() : "asc";
                Sort.Direction direction = dir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

                finalSort = finalSort.and(Sort.by(direction, field));
            }
        }

        return PageRequest.of(page, size, finalSort);
    }
}
