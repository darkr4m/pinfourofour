# pin404
An internal Pinterest management tool. Designed to automate the process of updating or deleting a **large amount of data** on Pinterest.

## Automates Pinterest Cleanup Tasks:
- Retrieve Pins (at 1000 per hour until rate limit increase, then 100,000 per hour)
- Update Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Remove Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Check Links for their HTTP Response code.
- Generates Reports on Problem Pins.

**Data is stored in an SQLite database and can be exported to CSV format and can be viewed in any spreadsheet program.**<br>
 _Ensure that the data is saved in the same format!_ <br>
CSV warnings

## Usage

The pin404.jar is meant to stay inside of a dedicated containing directory. 
On first and subsequent uses, the application will create and modify subdirectories and files needed for operation.

### Configuration
**Configuration file found in  config/pin.config** <br>
#### Pinterest - <br>
**Access_token** - The access token to be used while performing any actions with the Pinterest API _**(REQUIRED)**_ <br>
**Use Postman to obtain an access token for this app. <br>
More information here:** https://developers.pinterest.com/docs/api/overview/ <br><br>
**Username** - Your pinterest username. _**(REQUIRED)**_<br>
<img src="https://i.imgur.com/k7mvH1n.png" title="source: imgur.com" />

Make sure your access token and username are correct and saved in config/pin.config. <br>
If the config directory does not exist, it can be found after the program runs for the first time.
See the command summary for usage details.

## Commands
### Command Line Syntax Example
java -jar pin404.jar command -p --parameters=pins.csv

### config 
_Used by the application for Pinterest connectivity_ **See configuration section**
 - options: <br>
 -t --token=(access token) <br> 
 -u --username=(username) <br>
 **Example** - java -jar pin404.jar config -t=2637891hkwejrh2917319312 -u=max
 
### rake
_Retrieve the **authenticated user's** pins. The owner of the access token set up in configuration is considered the authenticated user._ <br>
 - rake -c --continue (<t/f> default false)
 - options: <br>
 -c --continue (continue from last rake) **Not quite supported yet. Use at own risk**<br>
 
#### check
 - check -f --filter (filter external <t/f> default false)
 
#### update
 - update -f=<filename.csv> 
 
#### remove
 - remove -f=<filename.csv>
