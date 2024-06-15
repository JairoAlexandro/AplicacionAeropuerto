package Clases;

public class PersonaImpl implements Persona {
	///////////PROPIEDADES////////////////
	private String nombre;
	private Integer edad;
	private String dNI;
	private Character sexo;
	private Double peso;
	private Double altura;
	private String codigo;
	///////////CONSTANTES///////////////////
	private final Character SEXO_POR_DEFECTO = 'H';
	private final Integer DEBAJO_PESO_IDEAL = -1;
	private final Integer PESO_IDEAL = 0;
	private final Integer ENCIMA_PESO_IDEAL = 1;
	private final Integer MAYOR_EDAD = 18;
	public final String LETRAS_DNI = "TRWAGMYFPDXBNJZSQVHLCKE";
	///////////CONSTRUCTORES////////////////
	//Por defecto
	
	public PersonaImpl(){
		this.nombre = "";
		this.edad = 0;
		this.dNI = "63562655Q";
		this.sexo = SEXO_POR_DEFECTO;
		this.peso = 0.0;
		this.altura = 0.0;
	}
	public PersonaImpl(String nombre, Integer edad, Character sexo) {
		this.nombre = nombre;
		this.edad = edad;
		this.dNI = "63562655Q";
		this.sexo = comprobarSexo(sexo);
		this.peso = 0.0;
		this.altura = 0.0;
	}
	public PersonaImpl(String nombre, Integer edad,String dNI, Character sexo, Double peso, Double altura) {
		this.nombre = nombre;
		this.edad = edad;
		this.dNI = dNI;
		this.sexo = comprobarSexo(sexo);
		this.peso = peso;
		this.altura = altura;
	}
	
	
	/////////////GET/SET////////////////
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEdad() {
		return edad;
	}
	public void setEdad(Integer edad) {
		this.edad = edad;
	}
	public String getDNI() {

		return dNI;
	}
	public Character getSexo() {
		return sexo;
	}
	public void setSexo(Character sexo) {
		this.sexo = comprobarSexo(sexo);
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Double getAltura() {
		return altura;
	}
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
	//////////////MÉTODOS/////////////////
	
	public Integer calcularIMC() {
		Integer res;
		if(this.getPeso()/(Math.pow(this.getAltura(), 2)) < 20) {
			res = DEBAJO_PESO_IDEAL;
		}
		else if(this.peso/(Math.pow(this.altura, 2)) >= 20 && this.peso/(Math.pow(this.altura, 2)) <= 25) {
			res = PESO_IDEAL;
		}
		else {
			res = ENCIMA_PESO_IDEAL;
		}
		return res;
	}
	public boolean esMayorDeEdad(){
		boolean res = false;
		if(this.getEdad() >= MAYOR_EDAD) {
			res = true;
		}
		return res;
	}
	
	private Character comprobarSexo(char sexo) {
		char res;
		String sexoMayuscula = String.valueOf(sexo).toUpperCase();
		sexo = sexoMayuscula.charAt(0);
		if(sexo == 'H' || sexo == 'M') {
			res = sexo;
		}
		else {
			res = SEXO_POR_DEFECTO;
		}
		return res;
	}
//Marcos Fernández Moya presenta: Mi codigo 2 Reloaded//
//	private Boolean comprobarSexo(Character sex) {
//
//	       boolean res = true;
//
//	       if (!(sex == 'H' || sex == 'F')) {
//
//	           res = false;
//
//	           this.setSexo(SEXO_DEFECTO);
//
//	       } else {
//	           this.setSexo(sex);
//	       }
//
//	       return res;
//	   }
	public String toString() {
		return "Nombre: " + this.getNombre() + ", Edad: " + this.getEdad() + ", DNI: " + this.getDNI() 
		+ ", Sexo: " + this.getSexo() + ", Peso: " + this.getPeso() + ", Altura: " + this.getAltura();
	}
	
	private String generaDNI() {
		int numero;
		String numeroString = "";
		String numeroDNI = "";
		int numeroDNIInt;

		for (int i = 0; i < 8; i++) {
			numero = (int) (Math.random() * 10);
			numeroString = String.valueOf(numero);
			numeroDNI = numeroDNI + numeroString;
		}
		numeroDNIInt = Integer.valueOf(numeroDNI);
		char letra = LETRAS_DNI.charAt(numeroDNIInt % 23);
		return numeroDNI + letra; 

	}
	

	
}
