import secrets

import logging
import random
import time
import os

from threading import Lock, Timer

import rest_user
from rest_user import RestUser
from locust import between, task

logging.basicConfig(level=logging.INFO)

# Define environment variables
FREQUENCY_LEVEL = os.getenv('FREQUENCY_LEVEL', 'medium')
SIZE_LEVEL = os.getenv('SIZE_LEVEL', 'large')

# Set mappings
FREQUENCY_MAPPING = {'low': 8, 'medium': 80, 'high': 400}
SIZE_MAPPING = {'small': 50, 'medium': 1000, 'large': 100000}

global_max_requests = FREQUENCY_MAPPING[FREQUENCY_LEVEL]
payload_size = SIZE_MAPPING[SIZE_LEVEL]

max_requests = {"add_customer": 600,
                "update_customer": 600,
                "get_customer": 200,
                "delete_customer": 200,
                "get_item": 200,
                "add_item": 600,
                "update_item": 600,
                "delete_item": 200,
                "create_order": 600,
                "get_order": 400,
                "get_orders": 400,
                "delete_order": 200,
                }
total_requests = {name: 0 for name in max_requests.keys()}


class AccountServiceTasks(rest_user.RestUser):
    wait_time = between(1, 3)

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.request_counts = None
        self.locks = {name: Lock() for name in ["add_customer", "update_customer",
                                                "get_customer", "delete_customer",
                                                "get_item", "add_item",
                                                "update_item", "delete_item",
                                                "create_order", "get_order",
                                                "get_orders", "delete_order"
                                                ]}
        self.created_customers = list(range(1, 1005))
        self.created_items = list(range(1, 1003))
        self.created_order = list(range(4, 2005, 2))
        self.filtered_items = [item for item in self.created_items if 10 <= item <= 50]
        self.delete_items = [item for item in self.created_items if 50 <= item <= 1002]
        self.filtered_customers = [customer for customer in self.created_customers if 10 <= customer <= 50]
        self.delete_customers = [customer for customer in self.created_customers if 50 <= customer <= 1003]
        self.filtered_orders = [order for order in self.created_order if 1 <= order <= 10]
        self.delete_orders = [order for order in self.created_order if 11 <= order <= 2004]

    def _increment_request_count(self, task_name):
        with self.locks[task_name]:
            if total_requests[task_name] < global_max_requests:
                total_requests[task_name] += 1
                return True
            return False

    @task
    def execute_tasks_in_sequence(self):
        self.add_customer()
        self.update_customer()
        self.get_customer()
        self.add_item()
        self.update_item()
        self.get_item()
        self.create_order()
        self.get_order()
        self.delete_order()
        self.delete_item()
        self.delete_customer()
        # self.get_orders()


    def add_customer(self):
        with self.locks["add_customer"]:
            if total_requests["add_customer"] < global_max_requests * 1.5:
                total_requests["add_customer"] += 1
                username = secrets.token_bytes(16910).hex()
                firstname = secrets.token_bytes(16154).hex()
                payload = {
                        "name": username,
                        "firstname": firstname,
                        "email": username+"@gmail.com"}
                try:
                    self.make_request("/customers/", "POST", payload=payload, task_name="customer")
                    self.delete_customers.append(len(self.created_customers)+1)
                    self.created_customers.append(len(self.created_customers)+1)
                except Exception as e:
                    logging.error(f"Failed to create account: {e}")

    def update_customer(self):
        with self.locks["update_customer"]:
            if total_requests["update_customer"] < global_max_requests * 1.5:
                total_requests["update_customer"] += 1
                username = secrets.token_bytes(24975).hex()
                userid = random.choice(self.filtered_customers)
                payload = {
                    "name": username,
                    "firstname": "Chen",
                    "email": username+"@gmail.com",
                    "street": "11 Street",
                    "city": "Amsterdam"}
                try:
                    self.make_request(f"/customers/{userid}", method="PUT", payload=payload, task_name="customer")
                except Exception as e:
                    logging.error(f"Failed to update customer: {e}")

    def get_customer(self):
        with self.locks["get_customer"]:
            if total_requests["get_customer"] < global_max_requests * 0.5:
                total_requests["get_customer"] += 1
                userid = random.choice(self.filtered_customers)
                try:
                    self.make_request(f"/customers/{userid}", method="GET", task_name="customer")
                except Exception as e:
                    logging.error(f"Failed to get customer: {e}")

    def add_item(self):
        with self.locks["add_item"]:
            if total_requests["add_item"] < global_max_requests * 1.5:
                total_requests["add_item"] += 1
                name = secrets.token_bytes(49986).hex()
                payload = {
                    "name": name,
                    "price": "500"
                }
                try:
                    self.make_request("/catalog/", "POST", payload=payload, task_name="catalog")
                    self.delete_items.append(len(self.created_items)+1)
                    self.created_items.append(len(self.created_items)+1)
                except Exception as e:
                    logging.error(f"Failed to create item: {e}")

    def update_item(self):
        with self.locks["update_item"]:
            if total_requests["update_item"] < global_max_requests * 1.5:
                total_requests["update_item"] += 1
                itemid = random.choice(self.filtered_items)
                name = secrets.token_bytes(49986).hex()
                payload = {
                    "name": name,
                    "price": "800"
                }

                try:
                    self.make_request(f"/catalog/{itemid}", method="PUT", payload=payload, task_name="catalog")
                except Exception as e:
                    logging.error(f"Failed to update item: {e}")

    def get_item(self):
        with self.locks["get_item"]:
            if total_requests["get_item"] < global_max_requests * 0.5:
                total_requests["get_item"] += 1
                name = random.choice(self.filtered_items)
                try:
                    self.make_request(f"/catalog/{name}", method="GET", task_name="catalog")
                except Exception as e:
                    logging.error(f"Failed to get item: {e}")

    def create_order(self):
        with self.locks["create_order"]:
            if total_requests["create_order"] < global_max_requests * 1.5:
                total_requests["create_order"] += 1
                customer_id = random.choice(self.filtered_customers)
                itemid = random.choice(self.filtered_items)
                note = secrets.token_bytes(49963).hex()
                payload = {
                    "customerId": customer_id,
                    "orderLine": [
                        {
                            "count": 10,
                            "itemId": itemid,
                            "note": note
                        }
                    ]
                }
                try:
                    self.make_request("/orders/", "POST", payload=payload, task_name="order")
                    self.delete_orders.append(2*len(self.created_order))
                    self.created_order.append(2*len(self.created_order))

                except Exception as e:
                    logging.error(f"Failed to create order: {e}")

    def get_order(self):
        if self._increment_request_count("get_order"):
            order_id = random.choice(self.filtered_orders)
            try:
                self.make_request(f"/order/{order_id}", method="GET", task_name="order")
            except Exception as e:
                logging.error(f"Failed to get a order: {e}")

    def get_orders(self):
        if self._increment_request_count("get_orders"):
            try:
                self.make_request("/orders/", method="GET", task_name="order")
            except Exception as e:
                logging.error(f"Failed to get orders: {e}")

    def delete_order(self):
        with self.locks["delete_order"]:
            if total_requests["delete_order"] < global_max_requests * 0.5:
                total_requests["delete_order"] += 1
                order_id = random.choice(self.delete_orders)
                try:
                    self.make_request(f"/orders/{order_id}", method="DELETE", task_name="order")
                    self.delete_orders.remove(order_id)
                except Exception as e:
                    logging.error(f"Failed to delete order: {e}")

    def delete_customer(self):
        with self.locks["delete_customer"]:
            if total_requests["delete_customer"] < global_max_requests * 0.5:
                total_requests["delete_customer"] += 1
                customer_id = random.choice(self.delete_customers)
                try:
                    self.make_request(f"/customer/{customer_id}", method="DELETE", task_name="customer")
                    self.delete_customers.remove(customer_id)
                except Exception as e:
                    logging.error(f"Failed to delete customer: {e}")

    def delete_item(self):
        with self.locks["delete_item"]:
            if total_requests["delete_item"] < global_max_requests * 0.5:
                total_requests["delete_item"] += 1
                item_id = random.choice(self.delete_items)
                try:
                    self.make_request(f"/catalog/{item_id}", method="DELETE", task_name="catalog")
                    self.delete_items.remove(item_id)
                except Exception as e:
                    logging.error(f"Failed to delete item: {e}")


class WebsiteUser(RestUser):
    tasks = [AccountServiceTasks]
    wait_time = between(1, 3)
    total_requests = {name: 0 for name in max_requests.keys()}
    start_time = None

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.stop_timer = None
        self.results = []

    def on_start(self):
        self.results = []
        self.start_time = time.time()
        self.stop_timer = Timer(150, self.stop_user)
        self.stop_timer.start()

    def on_stop(self):
        if self.stop_timer and self.stop_timer.is_alive():
            self.stop_timer.cancel()
            self.stop_timer.join()

    def stop_user(self):
        self.environment.runner.quit()
        logging.info("Test stopped after 2 min 30s")
