import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import canvas.Arrow;
import canvas.Canvas;
import canvas.Enregistrement;
import canvas.Grob;
import canvas.TextBox;
import canvas.Listener;


public class Directory extends JComponent{
	
	public static class ListListener extends JList implements Listener {

		Explorer e;
		DefaultListModel data;
		private Grob grob;
		
		
		public ListListener(Explorer exp, DefaultListModel model) {
			super(model);
			this.data = (DefaultListModel) this.getModel();
			e = exp;
			grob =  null;
			this.setFixedCellWidth(-1);
//			this.setLayoutOrientation(JList.HORIZONTAL_WRAP);
			this.setMinimumSize(new Dimension(100,40));
			this.setPreferredSize(new Dimension(200,40));
			this.setMaximumSize(new Dimension(200,40));
		}
		
		public ListListener(Explorer exp) {
			this(exp,new DefaultListModel());
		}

		public void hasMoved(Grob grob) {
			this.grob = grob;
			update();
		}

		void update() {
			data.clear();
			for( Map<Enregistrement, String> it : e.name.values()){
				String s =it.get(this.grob);
				if (s != null){
					data.addElement(s);
				}
			}
			if (data.isEmpty()){
				data.addElement(null);
			}
		}
	}

	private static Directory directory;
	private static Canvas canvas;
	private static Explorer exp;

	Record record; 
	Record.Personne list;
	Record.Personne current; 
	boolean inList;

	JTextField textField[];
	Field field[];

	private  ListListener nameList;


	private void refresh(boolean refresh){
		for(int i = 0; i< textField.length; ++i){
			try {
				//System.out.println(""+field[i].get(current));
				textField[i].setText((String) field[i].get(current));

			} catch (IllegalArgumentException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		}
		if (refresh) refreshCanvas();
	}
	
	private void refreshCanvas(){
		exp.read(list,"liste");
		exp.read(current,"p");
		exp.refresh();
		this.nameList.update();
		repaint();
	}

	private void flush(boolean refresh){
		for(int i = 0; i< textField.length; ++i){
			try {
				field[i].set(current, textField[i].getText());				
			} catch (IllegalArgumentException e) {				
				e.printStackTrace();
			} catch (IllegalAccessException e) {				
				e.printStackTrace();
			}
		}
		if (refresh) refreshCanvas();
	}

	private void findField(){
		Field field[] =Record.Personne.class.getDeclaredFields();

		Vector<Field> stringField = new Vector<Field>();
		for (int i = 0; i < field.length; ++i){			
			if (field[i].getType().equals(String.class)){
				stringField.add(field[i]);
			}
		}
		this.field = stringField.toArray(new Field[stringField.size()]);
	}

	private void init(){
		record = new Record();
		list = null;
		inList = false;
		current = record.new Personne();
	}

	public class FieldEditAction extends AbstractAction{

		int i;

		public FieldEditAction(int i) {
			this.i = i;
		}

		public void actionPerformed(ActionEvent arg0) {
			if (current != null){
				try {
					field[i].set(current, textField[i].getText());
					System.out.println(""+i+":"+textField[i].getText());
				} catch (IllegalArgumentException e) {					
					e.printStackTrace();
				} catch (IllegalAccessException e) {					
					e.printStackTrace();
				}
			}	
		}

	}

	public Directory(Container contentPane) {

		findField();
		init();

		Box panel = Box.createVerticalBox();
		panel.setBorder(BorderFactory.createTitledBorder("Enregistrement"));

		textField = new JTextField[field.length];
		for(int i = 0; i < field.length; ++i){
			Box fields= Box.createHorizontalBox();
			fields.add(new JLabel(field[i].getName()));
			textField[i] = new JTextField(20); 
			//textField[i].addActionListener(new FieldEditAction(i));
			fields.add(textField[i]);
			panel.add(fields);
		}

		contentPane.add(panel);

		panel = Box.createVerticalBox();
		panel.setBorder(BorderFactory.createTitledBorder("Actions"));

		Box buttons = Box.createHorizontalBox();
		buttons.setBorder(BorderFactory.createTitledBorder("Edition"));
		JButton button = new JButton("Nouveau");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						current = record.new Personne();
						inList = false;
						refresh(true);
					}					
				}
		);
		buttons.add(button);
		button = new JButton("Ajouter/Modifier");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						flush(inList);
						if (!inList){
							list = record.ajoute(list, current);
							//current = record.new Personne();
							current = list;
							refresh(true);
							inList = true;
						}
					}					
				}
		);
		buttons.add(button);
		button = new JButton("Supprimer");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						if (inList){
							Record.Personne next = record.suivant(current);
							if (next == null) next = record.precedent(list,current); 
							list = record.supprime(list, current);
							current = next;
							if (current == null) current = list;
							if (current == null) {
								current = record.new Personne();
								inList = false;
							}
							refresh(true);
						}
					}					
				}
		);
		buttons.add(button);
		panel.add(buttons);


		buttons = Box.createHorizontalBox();
		buttons.setBorder(BorderFactory.createTitledBorder("Navigation"));
		button = new JButton("Début");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						current = list;
						inList = true;
						if (current == null){
							current = record.new Personne();
							inList = false;
						}
						refresh(true);
					}					
				}
		);
		buttons.add(button);
		button = new JButton("Précédent");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {
						if (inList){
							Record.Personne next = record.precedent(list,current);
							if (next != null){
								current = next;
								refresh(true);							
							}
						}
					}					
				}
		);
		buttons.add(button);
		button = new JButton("Suivant");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {		
						if (inList) {
							Record.Personne next = record.suivant(current);
							if (next != null){
								current = next;
								refresh(true);							
							}
						}
					}					
				}
		);
		buttons.add(button);
		button = new JButton("Rechercher");
		button.addActionListener(
				new AbstractAction(){
					public void actionPerformed(ActionEvent arg0) {	
						if (inList){
							current = record.new Personne();
							inList = false;
						}
						if (!inList) {
							flush(false);

							Record.Personne next = record.recherche(list,current);
							if (next != null){
								current = next;
								refresh(true);	
								inList = true;
							}
						}
					}					
				}
		);

		buttons.add(button);

		panel.add(buttons);

		contentPane.add(panel);

	}

	public static void start(boolean displayMemory) {
		// Création de la fenêtre
		JFrame frame = new JFrame("Repertoire");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Remplissage de la fenêtre
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new FlowLayout());
		Box hbox = Box.createHorizontalBox(); 
		directory= new Directory(hbox);
		Box vbox = Box.createVerticalBox();
		vbox.add(hbox);
		canvas = new Canvas(800, 400);
		exp = new Explorer(canvas);
	
		JScrollPane scrollPane = new JScrollPane(   canvas);
//		scrollPane.setSize(500, 300);
		contentPane.add(vbox);
		
		hbox = Box.createHorizontalBox();
		JButton button = new JButton("Zoom +");
		button.addActionListener(new AbstractAction(){

			public void actionPerformed(ActionEvent arg0) {
				canvas.multScale(1.2);				
			}
		}
		);
		hbox.add(button);
		button = new JButton("Normal");
		button.addActionListener(new AbstractAction(){

			public void actionPerformed(ActionEvent arg0) {
				canvas.setScale(1);				
			}
		}
		);
		hbox.add(button);

		button = new JButton("Zoom -");
		button.addActionListener(new AbstractAction(){

			public void actionPerformed(ActionEvent arg0) {
				canvas.multScale(0.8);				
			}
		}
		);
		hbox.add(button);
		
		hbox.add(Box.createGlue());
		
		hbox.add(new JLabel("Selection "));
		
		directory.nameList = new ListListener(exp);
		canvas.addSelectionListener(directory.nameList);
		hbox.add(directory.nameList);

		if (displayMemory) {
			vbox.add(hbox);
			vbox.add(scrollPane);
			
			scrollPane.setBorder(BorderFactory.createTitledBorder("Mémoire"));
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
		}



		// Affichage de la fenêtre		
		frame.pack();
		frame.setVisible(true);	




	}

	private  void fillRandomRecord(int n) {
		for(int i =0; i< n ; ++i){
			current = record.new Personne();
			current.nom = makeString();
			
			Record.Personne ajout = record.ajoute(list, current);
			if ( ajout == null || ajout == list || ajout.suivant != current){
				current.suivant = list;
				list = current;
			} else {
				list = ajout;
			}
		}
		inList = true;
		refresh(true);


	}

	public static String makeString() {
		String s = "";
		for(int i = 0; i < 6 ; ++i){
			s =s +(char)(Math.random()*26 +'a');
		}
		return s;
	}

	public static void fillRandom(int i) {
		directory.fillRandomRecord(i);
	}




}
