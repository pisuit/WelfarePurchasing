package welfare.utils;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.StringTokenizer;

import javax.servlet.jsp.SkipPageException;
import javax.swing.JFileChooser;

import welfare.persistent.controller.MatController;
import welfare.persistent.customtype.Status;
import welfare.persistent.domain.stock.Material;
import welfare.persistent.domain.stock.MaterialGroup;
import welfare.persistent.exception.ControllerException;

import com.csvreader.CsvReader;

public class ImportData {
	public void importMaterialData(){
		try {
			MatController matController = new MatController();
			JFileChooser chooser = new JFileChooser();
			
			chooser.showOpenDialog(null);
			File file = chooser.getSelectedFile();
			
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringTokenizer tokenizer = null;
			String stringLine = "";
			int firstLine = 1;
			
			while((stringLine = br.readLine()) != null) {
				if(firstLine == 1) {
					firstLine++;
					continue;				
				}						
				
				int counter = 0;
				String[] token = new String[12];
				tokenizer = new StringTokenizer(stringLine, ",");
				
				while(tokenizer.hasMoreTokens()) {
					token[counter] = tokenizer.nextToken();
					counter++;
				}
				Material material = new Material();
				MaterialGroup materialGroup = new MaterialGroup();
				materialGroup.setId(Long.valueOf(token[10]));
				
				material.setCode(token[0]);
				material.setDescription(token[1]);
				material.setIssueUnit(token[2]);
				material.setOrderUnit(token[3]);
				material.setUnitConverter(Double.valueOf(token[4]));
				material.setOrderUnitPrice(new BigDecimal(token[5]));
				material.setMaxStock(Double.valueOf(token[6]));
				material.setMinStock(Double.valueOf(token[7]));
				material.setWarehouseCode(token[8]);
				material.setStatus(Status.find(token[9]));
				material.setMaterialGroup(materialGroup);
				
				matController.saveMaterial(material);						
			}		
		} catch (IOException e) {
			e.printStackTrace();
			// TODO: handle exception
		} catch (ControllerException e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
}
