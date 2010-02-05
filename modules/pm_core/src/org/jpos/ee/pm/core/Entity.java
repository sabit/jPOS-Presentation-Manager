/*
 * jPOS Project [http://jpos.org]
 * Copyright (C) 2000-2008 Alejandro P. Revilla
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jpos.ee.pm.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**An Entity is the visual representation and operation over a class of a data model. One entity is 
 * configured through an xml file.<br/>
 * <h2>Simple entity configuration file</h2>
 * <pre>
 * {@code
 * <?xml version='1.0' ?>
 * <!DOCTYPE entity SYSTEM "cfg/entity.dtd">
 * <entity>
 *     <id>entity_id</id>
 *     <clazz>the.entity.Class</clazz>
 *     <auditable>false</auditable>
 *     <persistent>true</persistent>
 *     <searchable>true</searchable>
 *     <paginable>true</paginable>
 *     ...
 *     <operations>
 *     ...
 *     </operations>
 *     <field>
 *     ...
 *     </field>
 *     <field>
 *     ...
 *     </field>
 * </entity>
 * }
 * </pre>
 * @author J.Paoletti jeronimo.paoletti@gmail.com
 * */
public class Entity extends PMCoreObject {
    /**Represents the entity id. This must me unique.
     * <br/>{@code <id>entity_id</id>}*/
    private String id;
    
    /**The full name of the class represented by the entity.
     * <br/>{@code <clazz>entity_class</clazz>}*/
    private String clazz;
    
    /**A filter for the list of instances of the entity.
     * <br/>{@code <listfilter class="a.class.implementing.ListFilter" />}
     * @see ListFilter */
    private ListFilter listfilter;
    
    /**If defined, represents the order of the fields. 
     * <br/>{@code <order>field_id2 field_id3 field_id1</order>}
     * @see Field*/
    private String order;
    
    /**If defined, indicates the id of another entity to inherits the fields, not the 
     * operations for now.
     * <br/>{@code <extendz>other_entity_id</extendz>}*/
    private String extendz;
    
    /**The parent entity if extendz is defined*/
    private Entity extendzEntity;
    
    /**Indicates if the entity is persistent. If its not persistent, the PM will not try to post 
     * any info on the database.
     * <br/>{@code <persistent>true</persistent>}*/
    private Boolean persistent;

    /**Indicates if the entity is auditable so every time an instance is modified, the PM will
     * create an auditory entry of the change 
     * <br/>{@code<auditable>true</auditable>}*/
    private Boolean auditable;
    
    /**Defines an owner to the entity. It makes this entity "weak".<br/>
     * {@code
     * <owner>
     *   <entity_id>owner_entity_id</entity_id>
     *   <entity_property>owner entity field id</entity_property>
     *   <local_property>field idto owner</local_property>
     * </owner>}
     * @see EntityOwner*/
    private EntityOwner owner;
    
    /**Internal map to optimize getFieldById() method
     * @see #getFieldById(String)*/
    private Map<String,Field> fieldsbyid;
    
    /**@deprecated*/ 
    private ArrayList<Relation> relations;

    /**Indicates if the interface allows search*/
    private Boolean searchable;
    
    /**Indicates if the interface allows pagination*/
    private Boolean paginable;
    
    /**Operations of the entity. Standard operations are "add", "edit", "delete", "show", "list"
     * but the developer can define whatever he wants.
     * <br/>{@code<operations>...</operations>}
     * @see Operations
     * @see Operation
     * @see OperationContext*/
    private Operations operations;
    
    /**List of fields
     * @see Field*/
    private ArrayList<Field> fields;

    /**Default constructor*/
    public Entity () {
        super();
        fieldsbyid = null;
        extendzEntity  = null;
    }
    
    /**A helper to get the visible fields in list operation. This may me in some support class because 
     * it is "operation specific"
     *  @return The list of visible (ordered) fields for the list
     * */
    public ArrayList<Field> getListableFields(){
    	ArrayList<Field> r = new ArrayList<Field>();
    	for(Field f : getOrderedFields()){
    		if(f.isDisplayInList())r.add(f);
    	}
    	return r;
    }
    
    public ArrayList<Field> getAllFields(){
    	ArrayList<Field> r = new ArrayList<Field>();
    	r.addAll(getFields());
    	if(extendzEntity != null)
    		r.addAll(getExtendzEntity().getAllFields());
    	return r;
    }
    
	/**Getter for a field by its id
     * @param id The Field id
     * @return The Field with the given id*/
    public Field getFieldById(String id){
    	return getFieldsbyid().get(id);
    }

    /**Getter for fieldsbyid. If its null, this methods fill it
     * @return The mapped field list
     * */
	private Map<String, Field> getFieldsbyid() {
		if(fieldsbyid == null){
    		fieldsbyid = new HashMap<String, Field>();
    		for(Field f : getFields()){
    			fieldsbyid.put(f.getId(), f);
    		}    		
    	}
		return fieldsbyid;
	}
    
    /**This method sorts the fields and returns them
     * @return fields ordered
     * */
    public ArrayList<Field> getOrderedFields(){
        try {
            if(isOrdered()){
            	ArrayList<Field> r = new ArrayList<Field>(getAllFields());
	            Collections.sort(r, new FieldComparator(getOrder()));
	            return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getAllFields();
    }

    /**Determine if some of the fields is multieditable
     * @deprecated not implemented yet*/
	public boolean haveMultiEdit(){
        for (Field field : getFields()) {
            if (field.isMultiEditable()) return true;
        }
        return false;
    }
    
	/**Determine if the entity have the order property
	 * @return true if order != null*/
    public boolean isOrdered(){
        return getOrder() != null;
    }

    public String toString() {
        return super.toString() + "[id='" + id + "']";
    }
    
    /**This method fills the extendsFields variable with the parent Fields.
     * If some field is redefined, parent field is ignored
     * @param eentity The parent entity given by PM engine*/
    public void fillFields(Entity eentity) {
        for (Field field : eentity.getAllFields()) {
            if (!containsField(field.getId())) {
                getFields().add(field);
            }
        }
    }
    
    /**Check if there is a Field with an id
     * @param id The field id
     * @return true if the entity contains a Field with the given id*/
    private boolean containsField(String id) {
    	return getFieldById(id) != null;
    }

	/**Getter for id
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**Getter for clazz
	 * @return the clazz
	 */
	public String getClazz() {
		return clazz;
	}

	/**
	 * @param clazz the clazz to set
	 */
	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	/**Getter for listfilter
	 * @return the listfilter
	 */
	public ListFilter getListfilter() {
		return listfilter;
	}

	/**
	 * @param listfilter the listfilter to set
	 */
	public void setListfilter(ListFilter listfilter) {
		this.listfilter = listfilter;
	}

	/**Getter for order
	 * @return the order
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(String order) {
		this.order = order;
	}

	/**The name of the parent entity
	 * @return the extendz
	 */
	public String getExtendz() {
		return extendz;
	}

	/**
	 * @param extendz the extendz to set
	 */
	public void setExtendz(String extendz) {
		this.extendz = extendz;
	}

	/**Indicates if the entity is persistent on database or not.
	 * @return the persistent
	 */
	public boolean isPersistent() {
		if(persistent==null)return true;
		return persistent;
	}

	/**
	 * @param persistent the persistent to set
	 */
	public void setPersistent(boolean persistent) {
		this.persistent = persistent;
	}

	/**Indicates if the entity is auditable or not
	 * @return the auditable
	 */
	public boolean isAuditable() {
		if(persistent==null)return false;
		return auditable;
	}

	/**
	 * @param auditable the auditable to set
	 */
	public void setAuditable(boolean auditable) {
		this.auditable = auditable;
	}

	/**Getter for owner
	 * @return the owner
	 */
	public EntityOwner getOwner() {
		return owner;
	}

	/**
	 * @param owner the owner to set
	 */
	public void setOwner(EntityOwner owner) {
		this.owner = owner;
	}

	/** Getter for entity fields
	 * @return the fields
	 */
	public ArrayList<Field> getFields() {
		return fields;
	}

	/**
	 * @param fields the fields to set
	 */
	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}

	/**Getter for entity relations
	 * @return the relations
	 * @deprecated
	 *
	public ArrayList<Relation> getRelations() {
		return relations;
	}

	/**
	 * @param relations the relations to set
	 * @deprecated
	 *
	public void setRelations(ArrayList<Relation> relations) {
		this.relations = relations;
	}
	 */
	/**Getter for entity operations
	 * @return the operations
	 */
	public Operations getOperations() {
		return operations;
	}

	/**
	 * @param operations the operations to set
	 */
	public void setOperations(Operations operations) {
		this.operations = operations;
	}

	/**
	 * @param extendzEntity the extendzEntity to set
	 */
	public void setExtendzEntity(Entity extendzEntity) {
		this.extendzEntity = extendzEntity;
	}

	/**
	 * @return the extendzEntity
	 */
	public Entity getExtendzEntity() {
		return extendzEntity;
	}

	/**Set also fields, operations and owner service
	 * @see org.jpos.ee.pm.core.PMCoreObject#setService(org.jpos.ee.pm.core.PMService)
	 */
	public void setService(PMService service) {
		super.setService(service);
		if(getOwner()!=null) getOwner().setService(service);
		if(getOperations() != null) getOperations().setService(service);
		if(getFields() != null){
			for(Field f: getFields()) f.setService(service);
		}
	}

	/**
	 * @param searchable the searchable to set
	 */
	public void setSearchable(boolean searchable) {
		this.searchable = searchable;
	}

	/**
	 * @return the searchable
	 */
	public boolean isSearchable() {
		if(searchable==null) return true;
		return searchable;
	}

	/**
	 * @param paginable the paginable to set
	 */
	public void setPaginable(Boolean paginable) {
		this.paginable = paginable;
	}

	/**
	 * @return the paginable
	 */
	public Boolean getPaginable() {
		if(paginable == null) return true;
		return paginable;
	}	
}