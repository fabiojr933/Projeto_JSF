package br.com.Repository;

import java.util.List;

import javax.faces.model.SelectItem;

import br.com.Entidades.Pessoa;

public interface IDaoPessoa {
   
	   Pessoa consultarUsuario(String Login, String Senha);
	   
	   List<SelectItem> listaEstados();
   
}
