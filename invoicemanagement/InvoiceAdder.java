package invoicemanagement;

import java.util.concurrent.BlockingQueue;

public class InvoiceAdder implements Runnable
{

    BlockingQueue<Invoice> invoices;

    Invoice invoice;

    public InvoiceAdder(BlockingQueue<Invoice> invoices, Invoice invoice)
    {
        this.invoices = invoices;

        this.invoice = invoice;
    }
    @Override
    public void run()
    {
        try
        {
            invoices.put(invoice);

            System.out.println("added invoice with id " + invoice.getId());

            Thread.sleep(90);
        }
        catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }

    }
}
