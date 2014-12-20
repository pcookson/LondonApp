/*
 * parse given excel file, get the value of the columns needed and save into table facility
 */

package ca.uwo.CityLondon;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class ExcelReader {

	/*
	 * parse given excel file and store each facility information into table
	 * facility parm: category name, excel file
	 */

	public static void parseFacilityDetail(String category, File file) {

		// information needed to be saved to database
		int nameIndex = 0;
		int addressIndex = 0;
		int xcoordIndex = 0;
		int ycoordIndex = 0;
		int busStpoIndex = 0;
		int latIndex = 0;
		int lonIndex = 0;
		int stopNameIndex = 0;
		int busIndex = 0;
		boolean busTableIndex = false;
		int cellType = 0;

		try {
			// load given excel file to work book
			InputStream input = new FileInputStream(file);
			POIFSFileSystem fs = new POIFSFileSystem(input);
			HSSFWorkbook wb = new HSSFWorkbook(fs);
			// first sheet only
			HSSFSheet sheet = wb.getSheetAt(0);

			// new facility data structure
			FacilityDataStructure ds = new FacilityDataStructure();

			// format category name (excel file name)
			ds.category = category;
			if (category.equals("London Transit Bus Stops (Sept 2011)"))
				busTableIndex = true;

			// new mysql object
			MySql ms = new MySql();

			// SQL statement
			String sqlInsertFacility = "";
			String sqlUpdateFacility = "";

			// Iterate over each row in the sheet
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {

				HSSFRow row = (HSSFRow) rows.next();


				// Iterate over each cell in the row
				Iterator<Cell> cells = row.cellIterator();

				while (cells.hasNext()) {
					HSSFCell cell = (HSSFCell) cells.next();


					// for column name row, get the index of the columns needed
					if (row.getRowNum() == 0) {
						if (busTableIndex) {
							switch (cell.getStringCellValue()) {
							case "My Bus Info Stop Number":
								busStpoIndex = cell.getColumnIndex();
								break;
							case "Latitude":
								latIndex = cell.getColumnIndex();
								break;
							case "Longitude":
								lonIndex = cell.getColumnIndex();
								break;
							case "Stop Name":
								stopNameIndex = cell.getColumnIndex();
								break;
							case "Routes Servicing the Stop":
								busIndex = cell.getColumnIndex();
								break;
							default:
								break;
							}
						} else {
							switch (cell.getStringCellValue()) {
							case "NAME":
								nameIndex = cell.getColumnIndex();
								break;
							case "ADDRESS":
								addressIndex = cell.getColumnIndex();
								break;
							case "Xcoord":
								xcoordIndex = cell.getColumnIndex();
								break;
							case "Ycoord":
								ycoordIndex = cell.getColumnIndex();
								break;
							default:
								break;
							}
						}
					}

					// non-column name rows
					else {

						if (busTableIndex) {

							if (cell.getColumnIndex() == busStpoIndex) {
								cellType = cell.getCellType();
								if (cellType == 1) {// String
									String stopNumberStr = cell
											.getStringCellValue();
									ds.stopNumber = Double
											.parseDouble(stopNumberStr);
								} else
									ds.stopNumber = cell.getNumericCellValue();
							} else if (cell.getColumnIndex() == latIndex) {
								ds.ycoord = cell.getNumericCellValue();
							} else if (cell.getColumnIndex() == lonIndex) {
								ds.xcoord = cell.getNumericCellValue();
							} else if (cell.getColumnIndex() == stopNameIndex) {

								ds.stopName = cell.getStringCellValue();
							} else if (cell.getColumnIndex() == busIndex) {
								cellType = cell.getCellType();
								if (cellType == 0) {// Number
									double busNum = cell.getNumericCellValue();
									ds.bus = String.valueOf(busNum);
								} else
									ds.bus = cell.getStringCellValue();
							}
						}

						else {


							// get excel value (1 cell only) and format facility
							// data structure
							if (cell.getColumnIndex() == nameIndex) {
								// check if ' exist
								if (cell.getStringCellValue().indexOf("'") != -1) {
									ds.name = cell.getStringCellValue()
											.replace("'", "''");
								} else
									ds.name = cell.getStringCellValue();
							} else if (cell.getColumnIndex() == addressIndex) {
								ds.address = cell.getStringCellValue();
							} else if (cell.getColumnIndex() == xcoordIndex) {
								ds.xcoord = cell.getNumericCellValue();
							} else if (cell.getColumnIndex() == ycoordIndex) {
								ds.ycoord = cell.getNumericCellValue();
							}

						}

					}
				}

				// insert facility data structure into table facility

				if (row.getRowNum() > 0 && busTableIndex && (ds.stopNumber != 0) || row.getRowNum() > 0 && !busTableIndex) {

					// initiate connection
					ms.intMySql();

					if (busTableIndex) {
						// reference output
						System.out.println(ds.stopNumber + "\n" + ds.stopName
								+ "\n" + ds.bus + "\n" + +ds.xcoord + "\n"
								+ ds.ycoord);
						// check if facility already existed (QUERY)
						int found = 0;
						found = ms.searchBus(ds.stopNumber);

						if (found == 0) {
							// initiate connection
							ms.intMySql();
							// insert facility if not found (INSERT)
							sqlInsertFacility = "INSERT INTO bus values('"
									+ ds.stopNumber + "','" + ds.stopName
									+ "','" + ds.bus + "','" + ds.xcoord
									+ "', '" + ds.ycoord + "')";
							ms.datatoMySql(sqlInsertFacility);
						} else {
							// initiate connection
							ms.intMySql();
							// update facility information if found (UPDATE)
							sqlUpdateFacility = "UPDATE bus SET stop_name =  '"
									+ ds.stopName + "', bus = '" + ds.bus
									+ "',xcoord = '" + ds.xcoord
									+ "', ycoord = '" + ds.ycoord
									+ "' WHERE stop_number = '" + ds.stopNumber
									+ "';";
							ms.updateMySql(sqlUpdateFacility);
						}
					}

					else {
						// check if facility already existed (QUERY)

						// reference output
						System.out.println(ds.name + "\n" + ds.address + "\n"
								+ ds.xcoord + "\n" + ds.ycoord);
						int found = 0;
						found = ms.searchMySqlFacility(ds.category, ds.name);

						if (found == 0) {
							// initiate connection
							ms.intMySql();
							// insert facility if not found (INSERT)
							sqlInsertFacility = "INSERT INTO facility values('"
									+ ds.category + "','" + ds.name + "','"
									+ ds.address + "','" + ds.xcoord + "', '"
									+ ds.ycoord + "')";
							ms.datatoMySql(sqlInsertFacility);
						} else {
							// initiate connection
							ms.intMySql();
							// update facility information if found (UPDATE)
							sqlUpdateFacility = "UPDATE facility SET address =  '"
									+ ds.address
									+ "', xcoord = '"
									+ ds.xcoord
									+ "', ycoord = '"
									+ ds.ycoord
									+ "' WHERE category = '"
									+ ds.category
									+ "' AND name = '" + ds.name + "';";
							ms.updateMySql(sqlUpdateFacility);
						}
					}

				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}


}