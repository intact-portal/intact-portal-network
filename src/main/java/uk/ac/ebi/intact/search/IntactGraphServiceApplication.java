package uk.ac.ebi.intact.search;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.solr.core.SolrOperations;
import org.springframework.data.solr.core.SolrTemplate;
import uk.ac.ebi.intact.search.utils.GraphUtility;

@SpringBootApplication
public class IntactGraphServiceApplication {

    public static void main(String[] args) {
        GraphUtility.initializeSpeciesDescendantsMapping();
        GraphUtility.initializeInteractorTypeDescendantsMapping();
        SpringApplication.run(IntactGraphServiceApplication.class, args);
    }

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient("http://localhost:8983/solr");
    }

    @Bean
    public SolrOperations solrTemplate() {
        return new SolrTemplate(solrClient());
    }
}
