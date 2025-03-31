package invoicemanagement;

public class InvoiceProcessor implements Runnable
{

    Invoice invoice;

    public InvoiceProcessor(Invoice invoice)
    {
        this.invoice = invoice;
    }

    @Override
    public void run()
    {

        try
        {
            invoice.setGstAmount(invoice.getAmount() * (invoice.getGstRate()/100f));

            System.out.println("Processed invoice with id: " + invoice.getId() + " GST: " + invoice.getGstAmount());

            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
    }
}
