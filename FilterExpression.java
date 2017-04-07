public class FilterExpression {
    
    public Object obj;
    public String comparator;
    public String val;

    public FilterExpression() {

    }

    public String toString() {
        String feString = this.obj.toString() + this.comparator + this.val;     
        return feString;
    }

    
}
