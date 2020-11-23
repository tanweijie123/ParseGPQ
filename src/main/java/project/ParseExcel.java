package project;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import project.model.Character;
import project.util.Storage;

public class ParseExcel {
    private static String data = "data/form.xlsx";
    private static List<Character> importCharList = new ArrayList<>();
    private static List<String> uniqueAlias = new ArrayList<>();

    public static void main(String[] args) {
        try {
            System.out.println("Non Unique Alias (if any) ...");
            readExcel();
            System.out.println(" =========================================== ");
        } catch (IOException e) {
            System.err.println("Unable to read from excel");
        }

        importCharList.stream().sorted(Comparator.comparing(x -> x.getFloor())).forEach(System.out::println);

        Storage database = new Storage("data/parseExcelSuccess.txt");

        try {
            database.saveToFile(importCharList.stream().sorted(Comparator.comparing(x -> x.getIgn())).map(x -> x.export()).collect(Collectors.toList()));
        } catch (IOException e) {
            System.err.println("Unable to save to file...");
        }
    }

    public static void readExcel() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(data)));
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next(); //remove header
        while (rows.hasNext()) {
            Row currentRow = rows.next();
            Cell ign = currentRow.getCell(1);
            Cell floor = currentRow.getCell(2);
            Cell job = currentRow.getCell(3);
            Cell alias = currentRow.getCell(4);

            String jobStr = job.getStringCellValue();
            jobStr = jobStr.substring(jobStr.indexOf(":") + 1).strip().replace(" ", "").toUpperCase();

            String[] aliasStr = null;
            if (alias != null) {
                aliasStr = alias.getStringCellValue().split("(\n|/|,)");

                for (String s : aliasStr) {
                    if (uniqueAlias.contains(s.toUpperCase())) {
                        System.out.printf("Alias <%s> is not unique, removed from <%s>\n", s, ign.getStringCellValue());
                    } else {
                        uniqueAlias.add(s.toUpperCase());
                    }
                }
            }

            Character c = new Character(ign.getStringCellValue(), jobStr, (int) floor.getNumericCellValue(),
                    aliasStr, true);
            importCharList.add(c);
        }
    }

}
