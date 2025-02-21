package com.AuctionApp.Auction.Component;

import com.AuctionApp.Auction.ExceptionHandling.CustomException;
import com.AuctionApp.Auction.Services.CategoryService;
import com.AuctionApp.Auction.entites.Category;
import com.AuctionApp.Auction.entites.Player;
import com.AuctionApp.Auction.repositories.CategoryRepository;
import com.AuctionApp.Auction.util.Generate;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Service
public class ExcelHelper {

        private final List<Category> categories;



        public ExcelHelper(List<Category> categories){
            this.categories = categories;
        }




        public static boolean checkExcelFormat(MultipartFile file){

            String contentType = file.getContentType();
            assert contentType != null;
            return contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        }



        public  List<Player> convertExcelToListOfPlayers(InputStream inputStream){
                List<Player> playerList = new ArrayList<>();

                try{
                  XSSFWorkbook workbook =  new XSSFWorkbook(inputStream);

                    XSSFSheet sheet = workbook.getSheet("Sheet1");


                    Iterator<Row> rows = sheet.iterator();

                    if (!rows.hasNext()) return playerList;

                    int rowNumber = 0;

                    while(rows.hasNext()){
                        Row row = rows.next();

                        if(rowNumber == 0){
                            rowNumber++;
                            continue;
                        }

                        Iterator<Cell> cells = row.iterator();
                        Player player = new Player();

//                     this map will store cellNumber and lambda function which execute on each row
                        Map<Integer, Consumer<Cell>> columnMap = new HashMap<>();

                        player.setPlayerId(Generate.generateId());
                        player.setStatus(Status.PENDING);
                        player.setIsUser(null);

                        columnMap.put(0,cell -> player.setFormNo(getSafeNumericValue(cell,"Form No must be Numeric in Excel file")));
                        columnMap.put(1,cell -> player.setPlayerName(getSafeStringValue(cell,"Name must be Text in Excel file")));
                        columnMap.put(2,cell -> player.setMobileNo(String.valueOf(getSafeNumericValue(cell,"mobile must be Numeric in Excel file"))));
                        columnMap.put(3,cell -> player.setPlayerAge(getSafeNumericValue(cell,"Age must be Numeric in Excel file")));
                        columnMap.put(4,cell -> player.setPlayerStyle(getSafeStyle(cell,"Player Style must be from 'BATSMAN','BOWLER','ALL_ROUNDER','BATSMAN_WICKET_KEEPER' in Excel file ")));
                        columnMap.put(5,cell -> player.setCategoryId(getCategory(getSafeNumericValue(cell,"Category should be Numeric in Excel file"))));
                        columnMap.put(6,cell -> player.setTShirtSize(getSafeStringValue(cell,"T-shirt size must be Text in Excel file").toUpperCase()));
                        columnMap.put(7,cell -> player.setTrouserSize(getSafeStringValue(cell,"Trouser size must be Text in Excel file").toUpperCase()));
                        columnMap.put(8,cell -> player.setJersseyNumber(getSafeNumericValue(cell,"Jersey number  must be Numeric in Excel file")));
                        columnMap.put(9,cell -> player.setJersseyName(getSafeStringValue(cell,"Jersey name must be Text in Excel file")));



                        int cellNumber = 0;


                        while(cells.hasNext()){
                            Cell cell = cells.next();
                            if (cell == null || cell.getCellType() == CellType.BLANK) {
                                continue;
                            }

//                          here one by one lambda function executes by cellNumber from columnMap if function not exits default function will execute c -> {};
                            columnMap.getOrDefault(cellNumber,c -> {}).accept(cell);
                            cellNumber++;
                        }

                        playerList.add(player);
                    }


                }catch (Exception e){
                    throw new CustomException(e.getMessage(),HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
                }
                return playerList;
        }

    private Category getCategory(int categoryId) {
                Category category = null ;
                for (Category cate: categories){
                    if(cate.getCategoryId().equals(String.valueOf(categoryId))){
                        category = cate;
                        break;
                    }
                }
                return category;
    }

    public int getSafeNumericValue(Cell cell,String errorMessage){
                if(cell.getCellType() != CellType.NUMERIC){
                    throw new CustomException(errorMessage,HttpStatus.BAD_REQUEST,"Error");
                }
                return (int)cell.getNumericCellValue();
        }

        public String getSafeStringValue(Cell cell,String errorMessage){
            if(cell.getCellType() != CellType.STRING){
                throw new CustomException(errorMessage,HttpStatus.BAD_REQUEST,"Error");

            }
            return cell.getStringCellValue();
        }

        private static boolean containStyle(String value){
            try{
                Style.valueOf(value.toUpperCase());
                return true;
            }catch(Exception e){
                return false;
            }
        }

        public Style getSafeStyle(Cell cell,String errorMessage){
            if(containStyle(cell.getStringCellValue().toUpperCase())){
                return Style.valueOf(cell.getStringCellValue().toUpperCase());
            }else{
                throw new CustomException(errorMessage,HttpStatus.BAD_REQUEST,"ERROR");
            }
        }
}
