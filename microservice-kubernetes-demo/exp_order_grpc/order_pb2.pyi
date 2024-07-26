from google.protobuf import empty_pb2 as _empty_pb2
from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class OrderId(_message.Message):
    __slots__ = ("id",)
    ID_FIELD_NUMBER: _ClassVar[int]
    id: int
    def __init__(self, id: _Optional[int] = ...) -> None: ...

class OrderListResponse(_message.Message):
    __slots__ = ("orders",)
    ORDERS_FIELD_NUMBER: _ClassVar[int]
    orders: _containers.RepeatedCompositeFieldContainer[Order]
    def __init__(self, orders: _Optional[_Iterable[_Union[Order, _Mapping]]] = ...) -> None: ...

class OrderResponse(_message.Message):
    __slots__ = ("order",)
    ORDER_FIELD_NUMBER: _ClassVar[int]
    order: Order
    def __init__(self, order: _Optional[_Union[Order, _Mapping]] = ...) -> None: ...

class Order(_message.Message):
    __slots__ = ("id", "customerId", "lines")
    ID_FIELD_NUMBER: _ClassVar[int]
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    LINES_FIELD_NUMBER: _ClassVar[int]
    id: int
    customerId: int
    lines: _containers.RepeatedCompositeFieldContainer[OrderLine]
    def __init__(self, id: _Optional[int] = ..., customerId: _Optional[int] = ..., lines: _Optional[_Iterable[_Union[OrderLine, _Mapping]]] = ...) -> None: ...

class OrderLine(_message.Message):
    __slots__ = ("count", "itemId", "note")
    COUNT_FIELD_NUMBER: _ClassVar[int]
    ITEMID_FIELD_NUMBER: _ClassVar[int]
    NOTE_FIELD_NUMBER: _ClassVar[int]
    count: int
    itemId: int
    note: str
    def __init__(self, count: _Optional[int] = ..., itemId: _Optional[int] = ..., note: _Optional[str] = ...) -> None: ...

class CreateOrderRequest(_message.Message):
    __slots__ = ("customerId", "lines")
    CUSTOMERID_FIELD_NUMBER: _ClassVar[int]
    LINES_FIELD_NUMBER: _ClassVar[int]
    customerId: int
    lines: _containers.RepeatedCompositeFieldContainer[OrderLine]
    def __init__(self, customerId: _Optional[int] = ..., lines: _Optional[_Iterable[_Union[OrderLine, _Mapping]]] = ...) -> None: ...

class AddLineRequest(_message.Message):
    __slots__ = ("id", "lines")
    ID_FIELD_NUMBER: _ClassVar[int]
    LINES_FIELD_NUMBER: _ClassVar[int]
    id: int
    lines: _containers.RepeatedCompositeFieldContainer[OrderLine]
    def __init__(self, id: _Optional[int] = ..., lines: _Optional[_Iterable[_Union[OrderLine, _Mapping]]] = ...) -> None: ...

class SuccessResponse(_message.Message):
    __slots__ = ("successMessage",)
    SUCCESSMESSAGE_FIELD_NUMBER: _ClassVar[int]
    successMessage: str
    def __init__(self, successMessage: _Optional[str] = ...) -> None: ...
