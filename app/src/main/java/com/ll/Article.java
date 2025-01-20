package com.ll;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Article {
    private int id;
    private String title;
    private String body;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private boolean blind;
}
