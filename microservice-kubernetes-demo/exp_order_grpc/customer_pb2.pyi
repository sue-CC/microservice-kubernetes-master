from google.protobuf import empty_pb2 as _empty_pb2
import order_pb2 as _order_pb2
from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class CustomerRequest(_message.Message):
    __slots__ = ("customerId",)
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    customerId: int
    def __init__(self, customerId: _Optional[int] = ...) -> None: ...

class CustomerResponse(_message.Message):
    __slots__ = ("customerId", "name", "firstname", "email", "street", "city")
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    FIRSTNAME_FIELD_NUMBER: _ClassVar[int]
    EMAIL_FIELD_NUMBER: _ClassVar[int]
    STREET_FIELD_NUMBER: _ClassVar[int]
    CITY_FIELD_NUMBER: _ClassVar[int]
    customerId: int
    name: str
    firstname: str
    email: str
    street: str
    city: str
    def __init__(self, customerId: _Optional[int] = ..., name: _Optional[str] = ..., firstname: _Optional[str] = ..., email: _Optional[str] = ..., street: _Optional[str] = ..., city: _Optional[str] = ...) -> None: ...

class Customer(_message.Message):
    __slots__ = ("customerId", "firstname", "name", "email", "street", "city")
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    FIRSTNAME_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    EMAIL_FIELD_NUMBER: _ClassVar[int]
    STREET_FIELD_NUMBER: _ClassVar[int]
    CITY_FIELD_NUMBER: _ClassVar[int]
    customerId: int
    firstname: str
    name: str
    email: str
    street: str
    city: str
    def __init__(self, customerId: _Optional[int] = ..., firstname: _Optional[str] = ..., name: _Optional[str] = ..., email: _Optional[str] = ..., street: _Optional[str] = ..., city: _Optional[str] = ...) -> None: ...

class CustomerListResponse(_message.Message):
    __slots__ = ("customers",)
    CUSTOMERS_FIELD_NUMBER: _ClassVar[int]
    customers: _containers.RepeatedCompositeFieldContainer[Customer]
    def __init__(self, customers: _Optional[_Iterable[_Union[Customer, _Mapping]]] = ...) -> None: ...

class CreateCustomerRequest(_message.Message):
    __slots__ = ("name", "firstname", "email", "street", "city")
    NAME_FIELD_NUMBER: _ClassVar[int]
    FIRSTNAME_FIELD_NUMBER: _ClassVar[int]
    EMAIL_FIELD_NUMBER: _ClassVar[int]
    STREET_FIELD_NUMBER: _ClassVar[int]
    CITY_FIELD_NUMBER: _ClassVar[int]
    name: str
    firstname: str
    email: str
    street: str
    city: str
    def __init__(self, name: _Optional[str] = ..., firstname: _Optional[str] = ..., email: _Optional[str] = ..., street: _Optional[str] = ..., city: _Optional[str] = ...) -> None: ...

class UpdateCustomerRequest(_message.Message):
    __slots__ = ("customerId", "name", "firstname", "email", "street", "city")
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    FIRSTNAME_FIELD_NUMBER: _ClassVar[int]
    EMAIL_FIELD_NUMBER: _ClassVar[int]
    STREET_FIELD_NUMBER: _ClassVar[int]
    CITY_FIELD_NUMBER: _ClassVar[int]
    customerId: int
    name: str
    firstname: str
    email: str
    street: str
    city: str
    def __init__(self, customerId: _Optional[int] = ..., name: _Optional[str] = ..., firstname: _Optional[str] = ..., email: _Optional[str] = ..., street: _Optional[str] = ..., city: _Optional[str] = ...) -> None: ...
