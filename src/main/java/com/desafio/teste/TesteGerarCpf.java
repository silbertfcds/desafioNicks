package com.desafio.teste;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class TesteGerarCpf {

	WebClient webClient;

	@Test
	public void gerarCpf() throws FailingHttpStatusCodeException, IOException {
		List<String> lista = new ArrayList<>();
		WebRequest webRequest = new WebRequest(new URL("https://www.4devs.com.br/ferramentas_online.php"),
				HttpMethod.POST);
		webRequest.setRequestBody("acao=gerar_cpf&pontuacao=S&cpf_estado=");

		while (lista.stream().count() != 50) {
			HtmlPage response = getWebClient().getPage(webRequest);
			lista.add(response.getWebResponse().getContentAsString());
		}

		lista.forEach(System.out::println);
		System.out.println("Total:" + lista.stream().count());
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
