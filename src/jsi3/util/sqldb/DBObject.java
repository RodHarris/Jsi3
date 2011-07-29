package jsi3.util.sqldb;

/**
all classes implementing this interface are auto generated.

accessors & mutators in classes that implement this interface have to follow java.beans style for the xml encoder/decoder to work :-(
*/
public abstract class DBObject
{
	//public abstract String db_table_name();

	//public abstract String db_singular_name();

	private int id;
	
	public int getId()
	{
		return id;
	}

	public void setId( int id )
	{
		this.id = id;
	}

	public int get_id()
	{
		return id;
	}

	public void set_id( int id )
	{
		this.id = id;
	}


	public abstract void _track_sets( boolean track_sets );


	//public abstract boolean _track_sets();
	
	
	private String _name, _lname;
	
	protected void _set_lname( String _lname )
	{
		this._lname = _lname;
	}
	
	protected void _set_name( String _name )
	{
		this._name = _name;
	}
	
	public String _get_lname()
	{
		return _lname;
	}
	
	public String _get_name()
	{
		return _name;
	}
	
	
	public abstract void add( DBObject... dbos ) throws Exception;
	
	
	public abstract void link( DBObject... dbos ) throws Exception;
}
