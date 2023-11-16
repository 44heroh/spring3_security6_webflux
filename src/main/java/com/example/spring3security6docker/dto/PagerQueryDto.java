package com.example.spring3security6docker.dto;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PagerQueryDto {

    private String sortBy;
    private String sortDirection;
    private Integer page;
    private Integer pageSize;

    @Override
    public String toString() {
        return "PagerQueryDto{" +
                "sortBy='" + sortBy + '\'' +
                ", sortDirection='" + sortDirection + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                '}';
    }

    public String getSortBy() {
        return sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getPageSize() {
        return pageSize;
    }
}
