package uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.InteractionType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.archetypes.InteractorType;
import uk.ac.ebi.intact.network.ws.controller.utils.mapper.ontology.def.Term;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class MIOntologyTest {
    public MIOntology ontology;
    public InteractorTypeMapper interactorTypeMapper;
    public InteractionTypeMapper interactionTypeMapper;


    @Before
    public void setUp() {
        ontology = new MIOntology();
        interactorTypeMapper = new InteractorTypeMapper(ontology);
        interactionTypeMapper = new InteractionTypeMapper(ontology);
    }

    @Test
    public void testGetRootOfOntology() {
        assertNotNull(interactorTypeMapper.getRootOfOntology());
        assertNotNull(interactionTypeMapper.getRootOfOntology());

        assertFalse(interactorTypeMapper.getRootOfOntology().getChildren().isEmpty());
        assertFalse(interactionTypeMapper.getRootOfOntology().getChildren().isEmpty());
    }

    @Test
    public void testMatchArchetype() {
        assertNull(ontology.get().getTerm(null));
        assertNull(ontology.get().getTerm(""));
        assertNull(ontology.get().getTerm("MI_0000"));

        Term candidateSet = ontology.get().getTerm("MI:1305");
        assertNotNull(candidateSet);
        assertEquals(InteractorType.MOLECULE_SET, interactorTypeMapper.getArchetype("MI:1305")); // Candidate Set
        assertEquals(InteractionType.ENZYMATIC_REACTION, interactionTypeMapper.getArchetype("MI:0207")); // Formylation reaction (level archetype + 1)
        assertEquals(InteractionType.ENZYMATIC_REACTION, interactionTypeMapper.getArchetype("MI:0202")); // Depalmitoylation reaction (level archetype + 3)
        assertEquals(InteractionType.DIRECT_INTERACTION, interactionTypeMapper.getArchetype("MI:0407")); // Direct interaction (Between 2 archetypes)
    }
}