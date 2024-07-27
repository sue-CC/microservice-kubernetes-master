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
FREQUENCY_LEVEL = os.getenv('FREQUENCY_LEVEL', 'high')
SIZE_LEVEL = os.getenv('SIZE_LEVEL', 'small')

# Set mappings
FREQUENCY_MAPPING = {'low': 8, 'medium': 80, 'high': 400}
SIZE_MAPPING = {'small': 50, 'medium': 1000, 'large': 100000}

global_max_requests = FREQUENCY_MAPPING[FREQUENCY_LEVEL]
payload_size = SIZE_MAPPING[SIZE_LEVEL]

max_requests = {"add_customer": 400,
                "update_customer": 400,
                "get_customer": 400,
                "get_item": 400,
                "add_item": 400,
                "update_item": 400,
                "create_order": 400,
                "get_order": 400
                }

total_requests = {name: 0 for name in max_requests.keys()}


class AccountServiceTasks(rest_user.RestUser):
    wait_time = between(1, 3)

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.request_counts = None
        self.locks = {name: Lock() for name in ["add_customer", "update_customer",
                                                "get_customer","get_item",
                                                "add_item","update_item",
                                                "create_order", "get_order"
                                                ]}
        self.created_customers = list(range(1, 100))
        self.created_items = list(range(100, 1000))
        self.created_order = list(range(1, 1003, 2))

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

    def add_customer(self):
        if self._increment_request_count("add_customer"):
            username = secrets.token_bytes(4).hex()
            payload = {
                    "name": username,
                    "firstname": "",
                    "email": ""}
            try:
                self.make_request("/customers/", "POST", payload=payload, task_name="customer")

            except Exception as e:
                logging.error(f"Failed to create account: {e}")

    def update_customer(self):
        if self._increment_request_count("update_customer"):
            username = random.choice(self.created_customers)
            payload = {
                "incomes": ["123"], "expenses": [],
                "saving": {}
            }
            try:
                self.make_request(f"/customers/{username}", method="PUT", payload=payload, task_name="customer")
            except Exception as e:
                logging.error(f"Failed to update customer: {e}")

    def get_customer(self):
        if self._increment_request_count("get_customer"):
                username = random.choice(self.created_customers)
                try:
                    self.make_request(f"/customers/{username}", method="GET", task_name="customer")
                except Exception as e:
                    logging.error(f"Failed to get customer: {e}")

    def add_item(self):
        if self._increment_request_count("add_item"):
            name = secrets.token_bytes(10).hex()
            payload = {
                "name": name,
                "price": 1000000
            }
            try:
                self.make_request("/catalog/", "POST", payload=payload, task_name="catalog")
            except Exception as e:
                logging.error(f"Failed to create item: {e}")

    def update_item(self):
        if self._increment_request_count("update_item"):
            name = secrets.token_bytes(10).hex()
            payload = {
                "name": name,
                "price": 1000000
            }
            try:
                self.make_request(f"/catalog/{1}", method="PUT", payload=payload, task_name="catalog")
            except Exception as e:
                logging.error(f"Failed to update item: {e}")

    def get_item(self):
        if self._increment_request_count("get_item"):
            name = random.choice(self.created_items)
            try:
                self.make_request(f"/catalog/{name}", method="GET", task_name="catalog")
            except Exception as e:
                logging.error(f"Failed to get item: {e}")

    def create_order(self):
        if self._increment_request_count("create_order"):
            payload = {
                "customerId": 1,
                "orderLine": [
                    {
                        "note": "None"
                    }
                ]
            }
            try:
                self.make_request("/orders/", "POST", payload=payload, task_name="order")

            except Exception as e:
                logging.error(f"Failed to create order: {e}")

    def get_order(self):
        if self._increment_request_count("get_order"):
            order_id = random.choice(self.created_order)
            try:
                self.make_request(f"/order/{order_id}", method="GET", task_name="order")
            except Exception as e:
                logging.error(f"Failed to get a order: {e}")


class WebsiteUser(RestUser):
    tasks = [AccountServiceTasks]
    wait_time = between(1, 3)
    total_requests = {name: 0 for name in max_requests.keys()}

    start_time = None
    frequency_level = os.getenv('FREQUENCY_LEVEL', 'low')

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
