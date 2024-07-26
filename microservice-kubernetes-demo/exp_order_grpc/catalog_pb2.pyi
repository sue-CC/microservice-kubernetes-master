from google.protobuf import empty_pb2 as _empty_pb2
import order_pb2 as _order_pb2
from google.protobuf.internal import containers as _containers
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from typing import ClassVar as _ClassVar, Iterable as _Iterable, Mapping as _Mapping, Optional as _Optional, Union as _Union

DESCRIPTOR: _descriptor.FileDescriptor

class ItemRequest(_message.Message):
    __slots__ = ("itemId",)
    ITEMID_FIELD_NUMBER: _ClassVar[int]
    itemId: int
    def __init__(self, itemId: _Optional[int] = ...) -> None: ...

class Item(_message.Message):
    __slots__ = ("itemId", "name", "price")
    ITEMID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    itemId: int
    name: str
    price: float
    def __init__(self, itemId: _Optional[int] = ..., name: _Optional[str] = ..., price: _Optional[float] = ...) -> None: ...

class ItemListResponse(_message.Message):
    __slots__ = ("items",)
    ITEMS_FIELD_NUMBER: _ClassVar[int]
    items: _containers.RepeatedCompositeFieldContainer[Item]
    def __init__(self, items: _Optional[_Iterable[_Union[Item, _Mapping]]] = ...) -> None: ...

class AddItemRequest(_message.Message):
    __slots__ = ("name", "price")
    NAME_FIELD_NUMBER: _ClassVar[int]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    name: str
    price: float
    def __init__(self, name: _Optional[str] = ..., price: _Optional[float] = ...) -> None: ...

class UpdateItemRequest(_message.Message):
    __slots__ = ("itemId", "name", "price")
    ITEMID_FIELD_NUMBER: _ClassVar[int]
    NAME_FIELD_NUMBER: _ClassVar[int]
    PRICE_FIELD_NUMBER: _ClassVar[int]
    itemId: int
    name: str
    price: float
    def __init__(self, itemId: _Optional[int] = ..., name: _Optional[str] = ..., price: _Optional[float] = ...) -> None: ...

class SearchRequest(_message.Message):
    __slots__ = ("query",)
    QUERY_FIELD_NUMBER: _ClassVar[int]
    query: str
    def __init__(self, query: _Optional[str] = ...) -> None: ...
