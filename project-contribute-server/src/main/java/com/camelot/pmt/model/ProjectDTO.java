package com.camelot.pmt.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectDTO {
    
    private Integer projectState;
    
    private Integer stateCount;
    
    private BigDecimal sumBudget;
}
