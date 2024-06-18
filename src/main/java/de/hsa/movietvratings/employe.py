class Employee:
    def __init__(self, name, department):
        self.name = name
        self.department = department

    def send_notification_to_manager(self, message):
        manager_email = self.fetch_manager_email()
        notification_message = f"Notification for manager: {message}"
        self.send_email(manager_email, notification_message)

    def fetch_manager_email(self):
        return "manager@example.com"

    def send_email(self, recipient, message):
        print(f"Sending email to {recipient}: {message}")


class SalesEmployee(Employee):
    def send_notification_to_manager(self, message):
        manager_email = "sales_manager@example.com"
        notification_message = f"Notification for sales manager: {message}"
        self.send_email(manager_email, notification_message)

# Verwendung der Klassen
employee1 = Employee("Alice", "HR")
employee1.send_notification_to_manager("Meeting scheduled for tomorrow")

sales_employee = SalesEmployee("Bob", "Sales")
sales_employee.send_notification_to_manager("New sales lead acquired")