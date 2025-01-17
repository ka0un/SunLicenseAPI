package com.hapangama;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.NetworkInterface;
import java.net.URL;
import java.io.OutputStream;
import java.util.Enumeration;

public class SunLicenseAPI {

    private static final String API_URL_SUFFIX = "/api/v1/validate";

    private String url;
    private String licenseKey;
    private int productId;
    private String productVersion;
    private String hwid;
    private String macAddress;
    private String operatingSystem;
    private String operatingSystemVersion;
    private String operatingSystemArchitecture;
    private String javaVersion;

    public SunLicenseAPI(String licenseKey, int productId, String productVersion, String url) {
        this.licenseKey = licenseKey;
        this.productId = productId;
        this.productVersion = productVersion;
        this.url = url;
        this.hwid = generateHWID();
        this.macAddress = getMacAddress();
        this.operatingSystem = System.getProperty("os.name");
        this.operatingSystemVersion = System.getProperty("os.version");
        this.operatingSystemArchitecture = System.getProperty("os.arch");
        this.javaVersion = System.getProperty("java.version");
    }

    public static SunLicenseAPI getLicenseFromFile(String filePath, int productId, String productVersion, String LicenseServerUrl) throws IOException {

        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path is missing.");
        }

        if (LicenseServerUrl == null || LicenseServerUrl.isEmpty()) {
            throw new IllegalArgumentException("License server URL is missing.");
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String licenseKey = br.readLine();
            if (licenseKey != null) {
                return new SunLicenseAPI(licenseKey, productId, productVersion, LicenseServerUrl);
            } else {
                throw new IllegalArgumentException("License key is missing in the file.");
            }
        }
    }

    public static SunLicenseAPI getLicense(String licenseKey, int productId, String productVersion, String LicenseServerUrl) {


        if (LicenseServerUrl == null || LicenseServerUrl.isEmpty()) {
            throw new IllegalArgumentException("License server URL is missing.");
        }

        if (LicenseServerUrl != null && LicenseServerUrl.endsWith("/")) {
            LicenseServerUrl = LicenseServerUrl.substring(0, LicenseServerUrl.length() - 1);
        }

        return new SunLicenseAPI(licenseKey, productId, productVersion, LicenseServerUrl);
    }

    public void validate() throws IOException {

        if (licenseKey == null || licenseKey.isEmpty()) {
            throw new IllegalArgumentException("License key is missing.");
        }

        String jsonInputString = String.format(
                "{\"licenseKey\": \"%s\", \"productId\": %d, \"productVersion\": \"%s\", \"hwid\": \"%s\", \"macAddress\": \"%s\", \"operatingSystem\": \"%s\", \"operatingSystemVersion\": \"%s\", \"operatingSystemArchitecture\": \"%s\", \"javaVersion\": \"%s\"}",
                licenseKey, productId, productVersion, hwid, macAddress, operatingSystem, operatingSystemVersion, operatingSystemArchitecture, javaVersion
        );

        URL url = new URL(this.url + API_URL_SUFFIX);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; utf-8");
        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        int responseCode = conn.getResponseCode();
        if (responseCode != 200) {
            throw new RuntimeException("Invalid license. Response Code: " + responseCode + " : " + conn.getResponseMessage());
        }
    }

    private static String generateHWID() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] macArray = network.getHardwareAddress();
                if (macArray != null) {
                    StringBuilder hwid = new StringBuilder();
                    for (byte b : macArray) {
                        hwid.append(String.format("%02X", b));
                    }
                    return hwid.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default-hwid"; // Default HWID if none found
    }

    private static String getMacAddress() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            while (networks.hasMoreElements()) {
                NetworkInterface network = networks.nextElement();
                byte[] macArray = network.getHardwareAddress();
                if (macArray != null) {
                    StringBuilder mac = new StringBuilder();
                    for (byte b : macArray) {
                        mac.append(String.format("%02X:", b));
                    }
                    if (mac.length() > 0) {
                        mac.deleteCharAt(mac.length() - 1);
                    }
                    return mac.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:1A:2B:3C:4D:5E"; // Default MAC address if none found
    }

}