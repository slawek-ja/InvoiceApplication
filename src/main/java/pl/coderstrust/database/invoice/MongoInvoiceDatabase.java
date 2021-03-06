package pl.coderstrust.database.invoice;

import java.util.Optional;
import lombok.NonNull;
import lombok.Synchronized;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pl.coderstrust.configuration.MongoDatabaseProperties;
import pl.coderstrust.database.DatabaseOperationException;
import pl.coderstrust.model.Invoice;

@ConditionalOnProperty(name = "pl.coderstrust.database", havingValue = "mongodb")
@Repository
public class MongoInvoiceDatabase implements InvoiceDatabase {

  private final MongoDatabaseProperties properties;
  private MongoTemplate mongoTemplate;

  @Autowired
  public MongoInvoiceDatabase(@NonNull MongoTemplate mongoTemplate, @NonNull MongoDatabaseProperties properties) {
    this.mongoTemplate = mongoTemplate;
    this.properties = properties;
  }

  @Synchronized
  @Override
  public Invoice save(@NonNull Invoice invoice) throws DatabaseOperationException {
    try {
      return mongoTemplate.save(invoice, properties.getCollectionName());
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems saving invoice: %s", invoice), e);
    }
  }

  @Synchronized
  @Override
  public Optional<Invoice> findById(@NonNull String id) throws DatabaseOperationException {
    try {
      return Optional.ofNullable(mongoTemplate.findById(id, Invoice.class, properties.getCollectionName()));
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoice:, %s", id), e);
    }
  }

  @Synchronized
  @Override
  public boolean existsById(@NonNull String id) throws DatabaseOperationException {
    try {
      return mongoTemplate.exists(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoice: %s", id), e);
    }
  }

  @Synchronized
  @Override
  public Iterable<Invoice> findAll() throws DatabaseOperationException {
    try {
      return mongoTemplate.findAll(Invoice.class, properties.getCollectionName());
    } catch (Exception e) {
      throw new DatabaseOperationException("Encountered problems while searching for invoices.", e);
    }
  }

  @Synchronized
  @Override
  public long count() throws DatabaseOperationException {
    try {
      return mongoTemplate.getCollection(properties.getCollectionName()).countDocuments();
    } catch (Exception e) {
      throw new DatabaseOperationException("Encountered problems while counting invoices.", e);
    }
  }

  @Synchronized
  @Override
  public void deleteById(@NonNull String id) throws DatabaseOperationException {
    try {
      Invoice removedInvoice = mongoTemplate.findAndRemove(Query.query(Criteria.where("_id").is(id)), Invoice.class, properties.getCollectionName());
      if (removedInvoice == null) {
        throw new DatabaseOperationException(String.format("There is no invoice with id: %s present in database. Nothing was removed.", id));
      }
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems while deleting invoice: %s", id), e);
    }
  }

  @Synchronized
  @Override
  public void deleteAll() throws DatabaseOperationException {
    try {
      mongoTemplate.getCollection(properties.getCollectionName()).deleteMany(new Document());
    } catch (Exception e) {
      throw new DatabaseOperationException("Encountered problem while deleting invoices.", e);
    }
  }

  @Synchronized
  @Override
  public Iterable<Invoice> findAllBySellerName(@NonNull String sellerName) throws DatabaseOperationException {
    try {
      return mongoTemplate.find(Query.query(Criteria.where("sellerName").is(sellerName)), Invoice.class, properties.getCollectionName());
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoices with seller name: %s", sellerName), e);
    }
  }

  @Synchronized
  @Override
  public Iterable<Invoice> findAllByBuyerName(@NonNull String buyerName) throws DatabaseOperationException {
    try {
      return mongoTemplate.find(Query.query(Criteria.where("buyerName").is(buyerName)), Invoice.class, properties.getCollectionName());
    } catch (Exception e) {
      throw new DatabaseOperationException(String.format("Encountered problems while searching for invoices with buyer name: %s", buyerName), e);
    }
  }
}
