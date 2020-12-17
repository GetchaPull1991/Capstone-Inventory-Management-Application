package Model;

/** Class to create and manage customers */
public class Customer {

    private String name;
    private String streetAddress;
    private String city;
    private String postalCode;
    private String country;
    private String division;
    private String phoneNumber;
    private int customerID;

    /**
     * Create a new Customer object
     * @param name set the name
     * @param address set the address
     * @param city set the city
     * @param postalCode set the postal code
     * @param country set the country
     * @param division set the division
     * @param phoneNumber set the phone number
     * @param customerID set the id
     */
    public Customer(int customerID, String name, String address, String city, String division, String postalCode, String country, String phoneNumber){
        this.name = name;
        this.streetAddress = address;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.division = division;
        this.phoneNumber = phoneNumber;
        this.customerID = customerID;
    }

    /**
     * Get the customers name
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the customers name
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the customers address
     * @return the address
     */
    public String getStreetAddress() {
        return streetAddress;
    }

    /**
     * Set the customers address
     * @param streetAddress the address to set
     */
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    /**
     * Get the customers phone number
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set the customers phone number
     * @param phoneNumber the phone number to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Get the customers id
     * @return the id
     */
    public int getCustomerID() {
        return customerID;
    }

    /**
     * Set the customers id
     * @param customerID the id to set
     */
    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    /**
     * Get city
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * Set city
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Get country
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Set country
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Get division
     * @return the division
     */
    public String getDivision() {
        return division;
    }

    /**
     * Set division
     * @param division the division to set
     */
    public void setDivision(String division) {
        this.division = division;
    }

    /**
     * Get postal code
     * @return the postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Set postal code
     * @param postalCode the postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}

