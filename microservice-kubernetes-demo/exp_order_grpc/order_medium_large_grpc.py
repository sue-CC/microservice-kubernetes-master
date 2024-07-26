import secrets

import logging
import time
import os
import random

from threading import Lock, Timer

import catalog_pb2
import grpc_user_order
import customer_pb2
import order_pb2
from grpc_user_order import GrpcUser
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


class AccountServiceTasks(grpc_user_order.GrpcUser):
    wait_time = between(1, 3)

    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.request_counts = None
        self.locks = {name: Lock() for name in ["add_customer", "update_customer",
                                                "get_customer", "get_item",
                                                "add_item", "update_item",
                                                "create_order", "get_order",

                                                ]}
        self.created_customers = list(range(1, 100))
        self.created_items = list(range(100, 999))
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
        self.set_host_for_task("customer")
        if self._increment_request_count("add_customer"):
            city = email = firstname = name = street = secrets.token_bytes(9998).hex()
            request = customer_pb2.CreateCustomerRequest(
                city=city,
                email=email,
                firstname=firstname,
                name=name,
                street=street)
            try:
                self.stub.CreateCustomer(request)
            except Exception as e:
                logging.error(f"Failed to create account: {e}")

    def update_customer(self):
        self.set_host_for_task("customer")
        if self._increment_request_count("update_customer"):
            customer_id = random.choice(self.created_customers)
            city = email = firstname = name = street = secrets.token_bytes(9997).hex()
            request = customer_pb2.UpdateCustomerRequest(
                customerId=customer_id,
                city=city,
                email=email + "@mailcom",
                firstname=firstname,
                name=name,
                street=street)
            try:
                self.stub.UpdateCustomer(request)
            except Exception as e:
                logging.error(f"Failed to update customer: {e}")

    def get_customer(self):
        self.set_host_for_task("customer")
        if self._increment_request_count("get_customer"):
            customer_id = random.choice(self.created_customers)
            request = customer_pb2.CustomerRequest(customerId=customer_id)
            try:
                self.stub.getCustomer(request)
            except Exception as e:
                logging.error(f"Failed to get customer: {e}")

    def add_item(self):
        self.set_host_for_task("catalog")
        if self._increment_request_count("add_item"):
            name = secrets.token_bytes(49993).hex()
            request = catalog_pb2.AddItemRequest(name=name + "1", price=1000)
            try:
                self.stub.AddItem(request)
            except Exception as e:
                logging.error(f"Failed to create item: {e}")

    def update_item(self):
        self.set_host_for_task("catalog")
        if self._increment_request_count("update_item"):
            item_id = random.choice(self.created_items)
            name = secrets.token_bytes(49992).hex()
            request = catalog_pb2.UpdateItemRequest(itemId=item_id, name=name, price=100)
            try:
                self.stub.UpdateItem(request)
            except Exception as e:
                logging.error(f"Failed to update item: {e}")

    def get_item(self):
        self.set_host_for_task("catalog")
        if self._increment_request_count("get_item"):
            item_id = random.choice(self.created_items)
            request = catalog_pb2.ItemRequest(itemId=item_id)
            try:
                self.stub.GetItem(request)
            except Exception as e:
                logging.error(f"Failed to get item: {e}")

    def create_order(self):
        self.set_host_for_task("order")
        if self._increment_request_count("create_order"):
            customer_id = random.choice(self.created_customers)
            item_id = random.choice(self.created_items)
            note = secrets.token_bytes(49992).hex()
            request = order_pb2.CreateOrderRequest(
                customerId=customer_id,
                lines=[order_pb2.OrderLine(count=1000, itemId=item_id, note=note)]
            )
            try:
                self.stub.CreateOrder(request)
            except Exception as e:
                logging.error(f"Failed to create order: {e}")

    def get_order(self):
        self.set_host_for_task("order")
        if self._increment_request_count("get_order"):
            order_id = random.choice(self.created_order)
            request = order_pb2.OrderId(id=order_id)
            try:
                self.stub.GetOrder(request)
            except Exception as e:
                logging.error(f"Failed to get a order: {e}")


class WebsiteUser(GrpcUser):
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
