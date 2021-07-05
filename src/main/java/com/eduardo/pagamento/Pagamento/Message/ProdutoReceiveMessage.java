package com.eduardo.pagamento.Pagamento.Message;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.eduardo.pagamento.Pagamento.entity.Produto;
import com.eduardo.pagamento.Pagamento.repository.ProdutoRepository;
import com.eduardo.pagamento.Pagamento.vo.ProdutoVO;

@Component
public class ProdutoReceiveMessage {
	
	private final ProdutoRepository produtoRepository;

	@Autowired
	public ProdutoReceiveMessage(ProdutoRepository produtoRepository) {
		this.produtoRepository = produtoRepository;
	}
	
	@RabbitListener(queues = {"${crud.rabbitmq.queue}"})
	public void receiveMessage(@Payload ProdutoVO produtoVO) {
		produtoRepository.save(Produto.create(produtoVO));
	}
	

}
