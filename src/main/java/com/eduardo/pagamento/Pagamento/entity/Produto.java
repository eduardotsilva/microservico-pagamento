package com.eduardo.pagamento.Pagamento.entity;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import com.eduardo.pagamento.Pagamento.vo.ProdutoVO;

@Entity
@Table(name = "produto")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Produto {

    @Id
    private Long id;

    @Column(name = "estoque", nullable = false, length = 10)
    private Integer estoque;

    
    public static Produto create(ProdutoVO produtoVO) {
    	return new ModelMapper().map(produtoVO, Produto.class);
    }
    
}
