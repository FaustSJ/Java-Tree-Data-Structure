import java.util.*;
import java.lang.*;

// Immutable.  Stores 3 strings referred to as entity, relation, and
// property. Each Record has a unique integer ID which is set on
// creation.  All records are made through the factory method
// Record.makeRecord(e,r,p).  Record which have some fields wild are
// created using Record.makeQuery(wild,e,r,p)
public class Record{

	public final int id;      // Must be unique
	/*we will refer to the three strings below as "cards" */
	public String entity = "e";     // Who
	public String relation = "r";   // How
	public String property = "p";   // What
	private static int uniqueID = 0;
	//stores whether or no a card is wild
	public boolean eWild;
	public boolean rWild;
	public boolean pWild;
	
	//a constructor, just to make creating records a little easier.
	public Record(String wild, String entity, String relation, String property)
	{	
		
		//the cards are given new strings to hold
		this.entity = entity;
		this.relation = relation;
		this.property = property;
		
		//if the constructor was called by makeQuery, then a wild is present.
		//	each card is check if it's wild or not.
		if(wild!="")
		{
			if(this.entity.equals(wild))
				this.eWild = true;
			else
				this.eWild = false;
			if(this.relation.equals(wild))
				this.rWild = true;
			else
				this.rWild = false;
			if(this.property.equals(wild))
				this.pWild = true;
			else
				this.pWild = false;
		}
		//otherwise, the constructor was called by makeRecord, meaning
		//	no wilds are present.
		else
		{
			this.eWild = false;
			this.rWild = false;
			this.pWild = false;
		}
		
		//then the id is assigned
		this.id = nextId();
	}
	// Factory method to create a Record. No public constructor is
	// required.
	public static Record makeRecord(String entity, String relation, String property)
	{
  	  //throws a runtime exception if any of the arguments are null
		if(entity==null || relation==null || property==null)
		{
			throw new IllegalArgumentException("ERROR: No nulls allowed!");
		}
		
		Record newr = new Record("", entity, relation, property);
  		
		return newr;
	}
	
	 // Create a record that has some fields wild. Any field that is
	 // equal to the first argument wild will be a wild card
  	public static Record makeQuery(String wild, String entity, String relation, String property)
  	{
  		//throws a runtime exception if any of the arguments are null
		if(wild==null || entity==null || relation==null || property==null)
		{
			throw new IllegalArgumentException("ERROR: No nulls allowed!");
		}
		
		Record newr = new Record(wild, entity, relation, property);
  		
		return newr;
  	}
	
  // Return the next ID that will be assigned to a Record on a call to
  // makeRecord() or makeQuery()
  public static int nextId()
  {
  	  int prev = uniqueID;
  	  uniqueID++;
  	  return prev;
  }

  // Return a stringy representation of the record. Each string should
  // be RIGHT justified in a field of 8 characters with whitespace
  // padding the left.  Java's String.format() is useful for padding
  // on the left.
  public String toString()
  {
  	  //The record is converted into a string 
  	  //	ex: "	entity relation property"
  	  StringBuilder sb = new StringBuilder();
  	  String e = String.format("%8s",this.entity);
  	  sb.append(e+" ");
  	  String r = String.format("%8s", this.relation);
  	  sb.append(r+ " ");
  	  String p = String.format("%8s", this.property);
  	  sb.append(p+ " ");
  	  return sb.toString();
  }

  // Return true if this Record matches the parameter record r and
  // false otherwise. Two records match if all their fields match.
  // Two fields match if the fields are identical or at least one of
  // the fields is wild.
  public boolean matches(Record r)
  { 	  
  	  //It is immediatly checked if if the entity, relation, and propery in
  	  //	this record and in record r are the exact same string.
  	  if(this.entity.equals(r.entity) &&
			this.relation.equals(r.relation) &&
			this.property.equals(r.property))
			return true;
			
	//--------------------------------------------------------------------------		
		//If the entity, relation, and/or property don't match, we check
		//	for wild cards. A wild card matches anything.
		boolean wilde = r.entityWild();									
		boolean wildr = r.relationWild();
		boolean wildp = r.propertyWild();								
			
		//if all cards are wild, then the two records match completely.
		if(wilde==true && wildr==true && wildp==true)
		{
			return true;
		}
		
		//otherwise, we must check which cards are wild and which ones are 
		//	not. The non-wild cards must be compared
		if(wilde==true)
		{
			//if r is *,*,p
			if(wildr==true && this.property.equals(r.property))
				return true;
			//if r is *,r,*
			if(wildp==true && this.relation.equals(r.relation))
				return true;
			//if r is *,r,p
			if(this.property.equals(r.property) &&
				this.relation.equals(r.relation))
				return true;
		}
		
		if(wildr==true)
		{
			//if r is e,*,*
			if(wildp==true && this.entity.equals(r.entity))
				return true;
			//if r is e,*,p
			if(this.property.equals(r.property) &&
				this.entity.equals(r.entity))
				return true;
		}
		
		if(wildp==true)
		{
			//if r is e,r,*
			if(this.relation.equals(r.relation) &&
				this.entity.equals(r.entity))
				return true;
		}
	//--------------------------------------------------------------------------
		//Now we much check if this record has wilds
		wilde = entityWild();									
		wildr = relationWild();
		wildp = propertyWild();							
			
		//if all cards are wild, then the two records match completely.
		if(wilde==true && wildr==true && wildp==true)
		{
			return true;
		}
		
		//otherwise, we must check which cards are wild and which ones are 
		//	not. The non-wild cards must be compared
		if(wilde==true)
		{
			//if this record is *,*,p
			if(wildr==true && this.property.equals(r.property))
				return true;
			//if this record is *,r,*
			if(wildp==true && this.relation.equals(r.relation))
				return true;
			//if this record is *,r,p
			if(this.property.equals(r.property) &&
				this.relation.equals(r.relation))
				return true;
		}
		
		if(wildr==true)
		{
			//if this record is e,*,*
			if(wildp==true && this.entity.equals(r.entity))
				return true;
			//if this record is e,*,p
			if(this.property.equals(r.property) &&
				this.entity.equals(r.entity))
				return true;
		}
		
		if(wildp==true)
		{
			//if this record is e,r,*
			if(this.relation.equals(r.relation) &&
				this.entity.equals(r.entity))
				return true;
		}
		
	//If none of the tests pass, then the two records don't match
		return false;
  }
  
  // Return this record's ID
  public int id() 
  {
  	  return this.id;
  }
  // Accessor methods to access the 3 main fields of the record:
  // entity, relation, and property.
  public String entity()
  {
  	  return this.entity;
  }

  public String relation()
  {
  	  return this.relation;
  }

  public String property()
  {
  	  return this.property;
  }

  // Returns true/false based on whether the the three fields are
  // fixed or wild.
  public boolean entityWild()
  {
  	  return this.eWild;
  }

  public boolean relationWild()
  {
  	  return this.rWild;
  }

  public boolean propertyWild()
  {
  	  return this.pWild;
  }
  
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//The following classes define how records are to be compared when determining
//	their position in TripleStore trees

	//This class orders records based on entity, then relation, and finally
	//	property. The sole method returns a value that represents how the 
	//	records should be ordered in relation to each other.
	static class CompERP implements Comparator<Record>
	{
		public int compare(Record r1, Record r2)
		{
			//first checks entity
			//c represents whether one record is "more" or "less than" another
			//	depending on their cards
			int c = 0;
			c = r1.entity.compareTo(r2.entity());
			//then checks if entity is a wildcard
			if(r1.entityWild())
				c = -1;
			if(r2.entityWild())
				c = 1;
			if(r1.entityWild()&&r2.entityWild())
				c=0;
			//if c = 0, it continues to check the others until it finds a value
			if(c==0)
			{
				c = r1.relation.compareTo(r2.relation());
				if(r1.relationWild())
					c = -1;
				if(r2.relationWild())
					c = 1;
				if(r1.relationWild()&&r2.relationWild())
					c=0;
			}
			if(c==0)
			{
				c = r1.property.compareTo(r2.property());
				if(r1.propertyWild())
					c = -1;
				if(r2.propertyWild())
					c = 1;
			}
			return c;
		}
	}
	
	//This class orders records based on relation, then property, and finally
	//	entity. The sole method returns a value that represents how the 
	//	records should be ordered in relation to each other.
	static class CompRPE implements Comparator<Record>
	{
		public int compare(Record r1, Record r2)
		{
			//first checks relation
			//c represents whether one record is "more" or "less than" another
			//	depending on their cards
			int c = 0;
			c = r1.relation.compareTo(r2.relation());
			//then checks to see if relation is a wildcard
			if(r1.relationWild())
				c = -1;
			if(r2.relationWild())
				c = 1;
			if(r1.relationWild()&&r2.relationWild())
				c=0;
			//if the relations are equal, then the properties are compared
			if(c==0)
			{
				c = r1.property.compareTo(r2.property());
				if(r1.propertyWild())
					c = -1;
				if(r2.propertyWild())
					c = 1;
				if(r1.propertyWild()&&r2.propertyWild())
					c=0;
			}
			//if the properties are equal, then the entities are compared
			if(c==0)
			{
				c = r1.entity.compareTo(r2.entity());
				if(r1.entityWild())
					c = -1;
				if(r2.entityWild())
					c = 1;
			}
			return c;
		}
	}
	
	//This class orders records based on property, then entity, and finally
	//	relation. The sole method returns a value that represents how the 
	//	records should be ordered in relation to each other.
	static class CompPER implements Comparator<Record>
	{
		public int compare(Record r1, Record r2)
		{	
			//first checks property
			//c represents whether one record is "more" or "less than" another
			//	depending on their cards
			int c = 0;
			c = r1.property.compareTo(r2.property());
			//then checks if property is the wildcard
			if(r1.propertyWild())
				c = -1;
			if(r2.propertyWild())
				c = 1;
			if(r1.propertyWild()&&r2.propertyWild())
				c=0;
			//if the properties are equal, then the entities are compared
			if(c==0)
			{
				c = r1.entity.compareTo(r2.entity());
				if(r1.entityWild())
					c = -1;
				if(r2.entityWild())
					c = 1;
				if(r1.entityWild()&&r2.entityWild())
					c=0;
			}
			//if the entities are equal, then the relations are compared
			if(c==0)
			{
				c = r1.relation.compareTo(r2.relation());
				if(r1.relationWild())
					c = -1;
				if(r2.relationWild())
					c = 1;
			}
			return c;
		}
	}
	//these are the object that allow odered trees to be created in TripleStore
	public static final Comparator<Record> ERPCompare = new CompERP();
	public static final Comparator<Record> RPECompare = new CompRPE();
	public static final Comparator<Record> PERCompare = new CompPER();

}
