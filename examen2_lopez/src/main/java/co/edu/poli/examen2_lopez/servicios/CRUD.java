package co.edu.poli.examen2_lopez.servicios;

import java.util.List;

public interface CRUD<T> {

	String create(T t) throws Exception;

	<K> T readone(K id) throws Exception;
	
	List<T> readall() throws Exception;
 
} 
