package com.ea.product.tools.string2cpp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class String2Cpp {

	private final static String USAGE = "Usage: java -jar string2cpp.jar [srcFileName]." + 
										"For example : java -jar string2cpp.jar ./string_table.txt";
	
	private final static String H_OUTPUT_FILE = "./string_table.h";
	private final static String TXT_OUTPUT_FILE = "./strings.txt";
	
	public static void main(String[] args) {
		
		if(args.length < 1)  {
			usage();
			return;
		}
		long time = System.currentTimeMillis();
		String srcFilePath = args[0];
		
		String2Cpp string2Cpp = new String2Cpp();
		string2Cpp.string2cpp(srcFilePath);
		
		System.out.println("Done:" + (System.currentTimeMillis() - time) + "ms");
	}

	public static void usage() {
		System.out.println(USAGE);
	}
	
	public void string2cpp(String srcFilePath) {
		
        FileWriter hFileWriter;
        FileWriter txtFileWriter;
		try {
			hFileWriter = new FileWriter(H_OUTPUT_FILE,false);
			txtFileWriter = new FileWriter(TXT_OUTPUT_FILE,false);
			
			HashMap<String,String>stringMap = parseFileContent(srcFilePath);
			
			dump(stringMap,hFileWriter,txtFileWriter);
			
			hFileWriter.close();
			txtFileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
        
	}
	
	private void dump(HashMap<String,String>stringMap,FileWriter hFileWriter,FileWriter txtFileWriter) {
		String head = 	"#ifndef __STRING_TABLE_H__\n" + 
						"#define __STRING_TABLE_H__\n\n" + 
						"typedef enum EGlobalStringID\n" + 
						"{\n\t";
		String end = 	"\n}GlobalStringID;\n\n" + 
						"#endif /* defined(__STRING_TABLE_H__) */";
		
		writeContent(hFileWriter,head);
		
		Iterator<String> it = stringMap.keySet().iterator();
		while (it.hasNext()) {
			String stringID = it.next();
	
			writeContent(hFileWriter,stringID + ",\n\t");

			if (!it.hasNext()) {
				writeContent(hFileWriter,"STR_MAX_ID");
			}
			
			writeContent(txtFileWriter,stringMap.get(stringID) + "\n");
		}
		writeContent(hFileWriter,end);
	}
	
	private HashMap<String,String> parseFileContent(String fileName) {
		HashMap<String,String> stringMap = new HashMap<String,String>();
		File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int lineNo = 0;
            while ((tempString = reader.readLine()) != null) {
            		lineNo++;
            		StringItem stringItem = parseLine(lineNo,tempString);
            		if(stringMap.containsKey(stringItem.getStringID())) {
            			throw new Exception("line : " + lineNo + " " + stringItem.getStringID() + " is existed.");
            		}
            		stringMap.put(stringItem.getStringID(), stringItem.getString());
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
			e.printStackTrace();
		} finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        }
        
        return stringMap;
	}
	
	private StringItem parseLine(int lineNo,String lineBuffer) throws Exception {
		lineBuffer = lineBuffer.trim();
		String[] temp = lineBuffer.split("=");
		if(temp.length != 2) {
			throw new Exception("line: " + lineNo + " data format is invalid.");
		} else if(temp[0].trim() == "") {
			throw new Exception("line: " + lineNo + " string id is empty.");
		} else {
			return new StringItem(temp[0].trim(),temp[1].trim());
		}
	}
	
	private void writeContent(FileWriter writer,String content) {
		try {
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
