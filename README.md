# SunLicense API 

## Maven
> get latest version from https://repo.hapangama.com/#/releases/com/hapangama/SunLicenseAPI

```xml
<repository>
  <id>sundevs-releases</id>
  <name>Sundevs Repository</name>
  <url>https://repo.hapangama.com/releases</url>
</repository>

<dependency>
  <groupId>com.hapangama</groupId>
  <artifactId>SunLicenseAPI</artifactId>
  <version>1.0.2</version> 
</dependency>
```

## Example Usage

```java
public class Main {
    public static void main(String[] args) {
        // Example of how to use SunLicenseAPI in a simple application

        // Creating an instance of SunLicenseAPI with a hardcoded license key, product ID, product version, and API URL.
        // Replace the placeholder values with your actual license key, product ID, and API URL.
        SunLicenseAPI api = SunLicenseAPI.getLicense("6AQB-N92G-K038-JZ02-V0UD", 1203, "1.0.0", "http://localhost:8080");

        try {
            // Validate the license. This method will throw an IOException if the license is invalid.
            api.validate();
            System.out.println("License is valid");
        } catch (IOException e) {
            // Handle the exception if the license validation fails.
            // Pro Tip: Customize the error message to provide more context to the user.
            System.out.println("Error: " + e.getMessage());
            System.exit(1); // Exit the program with a non-zero status code indicating an error.
        }

        // Pro Tip: For production use, it's better to load the license key from a file
        // rather than hard-coding it in your source code. This enhances security and makes
        // it easier to update the license key without modifying the code.
        //
        // Example of loading the license from a file in production:
        //
        // try {
        //     // Replace "path/to/license.txt" with the actual path to your license file
        //     SunLicenseAPI api = SunLicenseAPI.getLicenseFromFile("path/to/license.txt", 1203, "1.0.0", "http://localhost:8080");
        //
        //     // Validate the license. This method will throw an IOException if the license is invalid.
        //     api.validate();
        //     System.out.println("License is valid");
        // } catch (IOException e) {
        //     // Handle the exception if the license validation fails.
        //     // Pro Tip: Customize the error message to provide more context to the user.
        //     System.out.println("Error: " + e.getMessage());
        //     System.exit(1); // Exit the program with a non-zero status code indicating an error.
        // }
    }
}
```
