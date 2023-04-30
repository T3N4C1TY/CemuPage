package cemuHomepage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.*;
import java.util.List;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.io.FileHandler;

public class Run1 {

	public static void main(String[] args) throws IOException {
		
		//System.setProperty("webdriver.chrome.driver", "./driver/chromedriver112.exe");
		
		//ChromeDriver driver = new ChromeDriver();
		FirefoxDriver driver = new FirefoxDriver();
		//nav to cemu homepage
		driver.get("http://cemu.info/");
		//take ss of home page
		int sscount = 0;
		File snap1 = driver.getScreenshotAs(OutputType.FILE);
		File dest1 = new File("C:\\Users\\Barathraj\\Desktop\\JAVA KT\\CemuSS\\img"+sscount+".png");
		FileHandler.copy(snap1, dest1);
		sscount++;
		String title = driver.findElementByXPath("//div[@class=\"jumbotron\"]/h1").getText();
		String para = driver.findElementByXPath("//div[@class=\"jumbotron\"]/p").getText();
		System.out.println(title);
		System.out.println(para);
		
		//sys req check
		System.out.println("*******************************************************");
		System.out.println("System requirement check ? Y/N");
		Scanner sc = new Scanner(System.in);
		String a = sc.nextLine();
		if (a.equals("Y")||a.equals("y")) {
			System.out.println("yes");
			String req = driver.findElementByXPath("//p[contains(.,\"Microsoft Visual\")]").getText();
			System.out.println(req);
			int minRAM =Integer.parseInt(req.substring(55, 56));
			int maxRAM =Integer.parseInt(req.substring(69, 70)); 
			System.out.println("please enter your system specification - ");
			System.out.println("Enter system ram size (in GB)");
			int ramSize = sc.nextInt(); 
			if (ramSize>=minRAM && ramSize<maxRAM) {
				System.out.println("Enough to run CEMU !!");
			}else if (ramSize<minRAM) {
				System.out.println("Not enough to run CEMU :(");
			}else if (ramSize>=maxRAM) {
				System.out.println("More than enough to run CEMU :)");
			}
			
		}
		sc.close();
		driver.findElementByXPath("//a[.=\"Compatibility\"]").click();
		String rating = driver.findElementByXPath("//div[@class=\"ratings-expl\"]").getText();
		System.out.println(rating);
		File snap2 = driver.getScreenshotAs(OutputType.FILE);
		File dest2 = new File("C:\\Users\\Barathraj\\Desktop\\JAVA KT\\CemuSS\\img"+sscount+".png");
		FileHandler.copy(snap2, dest2);
		//time to fetch 1207 records
		Instant inst1 = Instant.now();
		
		WebElement Table = driver.findElementByClassName("compat-list");
		List<WebElement> headers = Table.findElements(By.xpath("//table/thead/tr"));
		for (WebElement webElement : headers) {
			System.out.println(webElement.getText());
		}
		System.out.println("*************************************");
		int size1 = Table.findElements(By.cssSelector("tbody tr")).size();
		System.out.println(size1);
		String datas[][] = new String[size1][4];
		List<WebElement> allrows = Table.findElements(By.cssSelector("tbody tr"));
		for (int i = 0; i < allrows.size(); i++) {
			List<WebElement> coln = allrows.get(i).findElements(By.tagName("td"));
			//System.out.println(coln.get(0).getText()+"=======>>>"+coln.get(4).findElement(By.tagName("img")).getAttribute("title"));
			datas[i][0]= coln.get(0).getText();
			datas[i][1]= coln.get(2).getText();
			datas[i][2]= coln.get(3).getText();
			datas[i][3]= coln.get(4).findElement(By.tagName("img")).getAttribute("title");
			
		}
		Instant inst2 = Instant.now();
//		for (int i = 0; i < size1; i++) {
//			for (int j = 0; j < 4; j++) {
//				System.out.println(datas[i][j]);
//			}
//		} 
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet();
		int row_len= datas.length;
		int col_len=datas[0].length;
		for (int i = 0; i < row_len; i++) {
			HSSFRow n_row = sheet.createRow(i);
			for (int j = 0; j < col_len; j++) {
				HSSFCell cell = n_row.createCell(j);
				String value = datas[i][j];
				cell.setCellValue((String)value);
				
			}
		}
		String fileloca = "./EXCELDATA/CemuGameCompatibilityList.xls";
		FileOutputStream fileOutputStream = new FileOutputStream(fileloca);
		wb.write(fileOutputStream);
		wb.close();
		
		System.out.println("*****************************************");
		System.out.println(Duration.between(inst1, inst2));
		System.out.println("*****************************************");
		System.out.println("Table data written in EXCEL. path => C:\\Users\\Barathraj\\eclipse-workspace\\Cemupage\\EXCELDATA");
		
	}

}
