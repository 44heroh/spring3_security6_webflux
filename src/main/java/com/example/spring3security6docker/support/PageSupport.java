package com.example.spring3security6docker.support;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import java.util.List;

@Value
public class PageSupport<T> {

    public static final String FIRST_PAGE_NUM = "0";
    public static final String DEFAULT_PAGE_SIZE = "20";

    List<T> content;
    int pageNumber;
    int pageSize;
    long totalElements;

    @JsonProperty
    public long totalPages() {
        return pageSize > 0 ? (totalElements - 1) / pageSize + 1 : 0;
    }

    @JsonProperty
    public boolean first() {
        return pageNumber == Integer.parseInt(FIRST_PAGE_NUM);
    }

    @JsonProperty
    public boolean last() {
        return (pageNumber + 1) * pageSize >= totalElements;
    }

}
