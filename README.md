# pin404
An internal Pinterest management tool. Designed to automate the process of updating or deleting a **large amount of data** on Pinterest.

## Automates Pinterest Cleanup Tasks:
- Retrieve Pins (at 1000 per hour until rate limit increase, then 100,000 per hour)
- Update Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Remove Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Check Links for their HTTP Response code.
- Generates Reports on Problem Pins.

**Data is stored in CSV format and can be viewed in any spreadsheet program.**<br>
 _Ensure that the data is saved in the same format!_ <br>
CSV warnings

##Usage

The pin404.jar is meant to stay inside of a dedicated containing directory. 
On first and subsequent uses, the application will create and modify subdirectories and files needed for operation.

###Configuration
**Configuration file found in  config/pin.config** <br>
#### Pinterest - <br>
**Access_token** - The access token to be used while performing any actions with the Pinterest API _**(REQUIRED)**_ <br>
**Username** - Your pinterest username. Will be used if you want to filter through pins that were not created by you. _**(REQUIRED)**_

Make sure your access token is correct and saved in config/pin.config. <br>
If the config directory does not exist, it can be found after the program runs for the first time.

###Commands
java -jar pin404.jar command -p --parameters=pins.csv

####config 
 - config -t --token=(access token) -u --username=(username)
 
####rake
 - rake -c --continue (continue from last rake <t/f> default false)
 
####check
 - check -f --filter (filter external <t/f> default false)
 
####update
 - update -f=<filename.csv> 
 
####remove
 - remove -f=<filename.csv>
