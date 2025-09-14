package com.dangphuoctai.Ebook_BE.payloads.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestOrder {
    String email;
    
    List<Long> bookIds;
}
