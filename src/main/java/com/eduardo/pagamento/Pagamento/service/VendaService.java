package com.eduardo.pagamento.Pagamento.service;

import com.eduardo.pagamento.Pagamento.entity.ProdutoVenda;
import com.eduardo.pagamento.Pagamento.entity.Venda;
import com.eduardo.pagamento.Pagamento.exception.ResourceNotFoundException;
import com.eduardo.pagamento.Pagamento.repository.ProdutoRepository;
import com.eduardo.pagamento.Pagamento.repository.ProdutoVendaRepository;
import com.eduardo.pagamento.Pagamento.repository.VendaRepository;
import com.eduardo.pagamento.Pagamento.vo.VendaVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoVendaRepository produtoVendaRepository;

    @Autowired
    public VendaService(VendaRepository vendaRepository, ProdutoRepository produtoRepository, ProdutoVendaRepository produtoRepository1, ProdutoVendaRepository produtoVendaRepository) {
        this.vendaRepository = vendaRepository;
        this.produtoVendaRepository = produtoVendaRepository;
    }

    public VendaVO create(VendaVO vendaVO) {

        Venda venda = vendaRepository.save(Venda.create(vendaVO));
        List<ProdutoVenda> listProdutosVenda = new ArrayList<>();

        vendaVO.getProdutos()
                .forEach(
                        p -> {
                            ProdutoVenda produtoVenda = ProdutoVenda.create(p);
                            produtoVenda.setVenda(venda);
                            listProdutosVenda.add(produtoVendaRepository.save(produtoVenda));
                        }
                );
        venda.setProdutos(listProdutosVenda);

        return VendaVO.create(venda);
    }

    public Page<VendaVO> findAll(Pageable pageable) {
        var page = vendaRepository.findAll(pageable);
        return page.map(this::convertToVendaVO);
    }

    private VendaVO convertToVendaVO(Venda venda) {
        return VendaVO.create(venda);
    }

    public VendaVO findById(Long id) {
        var entity = vendaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID")
        );
        return VendaVO.create(entity);
    }
}
