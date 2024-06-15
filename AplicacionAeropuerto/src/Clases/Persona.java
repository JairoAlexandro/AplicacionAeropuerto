package Clases;

public interface Persona {
	public String getNombre();
	
	public void setNombre(String nombre);
	
	public Integer getEdad();
	
	public void setEdad(Integer edad);
	
	public String getDNI();
	
	public Character getSexo();
	
	public void setSexo(Character sexo);
	
	public Double getPeso();
	
	public void setPeso(Double peso);
	
	public Double getAltura();
	
	public void setAltura(Double altura);
	
	public String getCodigo();
	
	public void setCodigo(String codigo);
	
	public Integer calcularIMC();
	
	public boolean esMayorDeEdad();
	
	public String toString();
	
}
