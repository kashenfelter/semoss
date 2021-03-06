package prerna.ui.transformer;

import java.util.Vector;

import org.apache.commons.collections15.Transformer;

import prerna.om.DBCMVertex;
import prerna.ui.components.ControlData;
import prerna.util.Constants;

public class VertexLabelTransformer implements Transformer <DBCMVertex, String> {
	
	public static VertexLabelTransformer tx = null;
	ControlData data = null;
	
	public VertexLabelTransformer(ControlData data)
	{
		this.data = data;
	}

	@Override
	public String transform(DBCMVertex arg0) {
		
		// get the DI Helper to find what is the property we need to get for vertex
		// based on that get that property and return it		
		String propName = "";//(String)arg0.getProperty(Constants.VERTEX_NAME);

		Vector props = this.data.getSelectedProperties(arg0.getProperty(Constants.VERTEX_TYPE)+"");
		if(props != null && props.size() > 0)
		{
			propName = "<html>";
			for(int propIndex=0;propIndex < props.size();propIndex++)
				propName = propName + "<br>" + arg0.getProperty(props.elementAt(propIndex)+"");
			propName = propName + "</html>";
		}
		//System.out.println("Prop Name " + propName);
		
		if(propName != null)
			return propName;		
		return "";
	}
}
