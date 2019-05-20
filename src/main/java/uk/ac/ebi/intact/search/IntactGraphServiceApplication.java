package uk.ac.ebi.intact.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;

@SpringBootApplication
public class IntactGraphServiceApplication {

	@Bean
	public SolrClient solrClient() {
		return new HttpSolrClient("http://localhost:8983/solr");
	}

	@Bean
	public SolrOperations solrTemplate() {
		return new SolrTemplate(solrClient());
	}

	public static void main(String[] args) {
		SpringApplication.run(IntactGraphServiceApplication.class, args);
	}
}
