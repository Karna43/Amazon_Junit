package com.amazon;
import java.io.File;
import java.io.FileOutputStream;
import java.time.Duration;
import java.util.List;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

public class TomorrowDeals {
	public static WebDriver driver;
	public static List<WebElement> offers;
	public static List<WebElement> productsInOffer;
	public static WebElement nextSlide;
	public static int currentMethod = 0;
	public static WebDriverWait wait;
	
	@BeforeClass
	public static void browserLaunch() {
		WebDriverManager.edgedriver().setup();
		EdgeOptions options = new EdgeOptions();
		options.addArguments("start-maximized");
		driver = new EdgeDriver(options);
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
		driver.get("https://www.amazon.com/");
	}
	@Before
	public void beforeMethod() {
		currentMethod+=1;
		System.out.println("\nBefore Method: TomorrowDeals.method"+currentMethod);
	}
	@After
	public void afterMethod() {
		System.out.println("After Method: TomorrowDeals.method"+currentMethod);
	}
	@Test
	public void method1() {
		WebElement dismissPopup = driver.findElement(By.xpath("//span[contains(text(),'Dismiss')]/parent::span"));
		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
		wait.until(ExpectedConditions.visibilityOf(dismissPopup));
		dismissPopup.click();
		WebElement tDeals = driver.findElement(By.xpath("//a[contains(text(),'Deals')]"));
		Assert.assertTrue(tDeals.isDisplayed());
		tDeals.click();
	}
	@Test
	public void method2(){
		productsInOffer = driver.findElements(By.xpath("//div[@data-id='TileTitle']"));
		offers = driver.findElements(By.xpath("//div[@data-id='TileTitle']//following::div//span[contains(text(),'off')]"));
		nextSlide = driver.findElement(By.xpath("//div//following::a[contains(@data-id,'carouselControlFeedPrev')]"));
		for(int i=0;i<offers.size();i++) {
			String productName, offerPercentage;
			if(offers.get(i).isDisplayed()) {
				productName = productsInOffer.get(i).getText();
				offerPercentage = offers.get(i).getText();
			}
			else {
				nextSlide.click();
				productName = productsInOffer.get(i).getText();
				offerPercentage = offers.get(i).getText();
			}
			//Assert.assertEquals("Upto 20%", offerPercentage);
			System.out.println("Product: "+productName+"\nOffer Percentage: "+offerPercentage+"\n");
		}
		nextSlide.click();
	}
	@Ignore
	public void method3(){
		File writeFile = new File("C:\\Users\\Karna\\Pictures\\AmazonTodaysOffers.xlsx");
		try {
			FileOutputStream file = new FileOutputStream(writeFile);
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Today_Deals");
			XSSFRow row = sheet.createRow(0);
			XSSFCell cell = row.createCell(0);
			cell.setCellValue("Product Name");
			cell = row.createCell(1);
			cell.setCellValue("Offer Percentage");
			for(int i=0;i<offers.size();i++) {
				row = sheet.createRow(i+1);
				if(offers.get(i).isDisplayed()) {
					for(int j=0;j<2;j++) {
						cell = row.createCell(j);
						if(j==0) {
							cell.setCellValue(productsInOffer.get(i).getText());
						}
						else {
							cell.setCellValue(offers.get(i).getText());
						}
					}
				}
				else {
					nextSlide.click();
					i-=1;
				}
			}
			workbook.write(file);
			workbook.close();
			file.close();
			System.out.println("Amazon Today's Deals Data updated in Excel");
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	@AfterClass
	public static void browserClose() {
		driver.quit();
	}
}
