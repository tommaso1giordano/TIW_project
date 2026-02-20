package TIW_project_JS.Beans;

public class Scaglione
{
	private int id;
	private String nome_insegnamento;
	private int matricola_docente;
	private int anno;
	
	public Scaglione (int id, String nome_insegnamento, int matricola_docente, int anno)
	{
		this.id = id;
		this.nome_insegnamento = nome_insegnamento;
		this.matricola_docente = matricola_docente;
		this.anno = anno;
	}

	public int getId()
	{
		return id;
	}
	public String getNome_insegnamento()
	{
		return nome_insegnamento;
	}
	public int getMatricola_docente()
	{
		return matricola_docente;
	}
	public int getAnno()
	{
		return anno;
	}
}