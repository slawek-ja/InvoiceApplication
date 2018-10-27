package pl.coderstrust.model;

import java.math.BigDecimal;
import lombok.Data;
import lombok.NonNull;

@Data
public class InvoiceEntry {
  @NonNull
  String item;

  @NonNull
  Long quantity;

  @NonNull
  UnitType unit;

  @NonNull
  BigDecimal price;

  @NonNull
  Vat vatRate;

  @NonNull
  BigDecimal netValue;

  @NonNull
  BigDecimal grossValue;
}
