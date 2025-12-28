package com.renault.garage.utlis;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public final class PageableUtil {

    private PageableUtil() {}

    /**
     * Build a Pageable object with dynamic page, size, and sorting.
     *
     * @param page       page index (0-based)
     * @param size       page size
     * @param sortFields list of field names to sort by (can be nested, e.g., "address.city")
     * @param directions list of directions corresponding to fields ("asc" or "desc"), if null or invalid defaults to "asc"
     * @return Pageable
     */
    public static Pageable buildPageable(int page, int size, List<String> sortFields, List<String> directions) {
        Sort sort = Sort.unsorted();

        if (sortFields != null && !sortFields.isEmpty()) {
            for (int i = 0; i < sortFields.size(); i++) {
                String field = sortFields.get(i);
                String dir = (directions != null && i < directions.size()) ? directions.get(i).toLowerCase() : "asc";

                Sort.Direction direction = dir.equals("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

                sort = sort.and(Sort.by(direction, field));
            }
        }

        return PageRequest.of(page, size, sort);
    }
}
