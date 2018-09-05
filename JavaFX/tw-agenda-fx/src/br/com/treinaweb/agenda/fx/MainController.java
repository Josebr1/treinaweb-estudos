package br.com.treinaweb.agenda.fx;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import br.com.treinaweb.agenda.entidades.Contato;
import br.com.treinaweb.agenda.repositorios.impl.ContatoRepositorio;
import br.com.treinaweb.agenda.repositorios.interfaces.AgendaRepositorio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class MainController implements Initializable {

	@FXML
	private TableView<Contato> tabelaContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAlterar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private TextField txfNome;
	@FXML
	private TextField txfIdade;
	@FXML
	private TextField txfTelefone;
	@FXML
	private Button botaoSalvar;
	@FXML
	private Button botaoCancelar;
	private boolean isInsert;
	private Contato contato;

	private void carregarTabelaContatos() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		List<Contato> contatos = repositorioContato.selecionar();
		ObservableList<Contato> contatosObservableList = FXCollections.observableArrayList(contatos);
		this.tabelaContatos.getItems().setAll(contatosObservableList);
	}

	private void habilitarEdicaoAgenda(boolean edicaoEstaHabilitado) {
		this.txfNome.setDisable(!edicaoEstaHabilitado);
		this.txfIdade.setDisable(!edicaoEstaHabilitado);
		this.txfTelefone.setDisable(!edicaoEstaHabilitado);
		
		this.botaoCancelar.setDisable(!edicaoEstaHabilitado);
		this.botaoSalvar.setDisable(!edicaoEstaHabilitado);
		
		this.botaoInserir.setDisable(edicaoEstaHabilitado);
		this.botaoAlterar.setDisable(edicaoEstaHabilitado);
		this.botaoExcluir.setDisable(edicaoEstaHabilitado);
	}
	
	public void botaoInserir_Action() {
		this.isInsert = true;
		this.txfNome.setText("");
		this.txfIdade.setText("");
		this.txfTelefone.setText("");
		
		habilitarEdicaoAgenda(true);
	}
	
	public void botaoAlterar_Action() {
		habilitarEdicaoAgenda(true);
		this.isInsert = false;
		this.txfNome.setText(contato.getNome());
		this.txfIdade.setText(Integer.toString(this.contato.getIdade()));
		this.txfTelefone.setText(contato.getTelefone());
		
	}
	
	public void botaoCancelar_Action() {
		habilitarEdicaoAgenda(false);
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
	public void botaoSalvar_Action() {
		AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
		Contato c = new Contato();
		c.setNome(txfNome.getText());
		c.setIdade(Integer.parseInt(txfIdade.getText().trim()));
		c.setTelefone(txfTelefone.getText());
		
		if (this.isInsert) {
			repositorioContato.inserir(c);
		} else {
			repositorioContato.atualizar(c);
		}
		
		habilitarEdicaoAgenda(false);
		carregarTabelaContatos();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}
	
	public void botaoExcluir_Action() {
		Alert confirm = new Alert(AlertType.CONFIRMATION);
		confirm.setTitle("Deseja excluir?");
		confirm.setHeaderText("Confirmação da exclusão do contato");
		confirm.setContentText("Tem certeza de que deseja excluir este contato?");
	
		Optional<ButtonType> result = confirm.showAndWait();
		
		if (result.isPresent() && result.get() == ButtonType.OK) {
			AgendaRepositorio<Contato> repositorioContato = new ContatoRepositorio();
			repositorioContato.excluir(contato);
			carregarTabelaContatos();
			this.tabelaContatos.getSelectionModel().selectFirst();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		carregarTabelaContatos();
		habilitarEdicaoAgenda(false);
		
		this.tabelaContatos.getSelectionModel().selectedItemProperty().addListener((observador, contatoAntigo, contatoNovo) -> {
			if (contatoNovo != null) {
				txfNome.setText(contatoNovo.getNome());
				txfIdade.setText(String.valueOf(contatoNovo.getIdade()));
				txfTelefone.setText(contatoNovo.getTelefone());
				this.contato = contatoNovo;
			}
		});
	}

}
