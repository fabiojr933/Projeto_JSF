package br.com.projetojsf01;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import br.com.Dao.DaoGeneric;
import br.com.Entidades.Lancamento;
import br.com.Entidades.Pessoa;
import br.com.Repository.IDaoLacamnetoImpl;
import br.com.Repository.IDaoLancamento;

@ViewScoped
@ManagedBean(name = "lancamentoBean")
public class LancamentoBean {

	private Lancamento lancamento = new Lancamento();
	private DaoGeneric<Lancamento> DaoGeneric = new br.com.Dao.DaoGeneric<Lancamento>();
	private List<Lancamento> lista = new ArrayList<Lancamento>();
	private IDaoLancamento daoLancamento = new IDaoLacamnetoImpl();

	// METEDO SALVAR
	public String salvar() {
		// RECUPERANDO A SESSION DO USUARIO
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		lancamento.setUsuario(pessoa);
		DaoGeneric.salvar2(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}

	// METEDO NOVO
	public String novo() {
		lancamento = new Lancamento();
		return "";
	}

	// METEDO REMOVER
	public String Remover() {
		DaoGeneric.DeletarPorId(lancamento);
		lancamento = new Lancamento();
		carregarLancamentos();
		return "";
	}

	// METEDO CARREGAR LANCAMENTOS
	@PostConstruct
	private void carregarLancamentos() {
		//RECUPERANDO A SESSION DO USUARIO
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		lista =  daoLancamento.consulta(pessoaUser.getId());
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public DaoGeneric<Lancamento> getDaoGeneric() {
		return DaoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Lancamento> daoGeneric) {
		DaoGeneric = daoGeneric;
	}

	public List<Lancamento> getLista() {
		return lista;
	}

	public void setLista(List<Lancamento> lista) {
		this.lista = lista;
	}

}
