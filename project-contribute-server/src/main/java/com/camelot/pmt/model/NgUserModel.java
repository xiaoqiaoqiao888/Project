package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NgUserModel {
    private String token;
    private String name;
    private String email;
    private Integer id;
    private Long time;
    private String telephone;
    private String realName;
    private String userDesc;

}
