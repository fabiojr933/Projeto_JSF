package br.com.Repository;

import java.util.List;

import br.com.Entidades.Lancamento;

public interface IDaoLancamento {

	List<Lancamento> consulta(Long codigoUser);
}
