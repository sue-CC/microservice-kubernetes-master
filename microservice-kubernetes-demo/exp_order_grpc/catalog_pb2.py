# -*- coding: utf-8 -*-
# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: catalog.proto
# Protobuf Python Version: 5.26.1
"""Generated protocol buffer code."""
from google.protobuf import descriptor as _descriptor
from google.protobuf import descriptor_pool as _descriptor_pool
from google.protobuf import symbol_database as _symbol_database
from google.protobuf.internal import builder as _builder
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()


from google.protobuf import empty_pb2 as google_dot_protobuf_dot_empty__pb2
import order_pb2 as order__pb2


DESCRIPTOR = _descriptor_pool.Default().AddSerializedFile(b'\n\rcatalog.proto\x1a\x1bgoogle/protobuf/empty.proto\x1a\x0border.proto\"\x1d\n\x0bItemRequest\x12\x0e\n\x06itemId\x18\x01 \x01(\x03\"3\n\x04Item\x12\x0e\n\x06itemId\x18\x01 \x01(\x03\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\r\n\x05price\x18\x03 \x01(\x01\"(\n\x10ItemListResponse\x12\x14\n\x05items\x18\x01 \x03(\x0b\x32\x05.Item\"-\n\x0e\x41\x64\x64ItemRequest\x12\x0c\n\x04name\x18\x01 \x01(\t\x12\r\n\x05price\x18\x02 \x01(\x01\"@\n\x11UpdateItemRequest\x12\x0e\n\x06itemId\x18\x01 \x01(\x03\x12\x0c\n\x04name\x18\x02 \x01(\t\x12\r\n\x05price\x18\x03 \x01(\x01\"\x1e\n\rSearchRequest\x12\r\n\x05query\x18\x01 \x01(\t2\x96\x02\n\x0e\x43\x61talogService\x12\x1e\n\x07GetItem\x12\x0c.ItemRequest\x1a\x05.Item\x12\x38\n\x0bGetItemList\x12\x16.google.protobuf.Empty\x1a\x11.ItemListResponse\x12!\n\x07\x41\x64\x64Item\x12\x0f.AddItemRequest\x1a\x05.Item\x12\'\n\nUpdateItem\x12\x12.UpdateItemRequest\x1a\x05.Item\x12\x30\n\x0bSearchItems\x12\x0e.SearchRequest\x1a\x11.ItemListResponse\x12,\n\nDeleteItem\x12\x0c.ItemRequest\x1a\x10.SuccessResponseB6\n$com.ewolff.microservice.catalog.grpcB\x0c\x43\x61talogProtoP\x00\x62\x06proto3')

_globals = globals()
_builder.BuildMessageAndEnumDescriptors(DESCRIPTOR, _globals)
_builder.BuildTopDescriptorsAndMessages(DESCRIPTOR, 'catalog_pb2', _globals)
if not _descriptor._USE_C_DESCRIPTORS:
  _globals['DESCRIPTOR']._loaded_options = None
  _globals['DESCRIPTOR']._serialized_options = b'\n$com.ewolff.microservice.catalog.grpcB\014CatalogProtoP\000'
  _globals['_ITEMREQUEST']._serialized_start=59
  _globals['_ITEMREQUEST']._serialized_end=88
  _globals['_ITEM']._serialized_start=90
  _globals['_ITEM']._serialized_end=141
  _globals['_ITEMLISTRESPONSE']._serialized_start=143
  _globals['_ITEMLISTRESPONSE']._serialized_end=183
  _globals['_ADDITEMREQUEST']._serialized_start=185
  _globals['_ADDITEMREQUEST']._serialized_end=230
  _globals['_UPDATEITEMREQUEST']._serialized_start=232
  _globals['_UPDATEITEMREQUEST']._serialized_end=296
  _globals['_SEARCHREQUEST']._serialized_start=298
  _globals['_SEARCHREQUEST']._serialized_end=328
  _globals['_CATALOGSERVICE']._serialized_start=331
  _globals['_CATALOGSERVICE']._serialized_end=609
# @@protoc_insertion_point(module_scope)