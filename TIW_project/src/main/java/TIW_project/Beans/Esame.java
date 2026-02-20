package TIW_project.Beans;

public class Esame
{
	private int id_appello;
	private int matricola_studente;
	private String nominativo_studente;
	private String email;
	private String corso_di_laurea;
	private String voto;
	private String desc_esito;


	public Esame(int id_appello, int matricola_studente, String nominativo,
			String email, String corso_di_laurea, String desc_esito, String voto)
	{
		this.id_appello = id_appello;
		this.matricola_studente = matricola_studente;
		this.nominativo_studente = nominativo;
		this.email = email;
		this.corso_di_laurea = corso_di_laurea;
		this.desc_esito = desc_esito;
		this.voto = voto;
	}
	
	public int getId_appello()
	{
		return id_appello;
	}
	
	public int getMatricola_studente()
	{
		return matricola_studente;
	}
	
	public String getNominativo()
	{
		return nominativo_studente;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getCorso_di_laurea()
	{
		return corso_di_laurea;
	}
	
	public String getDesc_esito()
	{
		return desc_esito;
	}
	
	public String getVoto()
	{
		return voto;
	}
}
