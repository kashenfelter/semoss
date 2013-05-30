package prerna.ui.main.listener.impl;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import prerna.om.DBCMEdge;
import prerna.om.DBCMVertex;
import prerna.ui.components.EdgePropertyTableModel;
import prerna.ui.components.GraphNodePopup;
import prerna.ui.components.GraphPlaySheet;
import prerna.ui.components.VertexPropertyTableModel;
import prerna.ui.components.api.IChakraListener;
import prerna.ui.components.api.IPlaySheet;
import prerna.ui.transformer.ArrowDrawPaintTransformer;
import prerna.ui.transformer.EdgeArrowStrokeTransformer;
import prerna.ui.transformer.EdgeStrokeTransformer;
import prerna.ui.transformer.SearchEdgeStrokeTransformer;
import prerna.ui.transformer.SearchVertexLabelFontTransformer;
import prerna.ui.transformer.SearchVertexPaintTransformer;
import prerna.ui.transformer.VertexLabelFontTransformer;
import prerna.ui.transformer.VertexPaintTransformer;
import prerna.util.Constants;
import prerna.util.DIHelper;
import prerna.util.QuestionPlaySheetStore;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.ModalLensGraphMouse;
import edu.uci.ics.jung.visualization.picking.PickedState;


public class GraphNodeListener extends ModalLensGraphMouse implements IChakraListener
{
	Logger logger = Logger.getLogger(getClass());
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		logger.debug(" Came to action performed here");
	}
	/*
	public void mouseEntered(MouseEvent e)
	{
    	if (e.isMetaDown())
		{

			System.out.println("alksdfj");
    		

		}
		DBCMVertex v=null;
		super.mouseClicked(e);
		VisualizationViewer vCheck = (VisualizationViewer)e.getSource();
		System.out.println(vCheck.getClass());
		Iterator viCheck = vCheck.getPickedVertexState().getPicked().iterator();
		Iterator eCheck = vCheck.getPickedEdgeState().getPicked().iterator();		
        while(viCheck.hasNext())
		{
			v = (DBCMVertex)viCheck.next();
		}
        System.out.println("here");
	}
	*/
	public void mouseClicked(MouseEvent e)
	{
		super.mouseClicked(e);
		logger.debug(e.getModifiers());
		logger.debug(" Clicked");
		logger.debug(e.getSource());
		VisualizationViewer viewer = (VisualizationViewer)e.getSource();
		viewer.repaint();
		
		
		IPlaySheet ps3 = QuestionPlaySheetStore.getInstance().getActiveSheet();
		
		JTable table = (JTable)DIHelper.getInstance().getLocalProp(Constants.PROP_TABLE);
		TableModel tm = new DefaultTableModel();
		table.setModel(tm);



		// handle the vertices
		PickedState <DBCMVertex> ps = viewer.getPickedVertexState();
		Iterator <DBCMVertex> it = ps.getPicked().iterator();
		boolean wasSelected = false;
		PickedState <DBCMEdge> ps2 = viewer.getPickedEdgeState();
		Iterator <DBCMEdge> it2 = ps2.getPicked().iterator();
		wasSelected = false;
		while(it2.hasNext())
		{
			DBCMEdge v = it2.next();
			logger.info(" Name  >>> " + v.getProperty(Constants.VERTEX_NAME));
			// this needs to invoke the property table model stuff
			EdgePropertyTableModel pm = new EdgePropertyTableModel(ps3.getFilterData(), v);
			table.setModel(pm);
			//table.repaint();
			pm.fireTableDataChanged();
			logger.debug("Add this in - Edge Table");	
			wasSelected = true;
		}

		DBCMVertex [] vertices = new DBCMVertex[ps.getPicked().size()];
		// right now we assume only one is selected
		for(int vertIndex = 0;it.hasNext();vertIndex++)
		{
			DBCMVertex v = it.next();
			vertices[vertIndex] = v;
			
			logger.info(" Name  >>> " + v.getProperty(Constants.VERTEX_NAME));
			// this needs to invoke the property table model stuff
			
			VertexPropertyTableModel pm = new VertexPropertyTableModel(ps3.getFilterData(),v);
			table.setModel(pm);
			//table.repaint();
			pm.fireTableDataChanged();
			logger.debug("Add this in - Prop Table");
			wasSelected = true;
		}

		if(e.getButton() == MouseEvent.BUTTON3)
		{
			logger.debug("Button 3 is pressed ");
			GraphNodePopup popup = new GraphNodePopup(ps3, vertices, e.getComponent(), e.getX(), e.getY());
			
			popup.show(e.getComponent(), e.getX(), e.getY());
			// need to show a menu here
		}

		if(!wasSelected)
		{
			ps.clear();
			//if in the middle of performing data latency play, don't want to reset transformers
			GraphPlaySheet gps = null;
			try{
				gps = (GraphPlaySheet) ps3;
			}catch (Exception ex){
				//ignored
			}
			gps.resetTransformers();
				
		}
		// handle the vertices

		
	}

	@Override
	public void setView(JComponent view) {
		// TODO Auto-generated method stub
		
	}

}