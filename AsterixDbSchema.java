import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.nio.file.Files;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.nio.file.Paths;

public class AsterixDbSchema {
    private JSONArray jsonArray = null;
    private Map<String, Collection<String>> adjacencyList = null;
    private AsterixDbSchemaNode<String> schemaRoot = null;

    public AsterixDbSchema(String schemaFile) {
        JSONParser parser = new JSONParser();
        this.adjacencyList = new HashMap<String, Collection<String>>();
        try {
            Object obj = parser.parse(new FileReader(schemaFile));
            this.jsonArray = (JSONArray) obj;
                
            //System.out.println(this.jsonArray);
            Iterator<JSONObject> iterator = this.jsonArray.iterator();
            
            while (iterator.hasNext()) {
                JSONObject currentObj = iterator.next();
                //System.out.println(currentObj);
                String datatype = new String(currentObj.get("DatatypeName").toString());
                JSONObject derived =  (JSONObject) currentObj.get("Derived");
                JSONObject record = (JSONObject) derived.get("Record");
                if (record == null)
                    continue;
                JSONArray children = (JSONArray) record.get("Fields");
                
                if (children != null ) {
                    System.out.println(children);
                    Iterator<JSONObject> fieldIter = children.iterator();
                    ArrayList fields = new ArrayList();
                    while(fieldIter.hasNext()) {
                        JSONObject currentField = fieldIter.next();
                        fields.add(currentField.get("FieldName")+":"+currentField.get("FieldType")); 
                    }

                    adjacencyList.put(datatype, fields);
                }
            }
            System.out.println("hello");
            System.out.println(adjacencyList);

        }
        catch (Exception e) {
            System.err.println(e);
        }
        

    }
    
    // TODO: Build schema from AdjacencyList
    // TODO: Crashing in the second while loop. Problem with adding child node to parent.
    public AsterixDbSchemaNode createSchemaTree() {
        AsterixDbSchemaNode root = null;
        Iterator it = this.adjacencyList.entrySet().iterator();
        Map derived_nodes = new HashMap<String, AsterixDbSchemaNode<String>>();
        // Build derived_nodes HashMap
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            String parent_name = (String) pair.getKey();
            ArrayList children = (ArrayList) pair.getValue();
            AsterixDbSchemaNode<String> parent_node = new AsterixDbSchemaNode<String>(parent_name, "derived");
            derived_nodes.put(parent_name.toLowerCase(), parent_node);

        }
        

        it = this.adjacencyList.entrySet().iterator();
        
        // Iterate over adjacencyList HashMap and process children
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            //System.out.println(pair.getKey() + " = " + pair.getValue());
            String parent_name = (String) pair.getKey();
            ArrayList children = (ArrayList) pair.getValue();
            AsterixDbSchemaNode<String> parent_node = new AsterixDbSchemaNode<String>(parent_name, "derived");
            Iterator<String> childiter = children.iterator();
            
            System.out.println("processing: " + parent_name);
            //System.out.println(children.size());
            while (childiter.hasNext()) {
                String child = (String) childiter.next();
                String[] childtype = child.split(":");
                String name = childtype[0].toLowerCase();
                String type = childtype[1].toLowerCase();
                //System.out.println(name + " : " +  type);        
                AsterixDbSchemaNode<String> child_node = new AsterixDbSchemaNode<String>(name, type);
                
                String[] typeToClean = type.split("_");
                String type_cleaned;
                if (typeToClean.length > 1) {
                    type_cleaned = typeToClean[1];
                } else {
                    type_cleaned = typeToClean[0];
                }
                //System.out.println("cleaned type: " + type_cleaned);
                //System.out.println(derived_nodes.get(type_cleaned));
                if (derived_nodes.containsKey(type_cleaned)) {
                    AsterixDbSchemaNode<String> node = (AsterixDbSchemaNode) derived_nodes.get(type_cleaned);
                    node.setParent(parent_node);
                    derived_nodes.put(type_cleaned, node);
                }
                //child_node.setParent(parent_node);
                parent_node.addChild(name.toLowerCase(), type.toLowerCase());
            }
            // TODO: Check if child is is a derived type AKA in adjacencyList
            
            ArrayList<AsterixDbSchemaNode<String>> schema_children = (ArrayList<AsterixDbSchemaNode<String>>) parent_node.getChildren();
        
        }
        Iterator<HashMap<String, AsterixDbSchemaNode<String>>> derivedIter = derived_nodes.entrySet().iterator();
        while(derivedIter.hasNext()) {
            Map.Entry pair = (Map.Entry) derivedIter.next();   
            AsterixDbSchemaNode<String> node = (AsterixDbSchemaNode<String>) pair.getValue();
            //System.out.println(node.isRoot());
            
            if (node.isRoot())
                root = node;
                
        }
        return root;
    }

    public static void main(String [] args) {
        AsterixDbSchema as = new AsterixDbSchema("/home/kareem/Documents/Research/thesis/jpathprocessor/store_schema.txt");
        AsterixDbSchemaNode root = as.createSchemaTree();
        System.out.println("schema root is: " + root.getName());
    }

}
