package org.eclipse.wb.swt;

import com.sun.org.apache.xpath.internal.operations.*;
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
import java.lang.String;
import java.net.MalformedURLException;
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
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Toolkit;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.JTabbedPane;
import java.awt.FlowLayout;
import javax.swing.border.BevelBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import com.ibm.icu.impl.StringUCharacterIterator;
import com.ibm.icu.text.SimpleDateFormat;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;

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
import it.unibz.krdb.obda.owlrefplatform.owlapi3.example.QuestOWLExample;

import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.eclipse.swt.widgets.MessageBox;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.ToStringRenderer;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

public class Main_Frame extends JFrame {

	private JPanel contentPane;
	private JTextField MinInjuriesTextBox;
	private JTextField MinFatalitiesTextBox;
	public JTable EventDetailsTable;
	private JTextField MaxNumInjuriesTexBox;
	private JTextField MaxFatalitiesTextBox;
	private JTextField NarativeTextBox;
	private JTextField MinDamageTextBox;
	public JRadioButton MaleRadioButton;
	public JRadioButton FemaleRadioButton;
	private JTextField MinAgeTextBox;
	private JTextField MaxAgeTextBox;
	private JTextField MinNumFatTextBox;
	private JTextField textField_4;
	private JTextField textField;
	private JTextField textField_1;
	private JTable EventFatalitiesTable;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField EventIDFatalitiesTextBox;
	private JTextField EventIDLocationsTextBox;
	private JTable EventLocationsTable;
	public DefaultTableModel model1,model2, model3;

    public int maxI = 0;
    public int maxF = 0;
    public int minI = Integer.MAX_VALUE;
    public int minF = Integer.MAX_VALUE;
    public double minD = Double.MAX_VALUE;
    public int minF2 = Integer.MAX_VALUE;
    public  int minA = Integer.MAX_VALUE;
    public  int maxF2 = 0;
    public int maxA = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_Frame frame = new Main_Frame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * 
	 * @throws ParseException
	 */
	public Main_Frame() throws ParseException {
		setIconImage(Toolkit.getDefaultToolkit().getImage("tornado2_png.png"));
		setTitle("Storm Data");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);

		JTabbedPane tabbedPaneStormData = new JTabbedPane(JTabbedPane.TOP);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap()
						.addComponent(tabbedPaneStormData, GroupLayout.PREFERRED_SIZE, 1020, Short.MAX_VALUE)
						.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup().addContainerGap().addComponent(tabbedPaneStormData,
						GroupLayout.DEFAULT_SIZE, 499, Short.MAX_VALUE)));

		final JPanel MaxInjuriesTextBox = new JPanel();
		tabbedPaneStormData.addTab("Event Details", null, MaxInjuriesTextBox, null);

		JLabel BeginDateLabel = new JLabel("Event Beginning Date :");

		JLabel MinInjuriesLabel = new JLabel("Minimum Number of Injuries :");

		final JLabel MinFatalitiesLabel = new JLabel("Minimum Number of fatalities :");

		JDateChooser BeginDateChooser = new JDateChooser();
		BeginDateChooser.setDateFormatString("dd/MM/yyyy");
		java.util.Date date = new SimpleDateFormat("dd/MM/yyyy").parse("01/01/2011");
		BeginDateChooser.setDate(date);

		JLabel EndDateLabel = new JLabel("Event Ending Date :");

		JLabel MaxInjuriesLabel = new JLabel("Maximum Number of Injuries :");

		JLabel MaxFatalitiesLabel = new JLabel("Maximum Number of fatalities :");

		MinInjuriesTextBox = new JTextField();
		MinInjuriesTextBox.setColumns(10);
        MinInjuriesTextBox.setEnabled(false);

		MinFatalitiesTextBox = new JTextField();
		MinFatalitiesTextBox.setColumns(10);
        MinFatalitiesTextBox.setEnabled(false);

		JDateChooser EndDateChooser = new JDateChooser();
		EndDateChooser.setDateFormatString("dd/MM/yyyy");
		java.util.Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse("31/12/2011");
		EndDateChooser.setDate(date1);
        BeginDateChooser.setEnabled(false);
        EndDateChooser.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();

		EventDetailsTable = new JTable();
		EventDetailsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(EventDetailsTable);
		EventDetailsTable.setSurrendersFocusOnKeystroke(true);
		EventDetailsTable.setColumnSelectionAllowed(true);
		EventDetailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		EventDetailsTable.setFillsViewportHeight(true);
		EventDetailsTable.setCellSelectionEnabled(true);
		model1 = new DefaultTableModel(new Object[][] {},
				new String[] { "Event ID", "Episode ID", "State", "Event Type", "County", "Event Begin Date Time",
						"Event End Date Time", "Time Zone", "Injuries Direct", "Injuries Indirect", "Death Direct",
						"Death Indirect", "Damage Property (*1000$)", "Damage Crops (*1000$)", "Source",
						"Begin Latitude", "Begin Longitude", "End Latitude", "End Longitude", "Tornado Scale" }) {
			boolean[] columnEditables = new boolean[] { false, true, true, true, true, true, true, true, true, true,
					true, true, true, true, true, true, true, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
		EventDetailsTable.setModel(model1);
		EventDetailsTable.getColumnModel().getColumn(0).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(0).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(0).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(0).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(1).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(1).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(1).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(1).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(2).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(2).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(2).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(3).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(3).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(3).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(3).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(4).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(4).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(4).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(4).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(5).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(5).setPreferredWidth(150);
		EventDetailsTable.getColumnModel().getColumn(5).setMinWidth(150);
		EventDetailsTable.getColumnModel().getColumn(5).setMaxWidth(150);
		EventDetailsTable.getColumnModel().getColumn(6).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(6).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(6).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(6).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(7).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(7).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(7).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(7).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(8).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(8).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(8).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(8).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(9).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(9).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(9).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(9).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(10).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(10).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(10).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(10).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(11).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(11).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(11).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(11).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(12).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(12).setPreferredWidth(170);
		EventDetailsTable.getColumnModel().getColumn(12).setMinWidth(170);
		EventDetailsTable.getColumnModel().getColumn(12).setMaxWidth(170);
		EventDetailsTable.getColumnModel().getColumn(13).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(13).setPreferredWidth(150);
		EventDetailsTable.getColumnModel().getColumn(13).setMinWidth(150);
		EventDetailsTable.getColumnModel().getColumn(13).setMaxWidth(150);
		EventDetailsTable.getColumnModel().getColumn(14).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(14).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(14).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(14).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(15).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(15).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(15).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(15).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(16).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(16).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(16).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(16).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(17).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(17).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(17).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(17).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(18).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(18).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(18).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(18).setMaxWidth(130);
		EventDetailsTable.getColumnModel().getColumn(19).setResizable(false);
		EventDetailsTable.getColumnModel().getColumn(19).setPreferredWidth(130);
		EventDetailsTable.getColumnModel().getColumn(19).setMinWidth(130);
		EventDetailsTable.getColumnModel().getColumn(19).setMaxWidth(130);

		MaxNumInjuriesTexBox = new JTextField();
		MaxNumInjuriesTexBox.setColumns(10);
        MaxNumInjuriesTexBox.setEnabled(false);

		MaxFatalitiesTextBox = new JTextField();
		MaxFatalitiesTextBox.setColumns(10);
        MaxFatalitiesTextBox.setEnabled(false);

		JButton NarativeButton = new JButton("What happend in this day");
		NarativeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// add here the event id to use it to select the right
				// narratives and display them in the new frame

				try {
                    System.out.println(NarativeTextBox.getText());
					int eventid = Integer.parseInt(NarativeTextBox.getText());
					Narative newframe = new Narative();
					ArrayList<Object[]> L= Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/"
							+ "project_ca#>SELECT DISTINCT ?x ?y ?r ?s WHERE {?x a :Event; :Even_ID ?y;  "
							+ ":Episode_Narrative ?r; :Event_Narrative ?s FILTER regex(?y, \""+eventid+"\", \"i\")}");
                    System.out.println(L.size());
					if(L.size() == 2)
					{
					//newframe.EventNarativeText.setText(L.get(0)[2].toString());
					/*	for(int i=1;i<L.size();i++)
							for(int j=0;j<L.get(i).length;j++) {
                                System.out.println(L.get(1)[3].toString());*/
                    newframe.EpisodeNarativeText.setText(L.get(1)[1].toString());
                    newframe.EventNarativeText.setText(L.get(1)[2].toString());

                          //  }
					newframe.setVisible(true);
					}
					else
						JOptionPane.showMessageDialog(null, "No Such ID", "Nothing to Show",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception c) {
					if (NarativeTextBox.getText() == null || NarativeTextBox.getText().equals("")) {
						JOptionPane.showMessageDialog(null, "Please specify an Event ID", "Error",
								JOptionPane.ERROR_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(null, "Please make sure that Event ID is an Integer",
								"Wrong Entry", JOptionPane.ERROR_MESSAGE);
					}
				}

			}
		});

		JLabel label = new JLabel(
				"______________________________________________________________________________________________________________________________________________");

		NarativeTextBox = new JTextField();
		NarativeTextBox.setColumns(10);
		// fill event id on tab change
		ChangeListener changeListener = new ChangeListener() {
			public void stateChanged(ChangeEvent changeEvent) {
				JTabbedPane sourceTabbedPane = (JTabbedPane) changeEvent.getSource();
				int index = sourceTabbedPane.getSelectedIndex();
				EventIDLocationsTextBox.setText(NarativeTextBox.getText());
				EventIDFatalitiesTextBox.setText(NarativeTextBox.getText());
			}
		};
		tabbedPaneStormData.addChangeListener(changeListener);
		JLabel lblEventState = new JLabel("");

		JLabel lblEventCounty = new JLabel("");
        lblEventCounty.setVisible(false);

		JLabel lblMinimumDamage = new JLabel("Minimum Damage :");

		MinDamageTextBox = new JTextField();
		MinDamageTextBox.setColumns(10);
        MinDamageTextBox.setEnabled(false);

        String[] states = new String[] { "", "Alabama", "Alaska", "Arizona",
                "Arkansas", "California", "Colorado", "Connecticut", "Delaware", "District of Columbia", "Florida",
                "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky", "Louisiana", "Maine",
                "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri", "Montana", "Nebraska",
                "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina", "North Dakota",
                "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina", "South Dakota",
                "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia", "Wisconsin",
                "Wyoming" };

		JComboBox StateDropDownList = new JComboBox();
		StateDropDownList.setModel(new DefaultComboBoxModel(states));
        StateDropDownList.setVisible(false);

		JComboBox CountyDropDownList = new JComboBox();
        CountyDropDownList.setVisible(false);
		CountyDropDownList.setModel(new DefaultComboBoxModel(new String[] { "", " "}));


		JButton EShowResultsButton = new JButton("Show Results");
		EShowResultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<Object[]> L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?y ?z ?a ?b ?c ?d ?e ?f ?g ?h ?i ?j ?k ?l ?m ?n ?o ?t ?p ?r ?s "
						+ "WHERE {?x a :Event; :Even_ID ?y; :Episode_ID ?z; :State_FIPS ?a; :Event_type ?b; :County_FIPS ?c;"
						+ " :Begin_Date_Time ?d; :End_Date_Time ?e; :Time_Zone ?f; :Injuries_Direct ?g; :Injuries_Indirect ?h;"
						+ " :Deaths_direct ?i; :Deaths_Indirect ?j; :Damage_Property ?k; :Damage_Crops ?l; :Source_ID ?m;"
						+ " :Begin_Lat ?n; :Begin_Lon ?o; :End_Lat ?t; :End_Lon ?p; :Episode_Narrative ?r;"
						+ " :Event_Narrative ?s}");
				for(int i=1;i<L.size();i++) {
                    model1.addRow(L.get(i));
                    int inj = Integer.parseInt(L.get(i)[9].toString());
                    int fat = Integer.parseInt(L.get(i)[11].toString());
                    double dam = 0;
                    if (L.get(i)[13].toString() != "?")
                        dam = Double.parseDouble(L.get(i)[13].toString().substring(0, 4));
                    if (inj > maxI)
                        maxI = inj;
                    if (inj < minI)
                        minI = inj;
                    if (fat > maxF)
                        maxF = fat;
                    if (fat < minF)
                        minF = fat;
                    if (dam < minD)
                        minD = dam;
                }
                MinInjuriesTextBox.setText(String.valueOf(minI));
                MaxNumInjuriesTexBox.setText(String.valueOf(maxI));
                MinFatalitiesTextBox.setText(String.valueOf(minF));
                MaxFatalitiesTextBox.setText(String.valueOf(maxF));
                MinDamageTextBox.setText(String.valueOf(minD));
                //MaxInjuriesTextBox.setText(String.valueOf(minI));
			}
		});



		GroupLayout gl_MaxInjuriesTextBox = new GroupLayout(MaxInjuriesTextBox);
		gl_MaxInjuriesTextBox.setHorizontalGroup(
				gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING).addGroup(gl_MaxInjuriesTextBox
						.createSequentialGroup().addContainerGap().addGroup(gl_MaxInjuriesTextBox
								.createParallelGroup(Alignment.LEADING).addGroup(gl_MaxInjuriesTextBox
										.createSequentialGroup()
										.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING)
												.addComponent(BeginDateLabel, GroupLayout.DEFAULT_SIZE, 181,
														Short.MAX_VALUE)
												.addComponent(MinInjuriesLabel, GroupLayout.DEFAULT_SIZE,
														181, Short.MAX_VALUE)
												.addComponent(NarativeButton).addComponent(MinFatalitiesLabel,
														GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_MaxInjuriesTextBox
														.createParallelGroup(Alignment.LEADING, false)
														.addComponent(BeginDateChooser, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
														.addComponent(MinInjuriesTextBox)
														.addComponent(MinFatalitiesTextBox, GroupLayout.PREFERRED_SIZE,
																133, GroupLayout.PREFERRED_SIZE))
												.addComponent(NarativeTextBox, GroupLayout.PREFERRED_SIZE, 130,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING)
												.addGroup(gl_MaxInjuriesTextBox.createSequentialGroup().addGap(32)
														.addGroup(gl_MaxInjuriesTextBox
																.createParallelGroup(Alignment.LEADING)
																.addComponent(MaxFatalitiesLabel,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(MaxInjuriesLabel)
																.addComponent(EndDateLabel, GroupLayout.PREFERRED_SIZE,
																		110, GroupLayout.PREFERRED_SIZE))
														.addPreferredGap(ComponentPlacement.RELATED)
														.addGroup(gl_MaxInjuriesTextBox
																.createParallelGroup(Alignment.LEADING)
																.addComponent(MaxFatalitiesTextBox, 134, 134, 134)
																.addComponent(MaxNumInjuriesTexBox, 134, 134, 134)
																.addComponent(EndDateChooser,
																		GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE))
														.addGap(14)
														.addGroup(
																gl_MaxInjuriesTextBox
																		.createParallelGroup(Alignment.LEADING)
																		.addGroup(gl_MaxInjuriesTextBox
																				.createSequentialGroup().addGap(14)
																				.addGroup(gl_MaxInjuriesTextBox
																						.createParallelGroup(
																								Alignment.LEADING)
																						.addComponent(lblMinimumDamage)
																						.addComponent(lblEventCounty)))
																		.addGroup(gl_MaxInjuriesTextBox
																				.createSequentialGroup().addGap(18)
																				.addComponent(lblEventState)))
														.addGap(18)
														.addGroup(gl_MaxInjuriesTextBox
																.createParallelGroup(Alignment.LEADING, false)
																.addComponent(StateDropDownList, 0,
																		GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																.addComponent(CountyDropDownList, 0, 0, Short.MAX_VALUE)
																.addComponent(MinDamageTextBox,
																		GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE))
														.addGap(84))
												.addGroup(gl_MaxInjuriesTextBox.createSequentialGroup().addGap(30)
														.addComponent(EShowResultsButton, GroupLayout.PREFERRED_SIZE,
																324, GroupLayout.PREFERRED_SIZE))))
								.addComponent(label, GroupLayout.PREFERRED_SIZE, 995, GroupLayout.PREFERRED_SIZE)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 995, GroupLayout.PREFERRED_SIZE))
						.addContainerGap()));
		gl_MaxInjuriesTextBox
				.setVerticalGroup(
						gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING).addGroup(gl_MaxInjuriesTextBox
								.createSequentialGroup().addContainerGap().addGroup(gl_MaxInjuriesTextBox
										.createParallelGroup(
												Alignment.LEADING)
										.addGroup(gl_MaxInjuriesTextBox.createSequentialGroup()
												.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING)
														.addComponent(EndDateChooser, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addGroup(gl_MaxInjuriesTextBox
																.createParallelGroup(Alignment.BASELINE)
																.addComponent(lblEventState).addComponent(
																		StateDropDownList, GroupLayout.PREFERRED_SIZE,
																		GroupLayout.DEFAULT_SIZE,
																		GroupLayout.PREFERRED_SIZE)))
												.addGap(18)
												.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.BASELINE)
														.addComponent(MinInjuriesLabel)
														.addComponent(MinInjuriesTextBox, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(MaxInjuriesLabel)
														.addComponent(MaxNumInjuriesTexBox, GroupLayout.PREFERRED_SIZE,
																GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(lblEventCounty).addComponent(CountyDropDownList,
																GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE)))
										.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.TRAILING)
												.addComponent(BeginDateLabel)
												.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.LEADING)
														.addComponent(EndDateLabel).addComponent(BeginDateChooser,
																GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
																GroupLayout.PREFERRED_SIZE))))
								.addGap(18)
								.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.BASELINE)
										.addComponent(MinFatalitiesLabel)
										.addComponent(MinFatalitiesTextBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(MaxFatalitiesLabel)
										.addComponent(MaxFatalitiesTextBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addComponent(lblMinimumDamage).addComponent(MinDamageTextBox,
												GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addGap(15).addComponent(label).addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(gl_MaxInjuriesTextBox.createParallelGroup(Alignment.BASELINE)
                                        .addComponent(NarativeButton).addComponent(NarativeTextBox,
                                                GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE)
                                        .addComponent(EShowResultsButton))
								.addGap(18)
								.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 270, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		MaxInjuriesTextBox.setLayout(gl_MaxInjuriesTextBox);
		tabbedPaneStormData.setEnabledAt(0, true);

		JPanel EventFatalitiesPanel = new JPanel();
		tabbedPaneStormData.addTab("Event Fatalities", null, EventFatalitiesPanel, null);

		JLabel lblNewLabel_2 = new JLabel("");

		JLabel lblNewLabel_3 = new JLabel("Minimum Age :");

		MaleRadioButton = new JRadioButton("");
		MaleRadioButton.setMnemonic('M');
		FemaleRadioButton = new JRadioButton("");
		FemaleRadioButton.setMnemonic('F');
        MaleRadioButton.setVisible(false);
        FemaleRadioButton.setVisible(false);
		ButtonGroup group = new ButtonGroup();
		group.add(MaleRadioButton);
		group.add(FemaleRadioButton);
		JButton FShowResultsButton = new JButton("Show Results");
		FShowResultsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String X = null;
				if (MaleRadioButton.isSelected()) {
					X = MaleRadioButton.getText();
				} else if (MaleRadioButton.isSelected()) {
					X = FemaleRadioButton.getText();
				}
                int eventid = Integer.parseInt(EventIDFatalitiesTextBox.getText());
                    List<Object[]> L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?y ?e ?d ?b ?c "
                            + "WHERE {?x a :Fatality; :Even_ID ?y;"
                            + ":Fatality_Age ?b; :Fatality_Sex ?c; :Fatality_Location ?d; :Fatatlity_Type ?e FILTER regex(?y, \""+eventid+"\")}");
                System.out.println(L.size());
                    for(int i=1;i<L.size();i++) {
                        model2.addRow(L.get(i));
                        int ag = Integer.parseInt(L.get(i)[3].toString());
                        if (ag > maxA)
                            maxA = ag;
                        if (ag < minA)
                            minA = ag;
                    }
                MinAgeTextBox.setText(String.valueOf(minA));
                MaxAgeTextBox.setText(String.valueOf(maxA));
                MinNumFatTextBox.setText(String.valueOf(L.size()));
                    //MaxInjuriesTextBox.setText(String.valueOf(minI));*/
                }
		});

		JLabel lblFatalityLocation = new JLabel("");

		JComboBox FatalityLocationComboBox = new JComboBox();
		FatalityLocationComboBox.setModel(new DefaultComboBoxModel(new String[] { "", "Ball Field", "Boating",
				"Business", "Camping", "Church", "Golfing", "Heavy Equipment/Construction", "In Water",
				"Long Span Roof", "Mobile/Trailer Home", "Other", "Outside/Open Areas", "Permanent Home",
				"Permanent Structure", "Under Tree", "Unknown", "Vehicle/Towed Trailer" }));
        FatalityLocationComboBox.setVisible(false);
		MinAgeTextBox = new JTextField();
		MinAgeTextBox.setColumns(10);
        MinAgeTextBox.setEnabled(false);
		JLabel lblMaximumAge = new JLabel("Maximum Age :");

		MaxAgeTextBox = new JTextField();
        MaxAgeTextBox.setEnabled(false);
		MaxAgeTextBox.setText("");
		MaxAgeTextBox.setColumns(10);

		JLabel lblMinNumOf = new JLabel("Num of Fatalities :");

		JLabel MaxNumFatTextBox = new JLabel("");

		MinNumFatTextBox = new JTextField();
		MinNumFatTextBox.setColumns(10);
        MinNumFatTextBox.setEnabled(false);

		textField_4 = new JTextField();
		textField_4.setColumns(10);
        textField_4.setEnabled(false);
        textField_4.setVisible(false);
		JLabel label_1 = new JLabel(
				"______________________________________________________________________________________________________________________________________________");

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		EventFatalitiesTable = new JTable();
		EventFatalitiesTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		EventFatalitiesTable.setSurrendersFocusOnKeystroke(true);
		EventFatalitiesTable.setFillsViewportHeight(true);
		EventFatalitiesTable.setColumnSelectionAllowed(true);
		EventFatalitiesTable.setCellSelectionEnabled(true);
		model2 = new DefaultTableModel(new Object[][] {},
				new String[] { "Event ID", "Fatality Type", "Fatality Location", "Age", "Sex" }) {
			boolean[] columnEditables = new boolean[] { false, true, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
        EventFatalitiesTable.setModel(model2);

        EventFatalitiesTable.getColumnModel().getColumn(0).setPreferredWidth(200);
		EventFatalitiesTable.getColumnModel().getColumn(0).setMinWidth(200);
		EventFatalitiesTable.getColumnModel().getColumn(1).setPreferredWidth(200);
		EventFatalitiesTable.getColumnModel().getColumn(1).setMinWidth(200);
		EventFatalitiesTable.getColumnModel().getColumn(2).setPreferredWidth(198);
		EventFatalitiesTable.getColumnModel().getColumn(2).setMinWidth(185);
		EventFatalitiesTable.getColumnModel().getColumn(3).setPreferredWidth(198);
		EventFatalitiesTable.getColumnModel().getColumn(3).setMinWidth(185);
		EventFatalitiesTable.getColumnModel().getColumn(4).setPreferredWidth(198);
		EventFatalitiesTable.getColumnModel().getColumn(4).setMinWidth(185);

		scrollPane_1.setViewportView(EventFatalitiesTable);

		JLabel lblSelectedEventId = new JLabel("Selected Event ID :");

		EventIDFatalitiesTextBox = new JTextField();

		EventIDFatalitiesTextBox.setEditable(false);
		EventIDFatalitiesTextBox.setColumns(10);
		GroupLayout gl_EventFatalitiesPanel = new GroupLayout(EventFatalitiesPanel);
		gl_EventFatalitiesPanel.setHorizontalGroup(
				gl_EventFatalitiesPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_EventFatalitiesPanel
						.createSequentialGroup().addGroup(gl_EventFatalitiesPanel
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_EventFatalitiesPanel.createSequentialGroup().addContainerGap()
										.addGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(lblNewLabel_3).addComponent(lblMinNumOf,
														GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
												.addComponent(lblNewLabel_2).addComponent(lblFatalityLocation))
										.addPreferredGap(ComponentPlacement.RELATED)
										.addGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.LEADING)
												.addComponent(FatalityLocationComboBox, GroupLayout.PREFERRED_SIZE, 166,
														GroupLayout.PREFERRED_SIZE)
												.addGroup(gl_EventFatalitiesPanel.createSequentialGroup()
														.addGroup(gl_EventFatalitiesPanel
																.createParallelGroup(Alignment.LEADING, false)
																.addGroup(gl_EventFatalitiesPanel
																		.createSequentialGroup()
																		.addComponent(MaleRadioButton)
																		.addPreferredGap(ComponentPlacement.RELATED,
																				GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addComponent(FemaleRadioButton))
																.addComponent(MinNumFatTextBox)
																.addComponent(MinAgeTextBox, GroupLayout.DEFAULT_SIZE,
																		161, Short.MAX_VALUE))
														.addGap(92)
														.addGroup(gl_EventFatalitiesPanel
																.createParallelGroup(Alignment.LEADING)
																.addComponent(lblMaximumAge, GroupLayout.PREFERRED_SIZE,
																		93, GroupLayout.PREFERRED_SIZE)
																.addComponent(MaxNumFatTextBox,
																		GroupLayout.PREFERRED_SIZE, 144, GroupLayout.PREFERRED_SIZE)
																.addComponent(lblSelectedEventId))
														.addGap(18)
														.addGroup(gl_EventFatalitiesPanel
																.createParallelGroup(Alignment.LEADING, false)
																.addComponent(EventIDFatalitiesTextBox)
																.addComponent(MaxAgeTextBox).addComponent(textField_4,
																		GroupLayout.DEFAULT_SIZE, 157,
																		Short.MAX_VALUE)))))
								.addGroup(
										gl_EventFatalitiesPanel.createSequentialGroup().addContainerGap().addComponent(
												label_1, GroupLayout.PREFERRED_SIZE, 995, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_EventFatalitiesPanel.createSequentialGroup().addGap(377).addComponent(
										FShowResultsButton, GroupLayout.PREFERRED_SIZE, 254,
										GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_EventFatalitiesPanel.createSequentialGroup().addContainerGap()
										.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)))
						.addContainerGap()));
		gl_EventFatalitiesPanel.setVerticalGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_EventFatalitiesPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNewLabel_3)
								.addComponent(MinAgeTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMaximumAge).addComponent(MaxAgeTextBox, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_EventFatalitiesPanel
								.createParallelGroup(Alignment.BASELINE).addComponent(lblMinNumOf)
								.addComponent(MinNumFatTextBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(MaxNumFatTextBox).addComponent(textField_4, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(MaleRadioButton).addComponent(lblNewLabel_2)
								.addComponent(FemaleRadioButton).addComponent(lblSelectedEventId)
								.addComponent(EventIDFatalitiesTextBox, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_EventFatalitiesPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblFatalityLocation).addComponent(FatalityLocationComboBox,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(label_1)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(FShowResultsButton)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE).addContainerGap()));
		EventFatalitiesPanel.setLayout(gl_EventFatalitiesPanel);
		tabbedPaneStormData.setEnabledAt(1, true);




/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////





		JPanel EvwntLocationsPanel = new JPanel();
		tabbedPaneStormData.addTab("Event Locations", null, EvwntLocationsPanel, null);

		JLabel lblNewLabel_5 = new JLabel("Event Longitude :");

		JLabel lblNewLabel_4 = new JLabel("Event Latitude :");

		textField = new JTextField();
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setColumns(10);

		JLabel lblAzimuth = new JLabel("Range :");

		JLabel lblRange = new JLabel("Azimuth :");

		textField_2 = new JTextField();
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setColumns(10);

		JLabel lblSelectedEventId_1 = new JLabel("");

		EventIDLocationsTextBox = new JTextField();

		EventIDLocationsTextBox.setEditable(false);
		EventIDLocationsTextBox.setColumns(10);
        EventIDLocationsTextBox.setVisible(false);
		JLabel label_2 = new JLabel(
				"______________________________________________________________________________________________________________________________________________");

		JButton LShowResultsButton = new JButton("Show Results");

        LShowResultsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                List<Object[]> L;
                String X1 = textField.getText();
                String X2 = textField_1.getText();
                String X3 = textField_2.getText();
                String X4 = textField_3.getText();
                System.out.println(X1.length());
                if (X1.length() != 0) {
                    L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?b ?y ?z ?a "
                            + "WHERE {?x a :Location; :Azimuth ?y;  :Longitude ?z; :Latitude ?a; :Range ?b; FILTER regex(?z,\""+X1+"\")}");
                }
                else {
                    if (X2.length() != 0) {
                        L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?b ?y ?z ?a "
                                + "WHERE {?x a :Location; :Azimuth ?y;  :Longitude ?z; :Latitude ?a; :Range ?b; FILTER regex(?a,\""+X2+"\")}");
                    }
                    else {
                        if (X3.length() != 0) {
                            L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?b ?y ?z ?a "
                                    + "WHERE {?x a :Location; :Azimuth ?y;  :Longitude ?z; :Latitude ?a; :Range ?b; FILTER regex(?y,\""+X3+"\")}");
                        }
                        else {
                            if (X4.length() != 0) {
                                L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?b ?y ?z ?a "
                                        + "WHERE {?x a :Location; :Azimuth ?y;  :Longitude ?z; :Latitude ?a; :Range ?b; FILTER regex(?b,\""+X4+"\")}");
                            }
                            else {
                                L = Queries.run("PREFIX : <file:/C:/Users/Asus/Documents/www.example.org/www.example.org/project_ca#>SELECT DISTINCT ?x ?b ?y ?z ?a "
                                        + "WHERE {?x a :Location; :Azimuth ?y;  :Longitude ?z; :Latitude ?a; :Range ?b; }");
                                System.out.println(L.size());
                            }
                        }
                    }
                }
                //for(int i=1;i<L.size();i++) {
                for(int i=1;i<L.size();i++) {
                    model3.addRow(L.get(i));
                /*    int ag = Integer.parseInt(L.get(i)[3].toString());
                    if (ag > maxA)
                        maxA = ag;
                    if (ag < minA)
                        minA = ag;
                */}
                //MaxInjuriesTextBox.setText(String.valueOf(minI));*/
            }
        });

		JScrollPane scrollPane_2 = new JScrollPane();
		GroupLayout gl_EvwntLocationsPanel = new GroupLayout(EvwntLocationsPanel);
		gl_EvwntLocationsPanel.setHorizontalGroup(
                gl_EvwntLocationsPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_EvwntLocationsPanel
                        .createSequentialGroup().addGroup(gl_EvwntLocationsPanel
                                .createParallelGroup(
                                        Alignment.LEADING)
                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup().addContainerGap()
                                        .addGroup(gl_EvwntLocationsPanel.createParallelGroup(Alignment.LEADING)
                                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup()
                                                        .addGroup(gl_EvwntLocationsPanel
                                                                .createParallelGroup(Alignment.LEADING, false)
                                                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup()
                                                                        .addGroup(gl_EvwntLocationsPanel
                                                                                .createParallelGroup(Alignment.TRAILING,
                                                                                        false)
                                                                                .addComponent(lblNewLabel_5,
                                                                                        Alignment.LEADING,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        GroupLayout.DEFAULT_SIZE,
                                                                                        Short.MAX_VALUE)
                                                                                .addComponent(lblNewLabel_4,
                                                                                        Alignment.LEADING,
                                                                                        GroupLayout.DEFAULT_SIZE, 106,
                                                                                        Short.MAX_VALUE))
                                                                        .addGap(18)
                                                                        .addGroup(gl_EvwntLocationsPanel
                                                                                .createParallelGroup(Alignment.TRAILING,
                                                                                        false)
                                                                                .addComponent(textField_1,
                                                                                        Alignment.LEADING)
                                                                                .addComponent(textField,
                                                                                        Alignment.LEADING,
                                                                                        GroupLayout.DEFAULT_SIZE, 136,
                                                                                        Short.MAX_VALUE)))
                                                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup()
                                                                        .addComponent(lblSelectedEventId_1,
                                                                                GroupLayout.PREFERRED_SIZE, 106,
                                                                                GroupLayout.PREFERRED_SIZE)
                                                                        .addGap(18)
                                                                        .addComponent(EventIDLocationsTextBox)))
                                                        .addGap(88)
                                                        .addGroup(gl_EvwntLocationsPanel
                                                                .createParallelGroup(Alignment.LEADING, false)
                                                                .addComponent(lblRange, GroupLayout.DEFAULT_SIZE,
                                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(lblAzimuth, GroupLayout.DEFAULT_SIZE, 79,
                                                                        Short.MAX_VALUE))
                                                        .addPreferredGap(ComponentPlacement.RELATED)
                                                        .addGroup(gl_EvwntLocationsPanel
                                                                .createParallelGroup(Alignment.LEADING, false)
                                                                .addComponent(textField_3).addComponent(textField_2,
                                                                        GroupLayout.DEFAULT_SIZE, 138,
                                                                        Short.MAX_VALUE)))
                                                .addComponent(label_2, GroupLayout.PREFERRED_SIZE, 995,
                                                        GroupLayout.PREFERRED_SIZE)))
                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup().addGap(275).addComponent(
                                        LShowResultsButton, GroupLayout.PREFERRED_SIZE, 247,
                                        GroupLayout.PREFERRED_SIZE))
                                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup().addContainerGap()
                                        .addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)))
                        .addContainerGap()));
		gl_EvwntLocationsPanel.setVerticalGroup(gl_EvwntLocationsPanel.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_EvwntLocationsPanel.createSequentialGroup().addContainerGap()
                        .addGroup(gl_EvwntLocationsPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lblNewLabel_4)
                                .addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblAzimuth).addComponent(textField_3, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(gl_EvwntLocationsPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lblNewLabel_5)
                                .addComponent(textField_1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblRange).addComponent(textField_2, GroupLayout.PREFERRED_SIZE,
                                        GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(18)
                        .addGroup(gl_EvwntLocationsPanel.createParallelGroup(Alignment.BASELINE)
                                .addComponent(lblSelectedEventId_1).addComponent(EventIDLocationsTextBox,
                                        GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                        GroupLayout.PREFERRED_SIZE))
                        .addGap(18).addComponent(label_2).addGap(18).addComponent(LShowResultsButton)
                        .addPreferredGap(ComponentPlacement.RELATED)
                        .addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE).addContainerGap()));

		EventLocationsTable = new JTable();
		EventLocationsTable.setSurrendersFocusOnKeystroke(true);
		EventLocationsTable.setFillsViewportHeight(true);
		EventLocationsTable.setColumnSelectionAllowed(true);
		EventLocationsTable.setCellSelectionEnabled(true);
        model3 = new DefaultTableModel(new Object[][] {},
				new String[] {"Range", "Azimuth", "Latitude", "Longitude" }) {
			boolean[] columnEditables = new boolean[] {true, true, true, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		};
        EventLocationsTable.setModel(model3);
		scrollPane_2.setViewportView(EventLocationsTable);
		EvwntLocationsPanel.setLayout(gl_EvwntLocationsPanel);
		tabbedPaneStormData.setEnabledAt(2, true);

		JPanel MainResultsPanel = new JPanel();
		tabbedPaneStormData.addTab("2011 Main Results", null, MainResultsPanel, null);

		JLabel lblNewLabel = new JLabel(
				"- Across the United States, for the year 2011, the type of event that was the most harmful with respect to the population health was Tornado, 5646 injured and 763 dead.");

		JLabel lblTheTypeOf = new JLabel(
				"- The types of events that caused the greatest economical consequences were Hail and Thunderstorm Wind.");

		JLabel lblTheTotalNumber = new JLabel("- The total number of fatalities among men was 384.");

		JLabel lblTheTotalNumber_1 = new JLabel("- The total number of fatalities among women was 379.");

		JLabel lblTheTotalValue = new JLabel(
				"- The total value of the properties damage was estimated at 634,182,120 $.");

		JLabel lblTheTotalValue_1 = new JLabel("- The total value of the crops damage was estimated at 91,392,890 $.");

		JLabel lblNewLabel_1 = new JLabel("- The state that was most hit by different types of events was Kansas.");
		GroupLayout gl_MainResultsPanel = new GroupLayout(MainResultsPanel);
		gl_MainResultsPanel.setHorizontalGroup(gl_MainResultsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MainResultsPanel.createSequentialGroup().addContainerGap()
						.addGroup(gl_MainResultsPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
								.addComponent(lblTheTypeOf, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
								.addComponent(lblTheTotalNumber, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
								.addComponent(lblTheTotalNumber_1, GroupLayout.DEFAULT_SIZE, 995, Short.MAX_VALUE)
								.addComponent(lblTheTotalValue, GroupLayout.PREFERRED_SIZE, 995,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTheTotalValue_1, GroupLayout.PREFERRED_SIZE, 995,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(lblNewLabel_1))
						.addContainerGap()));
		gl_MainResultsPanel.setVerticalGroup(gl_MainResultsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_MainResultsPanel.createSequentialGroup().addContainerGap().addComponent(lblNewLabel)
						.addGap(18).addComponent(lblTheTypeOf).addGap(18).addComponent(lblTheTotalNumber).addGap(18)
						.addComponent(lblTheTotalNumber_1).addGap(18).addComponent(lblTheTotalValue).addGap(18)
						.addComponent(lblTheTotalValue_1).addGap(18).addComponent(lblNewLabel_1)
						.addContainerGap(244, Short.MAX_VALUE)));
		MainResultsPanel.setLayout(gl_MainResultsPanel);
		panel.setLayout(gl_panel);
		setSize(1066, 569);
	}
}
