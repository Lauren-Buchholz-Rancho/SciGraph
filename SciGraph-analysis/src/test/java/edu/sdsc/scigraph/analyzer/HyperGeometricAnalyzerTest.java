/**
 * Copyright (C) 2014 The SciGraph authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sdsc.scigraph.analyzer;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLOntologyWalker;

import com.google.common.io.Resources;

import edu.sdsc.scigraph.owlapi.GraphOwlVisitor;
import edu.sdsc.scigraph.owlapi.OwlPostprocessor;
import edu.sdsc.scigraph.owlapi.curies.CurieUtil;
import edu.sdsc.scigraph.owlapi.loader.OwlLoadConfiguration.MappedProperty;
import edu.sdsc.scigraph.util.GraphTestBase;


public class HyperGeometricAnalyzerTest extends GraphTestBase {

  static HyperGeometricAnalyzer analyzer;
  static CurieUtil util;

  @BeforeClass
  public static void loadPizza() throws Exception {
    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
    String uri = Resources.getResource("pizza.owl").toURI().toString();
    IRI iri = IRI.create(uri);
    manager.loadOntologyFromOntologyDocument(iri);
    OWLOntologyWalker walker = new OWLOntologyWalker(manager.getOntologies());

    GraphOwlVisitor visitor = new GraphOwlVisitor(walker, graph, new ArrayList<MappedProperty>());
    walker.walkStructure(visitor);
    Map<String, String> categories = new HashMap<>();
    categories.put("http://www.co-ode.org/ontologies/pizza/pizza.owl#NamedPizza", "pizza");
    categories.put("http://www.co-ode.org/ontologies/pizza/pizza.owl#PizzaTopping", "topping");
    OwlPostprocessor postprocessor = new OwlPostprocessor(graphDb, categories);
    postprocessor.processCategories(categories);
    postprocessor.processSomeValuesFrom();

    Map<String, String> map = new HashMap<>();
    map.put("pizza", "http://www.co-ode.org/ontologies/pizza/pizza.owl#");
    util = new CurieUtil(map);
    analyzer = new HyperGeometricAnalyzer(graphDb, util, graph);
  }

  @Test
  public void test() {
    AnalyzeRequest request = new AnalyzeRequest();
    request.getSamples().addAll(newHashSet("pizza:FourSeasons", "pizza:AmericanHot", "pizza:Cajun"));
    request.setOntologyClass("pizza:Pizza");
    request.setPath("pizza:hasTopping");
    //assertThat(analyzer.analyze(request), is(not(empty())));
  }

}
