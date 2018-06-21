import java.util.*;
import java.lang.*;
import java.util.TreeSet;
import java.util.Comparator;

// Three-column database that supports query, add, and remove in
// logarithmic time.
public class TripleStore{
//Three treeSets store records based on the three different comparators.
//	This makes it easier to find a record when you can traverse three
//	trees that have records stored in different orders, unlike searching
//	through just one tree in one order.
	TreeSet<Record> ERP;
	TreeSet<Record> RPE;
	TreeSet<Record> PER;
	private String wild;
	
	
  // Create an empty TripleStore. Initializes storage trees
  public TripleStore()
  {
  	  //each tree is assigned its proper comparator that it will use
  	  //	to sort the records. 
  	  	this.ERP = new TreeSet<Record>(Record.ERPCompare);
		this.RPE = new TreeSet<Record>(Record.RPECompare);
		this.PER = new TreeSet<Record>(Record.PERCompare);	
		
		this.wild = "*";
  }

  // Access the current wild card string for this TripleStore which
  // may be used to match multiple records during a query() or
  // remove() calll
  public String getWild()
  {
  	  return this.wild;
  }

  // Set the current wild card string for this TripleStore
  public void setWild(String w)
  {
  	  this.wild = w;
  }

  // Ensure that a record is present in the TripleStore by adding it
  // if necessary.  Returns true if the addition is made, false if the
  // Record was not added because it was a duplicate of an existing
  // entry. A Record with any fields may be added to the TripleStore
  // including a Record with fields that are equal to the
  // TripleStore's current wild card.
  // 
  // Target Complexity: O(log N)
  // N: number of records in the TripleStore
  public boolean add(String entity, String relation, String property)
  {
  	  	if((entity==null)||(relation==null)||(property==null))
		{
			return false;
		}
		
		//a new record is made		
		Record temp = Record.makeRecord(entity, relation, property);
		
		//if record already exists, don't add the element
		if(this.ERP.contains(temp))
			return false;
		
		//otherwise, the new record is added, with the treeset
		//	doing all of the sorting for us.
		this.ERP.add(temp);
		this.RPE.add(temp);
		this.PER.add(temp);
		
		return true;
  }

  // Return a List of the Records that match the given query. If no
  // Records match, the returned list should be empty. If a String
  // matching the TripleStore's current wild card is used for one of
  // the fields of the query, multiple Records may be returned in the
  // match.  An appropriate tree must be selected and searched
  // correctly in order to meet the target complexity.
  // 
  // TARGET COMPLEXITY: O(K + log N) 
  // K: the number of matching records 
  // N: the number of records in the triplestore.
  public List<Record> query(String entity, String relation, String property)
  {
		//matches keeps track of the matching Records
		ArrayList<Record> matches = new ArrayList<Record>();
		//navi is the iterator that will navigate through the chosen tree
		Iterator<Record> navi;
		//priority keeps track of which tree is being used
		String priority;
		//pavi grabs each record for reading as navi moves through the tree
		Record pavi;
		//low is the main record that pavi will be compared to each time 
		//	pavi changes.
		Record low = Record.makeQuery(this.wild, entity, relation, property);
		
		//sees if any arguments feature the wild card
		boolean ew = entity.equals(this.wild);
		boolean rw = relation.equals(this.wild);
		boolean pw = property.equals(this.wild);
		
		//makes an iterator based on which tree would be more efficient
		if(ew)
		{
			if(rw)
			{
				//if entity and relation are wild, then we depend on property,
				//	so we use the tree that starts with the property card
				navi = this.PER.iterator();
				priority = "per";
			}
			else
			{
				//if relation isn't wild, we use the tree that's sorted 
				//	with relation as priority. Property can be wild or not.
				navi = this.RPE.iterator();
				priority = "rpe";
			}
		}
		else
		{
			//otherwise, since entity isn't wild, we start with that as the
			//	priority card, whether relation or property are wild or not.
			navi = this.ERP.iterator();
			priority = "erp";
		}
		

		//we move through the tree and collect matching records
		while(navi.hasNext())
		{
			//pavi becomes the record the iterator is currently on.
			pavi = navi.next();
			//pavi is compared to low, and if they match, the record is added 
			//	to the matches list.
			if(pavi.matches(low))
			{
					matches.add(pavi);
			}
		}
		
		//after the tree has been searched through, the list is returned.
		return matches;
  }

  // Remove elements from the TripleStore that match the parameter
  // query. If no Records match, no Records are removed.  Any of the
  // fields given may be the TripleStore's current wild card which may
  // lead to multiple Records bein matched and removed. Return the
  // number of records that are removed from the TripleStore.
  // 
  // TARGET COMPLEXITY: O(K * log N)
  // K: the number of matching records 
  // N: the number of records in the triplestore.
  public int remove(String e, String r, String p)
  {
  	  	//found tracks the number of records that matched and were removed
  	  	int found = 0;
  	  	//priority keeps track of which tree is being used
		String priority;
		//navi is the iterator that will move through the tree
		Iterator<Record> navi;
		//low is the record that pavi is compared to each time pavi changes
		Record low = Record.makeQuery(this.wild, e, r, p);
		//pavi grabs the records as the iterator moved through the list.
		Record pavi;
		ArrayList<Record> arl = new ArrayList<Record>();
		//sees if any arguments feature the wild card
		boolean ew = e.equals(this.wild);
		boolean rw = r.equals(this.wild);
		boolean pw = p.equals(this.wild);
			

		//checks if any of the arguments are wild cards
		//makes an iterator based on which tree would be more efficient
		if(ew)
		{
			if(rw)
			{
				navi = this.PER.iterator();
				priority = "per";
			}
			else
			{
				navi = this.RPE.iterator();
				priority = "rpe";
			}
		}
		else
		{
			navi = this.ERP.iterator();
			priority = "erp";
		}	


		//we move through the tree and collect matching records
		while(navi.hasNext())
		{
			pavi = navi.next();
			if(pavi.matches(low))
			{
				if(priority.equals("erp"))
				{
					arl.add(pavi);
					PER.remove(pavi);
					RPE.remove(pavi);
					navi.remove();
					found++;
				}
				if(priority.equals("rpe"))
				{
					arl.add(pavi);
					ERP.remove(pavi);
					PER.remove(pavi);
					navi.remove();
					found++;
				}
				if(priority.equals("per"))
				{
					arl.add(pavi);
					ERP.remove(pavi);
					RPE.remove(pavi);
					navi.remove();
					found++;
				}
			}
		}
	
		
		return found;
  }

  // Produce a String representation of the TripleStore. Each Record
  // is formatted with its toString() method on its own line. Records
  // must be shown sorted by Entity, Relation, Property in the
  // returned String. 
  // 
  // TARGET COMPLEXITY: O(N)
  // N: the number of records stored in the TripleStore
  public String toString()
  {
  	  //grabs the iterator to move through the tree
		Iterator<Record> navi = this.ERP.iterator();
		
		//collects the strings...
		StringBuilder sb = new StringBuilder();

		//...as navi moves through the tree
		while(navi.hasNext())
		{
			sb.append(navi.next().toString());
			sb.append("\n");
		}
		
		//the stringbuilder is converted to a sting and returned
		return sb.toString();
  }

}
