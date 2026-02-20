package TIW_project_JS.Controllers.Student;

public class ExamInfo
{
	@SuppressWarnings("unused")
	private String nomeInsegnamento;
	@SuppressWarnings("unused")
	private String nomeDocente;
	@SuppressWarnings("unused")
	private String data;
	@SuppressWarnings("unused")
	private String voto;
	@SuppressWarnings("unused")
	private String stato;
	@SuppressWarnings("unused")
	private boolean rifiutabile;
	
	public ExamInfo(String nomeInsegnamento, String nomeDocente, String data, String voto, String stato, boolean rifiutabile) {
		super();
		this.nomeInsegnamento = nomeInsegnamento;
		this.nomeDocente = nomeDocente;
		this.data = data;
		this.voto = voto;
		this.stato = stato;
		this.rifiutabile = rifiutabile;
	}
}
