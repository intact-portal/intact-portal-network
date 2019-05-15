package uk.ac.ebi.intact.search.service;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;


/**
 * @author Elisabet Barrera
 */

@Configuration
@EnableSolrRepositories(basePackages = "uk.ac.ebi.intact.search.repository",
        schemaCreationSupport = true)
@ComponentScan(basePackages = {"uk.ac.ebi.intact.search.service"})
@SpringBootApplication
public class SolrTestConfiguration {

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient("http://localhost:8983/solr");
    }

    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client);
    }

//    /**
//     * Remove test data when context is shut down.
//     */
//    public @PreDestroy
//    void deleteDocumentsOnShutdown() {
//        service.deleteAll();
//    }


//   //TODO Try this configuration for embeddedSolr (testing)
//    @Bean
//    public SolrClient solrClient() {
//        EmbeddedSolrServerFactory factory = new EmbeddedSolrServerFactory("classpath:com/acme/solr");
//        return factory.getSolrServer();
//    }
//
//    @Bean
//    public SolrOperations solrTemplate() {
//        return new SolrTemplate(solrClient());
//    }
}
