import java.util.*;

// ======================= DECORATOR PATTERN =======================

interface IReport {
    String generate();
}

// Base Reports

class SalesReport implements IReport {

    public String generate() {
        return "Sales Report Data:\n" +
                "Order1 - $120 - 2024-01-01\n" +
                "Order2 - $250 - 2024-02-10\n" +
                "Order3 - $75 - 2024-03-05\n";
    }
}

class UserReport implements IReport {

    public String generate() {
        return "User Report Data:\n" +
                "User1 - Premium\n" +
                "User2 - Standard\n" +
                "User3 - Premium\n";
    }
}

// Abstract Decorator

abstract class ReportDecorator implements IReport {

    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    public String generate() {
        return report.generate();
    }
}

// Date Filter

class DateFilterDecorator extends ReportDecorator {

    private String startDate;
    private String endDate;

    public DateFilterDecorator(IReport report, String startDate, String endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String generate() {
        return report.generate() +
                "\n[Filtered by Date: " + startDate + " - " + endDate + "]";
    }
}

// Sorting

class SortingDecorator extends ReportDecorator {

    private String criteria;

    public SortingDecorator(IReport report, String criteria) {
        super(report);
        this.criteria = criteria;
    }

    public String generate() {
        return report.generate() +
                "\n[Sorted by: " + criteria + "]";
    }
}

// CSV Export

class CsvExportDecorator extends ReportDecorator {

    public CsvExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        return report.generate() +
                "\n[Exported to CSV format]";
    }
}

// PDF Export

class PdfExportDecorator extends ReportDecorator {

    public PdfExportDecorator(IReport report) {
        super(report);
    }

    public String generate() {
        return report.generate() +
                "\n[Exported to PDF format]";
    }
}

// Additional Decorator (Filter by amount)

class AmountFilterDecorator extends ReportDecorator {

    private double minAmount;

    public AmountFilterDecorator(IReport report, double minAmount) {
        super(report);
        this.minAmount = minAmount;
    }

    public String generate() {
        return report.generate() +
                "\n[Filtered by Minimum Amount: $" + minAmount + "]";
    }
}

// ======================= ADAPTER PATTERN =======================

interface IInternalDeliveryService {

    void deliverOrder(String orderId);

    String getDeliveryStatus(String orderId);

    double calculateCost(double weight);
}

// Internal Service

class InternalDeliveryService implements IInternalDeliveryService {

    public void deliverOrder(String orderId) {
        System.out.println("Internal delivery started for order: " + orderId);
    }

    public String getDeliveryStatus(String orderId) {
        return "Internal delivery in progress for " + orderId;
    }

    public double calculateCost(double weight) {
        return weight * 2;
    }
}

// ======================= External Services =======================

// External A

class ExternalLogisticsServiceA {

    public void shipItem(int itemId) {
        System.out.println("ServiceA shipping item: " + itemId);
    }

    public String trackShipment(int shipmentId) {
        return "ServiceA tracking shipment " + shipmentId;
    }

    public double getPrice(double weight) {
        return weight * 3;
    }
}

// External B

class ExternalLogisticsServiceB {

    public void sendPackage(String packageInfo) {
        System.out.println("ServiceB sending package: " + packageInfo);
    }

    public String checkPackageStatus(String trackingCode) {
        return "ServiceB tracking " + trackingCode;
    }

    public double deliveryPrice(double weight) {
        return weight * 2.5;
    }
}

// External C

class ExternalLogisticsServiceC {

    public void dispatch(String id) {
        System.out.println("ServiceC dispatching order " + id);
    }

    public String status(String id) {
        return "ServiceC status for " + id;
    }

    public double computeCost(double weight) {
        return weight * 4;
    }
}

// ======================= ADAPTERS =======================

class LogisticsAdapterA implements IInternalDeliveryService {

    private ExternalLogisticsServiceA service = new ExternalLogisticsServiceA();

    public void deliverOrder(String orderId) {

        try {
            System.out.println("[AdapterA LOG]");
            service.shipItem(Integer.parseInt(orderId));
        } catch (Exception e) {
            System.out.println("Error in AdapterA delivery");
        }
    }

    public String getDeliveryStatus(String orderId) {
        return service.trackShipment(Integer.parseInt(orderId));
    }

    public double calculateCost(double weight) {
        return service.getPrice(weight);
    }
}


class LogisticsAdapterB implements IInternalDeliveryService {

    private ExternalLogisticsServiceB service = new ExternalLogisticsServiceB();

    public void deliverOrder(String orderId) {

        try {
            System.out.println("[AdapterB LOG]");
            service.sendPackage(orderId);
        } catch (Exception e) {
            System.out.println("Error in AdapterB delivery");
        }
    }

    public String getDeliveryStatus(String orderId) {
        return service.checkPackageStatus(orderId);
    }

    public double calculateCost(double weight) {
        return service.deliveryPrice(weight);
    }
}


class LogisticsAdapterC implements IInternalDeliveryService {

    private ExternalLogisticsServiceC service = new ExternalLogisticsServiceC();

    public void deliverOrder(String orderId) {

        try {
            System.out.println("[AdapterC LOG]");
            service.dispatch(orderId);
        } catch (Exception e) {
            System.out.println("Error in AdapterC delivery");
        }
    }

    public String getDeliveryStatus(String orderId) {
        return service.status(orderId);
    }

    public double calculateCost(double weight) {
        return service.computeCost(weight);
    }
}

// ======================= FACTORY =======================

class DeliveryServiceFactory {

    public static IInternalDeliveryService getService(String type) {

        switch (type) {

            case "internal":
                return new InternalDeliveryService();

            case "A":
                return new LogisticsAdapterA();

            case "B":
                return new LogisticsAdapterB();

            case "C":
                return new LogisticsAdapterC();

            default:
                throw new IllegalArgumentException("Unknown service type");
        }
    }
}

// ======================= CLIENT =======================

public class Main {

    public static void main(String[] args) {

        System.out.println("===== DECORATOR REPORT SYSTEM =====");

        IReport report = new SalesReport();

        report = new DateFilterDecorator(report, "2024-01-01", "2024-12-31");
        report = new SortingDecorator(report, "Amount");
        report = new CsvExportDecorator(report);
        report = new AmountFilterDecorator(report, 100);

        System.out.println(report.generate());


        System.out.println("\n===== ADAPTER LOGISTICS SYSTEM =====");

        IInternalDeliveryService service =
                DeliveryServiceFactory.getService("A");

        service.deliverOrder("1001");

        System.out.println(service.getDeliveryStatus("1001"));

        System.out.println("Delivery cost: $" +
                service.calculateCost(5));
    }
}