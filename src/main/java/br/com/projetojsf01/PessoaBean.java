package br.com.projetojsf01;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

import br.com.Dao.DaoGeneric;
import br.com.Entidades.Cidades;
import br.com.Entidades.Estados;
import br.com.Entidades.Pessoa;
import br.com.JPAUtil.JPAUtil;
import br.com.Repository.IDaoPessoa;
import br.com.Repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();
	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();
	private List<SelectItem> estados;
	private List<SelectItem> cidades;
	private Part imageFoto;

	// METEDO SALVAR
	public String salvar() throws IOException{
		
		System.out.println(imageFoto);
		System.out.println(imageFoto);
		System.out.println(imageFoto);
		
		//Processar Imagem
		byte[] imagemByte = getByte(imageFoto.getInputStream());
		pessoa.setFotoIconBseOriginal(imagemByte); /*SALVA A IMAGEM ORIGINAL*/
		
		/*TRANSFORMA EM UMA BUFFIMAGE*/
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagemByte));
		
		/*PEGAR O TIPO DA IMAGE*/
		int type = bufferedImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		
		int largura = 200;
		int altura = 200;
		
		/*CRIAR A MINIATURA*/
		BufferedImage resizedImage = new BufferedImage(largura, altura, type);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		
		/*ESCREVER NOVAMENTE A IMAGE EM TAMANHO MENOR*/
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String extensao = imageFoto.getContentType().split("\\/")[1];
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem = "data:" + imageFoto.getContentType() + ";base64," + DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		/*processar image*/
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
		
		pessoa = daoGeneric.salvar2(pessoa);
		carregarListaPessoa();
		mostrarMsg("Cadastrado com sucesso");
		Novo();
		return "";
	}

	// MENSAGEM PARA O USUARIO QUANDO CADASTRO É FEITO COM SUCESSO
	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);

	}

	// deslogar
	@SuppressWarnings("static-access")
	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");
		HttpServletRequest http = (HttpServletRequest) context.getCurrentInstance().getExternalContext().getRequest();
		http.getSession().invalidate();
		return "index.xhtml";
	}

	// combo estado

	// pesquisaCep
	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = "";
			StringBuilder jsonCep = new StringBuilder();
			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}
			Pessoa gsonAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);
			pessoa.setCep(gsonAux.getCep());
			pessoa.setLogradouro(gsonAux.getLogradouro());
			pessoa.setComplemento(gsonAux.getComplemento());
			pessoa.setBairro(gsonAux.getBairro());
			pessoa.setLocalidade(gsonAux.getLocalidade());
			pessoa.setUf(gsonAux.getUf());
			pessoa.setIbge(gsonAux.getIbge());
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o cep! ");
		}
	}
	
	/*download*/
	public void download() throws IOException {
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileId = params.get("FileId");
		Pessoa pessoa = daoGeneric.consultar(Pessoa.class, fileId);
		
		HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download." + pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBseOriginal().length);
		response.getOutputStream().write(pessoa.getFotoIconBseOriginal());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().getResponseComplete();
	}

	// editar
	@SuppressWarnings("unchecked")
	public void editar() {
		if (pessoa.getCidades() != null) {
			Estados estado = pessoa.getCidades().getEstados();
			pessoa.setEstados(estado);
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
			List<SelectItem> selectItem = new ArrayList<SelectItem>();
			for (Cidades item : cidades) {
				selectItem.add(new SelectItem(item, item.getNome()));
			}
			setCidades(selectItem);
		}
	}

	public String Novo() {
		pessoa = new Pessoa();
		return "";
	}

	public String remove() {
		daoGeneric.DeletarPorId(pessoa);
		pessoa = new Pessoa();
		mostrarMsg("Removido com sucesso");
		carregarListaPessoa();
		return "";
	}

	@PostConstruct
	public void carregarListaPessoa() {
		pessoas = daoGeneric.getLista(Pessoa.class);
	}

	// METEDO LOGAR
	public String Logar() {
		Pessoa usuario = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());
		if (usuario != null) {
			// ADICIONANDO O USUARIO NA SESSION
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", usuario);
			return "PrimeiraPagina.jsf";
		} else {
			return "index.jsf";
		}

	}

	// CONTROLE DE ACESSO
	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoa = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");
		return pessoa.getPerfilUser().equals(acesso);
	}

	// carregaCidades
	@SuppressWarnings("unchecked")
	public void carregaCidades(AjaxBehaviorEvent event) {
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estado != null) {
			pessoa.setEstados(estado);
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id = " + estado.getId()).getResultList();
			List<SelectItem> selectItem = new ArrayList<SelectItem>();
			for (Cidades item : cidades) {
				selectItem.add(new SelectItem(item, item.getNome()));
			}
			setCidades(selectItem);
		}
	}

	//

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public void setImageFoto(Part imageFoto) {
		this.imageFoto = imageFoto;
	}
	public Part getImageFoto() {
		return imageFoto;
	}
	
	
	/*METEDO PARA CONVERTER UM INPUTSTREAN PARA UM ARRAY DE BYTES*/
	@SuppressWarnings("unused")
	private byte[] getByte(InputStream is) throws IOException {
		int len;
		int size = 1024;
		byte[] buf = null;
		if(is instanceof ByteArrayInputStream) {
			size = is.available();
			buf = new byte[size];
			len = is.read(buf, 0, size);
		}else {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			buf = new byte[size];
			while((len = is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf = bos.toByteArray();
		}
		return buf;
	}
}
