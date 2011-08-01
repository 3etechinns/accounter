/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.Set;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientMeasurement implements IAccounterCore {

	private ClientUnit defaultUnit;

	private String desctiption;

	private long id;

	private String name;
	private Set<ClientUnit> units;

	/**
	 * @return the defaultUnit
	 */
	public ClientUnit getDefaultUnit() {
		return defaultUnit;
	}

	/**
	 * @param defaultUnit
	 *            the defaultUnit to set
	 */
	public void setDefaultUnit(ClientUnit defaultUnit) {
		this.defaultUnit = defaultUnit;
	}

	/**
	 * @return the desctiption
	 */
	public String getDesctiption() {
		return desctiption;
	}

	/**
	 * @param desctiption
	 *            the desctiption to set
	 */
	public void setDesctiption(String desctiption) {
		this.desctiption = desctiption;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the units
	 */
	public Set<ClientUnit> getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            the units to set
	 */
	public void setUnits(Set<ClientUnit> units) {
		this.units = units;
	}

	/**
	 * Gives factor for converting given measure to default measure.
	 * 
	 * @param fromMeasure
	 * @return
	 */
	public double getConversionFactor(String fromMeasure) {
		return getConversionFactor(fromMeasure, defaultUnit.getType());
	}

	/**
	 * This will give you the factor to convert from type to another.
	 * 
	 * <pre>
	 *  result = fromMeasureFactor/toMeasureFactor.
	 * </pre>
	 * 
	 * @param fromMeasure
	 * @param toMeasure
	 * @return
	 */
	public double getConversionFactor(String fromUnit, String toUnit) {
		return getFactor(fromUnit) / getFactor(toUnit);
	}

	/**
	 * 
	 * @param unitType
	 * @return
	 * @exception IllegalArgumentException
	 *                if unitType not found.
	 */
	public double getFactor(String unitType) {

		for (ClientUnit unit : units) {
			if (unit.getType().equals(unitType)) {
				return unit.getFactor();
			}
		}

		throw new IllegalArgumentException(
				"Specified unit type not found in measure.");
	}

	/**
	 * 
	 * NOTE: the 'factor' value should be pre-calculated to default measure [or]
	 * all factor values of this 'Unit' are should be calculated in one measure.
	 * 
	 * @param measureType
	 * @param factor
	 */
	public void addUnit(String unitType, double factor) {
		ClientUnit unit = new ClientUnit(unitType, factor);
		unit.setMeasurement(this);
		units.add(unit);
	}

	@Override
	public String getDisplayName() {
		return this.name;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.MEASUREMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public String getClientClassSimpleName() {
		return "ClientMeasurement";
	}

}
