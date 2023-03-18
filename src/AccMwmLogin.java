import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.interactions.Actions;


public class AccMwmLogin {

	public static void main(String[] args) throws Throwable {
		ProfilesIni allprofiles = new ProfilesIni();
		FirefoxProfile ff = allprofiles.getProfile("TestBrowser");
		WebDriver driver = new FirefoxDriver(ff);
		driver.get("https://10.222.115.155:9999/mwm");
		WebElement username=driver.findElement(By.id("M__Id"));
		username.sendKeys("admin@acc1");
		WebElement password=driver.findElement(By.id("M__Ida"));
		password.sendKeys("admin@12");
		WebElement login=driver.findElement(By.id("submitbutton"));
		login.click();
		WebElement task=driver.findElement(By.xpath(".//*[@id='mainnav__TaskTab']/a[2]"));
		Thread.sleep(2000);
		task.click();
		Thread.sleep(2000);
		driver.switchTo().frame("ExtjsCustomerTaskListDiv");
		Thread.sleep(2000);
		WebElement Addcustomer=driver.findElement(By.xpath(".//*[@id='button-1011-btnInnerEl']"));
		Addcustomer.click();
		WebElement Custname=driver.findElement(By.id("custname-inputEl"));
		Custname.sendKeys("pradeep");
		WebElement AcctNum=driver.findElement(By.id("accountNumber-inputEl"));
		AcctNum.sendKeys("1234678920");
		WebElement PhnNo=driver.findElement(By.id("mobileNo-inputEl"));
		PhnNo.sendKeys("9618234931");
		WebElement HouseNo=driver.findElement(By.id("houseno-inputEl"));
		HouseNo.sendKeys("FLAT NO-707");
		WebElement Street=driver.findElement(By.id("street-inputEl"));
		Street.sendKeys("Begmpet");
		WebElement Locality=driver.findElement(By.id("locality-inputEl"));
		Locality.sendKeys("BS MAKTHA");
		Thread.sleep(2000);
		WebElement country = driver.findElement(By.xpath(".//*[@id='locality-inputEl']/following::div[4]"));		
		country.click();
		WebElement Menu=driver.findElement(By.xpath(".//*[@id='combo-1056-trigger-picker']"));
		Menu.click();
		Actions actions=new Actions(driver);
		actions.sendKeys(Keys.ENTER).build().perform();
		actions.click().build().perform();
		
		//actions.moveToElement(Menu).build().perform(); 
		//Thread.sleep(2000);
		//actions.sendKeys(Keys.ARROW_DOWN).build().perform();
		//Thread.sleep(2000);
		//actions.click().build().perform();
				
		
		
		
		
		
		
		
		
		
		
		//Actions act = new Actions(driver);
        //act.sendKeys(Keys.ENTER).build().perform();
        //driver.findElement(By.xpath(".//*[@id='locality-inputEl']/following::div[4]")).click();
        //act.sendKeys(Keys.UP).sendKeys(Keys.UP).sendKeys(Keys.UP).sendKeys(Keys.ENTER).build().perform();
        //driver.findElement(By.xpath("//*[@id='locality-inputEl']/following::div[4]']")).sendKeys("India");


		//Select oSelect1=new Select(driver.findElement(By.xpath(".//*[@id='combo-1057-trigger-picker']")));
		//oSelect1.selectByVisibleText("Andhra Pradesh");
		//WebElement City=driver.findElement(By.id("city-inputEl"));
		//City.sendKeys("Hyderabad");
		//WebElement Pin=driver.findElement(By.id("pin-inputEl"));
		//Pin.sendKeys("500016");
		//driver.switchTo().frame("ExtjsDispatchTabDiv");
		//Thread.sleep(2000);
		//WebElement Save=driver.findElement(By.xpath(".//*[@id='fieldcontainer-1121-innerCt']"));
		//Save.click();
		// TODO Auto-generated method stub
		

	}

}
