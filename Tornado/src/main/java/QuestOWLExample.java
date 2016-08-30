
/*
 * #%L
 * ontop-quest-owlapi3
 * %%
 * Copyright (C) 2009 - 2014 Free University of Bozen-Bolzano
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import it.unibz.krdb.obda.io.ModelIOManager;
import it.unibz.krdb.obda.model.OBDADataFactory;
import it.unibz.krdb.obda.model.OBDAModel;
import it.unibz.krdb.obda.model.impl.OBDADataFactoryImpl;
import it.unibz.krdb.obda.owlrefplatform.core.QuestConstants;
import it.unibz.krdb.obda.owlrefplatform.core.QuestPreferences;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.MappingLoader;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWL;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConfiguration;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLConnection;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLFactory;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLResultSet;
import it.unibz.krdb.obda.owlrefplatform.owlapi3.QuestOWLStatement;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.wb.swt.Main_Frame;
import org.openrdf.query.Binding;
import org.openrdf.query.Query;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLObjectRenderer;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.SimpleConfiguration;

public class QuestOWLExample {

	/*
	 * Use the sample database using H2 from
	 * https://github.com/ontop/ontop/wiki/InstallingTutorialDatabases
	 *
	 * Please use the pre-bundled H2 server from the above link
	 *
	 */
	final String owlfile = "D:\\Uni\\Bolzano\\2nd Semester\\KRO\\Workspace\\StormData\\src\\resources\\example\\ca.owl";
	final String obdafile = "D:\\Uni\\Bolzano\\2nd Semester\\KRO\\Workspace\\StormData\\src\\resources\\example\\ca.obda";

	public  void runQuery() throws Exception {

		/*
         * Load the ontology from an external .owl file.
		 */
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new File(owlfile));

		/*
		 * Load the OBDA model from an external .obda file
		 */
        final OBDAModel obdaModel = new MappingLoader().loadFromOBDAFile(obdafile);

		/*
		 * Prepare the configuration for the Quest instance. The example below shows the setup for
		 * "Virtual ABox" mode
		 */
        QuestPreferences preference = new QuestPreferences();
        preference.setCurrentValueOf(QuestPreferences.ABOX_MODE, QuestConstants.VIRTUAL);

		/*
		 * Create the instance of Quest OWL reasoner.
		 */
        QuestOWLConfiguration config = QuestOWLConfiguration.builder()
                .preferences(preference).obdaModel(obdaModel).build();

        QuestOWLFactory factory = new QuestOWLFactory();

        QuestOWL reasoner = factory.createReasoner(ontology, config);

		/*
		 * Prepare the data connection for querying.
		 */
        QuestOWLConnection conn = reasoner.getConnection();
        QuestOWLStatement st = conn.createStatement();

		/*
		 * Get the book information that is stored in the database
		 */
        String var = "266452";
        String sparqlQuery =
        		"PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/"
						+ "project_ca#>SELECT DISTINCT ?x ?y ?r ?s WHERE {?x a :Event; :Even_ID ?y;  "
						+ ":Episode_Narrative ?r; :Event_Narrative ?s FILTER regex(?y, \"" + var +"\", \"i\")}";
        try {
        	List<String> elephantList = null;
        	Object[] c;
        	String tmp = "";
        	boolean first =true;
        	int i =0;
            Main_Frame mf = new Main_Frame();
            QuestOWLResultSet rs = st.executeTuple(sparqlQuery);
            int columnSize = rs.getColumnCount();
            System.out.println(columnSize);
            c = new Object[columnSize]; 
            final ToStringRenderer renderer = ToStringRenderer.getInstance();
            while (rs.nextRow()) {
                for (int idx = 1; idx <= columnSize; idx++) {
                    OWLObject binding = rs.getOWLObject(idx);
                    if('\"'==renderer.getRendering(binding).charAt(0))
					{
						elephantList = Arrays.asList(renderer.getRendering(binding).split("\""));
						if (elephantList.size() > 0) {
							tmp = elephantList.get(1);
						} 
						else
							tmp = "?";
						c[i] = tmp;
						i++;
					} 
                    else
                    {
                    	if(!first)
                       	
                    		mf.model1.addRow(c);
                    	first = false;
                    	i=0;
                    	c = new Object[columnSize];
                    	
                    }
                    	
                    System.out.println(renderer.getRendering(binding) + ", ");
                }
                System.out.print("\n");
                mf.setVisible(true);
            }
            rs.close();
            

			/*
			 * Print the query summary
			 */
            String sqlQuery = st.getUnfolding(sparqlQuery);

            System.out.println();
            System.out.println("The input SPARQL query:");
            System.out.println("=======================");
            System.out.println(sparqlQuery);
            System.out.println();

            System.out.println("The output SQL query:");
            System.out.println("=====================");
            System.out.println(sqlQuery);

        } finally {
			/*
			 * Close connection and resources
			 */
            st.close();

            conn.close();

            reasoner.dispose();
        }
    }

	/**
	 * Main client program
	 */
	public static void main(String[] args) {
		try {
			QuestOWLExample example = new QuestOWLExample();
			example.runQuery();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
