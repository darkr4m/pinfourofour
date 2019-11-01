# pin404
An internal Pinterest management tool. Designed to automate the process of updating or deleting a **large amount of data** on Pinterest.

## Technologies Used and Requirements:
**Requires Java 8 +** <br>

### Featuring:
- JCommander
- Pinterest Developer SDK by Chris Dempwolf @https://github.com/dempe
- Maven - for building and packaging
- SQLite
- Apache CSV Commons

## Automates Pinterest Cleanup Tasks:
- Retrieve Pins (at 1000 per hour until rate limit increase, then 100,000 per hour)
- Update Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Remove Pins (at 10 per hour until rate limit increase, then 1000 per hour)
- Check Links for their HTTP Response code.
- Generates Reports on Problem Pins.




## Configuration

**Configuration file found in  config/pin.config** <br>
On first and subsequent uses, the application will create and modify subdirectories and files needed for operation. <br>
_The pin404.jar is meant to stay inside of it's dedicated containing directory. Please keep it there. You can move the directory around as you please._<br>

**Data is stored in an SQLite database and can be exported to CSV format and can be viewed in any spreadsheet program.**<br>
 This application supports importing data from a CSV file, however there are some specific formatting requirements that should be followed: <br>

### CSV Hard Requirements
 **Column headers are required and must be set in _this_ order:**<br> (not case sensitive)
 PIN_ID, BOARD, LINK, NOTE, LINK_RESPONSE_CODE, LINK_REDIRECT_LOCATION, LINK_REDIRECT_RESPONSE_CODE, ACTION <br>
 
 **HACK:**(If you need a template)<br>
 Copy and paste the above string into a new text file and save it as .csv.

### Pinterest - <br>
**Access_token** - The access token to be used while performing any actions with the Pinterest API _**(REQUIRED)**_ <br>
**Use Postman to obtain an access token for this app. <br>
More information here:** https://developers.pinterest.com/docs/api/overview/ <br><br>
**Username** - Your pinterest username. _**(REQUIRED)**_<br>
<img src="https://i.imgur.com/k7mvH1n.png" title="source: imgur.com" />

Make sure your access token and username are correct and saved in config/pin.config. <br>
If the config directory does not exist, it can be found after the program runs for the first time.
See the command summary for usage details.

## Usage and Commands

### Command Line Syntax Example
java -jar pin404.jar command -p --parameters=pins.csv

Supports the @ syntax, which allows you to put all your options into a file.txt and pass the file as parameter:<br>
<img src="https://i.imgur.com/N3A2neK.png?1" title="source: imgur.com" />

#### config 
_Used by the application for Pinterest connectivity_ **See configuration section**
 - options: <br>
    t --token=(access token) <br> 
    u --username=(username) <br>
    **Example** - java -jar pin404.jar config -t=2637891hkwejrh2917319312 -u=max
 
#### rake
_Retrieve the **authenticated user's** pins. The owner of the access token set up in configuration is considered the authenticated user._ <br>
 - options: <br>
    c --continue (continue from last rake) (<t/f> default false) <br>
    **Example** - java -jar pin404.jar rake -c <br>
 
#### status
_Returns the HTTP/S URL Status Codes of the link on the pin. Checks for redirect location and redirect location response code._<br>
- options: <br>
   -f --filter (filter external links <t/f> default false)<br>
   **Example** - java -jar pin404.jar status --filter <br>
   
#### report 
_Generates a report in the format of a CSV file. The file will be located in the **same directory as the .jar file.**_<br>
- options: <br>
  _None_
**Example** - java -jar pin404.jar report

#### import 
_Imports data from a CSV file to the database. **See above for CSV header requirements.**_<br>
- options: <br>
   -f=<filename.csv> <br>
   **Example** - java -jar pin404.jar import -f=samples.csv
 
#### update
_Updates the specified field names on Pinterest and in the database that is located in a local CSV file._<br>
- options: <br>
   -f=<filename.csv> <br>
   **Example** - java -jar pin404.jar update -f=samples.txt<br>
 
#### remove
_Removes the specified pin referenced by **PIN ID** on Pinterest and in the database that is located in a local CSV file._<br>
- options: <br>
   -f=<filename.csv>
   **Example** - java -jar pin404.jar remove -f=samples.csv<br>
