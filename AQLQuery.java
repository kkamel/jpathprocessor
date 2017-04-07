import java.lang.StringBuilder;

public class AQLQuery {
    //public Map<String, String> varBindings; 
    public Object root;
    private String _for;
    private String _limit;
    private String _orderby;
    private String _where;
    private String _ret;

    public AQLQuery() {
        this._for = "";
        this._limit = "";
        this._orderby = "";
        this._where = "";
        this._ret = "";
    }
    
    public void add_root(String root) {
        this.root = root;
    }
    public void add_for(String fexpr) {
        this._for += fexpr;
        this._for += "\n";
    }
    public void add_limit(String lexpr) {
        this._limit = lexpr;
    }
    public void add_orderby(String oexpr) {
        this._orderby = oexpr;
    }
    public void add_where(String wexpr) {
        if (this._where != "")
            this._where += " and ";
        this._where += "where ";
        this._where += wexpr;
    }
    public void add_ret(String rexpr) {
        this._ret = "return $" + rexpr;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.root);
        if(this._for != "")
            sb.append("\n");
        sb.append(this._for);
        
        if(this._limit != "")
            sb.append("\n");
        sb.append(this._limit);
        
        if(this._orderby != "")
            sb.append("\n");
        sb.append(this._orderby);
        
        if(this._where != "")
            sb.append("\n");
        sb.append(this._where);
        
        if(this._ret != "")
            sb.append("\n");
        sb.append(this._ret);
        return sb.toString();
    }
}
