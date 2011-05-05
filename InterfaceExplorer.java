import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;

import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.*;
import org.rsbot.script.wrappers.*;


@ScriptManifest(authors = {"Iscream", "joku.rules"}, keywords = "Development", name = "Interface Explorer", version = 0.5, description = "Fetches various interface data for developers.")
public class InterfaceExplorer extends Script implements PaintListener {
	private final ArrayList<HighLightInterface> HighLightWraps = new ArrayList<HighLightInterface>();
	private ArrayList<RSComponentListener> OldInterfaces = new ArrayList<RSComponentListener>();
	threadlistener o = null;
	private int change = -1;
	private Rectangle highlightArea = null;
	private HighlightTreeCellRenderer renderer = null;

	public void check() {
		if (listenerButton.isSelected()) {
			addOldComponent();
		}
		o = new threadlistener();
	}

	private void addOldComponent() {
		OldInterfaces.clear();
		for (RSInterface iface : interfaces.getAll()) {
			for (RSComponent child : iface.getComponents()) {
				OldInterfaces.add(new RSComponentListener(child));
				if (child.getComponentStackSize() != 0) {
					for (final RSComponent component : child.getComponents()) {
						OldInterfaces.add(new RSComponentListener(component));
					}
				}
			}
		}
	}

	private void compare(RSComponentListener a) {
		//getType
		if (a.getComponent().getType() != a.getType()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 1));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 1));
			}
		}
		//SpecialType
		if (a.getComponent().getSpecialType() != a.getSpecialType()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 2));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 2));
			}
		}
		//Bounds
		if (a.getComponent().getBoundsArrayIndex() != a.bounds()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 3));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 3));
			}
		}
		//Model ID
		if (a.getComponent().getModelID() != a.getModelID()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 4));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 4));
			}
		}
		//Texture ID
		if (a.getComponent().getBackgroundColor() != a.getColor()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 5));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 5));
			}
		}
		//Parent ID
		if (a.getComponent().getParentID() != a.getID()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 6));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 6));
			}
		}
		//Text
		if (a.getComponent().getText() != a.getText()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 7));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 7));
			}
		}
		//Tooltip
		if (a.getComponent().getTooltip() != a.getToolTip()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 8));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 8));
			}
		}
		//SelActionName
		if (a.getComponent().getSelectedActionName() != a.getSelectionName()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 9));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 9));
			}
		}
		//ComID
		if (a.getComponent().getComponentID() != a.getCompID()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 11));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 11));
			}
		}
		//Comp Stack Size
		if (a.getComponent().getComponentStackSize() != a.getCompSize()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 12));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 12));
			}
		}
		//Relative Pos
		if (a.getComponent().getRelativeX() != a.getRelativeX() && a.getComponent().getRelativeY() != a.getRelativeY()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 13));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 13));
			}
		}
		//Absolute Pos
		if (a.getComponent().getAbsoluteX() != a.getAbsoluteX() && a.getComponent().getAbsoluteY() != a.getAbsoluteY()) {
			if (a.getComponent().getParent() == null) {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getInterface(), a.getComponent(), null, 14));
			} else {
				HighLightWraps.add(new HighLightInterface(a.getComponent().getParent().getInterface(), a.getComponent().getParent(), a.getComponent(), 14));
			}
		}
	}

	@SuppressWarnings("serial")
	private class HighlightTreeCellRenderer extends DefaultTreeCellRenderer {
		public final ArrayList<HighLightInterface> HighLightableWraps = new ArrayList<HighLightInterface>();
		private HighLightInterface NextInterface;
		private final TreeCellRenderer renderer;

		public HighlightTreeCellRenderer(TreeCellRenderer renderer) {
			this.renderer = renderer;
		}

		public Component getTreeCellRendererComponent(JTree tree, Object value,
		                                              boolean isSelected, boolean expanded,
		                                              boolean leaf, int row, boolean hasFocus) {
			JComponent c = (JComponent) renderer.getTreeCellRendererComponent(
					tree, value, isSelected, expanded, leaf, row, hasFocus);

			HighLightableWraps.clear();
			HighLightableWraps.addAll(HighLightWraps);
			if (value instanceof RSInterfaceWrap) {
				for (int i = 0; i < HighLightableWraps.size(); i++) {
					NextInterface = HighLightableWraps.get(i);
					if (NextInterface.getComp() == null) {
						if (NextInterface.getChild().getInterface().getIndex() == ((RSInterfaceWrap) value).wrapped.getIndex()) {
							c.setForeground(Color.blue);

						}
					}
				}
			}
			if (value instanceof RSComponentWrap) {
				for (int i = 0; i < HighLightableWraps.size(); i++) {
					NextInterface = HighLightableWraps.get(i);
					if (NextInterface.getComp() == null) {
						if (NextInterface.getChild().getInterface().getIndex() == ((RSComponentWrap) value).wrapped.getInterface().getIndex()) {
							if (NextInterface.getChild().getIndex() == ((RSComponentWrap) value).wrapped.getIndex()) {
								c.setForeground(Color.blue);
							}
						}
					} else {
						if (NextInterface.getChild().getIndex() == ((RSComponentWrap) value).wrapped.getParent().getIndex() && NextInterface.getParent().getIndex() == ((RSComponentWrap) value).wrapped.getParent().getInterface().getIndex()) {
							c.setForeground(Color.blue);
						}
						/*
		    			if (NextInterface.getComp().getID() == ((RSComponentWrap) value).wrapped.getID()) {
			    		   	//log(((RSComponentWrap) value).wrapped.getIndex());
		    			}
		    			if (NextInterface.getChild().getID() == ((RSComponentWrap) value).wrapped.getID()) {
			    		   	//log(((RSComponentWrap) value).wrapped.getIndex());
		    			}
						 */
					}
				}
			}
			return c;
		}
	}

	private class HighLightInterface {
		private RSInterface parent = null;
		private RSComponent child = null;
		private RSComponent component = null;
		private int change;

		HighLightInterface(RSInterface a, RSComponent b, RSComponent c, int change) {
			this.parent = a;
			this.child = b;
			this.component = c;
			this.change = change;
		}

		private RSInterface getParent() {
			return parent;
		}

		private RSComponent getChild() {
			return child;
		}

		private RSComponent getComp() {
			return component;
		}

		//-1 is default
		private int getChange() {
			return change;
		}
	}

	private class InterfaceTreeModel implements TreeModel {
		private final Object root = new Object();
		private final ArrayList<TreeModelListener> treeModelListeners = new ArrayList<TreeModelListener>();

		// only call getAllInterfaces() once per GUI update, because
		// otherwise closed interfaces might mess up the indexes
		private final ArrayList<RSInterfaceWrap> interfaceWraps = new ArrayList<RSInterfaceWrap>();

		@Override
		public void addTreeModelListener(final TreeModelListener l) {
			treeModelListeners.add(l);
		}

		private void fireTreeStructureChanged(final Object oldRoot) {
			treeModelListeners.size();
			final TreeModelEvent e = new TreeModelEvent(this,
					new Object[]{oldRoot});
			for (final TreeModelListener tml : treeModelListeners) {
				tml.treeStructureChanged(e);
			}
		}

		@Override
		public Object getChild(final Object parent, final int index) {
			if (parent == root) {
				return interfaceWraps.get(index);
			} else if (parent instanceof RSInterfaceWrap) {
				return new RSComponentWrap(
						((RSInterfaceWrap) parent).wrapped.getComponents()[index]);
			} else if (parent instanceof RSComponentWrap) {
				return new RSComponentWrap(
						((RSComponentWrap) parent).wrapped.getComponents()[index]);
			}
			return null;
		}

		@Override
		public int getChildCount(final Object parent) {
			if (parent == root) {
				return interfaceWraps.size();
			} else if (parent instanceof RSInterfaceWrap) {
				return ((RSInterfaceWrap) parent).wrapped.getComponents().length;
			} else if (parent instanceof RSComponentWrap) {
				return ((RSComponentWrap) parent).wrapped.getComponents().length;
			}
			return 0;
		}

		@Override
		public int getIndexOfChild(final Object parent, final Object child) {
			if (parent == root) {
				return interfaceWraps.indexOf(child);
			} else if (parent instanceof RSInterfaceWrap) {
				return Arrays.asList(
						((RSInterfaceWrap) parent).wrapped.getComponents())
						.indexOf(((RSComponentWrap) child).wrapped);
			} else if (parent instanceof RSComponentWrap) {
				return Arrays.asList(
						((RSComponentWrap) parent).wrapped.getComponents())
						.indexOf(((RSComponentWrap) child).wrapped);
			}
			return -1;
		}

		@Override
		public Object getRoot() {
			return root;
		}

		@Override
		public boolean isLeaf(final Object o) {
			return o instanceof RSComponentWrap
					&& ((RSComponentWrap) o).wrapped.getComponents().length == 0;
		}

		@Override
		public void removeTreeModelListener(final TreeModelListener l) {
			treeModelListeners.remove(l);
		}

		public boolean searchMatches(final RSComponent iface,
		                             final String contains) {
			return iface.getText().toLowerCase()
					.contains(contains.toLowerCase());
		}

		public void update(final String search) {
			interfaceWraps.clear();
			HighLightWraps.clear();
			for (RSInterface iface : interfaces.getAll()) {
				toBreak:
				for (RSComponent child : iface.getComponents()) {
					if (searchMatches(child, search)) {
						interfaceWraps.add(new RSInterfaceWrap(iface));
						if (window.isVisible() && !listenerButton.isSelected() && !searchBox.getText().equals("")) {
							HighLightWraps.add(new HighLightInterface(iface, child, null, -1));
						}
						break;
					}

					for (final RSComponent component : child.getComponents()) {
						if (searchMatches(component, search)) {
							interfaceWraps.add(new RSInterfaceWrap(iface));
							if (window.isVisible() && !listenerButton.isSelected() && !searchBox.getText().equals("")) {
								HighLightWraps.add(new HighLightInterface(iface, child, component, -1));
							}
							break toBreak;
						}
					}
				}
			}
			fireTreeStructureChanged(root);
		}

		@Override
		public void valueForPathChanged(final TreePath path,
		                                final Object newValue) {
			// tree represented by this model isn't editable
		}
	}

	private class RSComponentListener {
		//int actiontype;
		int type;
		int specialtype;
		int bounds;
		int model;
		int color;
		int getID;
		String getText;
		String tooltip;
		String selectionname;
		int compID;
		int compStackSize;
		Point relativeloc;
		Point absoluteloc;
		RSComponent a;

		RSComponentListener(RSComponent child) {
			this.a = child;
			//Action Type Busted?
			//this.actiontype = child.getActionType();
			this.type = child.getType();
			this.specialtype = child.getSpecialType();
			this.bounds = child.getBoundsArrayIndex();
			this.model = child.getModelID();
			this.color = child.getBackgroundColor();
			this.getID = child.getParentID();
			this.getText = child.getText();
			this.tooltip = child.getTooltip();
			this.selectionname = child.getSelectedActionName();
			this.compID = child.getComponentID();
			this.compStackSize = child.getComponentStackSize();
			this.relativeloc = new Point(child.getRelativeX(), child.getRelativeY());
			this.absoluteloc = new Point(child.getAbsoluteX(), child.getAbsoluteY());
		}

		private RSComponent getComponent() {
			return a;
		}

		private int getType() {
			return type;
		}

		private int getSpecialType() {
			return specialtype;
		}

		private int bounds() {
			return bounds;
		}

		private int getModelID() {
			return model;
		}

		private int getID() {
			return getID;
		}

		private int getColor() {
			return color;
		}

		private String getText() {
			return getText;
		}

		private String getToolTip() {
			return tooltip;
		}

		private String getSelectionName() {
			return selectionname;
		}

		private int getCompID() {
			return compID;
		}

		private int getCompSize() {
			return compStackSize;
		}

		private int getRelativeX() {
			return relativeloc.x;
		}

		private int getRelativeY() {
			return relativeloc.y;
		}

		private int getAbsoluteX() {
			return absoluteloc.x;
		}

		private int getAbsoluteY() {
			return absoluteloc.y;
		}
	}

	private class RSComponentWrap {
		public RSComponent wrapped;

		public RSComponentWrap(final RSComponent wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean equals(final Object o) {
			return o instanceof RSComponentWrap
					&& wrapped == ((RSComponentWrap) o).wrapped;
		}

		@Override
		public String toString() {
			return "Component " + wrapped.getIndex();
		}
	}

	private class threadlistener implements Runnable {

		public threadlistener() {
			new Thread(this).start();
		}

		@Override
		public void run() {
			try {
				while (listenerButton.isSelected()) {
					for (int i = 0; i < OldInterfaces.size(); i++) {
						compare(OldInterfaces.get(i));
					}
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		public void kill() {
			Thread.interrupted();
		}
	}

	// these wrappers just add toString() methods
	private class RSInterfaceWrap {
		public RSInterface wrapped;

		public RSInterfaceWrap(final RSInterface wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public boolean equals(final Object o) {
			return o instanceof RSInterfaceWrap
					&& wrapped == ((RSInterfaceWrap) o).wrapped;
		}

		@Override
		public String toString() {
			return "Interface " + wrapped.getIndex();
		}
	}

	private JFrame window;
	private JTree tree;
	private InterfaceTreeModel treeModel;
	private JPanel infoArea;
	private JTextField searchBox;
	final JToggleButton listenerButton = new JToggleButton("Listen");

	@Override
	public boolean onStart() {
		window = new JFrame("Interface Explorer");

		treeModel = new InterfaceTreeModel();
		treeModel.update("");
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		tree.setEditable(false);
		renderer = new HighlightTreeCellRenderer(tree.getCellRenderer());
		tree.setCellRenderer(renderer);
		tree.getSelectionModel().setSelectionMode(
				TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			private void addInfo(final String key, final String value, boolean highlight) {
				final JPanel row = new JPanel();
				row.setAlignmentX(Component.LEFT_ALIGNMENT);
				row.setLayout(new BoxLayout(row, BoxLayout.X_AXIS));

				for (final String data : new String[]{key, value}) {
					final JLabel label = new JLabel(data);
					label.setAlignmentY(Component.TOP_ALIGNMENT);
					if (highlight) {
						label.setForeground(Color.magenta);
					}

					row.add(label);
				}
				infoArea.add(row);
			}

			@Override
			public void valueChanged(final TreeSelectionEvent e) {
				final Object node = tree.getLastSelectedPathComponent();
				if (node == null || node instanceof RSInterfaceWrap) {
					return;
				}
				// at this point the node can only be an instace of
				// RSInterfaceChildWrap
				// or of RSInterfaceComponentWrap

				infoArea.removeAll();
				RSComponent iface = null;
				if (node instanceof RSComponentWrap) {
					iface = ((RSComponentWrap) node).wrapped;
				}
				if (iface == null) {
					return;
				}
				change = -1;
				for (int i = 0; i < HighLightWraps.size(); i++) {
					if (iface.getParent() == null) {
						if (HighLightWraps.get(i).getChild().getIndex() == iface.getIndex() && HighLightWraps.get(i).getParent().getIndex() == iface.getInterface().getIndex()) {
							change = HighLightWraps.get(i).getChange();
						}
					} else {
						if (HighLightWraps.get(i).getChild().getIndex() == iface.getParent().getIndex() && HighLightWraps.get(i).getParent().getIndex() == iface.getParent().getInterface().getIndex()) {
							change = HighLightWraps.get(i).getChange();
						}
					}
				}
				if (change == 0) {
					addInfo("Action type: ", "-1" /* + iface.getActionType() */, true);
				} else {
					addInfo("Action type: ", "-1" /* + iface.getActionType() */, false);
				}
				if (change == 1) {
					addInfo("Type: ", "" + iface.getType(), true);
				} else {
					addInfo("Type: ", "" + iface.getType(), false);
				}
				if (change == 2) {
					addInfo("SpecialType: ", "" + iface.getSpecialType(), true);
				} else {
					addInfo("SpecialType: ", "" + iface.getSpecialType(), false);
				}
				if (change == 3) {
					addInfo("Bounds Index: ", "" + iface.getBoundsArrayIndex(), true);
				} else {
					addInfo("Bounds Index: ", "" + iface.getBoundsArrayIndex(), false);
				}
				if (change == 4) {
					addInfo("Model ID: ", "" + iface.getModelID(), true);
				} else {
					addInfo("Model ID: ", "" + iface.getModelID(), false);
				}
				if (change == 5) {
					addInfo("Texture ID: ", "" + iface.getBackgroundColor(), true);
				} else {
					addInfo("Texture ID: ", "" + iface.getBackgroundColor(), false);
				}
				if (change == 6) {
					addInfo("Parent ID: ", "" + iface.getParentID(), true);
				} else {
					addInfo("Parent ID: ", "" + iface.getParentID(), false);
				}
				if (change == 7) {
					addInfo("Text: ", "" + iface.getText(), true);
				} else {
					addInfo("Text: ", "" + iface.getText(), false);
				}
				if (change == 8) {
					addInfo("Tooltip: ", "" + iface.getTooltip(), true);
				} else {
					addInfo("Tooltip: ", "" + iface.getTooltip(), false);
				}
				if (change == 9) {
					addInfo("SelActionName: ", "" + iface.getSelectedActionName(), true);
				} else {
					addInfo("SelActionName: ", "" + iface.getSelectedActionName(), false);
				}
				if (iface.getActions() != null) {
					String actions = "";
					for (final String action : iface.getActions()) {
						if (!actions.equals("")) {
							actions += "\n";
						}
						actions += action;
					}
					if (change == 10) {
						addInfo("Actions: ", actions, true);
					} else {
						addInfo("Actions: ", actions, false);
					}

				}
				if (change == 11) {
					addInfo("Component ID: ", "" + iface.getComponentID(), true);
				} else {
					addInfo("Component ID: ", "" + iface.getComponentID(), false);
				}
				if (change == 12) {
					addInfo("Component Stack Size: ",
							"" + iface.getComponentStackSize(), true);
				} else {
					addInfo("Component Stack Size: ",
							"" + iface.getComponentStackSize(), false);
				}
				if (change == 13) {
					addInfo("Relative Location: ", "(" + iface.getRelativeX() + ","
							+ iface.getRelativeY() + ")", true);
				} else {
					addInfo("Relative Location: ", "(" + iface.getRelativeX() + ","
							+ iface.getRelativeY() + ")", false);
				}
				if (change == 14) {
					addInfo("Absolute Location: ", "(" + iface.getAbsoluteX() + ","
							+ iface.getAbsoluteY() + ")", true);
				} else {
					addInfo("Absolute Location: ", "(" + iface.getAbsoluteX() + ","
							+ iface.getAbsoluteY() + ")", false);
				}
				infoArea.validate();
				infoArea.repaint();
			}
		});

		final JDialog Help = new JDialog();
		JScrollPane jScrollPane1;
		JTextArea jTextArea1;

		jScrollPane1 = new JScrollPane();
		jTextArea1 = new JTextArea();

		Help.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		Help.setTitle("Help");
		Help.setResizable(false);

		jTextArea1.setColumns(20);
		jTextArea1.setEditable(false);
		jTextArea1.setFont(new java.awt.Font("MS UI Gothic", 0, 12)); // NOI18N
		jTextArea1.setLineWrap(true);
		jTextArea1.setRows(5);
		jTextArea1.setText("Once toggled the listener feature of the interface explorer will detect any changes made to Runescapes interfaces in realtime. If a change is found that interface and data will then be highlighted within the explorers tree model. To use the listener feature you would :\n\n1) Toggle the listener button as active\n2) Wait or commit changes in Runescape\n3) Repaint tree using repaint button or reclick interface folders in GUI\n\n\nTips : While listening for changes the tree model in the GUI will not update itself, changing colors. To refresh the GUI either use the repaint button or close and open Interface folder already within the tree model.");
		jTextArea1.setWrapStyleWord(true);
		jScrollPane1.setViewportView(jTextArea1);
		GroupLayout layout = new GroupLayout(Help.getContentPane());
		Help.getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 348, Short.MAX_VALUE)
								.addContainerGap())
		);
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 220, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		Help.pack();

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setPreferredSize(new Dimension(250, 500));
		window.add(scrollPane, BorderLayout.WEST);

		infoArea = new JPanel();
		infoArea.setLayout(new BoxLayout(infoArea, BoxLayout.Y_AXIS));
		scrollPane = new JScrollPane(infoArea);
		scrollPane.setPreferredSize(new Dimension(250, 500));
		window.add(scrollPane, BorderLayout.CENTER);

		final ActionListener actionListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				treeModel.update(searchBox.getText());
				infoArea.removeAll();
				infoArea.validate();
				infoArea.repaint();
			}
		};
		final ActionListener toggleListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				if (listenerButton.isSelected()) {
					log("Cleared");
					HighLightWraps.clear();
				}
				check();
			}
		};
		final ActionListener repaintListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				log("Refreshed Tree");
				treeModel.fireTreeStructureChanged(treeModel.getRoot());
				infoArea.removeAll();
				infoArea.validate();
				infoArea.repaint();
			}
		};
		final ActionListener helpListener = new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				Help.setVisible(true);
			}
		};

		final JPanel toolArea = new JPanel();
		toolArea.setLayout(new FlowLayout(FlowLayout.LEFT));
		toolArea.add(new JLabel("Filter:"));

		searchBox = new JTextField(20);
		searchBox.addActionListener(actionListener);
		toolArea.add(searchBox);

		final JButton updateButton = new JButton("Update");
		final JButton repaintButton = new JButton("Repaint");
		final JButton helpButton = new JButton("Help");
		helpButton.addActionListener(helpListener);
		listenerButton.addActionListener(toggleListener);
		updateButton.addActionListener(actionListener);
		repaintButton.addActionListener(repaintListener);
		toolArea.add(updateButton);
		toolArea.add(listenerButton);
		toolArea.add(repaintButton);
		toolArea.add(helpButton);
		window.add(toolArea, BorderLayout.NORTH);
		window.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		window.pack();
		window.setVisible(true);
		return true;
	}

	@Override
	public int loop() {
		if (window.isVisible()) {
			return 1000;
		}
		return -1;
	}

	@Override
	public void onRepaint(final Graphics g) {
		if (highlightArea != null) {
			g.setColor(Color.ORANGE);
			g.drawRect(highlightArea.x, highlightArea.y, highlightArea.width,
					highlightArea.height);
		}
	}

	@Override
	public void onFinish() {
		o.kill();
	}
}