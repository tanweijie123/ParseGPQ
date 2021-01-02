package project.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import project.logic.Database;
import project.model.Character;
import project.util.Storage;

public class ParseExcel {
    private static List<Character> importCharList = new ArrayList<>();
    private static List<String> uniqueAlias = new ArrayList<>();

    public static void readExcel() throws IOException {
        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream(new File(Config.getConfig().getDownloadedExcelPath())));
        XSSFSheet sheet = wb.getSheetAt(0);
        Iterator<Row> rows = sheet.iterator();
        rows.next(); //remove header

        while (rows.hasNext()) {
            Row currentRow = rows.next();

            //If the IGN field is blank / empty, it will be skipped.
            try {
                String checkNull = currentRow.getCell(1).getStringCellValue().strip();
            } catch (NullPointerException e) {
                continue;
            }

            String ign = currentRow.getCell(1).getStringCellValue().strip();
            int floor = (int) currentRow.getCell(2).getNumericCellValue();
            String job = currentRow.getCell(3).getStringCellValue();
            //job = job.substring(job.indexOf(":") + 1).strip().replace(" ", "").toUpperCase();
            Cell alias = currentRow.getCell(4);

            //check if it is a duplicate ign from above, remove it
            Optional<Character> dup =
                    importCharList.stream().filter(x -> x.getIgn().toUpperCase().equals(ign.toUpperCase())).findFirst();
            dup.ifPresent(x -> {
                importCharList.remove(x);
                uniqueAlias.remove(x.getIgn().toUpperCase());
                uniqueAlias.removeAll(x.getAlias());
            });

            uniqueAlias.add(ign.toUpperCase());
            String[] aliasStr = null;
            if (alias != null) {
                aliasStr = alias.getStringCellValue().split("(\n|/|,)");

                for (String s : aliasStr) {
                    if (s.toUpperCase().equals(ign.toUpperCase())) {
                        //do nothing
                    } else if (uniqueAlias.contains(s.toUpperCase())) {
                        System.out.printf("Alias <%s> is not unique, removed from <%s>\n", s, ign);
                    } else {
                        uniqueAlias.add(s.toUpperCase());
                    }
                }
            }

            Character c = new Character(ign).setJob(job).setFloor(floor).setAlias(aliasStr);
            importCharList.add(c);
        }
    }

    /**
     * Stores both ign and alias into database. both case insensitive (lower case)
     */
    public static void pushToDatabase() {
        for (Character c : importCharList) {
            Database.put(c.getIgn(), c);
            for (String s : c.getAlias())
                Database.put(s, c);
        }
    }
}
