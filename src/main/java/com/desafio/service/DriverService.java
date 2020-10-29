package com.desafio.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.desafio.util.Constantes;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

@Service
public class DriverService {

	private WebClient webClient;

	/**
	 * Executa o lógica para gerar os nicks e gerar os cpf's
	 * 
	 * @return String
	 */
	public String executar() {
		try {
			WebDriver driver = iniciarPhantomDriver();

			System.out.println("Iniciando...");
			gerarArquivo(driver);

			return "Finalizando com sucesso";
		} catch (Exception e) {
			return "Erro ao executar";
		}
	}

	/**
	 * Executa o phantomJS
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public WebDriver iniciarPhantomDriver() throws FileNotFoundException {
		System.setProperty("phantomjs.binary.path", getPathPhatonDriver());
		return new PhantomJSDriver();
	}

	/**
	 * Retorna o path do caminha do phantomJS
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getPathPhatonDriver() throws FileNotFoundException {
		String path = ResourceUtils.getFile("src/main/resources/drivers/phantomjs").toString();

		return path;
	}

	/**
	 * Retorna o path do caminho do arquivo resultado.txt
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public String getPathResultado() throws FileNotFoundException {
		return ResourceUtils.getFile("src/main/resources/resultado.txt").toString();
	}

	/**
	 * Gera os 50 nicks
	 * 
	 * @param driver
	 */
	public List<WebElement> getListaNicks(WebDriver driver) {
		System.out.println("Gerando nicks..");
		driver.get(Constantes.URL_GERADOR_NICKS);
		driver.findElement(By.xpath("//*[@id=\"method\"]/option[2]")).click();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).clear();
		driver.findElement(By.xpath("//*[@id=\"quantity\"]")).sendKeys("50");
		Select nrLetras = new Select(driver.findElement(By.id("limit")));
		nrLetras.selectByValue("8");
		driver.findElement(By.xpath("//*[@id=\"bt_gerar_nick\"]")).click();
		
		WebElement allElements = driver.findElement(By.xpath("//*[@id=\"nicks\"]/ul"));
		return allElements.findElements(By.tagName("li"));
	}

	
	public void gerarArquivo(WebDriver driver) throws FailingHttpStatusCodeException, IOException {
		List<WebElement> elements = getListaNicks(driver);

		PrintStream stream = new PrintStream(new FileOutputStream(getPathResultado()));
		for (WebElement element: elements) {
			for(String cpf: getListaCpf()) {
				stream.println(element.getText() + "; " + cpf);
			}
		}
	}

	public List<String> getListaCpf() throws FailingHttpStatusCodeException, IOException {
		List<String> lista = new ArrayList<>();
		WebRequest webRequest = new WebRequest(new URL("https://www.4devs.com.br/ferramentas_online.php"),
				HttpMethod.POST);
		webRequest.setRequestBody("acao=gerar_cpf&pontuacao=S&cpf_estado=");

		while (lista.stream().count() != 50) {
			HtmlPage response = getWebClient().getPage(webRequest);
			lista.add(response.getWebResponse().getContentAsString());
		}

		return lista;
	}
	private void setWebClient() {

		webClient = new WebClient(BrowserVersion.CHROME);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setUseInsecureSSL(true);
		webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setJavaScriptEnabled(false);

	}

	public WebClient getWebClient() {
		if (webClient == null) {
			setWebClient();
		}
		return webClient;
	}

}
