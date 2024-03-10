package Challenges;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
	
	private static By datePicker = By.cssSelector(".dateText");
	private static By monthHoliday = By.cssSelector(".fxvMrr div:nth-child(2)");
	private static By next = By.cssSelector(".fxvMrr > div:nth-child(3) > svg:nth-child(1)");
	private static By weekend = By.cssSelector("span.bwoYtA,span.fgdqFw");
//	private static By monthHoliday = By.xpath("//div[contains(@class,'CalendarHeader')]//div[2]");
//	private static By next = By.xpath("//div[contains(@class,'CalendarHeader')]//div[3]/*[local-name()=\"svg\"]");		

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", "geckodriver.exe");
		FirefoxOptions options = new FirefoxOptions();
		options.setProfile(new FirefoxProfile());
		options.addPreference("dom.webnotifications.enabled", false);
		driver = new FirefoxDriver(options);
		driver.get("https://www.redbus.in");
		driver.manage().window().maximize();
		
		List <String> weekendDates = getWeekendDates("Jun 2023");
		System.out.println(weekendDates);

	}
	
	public static List<String> getWeekendDates(String monthYear) {
		
		if (!isValidMonthYear(monthYear)) {
			System.out.println("Invalid Calendar Month: "+monthYear);
			return null;
		}
		
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
		
		List<String> weekendDates = new ArrayList<>();
		for(WebElement e: driver.findElements(weekend)) {
			weekendDates.add(e.getText());
		}
		
		return weekendDates;
	}
	
	public static String[] getMonthYear(String txt) {
		return txt.split("\n")[0].split(" ");
	}
	
	public static boolean isValidMonthYear(String txt) {
		int currentMonth = LocalDate.now().getMonthValue();
		int currentYear = LocalDate.now().getYear();
		int month = -1;
		SimpleDateFormat sdf = new SimpleDateFormat("MMM");
        try {
            Date date = sdf.parse(getMonthYear(txt)[0]);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0, so add 1
        } catch (ParseException e) {
            e.printStackTrace();
       }
        if (Integer.parseInt(getMonthYear(txt)[1])==currentYear){
        	return !(month<currentMonth);
        }
        return (!(Integer.parseInt(getMonthYear(txt)[1])<currentYear));
	}
	
//public static void getWeekendWithXpath() {		
//		
//		By sat = By.xpath("//div[contains(@class,'DayTiles__CalendarDays')][6]/span[not(contains(@class,'hVMWpe') or contains(@class,'gigHYE'))]");
//		By sun = By.xpath("//div[contains(@class,'DayTiles__CalendarDays')][7]/span[not(contains(@class,'hVMWpe') or contains(@class,'gigHYE'))]");
//		
//		List<WebElement> sat_list = driver.findElements(sat);
//		List<WebElement> sun_list = driver.findElements(sun);
//		
//		List<Integer> weekendDates = new ArrayList<>();
//		for(WebElement e: sat_list) {
//			weekendDates.add(Integer.parseInt(e.getText()));
//		}
//		for(WebElement e: sun_list) {
//			weekendDates.add(Integer.parseInt(e.getText()));
//		}
//		Collections.sort(weekendDates);		
//		System.out.println(weekendDates);
//	}

}
