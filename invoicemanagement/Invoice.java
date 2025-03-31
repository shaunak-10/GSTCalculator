package invoicemanagement;

import java.util.concurrent.atomic.AtomicInteger;

public class Invoice
{
    private final int id;

    private final int amount;

    private final float gstRate;

    static AtomicInteger invoiceCount = new AtomicInteger(1);

    private float gstAmount;

    public Invoice(int amount, float gstRate)
    {
        this.amount = amount;

        this.gstRate = gstRate;

        this.id = invoiceCount.getAndIncrement();
    }
    public void setGstAmount(float gstAmount)
    {
        this.gstAmount = gstAmount;
    }

    public float getGstAmount()
    {
        return gstAmount;
    }

    public int getId()
    {
        return id;
    }

    public int getAmount()
    {
        return amount;
    }

    public float getGstRate()
    {
        return gstRate;
    }
}
