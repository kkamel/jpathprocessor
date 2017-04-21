import java.util.ArrayList;
import java.util.List;

public class AsterixDbSchemaNode<T> {
    private List<AsterixDbSchemaNode<T>> children = new ArrayList<AsterixDbSchemaNode<T>>();
    private AsterixDbSchemaNode<T> parent = null;
    private String name = null;
    private T type = null;

    public AsterixDbSchemaNode(String name, T type) {
        this.name = name;
        this.type = type;
    }

    public AsterixDbSchemaNode(String name, T type, AsterixDbSchemaNode<T> parent) {
        this.name = name;
        this.type = type;
        this.parent = parent;
    }

    public List<AsterixDbSchemaNode<T>> getChildren() {
        return children;
    }

    public void setParent(AsterixDbSchemaNode<T> parent) {
        this.parent = parent;
    }
    
    public void addChild(String name, T type) {
        AsterixDbSchemaNode<T> child = new AsterixDbSchemaNode<T>(name, type);
        child.setParent(this);
        if (!this.children.contains(child))
            this.children.add(child);
    }

    public void addChild(AsterixDbSchemaNode<T> child) {
        child.setParent(this);
        if (!this.children.contains(child))
            this.children.add(child);
    }

    public String getName() {
        return this.name;
    }

    public T getType() {
        return this.type;
    }
    
    public AsterixDbSchemaNode<T> getParent() {
        return this.parent;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setType(T type) {
        this.type = type;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }
}
