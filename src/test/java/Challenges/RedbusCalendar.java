package Challenges;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RedbusCalendar {
	
	static WebDriver driver;

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(new FirefoxProfile());
		options.addPreference("dom.webnotifications.enabled", false);
		driver = new FirefoxDriver(options);
		driver.get("https://www.redbus.in");
		
		List <String> weekendDates = getWeekendDates("May 2025");
		System.out.println(weekendDates);
	}
	
	public static String[] getMonthYear(String txt) {
		return txt.split("\n")[0].split(" ");
	}

	public static List<String> getWeekendDates(String monthYear) {
		By datePicker = By.cssSelector(".dateText");
		By monthHoliday = By.cssSelector(".fxvMrr div:nth-child(2)");
		By next = By.cssSelector(".fxvMrr > div:nth-child(3) > svg:nth-child(1)");
		
//		By monthHoliday = By.xpath("//div[contains(@class,'CalendarHeader')]//div[2]");
//		By next = By.xpath("//div[contains(@class,'CalendarHeader')]//div[3]/*[local-name()=\"svg\"]");		
		
		driver.findElement(datePicker).click();
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
		String txt = wait.until(ExpectedConditions.visibilityOfElementLocated(monthHoliday)).getText();
		System.out.println(txt);
		
		while(!(getMonthYear(txt)[0].equals(getMonthYear(monthYear)[0]) && getMonthYear(txt)[1].equals(getMonthYear(monthYear)[1]))) {
			driver.findElement(next).click();
			txt = wait.until(ExpectedConditions.visibilityOfElementLocated(monthHoliday)).getText();
			System.out.println(txt);
		}
		
		return getWeekendWithCss();		
	}
	
	public static List<String> getWeekendWithCss() {		
		
		By weekend = By.cssSelector("span.bwoYtA,span.fgdqFw");
		List<String> weekendDates = new ArrayList<>();
		for(WebElement e: driver.findElements(weekend)) {
			weekendDates.add(e.getText());
		}
		
		return weekendDates;
	}
	
public static void getWeekendWithXpath() {		
		
		By sat = By.xpath("//div[contains(@class,'DayTiles__CalendarDays')][6]/span[not(contains(@class,'hVMWpe') or contains(@class,'gigHYE'))]");
		By sun = By.xpath("//div[contains(@class,'DayTiles__CalendarDays')][7]/span[not(contains(@class,'hVMWpe') or contains(@class,'gigHYE'))]");
		
		List<WebElement> sat_list = driver.findElements(sat);
		List<WebElement> sun_list = driver.findElements(sun);
		
		List<Integer> weekendDates = new ArrayList<>();
		for(WebElement e: sat_list) {
			weekendDates.add(Integer.parseInt(e.getText()));
		}
		for(WebElement e: sun_list) {
			weekendDates.add(Integer.parseInt(e.getText()));
		}
		Collections.sort(weekendDates);		
		System.out.println(weekendDates);
	}

}
