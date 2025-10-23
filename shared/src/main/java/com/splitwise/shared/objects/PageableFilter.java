package com.splitwise.shared.objects;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageableFilter {
    private int page = 0;
    @Min(value = 1, message = "Page size must be at least 1")
    @Max(value = 500, message = "Page size must be maximum 500")
    private int pageSize = 20;

    public Pageable toPageable() {
        return PageRequest.of(page, pageSize);
    }
}
