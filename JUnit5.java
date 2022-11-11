import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class JUnit5 {

	@Test
	void test() {
		MyHashMap<String, Integer> caca = new MyHashMap<>();
		
		caca.put("hola", 1);
		caca.put("perro", 2);
		caca.put("agua", 3);
		System.out.println(caca);
		
	}

}
