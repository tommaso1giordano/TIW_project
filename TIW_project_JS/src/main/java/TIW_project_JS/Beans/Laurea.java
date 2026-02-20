package TIW_project_JS.Beans;

public class Laurea
{
	private int id;
	private String nome;
	private String tipo_laurea;
	
	public Laurea(int id, String nome, String tipo_laurea)
	{
		this.id = id;
		this.nome = nome;
		this.tipo_laurea = tipo_laurea;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getNome()
	{
		return nome;
	}
	
	public String getTipo_laurea()
	{
		return tipo_laurea;
	}
}
