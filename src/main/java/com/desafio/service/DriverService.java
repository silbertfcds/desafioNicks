package com.desafio.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.desafio.util.Constantes;

@Service
public class DriverService {

	
	/**Executa o lógica para gerar os nicks e gerar os cpf's
	 * @return String
	 */
	public String executar(){
		try {
			WebDriver driver = iniciarPhantomDriver();
			WebDriver driver2 = iniciarPhantomDriver();
			
			System.out.println("Iniciando...");
			gerarNick(driver);
			gerarCPF(driver, driver2);
			
			return "Finalizando com sucesso";
		} catch (Exception e) {
			return "Erro ao executar";
		}
	}
	
	/**Executa o phantomJS
	 * @return
	 * @throws FileNotFoundException
	 */
	public WebDriver iniciarPhantomDriver() throws FileNotFoundException {
		System.setProperty("phantomjs.binary.path", getPathPhatonDriver());
		return new PhantomJSDriver();
	}
	
	
	/**Retorna o path do caminha do phantomJS
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getPathPhatonDriver() throws FileNotFoundException {
		String path = ResourceUtils.getFile("src/main/resources/drivers/phantomjs").toString();
		
		return path;
	}
	
	/**Retorna o path do caminho do arquivo resultado.txt
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getPathResultado() throws FileNotFoundException {
		return  ResourceUtils.getFile("src/main/resources/resultado.txt").toString();
	}
	
	/**Gera os 50 nicks
	 * @param driver
	 */
	public void gerarNick(WebDriver driver) {
		System.out.println("Gerando nicks..");
		driver.get(Constantes.URL_GERADOR_NICKS);
		driver.findElement(By.xpath("//*[@id=\"method\"]/option[2]")).click();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).sendKeys("50");
		Select nrLetras = new Select(driver.findElement(By.id("limit")));
		nrLetras.selectByValue("8");
		driver.findElement(By.xpath("//*[@id=\"bt_gerar_nick\"]")).click();	
	} 
	
	
	/**Gera os cpf's de forma aleatória.
	 * @param driver
	 * @param driver2
	 * @throws FileNotFoundException
	 */
	public void gerarCPF(WebDriver driver, WebDriver driver2) throws FileNotFoundException {
		WebElement allElements = driver.findElement(By.xpath("//*[@id=\"nicks\"]/ul"));
		List<WebElement> Elements = allElements.findElements(By.tagName("li")); 
		
	    PrintStream stream = new PrintStream(new FileOutputStream(getPathResultado()));
	    for (int i = 0; i < Elements.size(); i++) {
	    	driver2.get(Constantes.URL_GERADOR_CPF);
			driver2.findElement(By.xpath("//*[@id=\"bt_gerar_cpf\"]")).click();

	      	new WebDriverWait(driver2, 10)
	      	.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"texto_cpf\"]/span")));

	      	String cpf = driver2.findElement(By.xpath("//*[@id=\"texto_cpf\"]")).getText();
	      	stream.println(Elements.get(i).getText()+"; "+cpf);
		}
	}
	
}
