package invoicemanagement;

import java.util.concurrent.*;

@SuppressWarnings("resource")
public class Main
{
    public static void main(String[] args) {

        final int MAX_INVOICES = 100;

        final int PROCESS_WAIT_TIME = 100;

        final int INVOICE_ADD_WAIT_TIME = 90;

        final int COMPUTE_TIME = 10;

        BlockingQueue<Invoice> invoices = new LinkedBlockingQueue<>(MAX_INVOICES);

        int cores = Runtime.getRuntime().availableProcessors();

        int adderThreads = (cores/2) * (1 + (INVOICE_ADD_WAIT_TIME/COMPUTE_TIME));

        int processorThreads = (cores/2) * (1 + (PROCESS_WAIT_TIME/COMPUTE_TIME));


        ExecutorService addInvoiceThreadPool = new ThreadPoolExecutor(
                adderThreads, adderThreads,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_INVOICES),
                (r, executor) ->
                {
                    try
                    {
                        // This will block until space becomes available
                        executor.getQueue().put(r);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();

                        throw new RejectedExecutionException("Task submission interrupted", e);
                    }
                }

        );

        ExecutorService processInvoiceThreadPool = new ThreadPoolExecutor(
                processorThreads, processorThreads,
                10L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(MAX_INVOICES),
                (r, executor) ->
                {
                    try
                    {
                        executor.getQueue().put(r);
                    }
                    catch (InterruptedException e)
                    {
                        Thread.currentThread().interrupt();

                        throw new RejectedExecutionException("Task submission interrupted", e);
                    }
                }
        );


        Thread adder = new Thread(() ->
        {
            try
            {
                for(int i = 0; i<5000; i++)
                {
                    Invoice invoice = new Invoice(i * 10, (i + 1));

                    addInvoiceThreadPool.submit(new InvoiceAdder(invoices, invoice));
                }
            }
            finally
            {
                addInvoiceThreadPool.shutdown();
            }
        });

        Thread processor = new Thread(() ->
        {
            try
            {
                while (true)
                {
                    Invoice invoice = invoices.take();

                    processInvoiceThreadPool.submit(new InvoiceProcessor(invoice));
                }
            }
            catch (InterruptedException e)
            {
                Thread.currentThread().interrupt();
            }
            finally
            {
                processInvoiceThreadPool.shutdown();
            }
        });


        processor.start();
        adder.start();
    }
}
