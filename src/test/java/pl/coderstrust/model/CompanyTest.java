package pl.coderstrust.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import pl.coderstrust.generators.AccountNumberGenerator;
import pl.coderstrust.generators.ContactDetailsGenerator;

class CompanyTest {

  @Test
  public void checkFullyInitialization() {
    //given
    String name = "SampleCompanyName";
    String taxId = "573-213-99";
    AccountNumber accountNumber = AccountNumberGenerator.getSampleAccountNumber();
    ContactDetails contactDetails = ContactDetailsGenerator.getSampleContactDetails();

    //when
    Company company = new Company(name, taxId, accountNumber, contactDetails);

    //then
    assertEquals(name, company.getName());
    assertEquals(taxId, company.getTaxIdentificationNumber());
    assertEquals(accountNumber, company.getAccountNumber());
    assertEquals(contactDetails, company.getContactDetails());
  }
}
