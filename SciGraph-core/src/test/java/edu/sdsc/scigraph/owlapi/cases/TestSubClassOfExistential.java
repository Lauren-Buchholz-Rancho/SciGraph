package edu.sdsc.scigraph.owlapi.cases;

import static com.google.common.collect.Iterables.getOnlyElement;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import edu.sdsc.scigraph.neo4j.GraphUtil;

public class TestSubClassOfExistential extends OwlTestCase {

	/**
	 * 
	 * See
	 * https://github.com/SciCrunch/SciGraph/wiki/MappingToOWL#subclassof-axioms
	 * 
	 * Reduction step should give us a simple edge {sub p super}
	 * 
	 */
	@Test
	public void testSubclass() {
		Node subclass = getNode("http://example.org/subclass");
		Node superclass = getNode("http://example.org/superclass");

		RelationshipType p = DynamicRelationshipType.withName( "http://example.org/p" );
		Relationship relationship = getOnlyElement(GraphUtil.getRelationships(subclass, superclass, p));
		assertThat("subclassOf relationship should start with the subclass.",
				relationship.getStartNode(), is(subclass));
		assertThat("subclassOf relationship should end with the superclass.",
				relationship.getEndNode(), is(superclass));
	}

}
