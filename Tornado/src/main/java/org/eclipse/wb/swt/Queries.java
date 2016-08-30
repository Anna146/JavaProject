package org.eclipse.wb.swt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLException;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import it.unibz.krdb.obda.exception.InvalidMappingException;
import it.unibz.krdb.obda.exception.InvalidPredicateDeclarationException;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.MappingLoader;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConfiguration;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

public class Queries {

	
	public static ArrayList<Object[]> run(String sparqlQuery) {
		ArrayList<Object[]> obj = new ArrayList<Object[]>();
		final String owlfile = "C:\\Users\\Asus\\StormData\\src\\resources\\example\\ca.owl";
		final String obdafile = "C:\\Users\\Asus\\StormData\\src\\resources\\example\\ca.obda";

		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology;
		try {
			ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));
			final OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);
			QuestPreferences preference = new QuestPreferences();
			preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);
			QuestOWLConfiguration config = QuestOWLConfiguration.builder().preferences(preference).obdaModel(obdaModel)
					.build();
			QuestOWLFactory factory = new QuestOWLFactory();
			QuestOWL reasoner = factory.createReasoner(ontology, config);
			QuestOWLConnection conn = reasoner.getConnection();
			QuestOWLStatement st = conn.createStatement();
			
			QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
			List<String> elephantList = null;
			Object[] c;
			String tmp = "";
			int i = 0;
			int columnSize = rs.getColumnCount();
			c = new Object[columnSize];
			final ToStringRenderer renderer = ToStringRenderer.getInstance();
			while (rs.nextRow()) {
				for (int idx = 1; idx <= columnSize; idx++) {
					OWLObject binding = rs.getOWLObject(idx);
					if ('\"' == renderer.getRendering(binding).charAt(0)) {
						elephantList = Arrays.asList(renderer.getRendering(binding).split("\""));
						if (elephantList.size() > 0) {
							tmp = elephantList.get(1);
						} else
							tmp = "?";
						c[i] = tmp;
						i++;
					} else {
						obj.add(c);
						i = 0;
						c = new Object[columnSize];
					}
				}
			}
            obj.add(c);
			rs.close();
			st.close();
			conn.close();
			reasoner.dispose();

		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPredicateDeclarationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return obj;

	}
}
