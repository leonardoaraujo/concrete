/**
 * @author Leonardo Araújo
 */
package br.com.concrete.teste.leonardo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The Class LeonardoApplication.
 */
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan
public class LeonardoApplication {

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(LeonardoApplication.class, args);
		
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		String resultado = bCryptPasswordEncoder.encode("123");
//		System.out.println("Hash " + resultado);
	}

}
